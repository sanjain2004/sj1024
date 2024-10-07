package cf.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

/**
 * Class contains all tools that can be rented
 */
public class AllTools {

    private AllTools() {}
    // Creating static list of all the tools
    private static final List<Tool> allTools = ImmutableList.of(
            new Tool("CHNS", "Chainsaw", "Stihl"),
            new Tool("LADW", "Ladder", "Werner"),
            new Tool("JAKD", "Jackhammer", "DeWalt"),
            new Tool("JAKR", "Jackhammer", "Ridgid")
    );

    // For quicker/easier fetch, load a map
    private final static Map<String, Tool> allToolsMap = Maps.newHashMap();

    static {
        for(Tool tool: allTools) {
            allToolsMap.put(tool.getCode(), tool);
        }
    }

    public static Optional<Tool> get(String code) {
        return Optional.ofNullable(allToolsMap.get(code));
    }

    public static void print() {
        System.out.println(""+allToolsMap);
    }
}
