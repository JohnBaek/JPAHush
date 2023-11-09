package com.john.jpahush.utils;

/**
 * Boolean 관련 유틸리티 클래스
 */
public class BooleanUtils {

    /**
     * 문자열을 Boolean 타입으로 변환한다.
     * @param value 문자열 값
     * @return 변환된 boolean 값
     */
    public static Boolean parseBoolean(String value) {

        // 0, n, no, false인 경우
        if("0".equals(value) || "n".equalsIgnoreCase(value) || "no".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value))
            return false;

        // 1, y, yes, true인 경우
        if("1".equals(value) || "y".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value))
            return true;

        return null;
    }
}
