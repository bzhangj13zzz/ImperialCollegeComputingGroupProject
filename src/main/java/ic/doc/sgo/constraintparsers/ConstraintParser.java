package ic.doc.sgo.constraintparsers;

import ic.doc.sgo.Constraint;
import java.util.Optional;

public interface ConstraintParser {
    Optional<Constraint> toConstraint();
}
