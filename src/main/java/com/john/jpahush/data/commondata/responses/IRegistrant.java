package com.john.jpahush.data.commondata.responses;

import java.time.Instant;

/**
 * 등록자 정보 인터페이스
 */
@SuppressWarnings("unused")
public interface IRegistrant {

	/**
	 * 등록일시를 반환한다.
	 *
	 * @return 등록일시
	 */
	Instant getRegDate();

	/**
	 * 등록 아이디를 반환한다.
	 *
	 * @return 등록 아이디
	 */
	String getRegId();

	/**
	 * 등록 이름을 반환한다.
	 *
	 * @return 등록 이름
	 */
	String getRegName();
}
