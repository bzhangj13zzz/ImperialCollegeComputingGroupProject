package ic.doc.sgo.groupingstrategies.vectorSpaceStrategies;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ic.doc.sgo.Constraint;
import ic.doc.sgo.Group;
import ic.doc.sgo.springframework.Controller.GroupingController;
import ic.doc.sgo.springframework.Service.GroupingService;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

public class VectorizedFixedPointStrategyTest {

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";


    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    public void testCases() {
        File casesDirectory = new File("src/test/cases");
        assertTrue(casesDirectory.exists() && casesDirectory.isDirectory());
        File[] cases = casesDirectory.listFiles();

        assert cases != null;
        System.out.println("Start Running tests for cases");
        for (File directory: cases) {
            assertTrue(directory.isDirectory());
            System.out.println("Testing for " + directory.getName());
            for (File file: Objects.requireNonNull(directory.listFiles())) {
                System.out.print(file.getName() + " ");
                try {
                    runTestFromFile(file);
                } catch (Throwable t) {
                    System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);
                    collector.addError(t);
                    continue;
                }
                System.out.println(ANSI_GREEN + "PASS" + ANSI_RESET);
            }
            System.out.println();
        }
    }

    private void runTestFromFile(File file) throws IOException {
        String jsonString = IOUtils.toString(new FileInputStream(file), Charsets.UTF_8);
        Gson gson = new Gson();
        JsonObject parsedJson = gson.fromJson(jsonString, JsonObject.class);
        JsonObject constraintJsonObject = parsedJson.getAsJsonObject("filters");
        int remaining = parsedJson.get("remaining").getAsInt();
        Constraint constraint = gson.fromJson(constraintJsonObject, Constraint.class);

        List<Group> groups = GroupingController.getAllocatedGroupFromJson(jsonString,
                new GroupingService(new VectorizedFixedPointStrategy()));

        for (Group group: groups.subList(1, groups.size())) {
            assertTrue(constraint.isValidGroup(group));
        }
        assertEquals(groups.get(0).size(), remaining);
    }

}