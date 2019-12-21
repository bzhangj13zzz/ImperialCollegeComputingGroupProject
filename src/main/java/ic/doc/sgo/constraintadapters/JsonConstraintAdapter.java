package ic.doc.sgo.constraintadapters;

import com.google.gson.JsonObject;
import ic.doc.sgo.Constraint;
import ic.doc.sgo.groupingstrategies.vectorspacestrategy.Pair;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JsonConstraintAdapter implements ConstraintAdapter {

    private final JsonObject constraintJson;

    public JsonConstraintAdapter(JsonObject constraintJson) {
        this.constraintJson = constraintJson;
    }

    @Override
    public Optional<Constraint> toConstraint() {
        // Check for invalid json
        if (!(constraintJson.has("groupSizeLowerBound") && constraintJson
            .has("groupSizeUpperBound"))) {
            return Optional.empty();
        }
        // Either both `minMale` and `minFemale` are passed in or both `genderRatio` and `genderErrorMargin` are passed in
        if (constraintJson.has("minMale") && !constraintJson.has("minFemale")) {
            return Optional.empty();
        }
        if (constraintJson.has("minFemale") && !constraintJson.has("minMale")) {
            return Optional.empty();
        }
        if (constraintJson.has("genderRatio") && !constraintJson.has("genderErrorMargin")) {
            return Optional.empty();
        }
        if (constraintJson.has("genderErrorMargin") && !constraintJson.has("genderRatio")) {
            return Optional.empty();
        }
        if ((constraintJson.has("minMale") && constraintJson.has("minFemale")) && (constraintJson
            .has("genderRatio") || constraintJson.has("genderErrorMargin"))) {
            return Optional.empty();
        }
        if ((constraintJson.has("genderRatio") && constraintJson.has("genderErrorMargin")) && (
            constraintJson
                .has("minMale") || constraintJson.has("minFemale"))) {
            return Optional.empty();
        }

        Constraint.Builder constraintBuilder;

        constraintBuilder = new Constraint.Builder(
            constraintJson.get("groupSizeLowerBound").getAsInt(),
            constraintJson.get("groupSizeUpperBound").getAsInt());
        for (String key : constraintJson.keySet()) {
            switch (key) {
                case "groupSizeLowerBound":
                    break;
                case "groupSizeUpperBound":
                    break;
                case "timezoneDiff":
                    constraintBuilder.setTimezoneDiff(constraintJson.get(key).getAsInt());
                    break;
                case "ageDiff":
                    constraintBuilder.setAgeDiff(constraintJson.get(key).getAsInt());
                    break;
                case "minMale":
                    constraintBuilder.setMinMale(constraintJson.get(key).getAsInt());
                    break;
                case "minFemale":
                    constraintBuilder.setMinFemale(constraintJson.get(key).getAsInt());
                    break;
                case "genderRatio":
                    constraintBuilder.setGenderRatio(constraintJson.get(key).getAsDouble());
                    break;
                case "genderErrorMargin":
                    constraintBuilder.setGenderErrorMargin(constraintJson.get(key).getAsDouble());
                    break;
                case "isSameGender":
                    if (constraintJson.get(key).getAsBoolean()) {
                        constraintBuilder.setIsSameGender();
                    }
                    break;
                default:
                    String pairString = constraintJson.get(key).getAsString();
                    try {
                        List<Integer> integers = Arrays.stream(pairString.split(","))
                            .map(String::trim)
                            .map(Integer::valueOf).collect(
                                Collectors.toList());
                        assert integers.size() == 2;
                        constraintBuilder.addDiscreteAttributeConstraint(key,
                            new Pair<>(integers.get(0), integers.get(1)));
                    } catch (Exception e) {
                        return Optional.empty();
                    }
            }
        }
        return Optional.of(constraintBuilder.createConstrain());
    }
}
