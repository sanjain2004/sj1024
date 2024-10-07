package cf.common;

import com.google.common.collect.Sets;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.Set;

public class DateTimeUtils {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    // This is just a lookup table. If needed, we can encode the rules of holidays and calculate.
    static private final Set<String> HOLIDAYS = Sets.newHashSet(
            "2025-07-04", "2025-09-01",
            "2026-07-03", "2026-09-07",
            "2027-07-05", "2027-09-06",
            "2028-07-04", "2028-09-04",
            "2029-07-04", "2029-09-03",
            "2030-07-04", "2030-09-02");

    static private final EnumSet<DayOfWeek> WEEKEND_DAYS = EnumSet.of(DayOfWeek.SATURDAY , DayOfWeek.SUNDAY);

    public static LocalDate from(String dateStr) {
        return LocalDate.parse(dateStr, formatter);
    }

    public static String from(LocalDate date) {
        return formatter.format(date);
    }


    public static Boolean isWeekend(LocalDate date) {
        return WEEKEND_DAYS.contains(date.getDayOfWeek());
//        System.out.println(date + "-is weekend=" + isweekend);
//        return isweekend;
    }

    public static Boolean isHoliday(LocalDate date) {
        return HOLIDAYS.contains(date.toString());
//        System.out.println(date + "-is holiday=" + isholiday);
//        return isholiday;
    }

}
