package io.sinso.dataland.util;

import lombok.Data;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * LocalDateUtil
 *
 * @author : alibeibei
 * @date : 2020/08/13 12:03
 */
public class LocalDateUtils {


    /**
     * @param localDate
     * @return
     */
    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneOffset.ofHours(0)).toInstant());
    }

    /**
     * @param localDateTime
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneOffset.ofHours(0)).toInstant());
    }

    /**
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneOffset.ofHours(0)).toLocalDateTime();
        return localDateTime;
    }

    /**
     * @param date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneOffset.ofHours(0)).toLocalDate();
        return localDate;
    }

    /**
     * @param localDate
     * @return
     */
    public static Long localDateToTimestamp(LocalDate localDate) {
        long timestamp = localDate.atStartOfDay(ZoneOffset.ofHours(0)).toInstant().toEpochMilli();
        return timestamp;
    }

    /**
     * @param localDateTime
     * @return
     */
    public static Long localDateTimeToTimestamp(LocalDateTime localDateTime) {
        long timestamp = localDateTime.toInstant(ZoneOffset.ofHours(0)).toEpochMilli();
        return timestamp;
    }

    /**
     * @param timestamp
     * @return
     */
    public static LocalDateTime timestampToLocalDateTime(Long timestamp) {
        LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(0)).toLocalDateTime();
        return localDateTime;
    }

    /**
     * @param timestamp
     * @return
     */
    public static LocalDate timestampToLocalDate(Long timestamp) {
        LocalDate localDate = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(0)).toLocalDate();
        return localDate;
    }

    /**
     * @param timestamp
     * @return
     */
    public static Date timestampToMinOfTheDate(Long timestamp) {
        return localDateTimeToDate(LocalDateTime.of(timestampToLocalDate(timestamp), LocalTime.MIN));
    }

    /**
     * @param timestamp
     * @return
     **/
    public static Date timestampToMaxOfTheDate(Long timestamp) {
        return localDateTimeToDate(LocalDateTime.of(timestampToLocalDate(timestamp), LocalTime.MAX));
    }

    /**
     * @return
     */
    public static LocalDateTime todayMaxWithNow(LocalDateTime now) {
        return LocalDateTime.of(now.toLocalDate(), LocalTime.MAX);
    }

    /**
     * @return
     */
    public static CustomLocalDateTime todayMinAndMax(LocalDate today) {
        CustomLocalDateTime localDateTime = new CustomLocalDateTime();
        localDateTime.setMin(LocalDateTime.of(today, LocalTime.MIN));
        localDateTime.setMax(LocalDateTime.of(today, LocalTime.MAX));
        return localDateTime;
    }

    /**
     * @return
     */
    public static CustomLocalDateTime todayMinAndMax(LocalDateTime now) {
        Long timestamp = localDateTimeToTimestamp(now);
        LocalDate today = timestampToLocalDate(timestamp);
        CustomLocalDateTime localDateTime = new CustomLocalDateTime();
        localDateTime.setMin(LocalDateTime.of(today, LocalTime.MIN));
        localDateTime.setMax(LocalDateTime.of(today, LocalTime.MAX));
        return localDateTime;
    }

    /**
     * @return
     */
    public static CustomLocalDateTime thisMonthMinAndMax(LocalDate today) {
        CustomLocalDateTime localDateTime = new CustomLocalDateTime();
        localDateTime.setMin(LocalDateTime.of(today.with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN));
        localDateTime.setMax(LocalDateTime.of(today.with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX));
        return localDateTime;
    }

    @Data
    public static class CustomLocalDateTime {

        private LocalDateTime min;

        private LocalDateTime max;
    }

    /**
     * @return yyyyMMddTHHmmssZ
     */
    public static String convertToTZ(LocalDateTime current) {
        return current.getYear() +
                checkValue(current.getMonth().getValue()) +
                checkValue(current.getDayOfMonth()) +
                "T" +
                checkValue(current.getHour()) +
                checkValue(current.getMinute()) +
                checkValue(current.getSecond()) +
                "Z";
    }

    /**
     * @return value||"0"+value
     */
    private static String checkValue(Integer value) {
        if (value.toString().length() < 2) {
            return "0" + value;
        } else {
            return value.toString();
        }
    }

    /**
     * @param date
     * @return
     */
    public static LocalDateTime stringToLocalDateTime(String date) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, df);
    }


    public static void main(String[] args) {
        Long timestampStart = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        CustomLocalDateTime customLocalDateTime = LocalDateUtils.todayMinAndMax(LocalDateTime.now());
        Long timestampEnd = customLocalDateTime.max.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println(timestampStart);
        System.out.println(timestampEnd);
        System.out.println((timestampEnd - timestampStart) / 1000);
        System.out.println((Duration.between(LocalDateTime.now(), customLocalDateTime.max).toMinutes()));
    }
}
