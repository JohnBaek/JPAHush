package com.john.jpahush.data.responses;

import com.john.jpahush.data.commondata.enums.EnumResponseResult;
import com.john.jpahush.data.commondata.responses.QueryResults;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 데이터 목록을 가지는 응답 결과 정보 클래스
 * @param <T> 데이터 타입
 */
@Getter
@Setter
@SuppressWarnings("unused")
public class ResponseList<T> extends ResponseBase {

    /**
     * 생성자
     */
    public ResponseList()
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
     * @param data         QueryResults<T> 데이터 오브젝트
     */
    public ResponseList(EnumResponseResult result, String code, String message, boolean needLogin, boolean accessDenied, QueryResults<T> data) {
        super(result, code, message, needLogin, accessDenied);
        this.data = data;
    }


    /**
     * 생성자
     * @param code 응답 코드
     * @param message 응답 메세지
     */
    public ResponseList(String code, String message)
    {
        super(EnumResponseResult.Error, code, message, false, false);
    }

    /**
     * 생성자
     * @param result 응답 결과
     * @param code 응답 코드
     * @param message 응답 메세지
     */
    public ResponseList(EnumResponseResult result, String code, String message)
    {
        super(result, code, message, false, false);
    }

    /**
     * 생성자
     * @param result 응답 결과
     * @param code 응답 코드
     * @param message 응답 메세지
     * @param needLogin 로그인 필요여부
     * @param accessDenied 접근 권한 없음 여부
     */
    public ResponseList(EnumResponseResult result, String code, String message, boolean needLogin, boolean accessDenied)
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
    public ResponseList(EnumResponseResult result, String code, String message, QueryResults<T> data)
    {
        this(result, code, message,  false, false , data);
    }

    /**
     * 생성자
     * @param data 데이터
     */
    public ResponseList(QueryResults<T> data)
    {
        this(EnumResponseResult.Success, "", "",  false, false,data);
    }

    /**
     * 데이터 객체
     */
    public QueryResults<T> data = null;

    /**
     * 데이터 객체를 반환한다.
     * @param data 데이터 객체
     */
    public void setData(QueryResults<T> data) {
        // data 가 null 인경우 new 로 객체 생성
        this.data = Objects.requireNonNullElseGet(data, QueryResults::new);
    }
}
