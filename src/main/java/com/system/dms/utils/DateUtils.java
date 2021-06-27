package com.system.dms.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String transferDateFormat(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date).toString();
        return time;
    }
}
