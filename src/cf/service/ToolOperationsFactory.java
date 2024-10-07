package cf.service;

import java.util.Map;

/**
 * Minimal Factory to return the operation based on some criteria. For this POC, it is returning a specific
 * operation.
 */
public class ToolOperationsFactory {

    public static BaseToolOperation getOperation(Map<String, String> opData) {
        return new GenerateRentalAgreementOperation(opData);
    }
}
