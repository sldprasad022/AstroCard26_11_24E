package com.techpixe.serviceImpl;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class AstrologyService {

    public String getZodiacSign(String dateOfBirth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob = LocalDate.parse(dateOfBirth, formatter);
        int month = dob.getMonthValue();
        int day = dob.getDayOfMonth();

        if ((month == Month.DECEMBER.getValue() && day >= 22) || (month == Month.JANUARY.getValue() && day <= 19)) {
            return "Capricorn";
        } else if ((month == Month.JANUARY.getValue() && day >= 20) || (month == Month.FEBRUARY.getValue() && day <= 18)) {
            return "Aquarius";
        } else if ((month == Month.FEBRUARY.getValue() && day >= 19) || (month == Month.MARCH.getValue() && day <= 20)) {
            return "Pisces";
        } else if ((month == Month.MARCH.getValue() && day >= 21) || (month == Month.APRIL.getValue() && day <= 19)) {
            return "Aries";
        } else if ((month == Month.APRIL.getValue() && day >= 20) || (month == Month.MAY.getValue() && day <= 20)) {
            return "Taurus";
        } else if ((month == Month.MAY.getValue() && day >= 21) || (month == Month.JUNE.getValue() && day <= 20)) {
            return "Gemini";
        } else if ((month == Month.JUNE.getValue() && day >= 21) || (month == Month.JULY.getValue() && day <= 22)) {
            return "Cancer";
        } else if ((month == Month.JULY.getValue() && day >= 23) || (month == Month.AUGUST.getValue() && day <= 22)) {
            return "Leo";
        } else if ((month == Month.AUGUST.getValue() && day >= 23) || (month == Month.SEPTEMBER.getValue() && day <= 22)) {
            return "Virgo";
        } else if ((month == Month.SEPTEMBER.getValue() && day >= 23) || (month == Month.OCTOBER.getValue() && day <= 22)) {
            return "Libra";
        } else if ((month == Month.OCTOBER.getValue() && day >= 23) || (month == Month.NOVEMBER.getValue() && day <= 21)) {
            return "Scorpio";
        } else if ((month == Month.NOVEMBER.getValue() && day >= 22) || (month == Month.DECEMBER.getValue() && day <= 21)) {
            return "Sagittarius";
        }

        return "Unknown";
    }
}

