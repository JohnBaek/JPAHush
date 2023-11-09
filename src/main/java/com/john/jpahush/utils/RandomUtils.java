package com.john.jpahush.utils;

import java.util.Date;
import java.util.Random;

/**
 * 램덤 유틸리티 클래스
 */
@SuppressWarnings("unused")
public class RandomUtils {
    private static final Random random = new Random((new Date()).getTime());

    /**
     * 양의 랜덤 long 값을 생성한다.
     * @return 결과
     */
    public static long getRandomLong() {
        return ((long)(random.nextInt(31)) << 32) + random.nextInt(32);
    }

    /**
     * 양의 랜덤 long 값을 생성한다.
     * @return 결과
     */
    public static long getBigRandomLong() {
        return getRandomLong(Integer.MAX_VALUE, Long.MAX_VALUE);
    }

    public static long getRandomLong(long min, long max) {
        long range = max - min;
        return (long)((random.nextDouble() * range)) + min;
    }

    /**
     * 양의 랜럼 int값을 생성한다.
     * @return 결과
     */
    public static int getRandomInt() {
        return random.nextInt();
    }

    public static int getRandomInt(int min, int max) {
        int range = max - min;
        return (int)((random.nextDouble() * range)) + min;
    }

    /**
     * 랜덤 문자열을 생성한다.
     * @return 결과
     */
    public static String getShortRandomString() {
        long randomLong = getRandomInt(200, Integer.MAX_VALUE);
        return convertLongToString(randomLong);
    }

    /**
     * 랜덤 문자열을 생성한다.
     * @return 결과
     */
    public static String getLongRandomString() {
        long randomLong = getRandomLong(Short.MAX_VALUE, Long.MAX_VALUE);
        return convertLongToString(randomLong);
    }

    public static String getRandomUpperString() {
        long randomLong = getRandomLong();
        return convertLongToUpperString(randomLong);
    }

    public static String getRandomEasyUpperString() {
        long randomLong = getRandomLong();
        return convertLongToEasyString(randomLong);
    }

    public static String getRandomSerialString(int size) {
        String sn = "";
        String result = "";
        if( size % 5 != 0 ) {
            size += 5 - (size % 5);
        }
        while(sn.length() < size)
            sn += convertLongToEasyString(getBigRandomLong());

        for( int i=0; i < size; i=i+5) {
            if( i > 0 )
                result += "-";
            result += sn.substring(i,  i+5);
        }

        return result;
    }

    /**
     * 해당 길이의 난수를 생성한다.
     * @param length 문자열 길이
     * @return 해당 길이의 난수 문자열
     */
    public static String getRandomNumber(int length) {
        StringBuilder result = new StringBuilder();
        Random rand = new Random();

        for(int i=0; i < length; i++) {
            //0~9 까지 난수 생성
            result.append(random.nextInt(10));
        }
        return result.toString();
    }


    private static final char[] arrLetter = "0123456789abcdefghijklnmopqrstuvwxyzABCDEFGHIJKLNMOPQRSTUVWXYZ".toCharArray();
    private static final char[] arrLetterUpper = "0123456789ABCDEFGHIJKLNMOPQRSTUVWXYZ".toCharArray();
    private static final char[] arrLetterEasy = "23456789ABCDEFGHIJKLNMOPQRSTUVWXYZ".toCharArray();

    /**
     * 긴 숫자를 문자열로 변환한다.
     * @param value 숫자
     * @return 결과
     */
    private static String convertLongToString(long value) {
        long quot = value / arrLetter.length;
        int rem = (int)(value % arrLetter.length);
        char letter = arrLetter[rem];
        if( quot == 0 )
            return ""+letter;
        else
            return convertLongToString(quot-1) + letter;
    }

    /**
     * 소문자는 제외하고, 숫자, 대문자로 변환
     * @param value 숫자
     * @return 결과
     */
    private static String convertLongToUpperString(long value) {
        long quot = value / arrLetterUpper.length;
        int rem = (int)(value % arrLetterUpper.length);
        char letter = arrLetterUpper[rem];
        if( quot == 0 )
            return ""+letter;
        else
            return convertLongToUpperString(quot-1) + letter;
    }

    /*
     * 0, 1이 영문 O, I와 쉽게 구분 안되어서 0,1을 빼고 나머지문자로 생성한다.
     */
    private static String convertLongToEasyString(long value) {
        long quot = value / arrLetterEasy.length;
        int rem = (int)(value % arrLetterEasy.length);
        char letter = arrLetterEasy[rem];
        if( quot == 0 )
            return ""+letter;
        else
            return convertLongToEasyString(quot-1) + letter;
    }
}
