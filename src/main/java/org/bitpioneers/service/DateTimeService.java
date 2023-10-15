package org.bitpioneers.service;

import lombok.extern.slf4j.Slf4j;
import org.bitpioneers.exception.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* The DateTimeService class is a Spring service component designed to provide functionality related to date and time
 * calculations and checks within a software application. This documentation provides a comprehensive explanation of
 * the class's purpose, fields, methods, and usage.
* @since 1.0
 * @author Mirolim Mirzayev
*/

@Slf4j
@Service
public class DateTimeService {

    /**
    * A configuration value retrieved from external properties, indicating whether date and time checks should be performed.
    */
    @Value("${app.data-generator.time-check-mode}")
    boolean checkFlag;

    /**
    * A private method that maps an integer day of the week to its corresponding string representation (e.g., "MONDAY").
    * Used to check if the current day is a weekend day.
    */
    private String getDayOfWeekString(int dayOfWeek) {
        log.debug("Calling method getDayOfWeekString()");
        return switch (dayOfWeek) {
            case Calendar.SUNDAY -> "SUNDAY";
            case Calendar.MONDAY -> "MONDAY";
            case Calendar.TUESDAY -> "TUESDAY";
            case Calendar.WEDNESDAY -> "WEDNESDAY";
            case Calendar.THURSDAY -> "THURSDAY";
            case Calendar.FRIDAY -> "FRIDAY";
            case Calendar.SATURDAY -> "SATURDAY";
            default -> "Ошибка";
        };
    }

    /**
    * A public method that checks if the current day is allowed for certain operations based on the checkFlag.
     * If enabled, it checks if the day is not Saturday or Sunday.
    */
    public boolean isAllowedByDay(){
        if(checkFlag) {
            log.debug("Calling method isAllowedByDay()");
            Calendar calendar = Calendar.getInstance();
            String dayOfWeekString = getDayOfWeekString(calendar.get(Calendar.DAY_OF_WEEK));
            return !dayOfWeekString.equals("SATURDAY") && !dayOfWeekString.equals("SUNDAY");
        } else {
            return true;
        }
    }

    /** A public method that checks if the current time is allowed for certain operations based on the checkFlag.
    * It compares the current time against a provided time range.*
    *
    */
    public boolean isAllowedByTime(String timeLine){
        if(checkFlag) {
            log.debug("Calling method isAllowedByTime()");
            Pattern pattern = Pattern.compile("\\d\\d[:.]\\d\\d\\-\\d\\d[:.]\\d\\d");
            Matcher matcher = pattern.matcher(timeLine);
            if (matcher.find()) {
                String workTime = matcher.group(1);
                String[] fromOpenToClose = workTime.split("-");
                for (String s : fromOpenToClose) {
                    LocalDateTime currentLocalDateTime = LocalDateTime.now();
                    LocalTime parsedDateTime = parseTime(s);
                    long diff = currentLocalDateTime.until(parsedDateTime, ChronoUnit.MINUTES);
                    return diff > 0;
                }
                return false;
            } else {
                log.error("Exception was thrown");
                throw new ParseException("Could not find any matchers:" + timeLine + " for this pattern " + pattern);
            }
        } else {
            return true;
        }
    }

    /**
    *  A public method that calculates the time remaining until a specified time based on the provided time range.
     *  Used to determine the time to live for a resource.
    */
    public long getTimeToLive(String timeLine) {
        if(checkFlag) {
            log.debug("Calling method getTimeToLive()");
            Pattern pattern = Pattern.compile("\\d\\d[:.]\\d\\d\\-\\d\\d[:.]\\d\\d");
            Matcher matcher = pattern.matcher(timeLine);
            if (matcher.find()) {
                String workTime = matcher.group(1);
                String s = workTime.split("-")[1];
                LocalTime localTime = LocalTime.now();
                LocalTime parsedTime = parseTime(s);
                return localTime.until(parsedTime, ChronoUnit.MINUTES);
            } else {
                throw new RuntimeException();
            }
        } else {
            return 60L;
        }
    }

    /**
    * A private method that parses a time string into a LocalTime object.
     * It handles different time formats (e.g., "HH:mm" or "HH.mm").
    */
    private LocalTime parseTime(String time){
        log.debug("Calling method parseTime()");
        try {
            DateTimeFormatter dateTimeFormatter;
            if (time.charAt(3) == ':'){
                dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            }else {
                dateTimeFormatter = DateTimeFormatter.ofPattern("HH.mm");
            }
            return LocalTime.parse(time, dateTimeFormatter);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
