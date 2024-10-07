package cf.service;

import cf.exception.ValidationException;

import java.util.Map;

/**
 * Abstract Tool operation base class. This has methods for shared functionality - validations or data or logic.
 * It also invokes the handle method of the subclass which performs the actual function.
 */
abstract public class BaseToolOperation {

    protected ToolOperationData toolOperationData;

    public BaseToolOperation(Map<String, String> toolArgs) {
        // Here we can validate on the type of operation (using some info from the toolArgs)
        // Based on the type of operation,
        getDataAndValidate(toolArgs);
    }

    // Validation and function are specific to the subclass/operation
    abstract protected Map<String, String> handle();
    abstract protected void getDataAndValidate(Map<String, String> toolArgs);

    public Map<String, String> process() {
        return handle();
    }

    protected static Integer stringToInt(String key, String strVal) {
        try {
            return Integer.parseInt(strVal);
        }
        catch(NumberFormatException nfe) {
            throw new ValidationException(key + " must have a number");
        }
    }

}
