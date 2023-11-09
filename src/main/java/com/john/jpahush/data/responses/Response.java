package com.john.jpahush.data.responses;

import com.john.jpahush.data.commondata.enums.EnumResponseResult;
import lombok.ToString;


/**
 * 단순 응답 결과 정보 클래스
 */
@SuppressWarnings("unused")
@ToString
public class Response extends ResponseBase {
    /**
     * 생성자
     */
    public Response()
    {
        super();
    }

    /**
     * 생성자
     * @param code 응답 코드
     * @param message 응답 메세지
     */
    public Response(String code, String message)
    {
        super(EnumResponseResult.Error, code, message, false, false);
    }

    /**
     * 생성자
     * @param code 응답 코드
     * @param message 응답 메세지
     * @param needLogin 로그인이 필요한지 여부
     * @param accessDenied 권한이 없는지 여부
     */
    public Response(String code, String message, boolean needLogin, boolean accessDenied)
    {
        super(EnumResponseResult.Error, code, message, needLogin, accessDenied);
    }

    /**
     * 생성자
     * @param result 응답 결과
     * @param code 응답 코드
     * @param message 응답 메세지
     */
    public Response(EnumResponseResult result, String code, String message)
    {
        super(result, code, message, false, false);
    }

    /**
     * 생성자
     * @param result 응답 결과
     * @param code 응답 코드
     * @param message 응답 메세지
     * @param needLogin 로그인이 필요한지 여부
     * @param accessDenied 권한이 없는지 여부
     */
    public Response(EnumResponseResult result, String code, String message, boolean needLogin, boolean accessDenied)
    {
        super(result, code, message, needLogin, accessDenied);
    }
}
