package cf.service;

import cf.common.DateTimeUtils;
import cf.exception.ValidationException;
import cf.model.AllToolTypeDetails;
import cf.model.AllTools;
import cf.model.Tool;
import cf.model.ToolTypeDetails;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Class that does validation of input, fetching of other necessary data, and logic to generate a rental agreement
 *
 */
public class GenerateRentalAgreementOperation extends BaseToolOperation {

    private LocalDate dueDate;
    private Integer numOfChargeableDays;
    private Float preDiscountCharge;
    private Float discountAmount;
    private ToolTypeDetails typeDetails;

    public GenerateRentalAgreementOperation(Map<String, String> args) {
        super(args);
    }

    /**
     * This acts as a facade or controller method
     * @return Map of response and value
     */
    @Override
    protected Map<String, String> handle() {
        calcDueDateAndNumDays();
        calcChargesBeforeAndAfterDiscount();
        String agreement = generateAgreement();
        Map<String, String> result = Maps.newHashMap();
        result.put("response", agreement);
        return result;
    }

    // code=CHNS,numDays=3,discount=10,checkoutDate=10/03/2024

    /**
     * Validate incoming data as needed to perform this operation
     * @param toolArgs Map of pre decided keys and their values
     */
    @Override
    protected void getDataAndValidate(Map<String, String> toolArgs) {
        toolOperationData = new ToolOperationData();
        toolOperationData.tool = validateCode(toolArgs.get("code"));
        toolOperationData.numOfRentalDays = validateRentalDays(toolArgs.get("numDays"));
        toolOperationData.discountPercent = validateDiscountPercent(toolArgs.get("discount"));
        toolOperationData.checkoutDate = validateCheckoutDate(toolArgs.get("checkoutDate"));
        typeDetails = validateTypeDetails(toolOperationData.tool);
    }

    /**
     * From the due date, check all dates till the number of rental days. Depending on chargeable days, get the
     * due date and num of chargeable days
     */
    private void calcDueDateAndNumDays() {
        LocalDate dueDate = toolOperationData.checkoutDate;
        int chargeableDays = 0;

        // Check every date starting from checkout date. If it's chargeable, count it
        for(int i = 1; i <= toolOperationData.numOfRentalDays; i++) {
            dueDate = dueDate.plusDays(1L);
            chargeableDays += (isChargeableDate(dueDate) ? 1 : 0);
        }
        this.dueDate = dueDate;
        this.numOfChargeableDays = chargeableDays;
    }

    /**
     * Calculate charges before and after discount with rounding.
     */
    private void calcChargesBeforeAndAfterDiscount() {
        float totalCharge = typeDetails.getDailyCharge() * numOfChargeableDays;
        BigDecimal bd = new BigDecimal(Float.toString(totalCharge));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        this.preDiscountCharge = bd.floatValue();

        float discountedCharge = (totalCharge * this.toolOperationData.discountPercent)/100;
        bd = new BigDecimal(Float.toString(discountedCharge));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        this.discountAmount = bd.floatValue();
    }

    /**
     * Check if the date is chargeable - holiday, weekend, or weekday
     * @param date LocalDate
     * @return Boolean
     */
    private Boolean isChargeableDate(LocalDate date) {
        if(DateTimeUtils.isHoliday(date)) { // Check holiday
            return typeDetails.getHolidayCharge();
        }
        if(DateTimeUtils.isWeekend(date)) { // Check weekend
            return typeDetails.getWeekendCharge();
        }
        // Return the weekday setting
        return typeDetails.getWeekdayCharge();
    }

