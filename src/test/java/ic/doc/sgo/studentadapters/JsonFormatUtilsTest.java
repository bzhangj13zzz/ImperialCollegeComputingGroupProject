package ic.doc.sgo.studentadapters;

import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JsonFormatUtilsTest {

    @Test
    public void yearNumStringToDouble() {
        assertThat(JsonFormatUtils.yearNumStringToDouble(null), is(0.0));
        assertThat(JsonFormatUtils.yearNumStringToDouble(""), is(0.0));
        assertThat(JsonFormatUtils.yearNumStringToDouble("12"), is(0.0));
        assertThat(JsonFormatUtils.yearNumStringToDouble("12y 10m"), is(12.0 + 10.0 / 12.0));
        assertThat(JsonFormatUtils.yearNumStringToDouble("12y"), is(12.0));
        assertThat(JsonFormatUtils.yearNumStringToDouble("12y10m"), is(12.0 + 10.0 / 12.0));
        assertThat(JsonFormatUtils.yearNumStringToDouble("12 y10m"), is(12.0 + 10.0 / 12.0));
        assertThat(JsonFormatUtils.yearNumStringToDouble("12 y10 m"), is(12.0 + 10.0 / 12.0));
        assertThat(JsonFormatUtils.yearNumStringToDouble("12 y 10 m "), is(12.0 + 10.0 / 12.0));
        assertThat(JsonFormatUtils.yearNumStringToDouble(" 12 y 10 m "), is(12.0 + 10.0 / 12.0));
        assertThat(JsonFormatUtils.yearNumStringToDouble(" 10 m "), is(10.0 / 12.0));
    }

    @Test
    public void dobToAge() {
        LocalDate now = LocalDate.of(2019, 10, 1);
        assertThat(JsonFormatUtils.dobToAge("2000/1/1", now), is(19));
        assertThat(JsonFormatUtils.dobToAge("2000/12/1", now), is(18));
        assertThat(JsonFormatUtils.dobToAge("2000/10/1", now), is(19));
        assertThat(JsonFormatUtils.dobToAge("2000/10/2", now), is(18));
    }
}