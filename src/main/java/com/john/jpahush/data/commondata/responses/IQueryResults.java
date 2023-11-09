package com.john.jpahush.data.commondata.responses;

import java.util.List;

/**
 * 쿼리 결과 목록 인터페이스
 *
 * @param <T> 처리할 타입
 */
public interface IQueryResults<T> extends IPaging {
	/**
	 * 결과 목록을 반환한다.
	 *
	 * @return 결과 목록
	 */
	List<T> getItems();
}
