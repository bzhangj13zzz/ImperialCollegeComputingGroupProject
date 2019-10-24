package ic.doc.sgo.groupingstrategies.vectorSpaceStrategies;

import ic.doc.sgo.Constraint;

import java.util.HashMap;
import java.util.Map;

public class VectorSpace {

    private final Map<String, Attribute> dimensions = new HashMap<>();
    private final int clusterSizeLowerBound;
    private final int clusterSizeUpperBound;
    private final Map<String, HashMap<String, Integer>> discreteAttribute = new HashMap<>();

    public VectorSpace(Constraint constraint) {
        if (constraint.getTimezoneDiff().isPresent()) {
            dimensions.put("timeZone", new Attribute(12.0, Type.CIRCLE));
        }

        if (constraint.getAgeDiff().isPresent()) {
            dimensions.put("age", new Attribute(120.0, Type.LINE));
        }

        if (constraint.isGenderMatter()) {
            discreteAttribute.put("gender", new HashMap<>());
            discreteAttribute.get("gender").put("male", constraint.getDiscreteAttributeValue("gender", "male"));
            discreteAttribute.get("gender").put("female", constraint.getDiscreteAttributeValue("gender", "female"));
        }

        this.clusterSizeLowerBound = constraint.getGroupSizeLowerBound();
        this.clusterSizeUpperBound = constraint.getGroupSizeUpperBound();
    }

    public int getClusterSizeLowerBound() {
        return clusterSizeLowerBound;
    }

    public int getClusterSizeUpperBound() {
        return clusterSizeUpperBound;
    }

    public int getDiscreteAttributeValue(String attribute, String type) {
        return discreteAttribute.get(attribute).get(type);
    }


    class Attribute {
        private final Double limit;
        private final Type type;

        public Attribute(Double limit, Type type) {
            this.limit = limit;
            this.type = type;
        }

        public Double getLimit() {
            return limit;
        }

        public Type getType() {
            return type;
        }
    }

    enum Type {
        LINE,
        CIRCLE;
    }
}
