package com.john.jpahush.data.commondata.responses;

/**
 * 인증 정보 응답 인터페이스
 */
@SuppressWarnings("unused")
public interface ILoginResponse
{
	/**
	 * 로그인 필요 여부를 반환한다.
	 */
	boolean getIsNeedLogin();

	/**
	 * 권한 없음 여부를 반환한다.
	 */
	boolean isAccessDenied();
}
