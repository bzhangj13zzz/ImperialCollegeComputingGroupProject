package ic.doc.sgo.groupingstrategies;

import ic.doc.sgo.Constraint;
import ic.doc.sgo.Group;
import ic.doc.sgo.Student;
import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

public class FixedPointStrategyTest {

    @Test
    public void successCasesTest() throws FileNotFoundException {
        File casesDirectory = new File("src/test/cases/success");
        assertTrue(casesDirectory.exists() && casesDirectory.isDirectory());
        File[] cases = casesDirectory.listFiles();

        assert cases != null;
        for (File file: cases) {
            System.out.println("Run test " + file.getName());
            runTestFromFile(file);
            System.out.println("Pass");
        }
    }

    @Test
    public void failCasesTest() throws FileNotFoundException {
        File casesDirectory = new File("src/test/cases/fail");
        assertTrue(casesDirectory.exists() && casesDirectory.isDirectory());
        File[] cases = casesDirectory.listFiles();

        assert cases != null;
        for (File file: cases) {
            runTestFromFile(file);
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
        assertEquals(0, groups.get(0).size(), remaining);
        for (Group group: groups.subList(1, groups.size())) {
            assertTrue(constraint.isValidGroup(group));
        }
    }

}