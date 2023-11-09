package com.john.jpahush.data.commondata.responses;

import java.time.Instant;

/**
 * 수정인 정보 인터페이스
 */
@SuppressWarnings("unused")
public interface IModifier
{
	/**
	 * 수정인 아이디를 반환한다.
	 */
	String getModId();

	/**
	 * 수정인 이름을 반환한다.
	 */
	String getModName();

	/**
	 * 수정일시를 반환한다.
	 */
	Instant getModDate();
}
