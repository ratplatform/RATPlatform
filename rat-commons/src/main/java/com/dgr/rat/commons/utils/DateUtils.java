package com.dgr.rat.commons.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String getNow(String format){
        DateFormat formatter = new SimpleDateFormat(format);
        Date date = new Date();
        String s = formatter.format(date);

        return s;
    }
    
    public static Date getDate(long milliseconds, String format)throws Exception {
    	Date resultdate = new Date(milliseconds);
    	
    	return resultdate;
   	}    

    public static String parseDate(String date, String format) throws ParseException{
        DateFormat formatter = new SimpleDateFormat(format);
        Date dateOut = (Date)formatter.parse(date);
        String s = formatter.format(dateOut);

        return s;
    }

    public static Date getDate(String date, String format){
        DateFormat formatter = new SimpleDateFormat(format);
        Date dateOut = null;
        try {
            dateOut = (Date) formatter.parse(date);
        }
        catch (ParseException ex) {
        	dateOut = null;
        }

        return dateOut;
    }

    public static String getDate(Date date, String dateFormat) {

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String dateStr = formatter.format(date);

        return dateStr;
    }

    public static String getDate(Date date, Date time) {
        String dateFormat = "yyyy-MM-dd";
        String timeFormat = "HH:mm:ss";

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String dateStr = formatter.format(date);

        formatter = new SimpleDateFormat(timeFormat);
        String timeStr = formatter.format(time);

        return dateStr + "T" + timeStr + "Z";
    }

}
