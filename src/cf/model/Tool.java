package cf.model;

/**
 * Represents a Tool
 */
public class Tool {

    private final String code;
    private final String type;
    private final String brand;

    public Tool(String code, String type, String brand) {
        this.code = code;
        this.type = type;
        this.brand = brand;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }
}

