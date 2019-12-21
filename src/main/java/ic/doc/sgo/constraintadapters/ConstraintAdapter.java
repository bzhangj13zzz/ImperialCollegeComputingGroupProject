package ic.doc.sgo.constraintadapters;

import ic.doc.sgo.Constraint;
import java.util.Optional;

public interface ConstraintAdapter {
    Optional<Constraint> toConstraint();
}
