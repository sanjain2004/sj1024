package cf.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Class contains all tools that can be rented
 */
public class AllToolTypeDetails {

    private AllToolTypeDetails() {}

    private static final List<ToolTypeDetails> allToolTypeDetails = ImmutableList.of(
            new ToolTypeDetails("Ladder", 1.99f, "y", "y", "n"),
            new ToolTypeDetails("Chainsaw", 1.49f, "y", "n", "y"),
            new ToolTypeDetails("Jackhammer", 2.99f, "y", "n", "n")
    );
    private static Map<String, ToolTypeDetails> allToolTypesMap = Maps.newHashMap();

    static {
        //System.out.println("in all tool types");
        for(ToolTypeDetails toolType: allToolTypeDetails) {
            allToolTypesMap.put(toolType.getType(), toolType);
        }
    }

    public static Optional<ToolTypeDetails> get(String type) {
        return Optional.ofNullable(allToolTypesMap.get(type));
    }

    public static void print() {
        System.out.println(""+allToolTypeDetails);
    }
}
