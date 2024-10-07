package cf.model;

/**
 * Represents a Tool Type
 */
public class ToolTypeDetails {

    private final String type;
    private final Float dailyCharge;
    private final Boolean weekdayCharge;
    private final Boolean weekendCharge;
    private final Boolean holidayCharge;

    public ToolTypeDetails(String type, Float dailyCharge, String weekdayCharge, String weekendCharge, String holidayCharge) {
        this.type = type;
        this.dailyCharge = dailyCharge;
        this.weekdayCharge = weekdayCharge.equalsIgnoreCase("y");
        this.weekendCharge = weekendCharge.equalsIgnoreCase("y");
        this.holidayCharge = holidayCharge.equalsIgnoreCase("y");
    }

    public String getType() {
        return type;
    }

    public Float getDailyCharge() {
        return dailyCharge;
    }

    public Boolean getWeekdayCharge() {
        return weekdayCharge;
    }

    public Boolean getWeekendCharge() {
        return weekendCharge;
    }

    public Boolean getHolidayCharge() {
        return holidayCharge;
    }
}

