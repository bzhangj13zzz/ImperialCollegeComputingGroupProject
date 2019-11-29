package ic.doc.sgo.constraintadapters;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.gson.JsonObject;
import ic.doc.sgo.Constraint;
import ic.doc.sgo.groupingstrategies.vectorspacestrategy.Pair;
import ic.doc.sgo.studentadapters.JsonConstraintAdapter;
import java.util.Optional;
import org.junit.Test;

public class JsonConstraintAdapterTest {

    @Test
    public void returnEmptyIfJsonNoUpperBound() {
        JsonObject json = new JsonObject();
        json.addProperty("groupSizeLowerBound", 1);
        assertThat(new JsonConstraintAdapter(json).toConstraint(), is(Optional.empty()));
    }

    @Test
    public void returnEmptyIfJsonNoLowerBound() {
        JsonObject json = new JsonObject();
        json.addProperty("groupSizeUpperBound", 1);
        assertThat(new JsonConstraintAdapter(json).toConstraint(), is(Optional.empty()));
    }

    @Test
    public void returnEmptyIfOnlyMinMale() {
        JsonObject json = new JsonObject();
        json.addProperty("minMale", 1);
        json.addProperty("groupSizeLowerBound", 1);
        json.addProperty("groupSizeUpperBound", 1);
        assertThat(new JsonConstraintAdapter(json).toConstraint(), is(Optional.empty()));
    }

    @Test
    public void returnEmptyIfOnlyMinFemale() {
        JsonObject json = new JsonObject();
        json.addProperty("minFemale", 1);
        json.addProperty("groupSizeLowerBound", 1);
        json.addProperty("groupSizeUpperBound", 1);
        assertThat(new JsonConstraintAdapter(json).toConstraint(), is(Optional.empty()));
    }

    @Test
    public void returnEmptyIfOnlyGenderRatio() {
        JsonObject json = new JsonObject();
        json.addProperty("genderRatio", 1);
        json.addProperty("groupSizeLowerBound", 1);
        json.addProperty("groupSizeUpperBound", 1);
        assertThat(new JsonConstraintAdapter(json).toConstraint(), is(Optional.empty()));
    }

    @Test
    public void returnEmptyIfOnlyGenderErrorMargin() {
        JsonObject json = new JsonObject();
        json.addProperty("genderErrorMargin", 1);
        json.addProperty("groupSizeLowerBound", 1);
        json.addProperty("groupSizeUpperBound", 1);
        assertThat(new JsonConstraintAdapter(json).toConstraint(), is(Optional.empty()));
    }

    @Test
    public void returnEmptyIfBothGenderConstraintsExist() {
        JsonObject json = new JsonObject();
        json.addProperty("minMale", 1);
        json.addProperty("genderRatio", 1);
        json.addProperty("minFemale", 1);
        json.addProperty("genderErrorMargin", 1);
        json.addProperty("groupSizeLowerBound", 1);
        json.addProperty("groupSizeUpperBound", 1);
        assertThat(new JsonConstraintAdapter(json).toConstraint(), is(Optional.empty()));
    }

    @Test
    public void canReturnConstraintIfJsonHasBounds() {
        JsonObject json = new JsonObject();
        json.addProperty("groupSizeLowerBound", "1");
        json.addProperty("groupSizeUpperBound", "1");
        Constraint constraint = new Constraint.Builder(1, 1).createConstrain();
        assertThat(new JsonConstraintAdapter(json).toConstraint(), is(Optional.of(constraint)));
    }

    @Test
    public void canReturnConstraintIfJsonHasAllMembers() {
        JsonObject json = new JsonObject();
        json.addProperty("groupSizeLowerBound", "1");
        json.addProperty("groupSizeUpperBound", "1");
        json.addProperty("timezoneDiff", "1");
        json.addProperty("ageDiff", "1");
        json.addProperty("minMale", "1");
        json.addProperty("minFemale", "1");
        Constraint constraint = new Constraint.Builder(1, 1).
            setTimezoneDiff(1).setAgeDiff(1).setMinFemale(1).setMinMale(1).createConstrain();
        assertThat(new JsonConstraintAdapter(json).toConstraint(), is(Optional.of(constraint)));
    }

    @Test
    public void canReturnConstraintIfJsonHasAdditionalAttributes() {
        JsonObject json = new JsonObject();
        json.addProperty("groupSizeLowerBound", "1");
        json.addProperty("groupSizeUpperBound", "1");
        json.addProperty("timezoneDiff", "1");
        json.addProperty("ageDiff", "1");
        json.addProperty("minMale", "1");
        json.addProperty("minFemale", "1");
        json.addProperty("quant", "1,1");
        Constraint constraint = new Constraint.Builder(1, 1).
            setTimezoneDiff(1).setAgeDiff(1).setMinFemale(1).setMinMale(1)
            .addDiscreteAttributeConstraint("quant", new Pair<>(1, 1)).createConstrain();
        assertThat(new JsonConstraintAdapter(json).toConstraint(), is(Optional.of(constraint)));
    }

}
