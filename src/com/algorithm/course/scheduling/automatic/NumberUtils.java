package com.algorithm.course.scheduling.automatic;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Hyberbin
 * Date: 13-11-14
 * Time: 下午6:32
 */
public class NumberUtils {
    /**
     * 格式化数字
     * @param num  数字
     * @param scale 保留几位小数
     * @return
     */
    public static Float format(Object num, int scale) {
        BigDecimal b = new BigDecimal(num.toString());
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static int parseInt(Object o) {
        if (o == null) {
            return 0;
        }else if(o instanceof Integer){
           return (Integer)o;
        }
        try {
            return Integer.parseInt(o.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static Integer[] parseInts(String str){
        String[] split = str.split(",");
        Integer[] integers=new Integer[split.length];
        for(int i=0;i<split.length;i++){
            integers[i]=parseInt(split[i]);
        }
        return integers;
    }
}