    /**
     * Format data, create a string, and return
     * @return String
     */
    private String generateAgreement() {

        String currencySymbol = Currency.getInstance("USD").getSymbol();

        //System.out.println(formatter.format(amount));
        StringBuilder builder = new StringBuilder();
        builder.append("Agreement\n---------\n");
        builder.append(String.format("Tool code            : %s\n", this.toolOperationData.tool.getCode()));
        builder.append(String.format("Tool type            : %s\n", this.toolOperationData.tool.getType()));
        builder.append(String.format("Tool brand           : %s\n", this.toolOperationData.tool.getBrand()));
        builder.append(String.format("Rental days          : %d\n", this.toolOperationData.numOfRentalDays));
        builder.append(String.format("Checkout date        : %s\n", DateTimeUtils.from(this.toolOperationData.checkoutDate)));
        builder.append(String.format("Due date             : %s\n", DateTimeUtils.from(this.dueDate)));
        builder.append(String.format("Daily rental charge  : %s%,.2f\n", currencySymbol, typeDetails.getDailyCharge()));
        builder.append(String.format("Charge days          : %d\n", this.numOfChargeableDays));
        builder.append(String.format("Pre-discount charge  : %s%,.2f\n", currencySymbol, this.preDiscountCharge));
        builder.append(String.format("Discount percent     : %d%%\n", this.toolOperationData.discountPercent));
        builder.append(String.format("Discount amount      : %s%,.2f\n", currencySymbol, this.discountAmount));
        Float finalCharge = preDiscountCharge - discountAmount;
        builder.append(String.format("Final charge         : %s%,.2f\n", currencySymbol, finalCharge));

        return builder.toString();

    }

    /**
     * Ensure provide code exists
     * @param code String
     * @return Tool
     */
    private Tool validateCode(String code) {
        if (Objects.isNull(code)) {
            throw new ValidationException("tool code is required");
        }
        Optional<Tool> opTool = AllTools.get(code);
        if (!opTool.isPresent()) {
            throw new ValidationException("cf.model.Tool code " + code + " does not exist");
        }

        return opTool.get();
    }

    /**
     * Check that the tool type exists and has some chargeable days.
     * This is not checking to see if the daily charges are 0 or less.
     * @param tool Tool
     * @return ToolTypeDetails
     */
    private ToolTypeDetails validateTypeDetails(Tool tool) {
        Optional<ToolTypeDetails> opToolTypeDetails = AllToolTypeDetails.get(tool.getType());
        if(!opToolTypeDetails.isPresent()) {
            throw new ValidationException("cf.model.Tool type " + tool.getType() + " for tool code " + tool.getCode() + " does not exist");
        }
        ToolTypeDetails typeDetails = opToolTypeDetails.get();
        boolean hasCharge = typeDetails.getHolidayCharge() || typeDetails.getWeekendCharge() || typeDetails.getWeekdayCharge();
        if(!hasCharge) {
            throw new ValidationException("cf.model.Tool type " + tool.getType() + " for tool code " + tool.getCode() + " does not have chargeable days set");
        }

        return typeDetails;
    }

    /**
     * Check if number of days is less than 1
     * @param daysStr String
     * @return Integer
     */
    private Integer validateRentalDays(String daysStr) {
        if (Objects.isNull(daysStr)) {
            throw new ValidationException("number of rental days is required");
        }
        Integer days = stringToInt("num of rental days", daysStr);
        if (days < 1) {
            throw new ValidationException("number of rental days must be at least 1");
        }

        return days;
    }

    /**
     * If discount is not given, assume 0
     * @param discountStr String
     * @return Integer
     */
    private Integer validateDiscountPercent(String discountStr) {
        if(Objects.isNull(discountStr)) {
            return 0;
        }
        Integer discount = stringToInt("discount percent", discountStr);

        if(discount < 0 || discount >= 100) {
            throw new ValidationException("discount percent if present must be between 1 and 99");
        }

        return discount;
    }

    /**
     * Validate checkout date - format and in the past.
     * @param dateStr String
     * @return LocalDate
     */
    private LocalDate validateCheckoutDate(String dateStr) {
        if (Objects.isNull(dateStr)) {
            throw new ValidationException("check out date of format mm/dd/yyyy is required");
        }

        try {
            LocalDate checkoutDate = DateTimeUtils.from(dateStr);
            LocalDate now = LocalDate.now();
            if(checkoutDate.isBefore(now)) {
                throw new ValidationException("checkout date cannot be in the past.");
            }
            return checkoutDate;
        }
        catch(DateTimeParseException dtpe) {
            throw new ValidationException("checkout date has invalid format:" + dateStr + ". Please enter date in format mm/dd/yyyy");
        }
    }

}
