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

@Slf4j
@Service
public class DateTimeService {

    @Value("${app.data-generator.time-check-mode}")
    boolean checkFlag;

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
