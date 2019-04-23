package se.elfu.sportprojectbackend.utils;

import org.junit.Test;
import se.elfu.sportprojectbackend.exception.customException.BadRequestException;
import static org.junit.Assert.*;
import java.time.LocalDateTime;
import java.time.Month;

public class DateTimeParserTest {

    @Test
    public void parse_dateTime_correct_test(){
        LocalDateTime expected = LocalDateTime.of(2019 , Month.MAY,2, 19,0);
        LocalDateTime actual = DateTimeParser.parseDateTime("2019-05-02", "19:00");

        assertTrue(actual.isEqual(expected));
    }

    @Test(expected = BadRequestException.class)
    public void parse_dateTime_non_existing_test(){
       DateTimeParser.parseDateTime("2019-15-02", "15:32");
    }

    @Test(expected = BadRequestException.class)
    public void parse_dateTime_null_test(){
        DateTimeParser.parseDateTime(null, null);
    }

    @Test(expected = BadRequestException.class)
    public void parse_date_null_test(){
        DateTimeParser.parseDateTime(null, "15:32");
    }

    @Test(expected = BadRequestException.class)
    public void parse_wrong_format_test(){
       DateTimeParser.parseDateTime("2019/03/02", "15:32");
    }

    @Test
    public void format_date_test(){
        String expected ="2019-05-02";
        LocalDateTime localDateTime = LocalDateTime.of(2019 , Month.MAY,2, 19,0);
        String actual = DateTimeParser.formatDate(localDateTime);

        assertTrue(actual.equals(expected));
    }

    @Test
    public void format_time_test(){
        String expected ="19:00";
        LocalDateTime localDateTime = LocalDateTime.of(2019 , Month.MAY,2, 19,0);
        String actual = DateTimeParser.formatTime(localDateTime);

        assertTrue(actual.equals(expected));
    }
}
