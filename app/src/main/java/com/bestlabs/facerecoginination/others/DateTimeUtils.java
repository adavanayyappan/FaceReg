package com.bestlabs.facerecoginination.others;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    public static String getCurrentDateTime(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
}
