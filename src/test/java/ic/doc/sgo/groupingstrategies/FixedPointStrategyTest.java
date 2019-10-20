package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Constraint;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

public class FixedPointStrategyTest {

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    public void casesTest() throws FileNotFoundException {
        File casesDirectory = new File("src/test/cases");
        assertTrue(casesDirectory.exists() && casesDirectory.isDirectory());
        File[] cases = casesDirectory.listFiles();

        assert cases != null;
        System.out.println("Start Running tests for cases");
        for (File file: cases) {
            try {
                System.out.print(file.getName() + " ");
                runTestFromFile(file);
            } catch (Throwable t) {
                System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);
                collector.addError(t);
                continue;
            }
            System.out.println(ANSI_GREEN + "PASS" + ANSI_RESET);
        }
    }

    private void runTestFromFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int numberOfStudents = scanner.nextInt();
        int groupSizeLowerBound = scanner.nextInt();
        int groupSizeUpperBound = scanner.nextInt();
        int timeZoneDifference = scanner.nextInt();
        Constraint constraint = new Constraint.Builder(groupSizeLowerBound, groupSizeUpperBound)
                .setTimezoneDiff(timeZoneDifference).createConstrain();

        List<Student> students = new ArrayList<>();
        for (int i = 0; i < numberOfStudents; i++) {
            Student student = new Student.Builder(String.valueOf(i)).createStudent();
            int timeZone = scanner.nextInt();
            assertTrue(-12 <= timeZone && timeZone <= 12);
            student.setTimeZoneTest(timeZone);
            students.add(student);
        }

        int remaining = scanner.nextInt();

        List<Group> groups = new FixedPointStrategy().apply(students, constraint);
        for (Group group: groups.subList(1, groups.size())) {
            assertTrue(constraint.isValidGroup(group));
        }
        assertEquals(groups.get(0).size(), remaining);
    }

}