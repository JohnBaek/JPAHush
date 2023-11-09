package com.john.jpahush.data.responses;

import com.john.jpahush.data.commondata.enums.EnumResponseResult;
import com.john.jpahush.data.commondata.responses.ICommonResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 기초 응답 객체
 */
@SuppressWarnings("unused")
@Getter
@Setter
public class ResponseBase implements ICommonResponse {
    /**
     * 기본 생성자
     */
    public ResponseBase() {
    }

    /**
     * 생성자
     * @param result       응답 결과
     * @param code         응답 코드
     * @param message      응답 메세지
     * @param needLogin    응답 메세지
     * @param accessDenied 응답 메세지
     */
    public ResponseBase(EnumResponseResult result, String code, String message, boolean needLogin, boolean accessDenied) {
        this.result = result;
        this.code = code;
        this.message = message;
        this.needLogin = needLogin;
        this.accessDenied = accessDenied;
    }

    /**
     * 응답 결과
     */
    @ApiModelProperty(value = "응답 결과", dataType = "com.john.jpahush.data.commondata.enums.EnumResponseResult", required = true)
    private EnumResponseResult result = EnumResponseResult.Error;


    /**
     * 응답 코드
     */
    @ApiModelProperty(value = "응답 코드", dataType = "java.lang.String", required = true)
    private String code = "";


    /**
     * 응답 메세지
     */
    @ApiModelProperty(value = "응답 메세지", dataType = "java.lang.String", required = true)
    private String message = "";



    /**
     * 로그인 필요 여부
     */
    @ApiModelProperty(value = "로그인 필요 여부", dataType = "boolean", required = true)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    boolean needLogin = false;
    public boolean getIsNeedLogin() {
        return needLogin;
    }
    public void setIsNeedLogin(boolean value) {
        needLogin = value;
    }

    /**
     * 권한 없음 여부
     */
    @ApiModelProperty(value = "권한 없음 여부", dataType = "boolean", required = true)
    boolean accessDenied = false;
}
