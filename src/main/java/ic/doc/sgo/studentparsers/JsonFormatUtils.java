package ic.doc.sgo.studentparsers;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class JsonFormatUtils {
    private JsonFormatUtils() {
    }

    static double yearNumStringToDouble(String yearNumStr) {
        if (yearNumStr == null || yearNumStr.equals("")) {
            return 0.0;
        }
        double result = 0.0;
        Pattern yearPattern = Pattern.compile("[0-9]+( *)y");
        Pattern monthPattern = Pattern.compile("[0-9]+( *)m");
        Matcher yearMatcher = yearPattern.matcher(yearNumStr);
        Matcher monthMatcher = monthPattern.matcher(yearNumStr);
        if (yearMatcher.find()) {
            result += Double.parseDouble(yearMatcher.group().replaceAll("[^0-9]", ""));
        }
        if (monthMatcher.find()) {
            result += Double.parseDouble(monthMatcher.group().replaceAll("[^0-9]", "")) / 12.0;
        }
        return result;
    }

    static int dobToAge(String dobStr, LocalDate currentDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");
        LocalDate birthDate = LocalDate.parse(dobStr, formatter);
        return Period.between(birthDate, currentDate).getYears();
    }
}
