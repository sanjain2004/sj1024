package cf.service;

import cf.model.Tool;
import java.time.LocalDate;

/**
 * Class containing input data - transformed or looked up
 * NOTE: The properties were intentionally not made private to maintain readability in this POC.
 */
public class ToolOperationData {
    Tool tool;
    Integer numOfRentalDays;
    Integer discountPercent;
    LocalDate checkoutDate;
}
