package com.john.jpahush.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * Exception handler 유틸 클래스.
 */
@Slf4j
public class MoreExceptionHandler {
    /**
     * 발생한 예외의 내용을 로그 파일 및 메세지로 출력한다.
     * @param ex 발생한 Exception 객체
     */
    public static void Log(Exception ex)
    {
        if(ex != null) {
            if(log != null)
                log.error(ex.toString());

            ex.printStackTrace();
        }
    }
}
