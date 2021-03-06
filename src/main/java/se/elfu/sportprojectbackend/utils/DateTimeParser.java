package se.elfu.sportprojectbackend.utils;

import se.elfu.sportprojectbackend.exception.customException.BadRequestException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

public final class DateTimeParser {

    private static DateTimeFormatter DATE_FORMATTER =  DateTimeFormatter.ofPattern("uuuu-MM-dd");
    private static DateTimeFormatter TIME_FORMATTER =  DateTimeFormatter.ofPattern("HH:mm");
    private static DateTimeFormatter swedishDate = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy", Locale.forLanguageTag("sv"));
    private static DateTimeFormatter swedishTime = DateTimeFormatter.ofPattern("HH:mm", Locale.forLanguageTag("sv"));
    private static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM-dd HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);



    public static LocalDateTime parseDateTime(String date, String time) {
        try {
            String string = date.trim() + " " + time.trim();
            return LocalDateTime.parse(string, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date");
        } catch (NullPointerException e) {
            throw new BadRequestException("Date or time is missing");
        } catch (Exception e) {
            throw new BadRequestException("Invalid date");
        }
    }

    public static String formatDate(LocalDateTime date) {
        try{
            return date.format(DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date");
        } catch (NullPointerException e) {
            throw new BadRequestException("Date or time is missing");
        } catch (Exception e) {
            throw new BadRequestException("Invalid date");
        }
    }

    public static String formatTime(LocalDateTime date) {
        try{
            return date.format(TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date");
        } catch (NullPointerException e) {
            throw new BadRequestException("Date or time is missing");
        } catch (Exception e) {
            throw new BadRequestException("Invalid date");
        }
    }


    public static LocalDateTime expirationDateTime() {
        return LocalDateTime.now().plusMinutes(30);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        try{
            return dateTime.format(DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date");
        } catch (NullPointerException e) {
            throw new BadRequestException("Date or time is missing");
        } catch (Exception e) {
            throw new BadRequestException("Invalid date");
        }
    }

    public static String formatDateTimeSwedish(LocalDateTime eventStart) {
        try{
            String date = eventStart.format(swedishDate);
            date = date.substring(0,1).toUpperCase() + date.substring(1);
            String time = eventStart.format(swedishTime);
            return date + " kl. " + time;
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date");
        } catch (NullPointerException e) {
            throw new BadRequestException("Date or time is missing");
        } catch (Exception e) {
            throw new BadRequestException("Invalid date");
        }
    }
}
