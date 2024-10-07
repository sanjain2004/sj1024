import cf.service.BaseToolOperation;
import cf.service.ToolOperationsFactory;
import com.google.common.collect.Maps;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Usage: java -jar app.jar code=<code>,numDays=<days>,discount=<discount>,checkoutDate=<mm/dd/yyyy>");
            System.exit(0);
        }

        // Get, parse, and create a map of all input data
        // Handle basic validations
        Map<String, String> argsMap = Maps.newHashMap();
        String[] allInputs = args[0].split(",");
        for(String kv: allInputs) {
            String[] params = kv.split("=");
            if(params.length != 2) {
                System.out.println("WARNING: Ignoring key " + params[0] + ". Missing value.");
                continue;
            }
            argsMap.put(params[0], params[1]);
        }
        System.out.println("Input params:" + argsMap);

        try {
            BaseToolOperation ops = ToolOperationsFactory.getOperation(argsMap);
            Map<String, String> result = ops.process();
            System.out.println(result.get("response"));
        }
        catch(Throwable ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }
}