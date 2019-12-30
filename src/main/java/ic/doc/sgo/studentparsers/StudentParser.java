package ic.doc.sgo.studentparsers;

import ic.doc.sgo.Student;

import java.util.Optional;

public interface StudentParser {
    Optional<Student> toStudent();
}
