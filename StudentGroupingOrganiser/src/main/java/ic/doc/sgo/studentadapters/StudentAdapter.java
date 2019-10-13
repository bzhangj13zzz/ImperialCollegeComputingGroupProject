package ic.doc.sgo.studentadapters;

import ic.doc.sgo.Student;

import java.util.Optional;

public interface StudentAdapter {
    Optional<Student> toStudent();
}
