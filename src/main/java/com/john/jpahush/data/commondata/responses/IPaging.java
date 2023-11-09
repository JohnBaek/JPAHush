package com.john.jpahush.data.commondata.responses;

import java.util.List;

/**
 * 페이징 관련 인터페이스
 */
@SuppressWarnings("unused")
interface IPaging
{

	/**
	 * 건너뛴 레코드 수를 반환한다.
	 *
	 * @return 건너뛴 레코드 수
	 */
	long getSkips();

	/**
	 * 전체 레코드 수를 반환한다.
	 *
	 * @return 전체 레코드 수
	 */
	long getTotalCount();

	/**
	 * 페이지 번호를 반환한다.
	 *
	 * @return 페이지 번호
	 */
	int getPageNo();

	/**
	 * 페이지당 개수를 반환한다.
	 *
	 * @return 페이지당 개수
	 */
	int getCountPerPage();

	/**
	 * 섹션당 페이지 수를 반환한다.
	 *
	 * @return 섹션당 페이지 수
	 */
	int getPagePerSection();

	/**
	 * 전체 페이지를 반환한다.
	 *
	 * @return 전체 페이지
	 */
	int getTotalPage();

	/**
	 * 현재 섹션 내의 시작 페이지 번호를 반환한다.
	 *
	 * @return 현재 섹션 내의 시작 페이지 번호
	 */
	int getStartPageNo();

	/**
	 * 현재 섹션 내의 마지막 페이지 번호를 반환한다.
	 *
	 * @return 현재 섹션 내의 마지막 페이지 번호
	 */
	int getEndPageNo();

	/**
	 * 표시될 페이지 번호 목록을 반환한다.
	 *
	 * @return 표시될 페이지 번호 목록
	 */
	List<Integer> getPageNos();

	/**
	 * 이전 페이지가 존재하는지 여부를 반환한다.
	 *
	 * @return 이전 페이지가 존재하는지 여부
	 */
	boolean isHavePreviousPage();

	/**
	 * 다음 페이지가 존재하는지 여부를 반환한다.
	 *
	 * @return 다음 페이지가 존재하는지 여부
	 */
	boolean isHaveNextPage();

	/**
	 * 첫번째 페이지 섹션인지 여부를 반환한다.
	 *
	 * @return 첫번째 페이지 섹션인지 여부
	 */
	boolean isHavePreviousPageSection();

	/**
	 * 마지막 페이지 섹션인지 여부를 반환한다.
	 *
	 * @return the boolean
	 */
	boolean isHaveNextPageSection();

	/**
	 * 아이템 목록으로 페이징 값을 재설정한다.
	 */
	void ResetWithItems();
}
