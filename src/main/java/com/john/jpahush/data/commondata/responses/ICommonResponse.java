package com.john.jpahush.data.commondata.responses;


import com.john.jpahush.data.commondata.enums.EnumResponseResult;

/**
 * 응답 기본 인터페이스
 */
public interface ICommonResponse {
	/**
	 * 응답 결과를 반환한다.
	 */
	EnumResponseResult getResult();

	/**
	 * 응답 코드를 반환한다.
	 */
	String getCode();

	/**
	 * 응답 메세지를 반환한다.
	 */
	String getMessage();
}
