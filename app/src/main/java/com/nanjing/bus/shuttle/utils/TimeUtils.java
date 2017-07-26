package com.nanjing.bus.shuttle.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fylder on 2017/3/11.
 */

public class TimeUtils {

    public static String getTime() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd HH:mm:ss");
        return format.format(new Date());

    }
}
