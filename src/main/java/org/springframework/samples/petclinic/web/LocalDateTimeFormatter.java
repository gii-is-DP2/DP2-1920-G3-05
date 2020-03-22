package org.springframework.samples.petclinic.web;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

    @Override
    public String print(LocalDateTime object, Locale locale) {
        return object.toString();
    }

    @Override
    public LocalDateTime parse(String text, Locale locale) throws ParseException {
        try{
        String[] date = text.split("T");
        String[] yearMonthDay = date[0].split("-");
        String[] hourMinute = date[1].split(":");
        int year = Integer.parseInt(yearMonthDay[0].trim());
        int month = Integer.parseInt(yearMonthDay[1].trim());
        int dayOfMonth = Integer.parseInt(yearMonthDay[2].trim());
        int hour = Integer.parseInt(hourMinute[0].trim());
        int minute = Integer.parseInt(hourMinute[1].trim());
        int second = 00;
        LocalDateTime res = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
        return res;
        }catch (Exception e){
            throw new ParseException("could not parse LocalDateTime: " + e.getMessage(), 0);
        }
    }

}