package com.john.jpahush.interfaces;

import com.john.jpahush.data.commondata.responses.QueryResults;

import java.util.Date;

public interface IDatabaseCallbackProvider {
    /**
     * createQueryResults 쿼리를 생성하고 난후 공통으로 돌려받는 콜백 메서드
     * @param name               쿼리명
     * @param isOnlyProjections  프로젝션의 세렉팅 된 컬럼만 허용하지여부
     * @param skip               건너뛸 레코드 수
     * @param countPerPage       페이지 당 레코드 수
     * @param orderFields        정렬 필드 목록
     * @param orderDirections    정렬 방향 목록 (asc / desc)
     * @param searchFields       검색 필드 목록
     * @param searchValues       검색할 값 목록
     * @param searchPeriodFields 기간 검색 필드 목록
     * @param searchStartDates   검색 시작 일시 목록
     * @param searchEndDates     검색 종료 일시 목록
     * @param queryResults       결과 쿼리
     * @param clazz              응답 리스폰스 클래스
     */
    <T> void afterCreatedQueryResultsCallback (
            String name
            , boolean isOnlyProjections
            , int skip , int countPerPage
            , String[] orderFields, String[] orderDirections
            , String[] searchFields, String[] searchValues
            , String[] searchPeriodFields, Date[] searchStartDates, Date[] searchEndDates
            , QueryResults<T> queryResults
            , Class clazz
    );
}
