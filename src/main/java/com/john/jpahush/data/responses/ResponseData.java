package com.john.jpahush.data.responses;

import com.john.jpahush.data.commondata.enums.EnumResponseResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 데이터를 가지는 응답 결과 정보 클래스
 * @param <T> 데이터 타입
 */
@SuppressWarnings("unused")
@Getter
@Setter
public class ResponseData<T> extends ResponseBase implements Serializable {
    /**
     * 생성자
     */
    public ResponseData()
    {
        super();
    }

    /**
     * 생성자
     * @param result       응답 결과
     * @param code         응답 코드
     * @param message      응답 메세지
     * @param needLogin    응답 메세지
     * @param accessDenied 응답 메세지
     * @param data         T 데이터 오브젝트
     */
    public ResponseData(EnumResponseResult result, String code, String message, boolean needLogin, boolean accessDenied, T data) {
        super(result, code, message, needLogin, accessDenied);
        this.data = data;
    }

    /**
     * 생성자
     * @param code 응답 코드
     * @param message 응답 메세지
     */
    public ResponseData(String code, String message)
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
    public ResponseData(String code, String message, boolean needLogin, boolean accessDenied)
    {
        super(EnumResponseResult.Error, code, message, needLogin, accessDenied);
    }

    /**
     * 생성자
     * @param result 응답 결과
     * @param code 응답 코드
     * @param message 응답 메세지
     */
    public ResponseData(EnumResponseResult result, String code, String message)
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
    public ResponseData(EnumResponseResult result, String code, String message, boolean needLogin, boolean accessDenied)
    {
        super(result, code, message, needLogin, accessDenied);
    }

    /**
     * 생성자
     * @param result 응답 결과
     * @param code 응답 코드
     * @param message 응답 메세지
     * @param data 데이터
     */
    public ResponseData(EnumResponseResult result, String code, String message, T data)
    {
        this(result, code, message, false, false, data);
    }

    /**
     * 생성자
     * @param data 데이터
     */
    public ResponseData(T data)
    {
        this(EnumResponseResult.Success, "", "", data);
    }


    /**
     * 데이터 객체
     */
    @ApiModelProperty(value = "데이터", dataType = "T", required = true)
    T data = null;
}
