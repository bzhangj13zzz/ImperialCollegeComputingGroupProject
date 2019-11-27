package ic.doc.sgo.studentadapters;

import ic.doc.sgo.Constraint;
import java.util.Optional;

public interface ConstraintAdapter {
    Optional<Constraint> toConstraint();
}
