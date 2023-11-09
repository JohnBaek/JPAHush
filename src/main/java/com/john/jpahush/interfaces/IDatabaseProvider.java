package com.john.jpahush.interfaces;

import com.john.jpahush.data.commondata.queries.SortItem;
import com.john.jpahush.data.commondata.responses.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 데이터베이스 프로바이더 인터페이스
 */
@SuppressWarnings("rawtypes,unused")
public interface IDatabaseProvider {
    /**
     * 기본 쿼리에 필요한 사용자 입력 값을 변경한다.
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
     */
    void setRequestResources(
              String name
            , boolean isOnlyProjections
            , int skip , int countPerPage
            , String[] orderFields, String[] orderDirections
            , String[] searchFields, String[] searchValues
            , String[] searchPeriodFields, Date[] searchStartDates, Date[] searchEndDates
    );

    /**
     * 주어진 쿼리 원본으로 부터 skip 만큼 건너뛰고 countPerPage 만큼의 레코드를 가져와 QueryResults 객체를 반환한다.
     * @param clazz 기본 테이블 클래스 객체
     * @param source JPAQuery<T> 객체
     * @param totalCountExpression 전체 레코드 수 조회 식
     * @param <T> 원본 타입
     * @return QueryResults<D> 객체
     */
    <T> QueryResults<T> createQueryResults(Class clazz, JPAQuery<T> source, Expression<Long> totalCountExpression);

    /**
     * 주어진 쿼리 원본으로 부터 전체 레코드가 담긴 QueryResults 객체를 반환한다.
     * @param source JPAQuery<T> 객체
     * @param <T> 원본 타입
     * @return QueryResults<D> 객체
     */
    <T> QueryResults<T> createQueryResults(JPAQuery<T> source);

    /**
     * 검색 대상이 포함되어있는지 확인한다.
     * @param key 검색어 키값
     * @return 포함되어있는경우 : true , 포함되어있지않은 경우 : false
     */
    Boolean hasKey(String key);

    /**
     * 검색 대상의 실제 검색 값을 가져온다.
     * @param key 검색어 키값
     * @return 값
     */
    List<String> getValues(String key);

    /**
     * 해당 검색어의 검색첫번째 검색값을 가져온다.
     * @param key 검색어 키값
     * @return 값
     */
    String getFirstValue(String key);

    /**
     * 기본 정렬 아이템과 기본 정렬 방향을 정한다.
     * @param column 컬럼 명
     * @param orderBy asc/desc
     */
    void setBaseSort(String column, String orderBy);

    /**
     * 정렬아이템을 등록한다.
     * @param sortItems SortItem 가변인자 전달
     */
    void setBaseSort(SortItem... sortItems);

    /**
     * 필요한 임의의 조건 목록을 추가한다.
     * @param predicates 조건 목록
     */
    void addOtherConditions(Predicate... predicates);

    /**
     * 주어진 정렬 필드 및 정렬 방향 목록으로 정렬 정보 목록을 반환한다.
     * @param defaultTable 기본 테이블명
     * @param tableAndColumns 테이블과 컬럼 매핑 목록
     * @param orderFields 정렬 필드 목록
     * @param orderDirections 정렬 방향 목록 (asc / desc)
     * @param defaultItems 기본 정렬 필드 목록
     * @return 정렬 정보 목록
     */
    List<SortItem> getSortOrders(String defaultTable, Map<String, Object[]> tableAndColumns, String[] orderFields, String[] orderDirections, SortItem... defaultItems);

    /**
     * 주어진 정렬 필드 및 정렬 방향 목록으로 정렬 정보 목록을 반환한다.
     * @param clazz 사용할 클래스 (JPASQLQuery 사용시만)
     * @param defaultTable 기본 테이블명
     * @param tableAndColumns 테이블과 컬럼 매핑 목록
     * @param orderFields 정렬 필드 목록
     * @param orderDirections 정렬 방향 목록 (asc / desc)
     * @param defaultItems 기본 정렬 필드 목록
     * @return 정렬 정보 목록
     */
    List<SortItem> getSortOrders(Class clazz, String defaultTable, Map<String, Object[]> tableAndColumns, String[] orderFields, String[] orderDirections, SortItem... defaultItems);

    /**
     * Projections 과 쿼리 그리고 응답 클래스 정보로 부터 Where 쿼리를 가져온다.
     * @param projectionArgs 프로젝션 정보
     * @param query JPA 쿼리 클래스
     * @param clazz 응답 클래스 정보
     * @return JPA쿼리 객체
     */
    <T> JPAQuery<T> getWhereQueries(Object[] projectionArgs, JPAQuery<T> query, Class clazz);

    /**
     * Projections 과 쿼리 그리고 응답 클래스 정보로 부터 OrderBy 쿼리를 가져온다.
     * @param projectionArgs 프로젝션 정보
     * @param query JPA 쿼리 클래스
     * @return JPA쿼리 객체
     */
    <T> JPAQuery<T> getOrderByQueries(Object[] projectionArgs, JPAQuery<T> query);

    /**
     * 주어진 쿼리 원본으로 부터 전체 레코드를 가져와 형 변환 후 QueryResults 객체를 반환한다.
     * @param source JPAQuery<T> 객체
     * @param orders 정렬필드 정보 목록
     * @param destClazz 대상 타입 클래스 객체
     * @param <T> 원본 타입
     * @param <D> 대상 타입
     * @return QueryResults<D> 객체
     */
    <T, D> QueryResults<D> createQueryResults(JPAQuery<T> source, List<SortItem> orders, Class<D> destClazz);

    /**
     * 주어진 쿼리 원본으로 부터 전체 레코드가 담긴 QueryResults 객체를 반환한다.
     * @param source JPAQuery<T> 객체
     * @param orders 정렬필드 정보 목록
     * @param <T> 원본 타입
     * @return QueryResults<D> 객체
     */
    <T> QueryResults<T> createQueryResults(JPAQuery<T> source, List<SortItem> orders);

    /**
     * 필드명 및 정렬 방향 문자열로 쿼리를 작성한다.
     * @param source 쿼리 소스
     * @param orders 정렬 정보 목록
     * @param <T> 대상 클래스 타입
     * @return JPAQuery<T> 객체
     */
     <T> JPAQuery<T> orderByWithDirection(JPAQuery<T> source, List<SortItem> orders);

    /**
     * 필드명 및 정렬 방향 문자열로 쿼리를 작성한다.
     * @param source 쿼리 소스
     * @param table 테이블명
     * @param field 필드명
     * @param direction 정렬 방향
     * @param <T> 대상 클래스 타입
     * @return JPAQuery<T> 객체
     */
    <T> JPAQuery<T> orderByWithDirection(JPAQuery<T> source, String table, String field, String direction);


}
