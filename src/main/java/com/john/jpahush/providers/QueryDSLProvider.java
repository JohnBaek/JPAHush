package com.john.jpahush.providers;

import com.john.jpahush.annotations.SearchFilter;
import com.john.jpahush.annotations.SearchFilterImplement;
import com.john.jpahush.data.commondata.enums.EnumFieldSearchType;
import com.john.jpahush.data.commondata.queries.*;
import com.john.jpahush.data.commondata.responses.QueryResults;
import com.john.jpahush.interfaces.IDatabaseCallbackProvider;
import com.john.jpahush.interfaces.IDatabaseProvider;
import com.john.jpahush.utils.BooleanUtils;
import com.john.jpahush.utils.MoreExceptionHandler;
import com.john.jpahush.utils.ObjectUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 데이터베이스 프로바이더 구현체
 */
@Component
@SuppressWarnings("rawtypes")
public class QueryDSLProvider implements IDatabaseProvider {
    /**
     * 쿼리명
     */
    String name = "";

    /**
     * 건너뛸 레코드 수
     */
    int skip = 0;

    /**
     * 페이지 당 레코드 수
     */
    int countPerPage = 20;

    /**
     * 정렬 필드 목록
     */
    String[] orderFields = new String[0];

    /**
     * 정렬 방향 목록 (asc / desc)
     */
    String[] orderDirections = new String[0];

    /**
     * 검색 필드 목록
     */
    String[] searchFields = new String[0];

    /**
     * 검색할 값 목록
     */
    String[] searchValues = new String[0];

    /**
     * 기간 검색 필드 목록
     */
    String[] searchPeriodFields = new String[0];

    /**
     * 검색 시작 일시 목록
     */
    Date[] searchStartDates = new Date[0];

    /**
     * 검색 종료 일시 목록
     */
    Date[] searchEndDates = new Date[0];

    /**
     * 검색 필드및 검색 값
     */
    Map<String, List<String>> searchFieldAndValues = new HashMap<>();

    /**
     * 정렬 필드
     */
    List<SortItem> sorts = new ArrayList<>();

    /**
     * 조건
     */
    List<Predicate> predicates = new ArrayList<>();

    /**
     * 쿼리 안 테이블 정보
     */
    List<JoinExpression> queryTables = new ArrayList<>();

    /**
     * true 인경우 프로젝션에 포함된 항목만 Select/OrderBy 처리한다.
     */
    boolean isUseOnlyProjections = false;

    /**
     * 쿼리가 만들어지고 난뒤 호출될 콜백
     */
    IDatabaseCallbackProvider m_queryDSLProviderCallback;

    /**
     * 생성자
     * @param queryDSLProviderCallback 콜백 이벤트
     */
    public QueryDSLProvider(IDatabaseCallbackProvider queryDSLProviderCallback) {
        this.m_queryDSLProviderCallback = queryDSLProviderCallback;
    }

    /**
     * 기본 쿼리에 필요한 사용자 입력 값을 변경한다.
     * @param name               쿼리명
     * @param isOnlyProjections  프로젝션의 셀렉팅 된 컬럼만 허용하지여부
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
    @Override
    public void setRequestResources(String name, boolean isOnlyProjections , int skip, int countPerPage, String[] orderFields, String[] orderDirections, String[] searchFields, String[] searchValues, String[] searchPeriodFields, Date[] searchStartDates, Date[] searchEndDates) {
        this.name               = name;
        this.skip               = skip;
        this.countPerPage       = countPerPage;
        this.orderFields        = orderFields;
        this.orderDirections    = orderDirections;
        this.searchFields       = searchFields;
        this.searchValues       = searchValues;
        this.searchPeriodFields = searchPeriodFields;
        this.searchStartDates   = searchStartDates;
        this.searchEndDates     = searchEndDates;
        this.isUseOnlyProjections = isOnlyProjections;

        if(this.orderFields == null)
            orderFields = new String[0];
        if(this.orderDirections == null)
            orderDirections = new String[0];
        if(this.searchFields == null)
            searchFields = new String[0];
        if(this.searchValues == null)
            searchValues = new String[0];
        if(this.searchPeriodFields == null)
            searchPeriodFields = new String[0];
        if(this.searchStartDates == null)
            searchStartDates = new Date[0];
         if(this.searchEndDates == null)
            searchEndDates = new Date[0];

         this.predicates.clear();
         this.sorts.clear();
         this.queryTables.clear();
      }

    /**
     * 주어진 쿼리 원본으로 부터 skip 만큼 건너뛰고 countPerPage 만큼의 레코드를 가져와 QueryResults 객체를 반환한다.
     * @param clazz 기본 테이블 클래스 객체
     * @param source JPAQuery<T> 객체
     * @param totalCountExpression 전체 레코드 수 조회 식
     * @param <T> 원본 타입
     * @return QueryResults<D> 객체
     */
    @Override
    public <T> QueryResults<T> createQueryResults(Class clazz, JPAQuery<T> source, Expression<Long> totalCountExpression) {
        QueryResults<T> result = new QueryResults<>();
        List<T> sourceList;

        try {
            if (source != null)
            {
                // 쿼리 저장
                JPAQuery<T> query = source.clone();

                // 프로젝션 정보를 가져온다.
                Object[] projectionArgs = Arrays.stream(Objects.requireNonNull(query.getMetadata().getProjection()).toString().split(",")).toArray();

                // 쿼리 테이블 정보를 가져온다.
                this.queryTables =  ((DefaultQueryMetadata) query.getMetadata()).getJoins();

                // 프로젝션에 있는것만 셀렉트하는경우
                if(this.isUseOnlyProjections) {
                    if(this.orderFields == null)
                        orderFields = new String[0];
                    if(this.searchValues == null)
                        searchValues = new String[0];
                    if(this.searchPeriodFields == null)
                        searchPeriodFields = new String[0];
                    if(this.searchStartDates == null)
                        searchStartDates = new Date[0];
                    if(this.searchEndDates == null)
                        searchEndDates = new Date[0];
                    if(this.searchFields == null)
                        this.searchFields = new String[0];

                    // 소트정리
                    List<String> newOrderFields = new ArrayList<>();
                    for (String orderField : this.orderFields) {
                        // 일치하는 컬럼이 있는경우
                        if(Arrays.stream(projectionArgs).anyMatch(i -> i.toString().contains(orderField)))
                            newOrderFields.add(orderField);
                    }

                    this.orderFields = null;
                    this.orderFields = newOrderFields.toArray(new String[0]);

                    // where 조건 정리
                    List<String> searches = new ArrayList<>();
                    for (String searchField : this.searchFields) {
                        // 일치하는 컬럼이 있는경우
                        if(Arrays.stream(projectionArgs).anyMatch(i -> i.toString().contains(searchField)))
                            searches.add(searchField);
                    }

                    this.searchFields = null;
                    this.searchFields = searches.toArray(new String[0]);
                }

                // 소트정보를 쿼리에 추가한다.
                // 셀렉트절에 포함되지 않은 구문인경우 찾을수있도록 추가
                query = this.getOrderByQueries(projectionArgs,query);

                // 조건 정보를 쿼리에 추가한다.
                // 셀렉트절에 포함되지 않은 구문인경우 찾을수있도록 추가
                query = this.getWhereQueries(projectionArgs,query,clazz);

                // 스킵이 존재하는 경우
                if (skip > 0)
                    query = query.offset(skip);

                // 전체 페이지가 아닌 경우
                if (countPerPage < Integer.MAX_VALUE)
                    query = query.limit(countPerPage);

                // 페이징을 적용한 목록을 가져온다.
                sourceList = query.fetch();

                // 목록 수 저장
                long totalCount = sourceList == null ? 0 : sourceList.size();

                // 전체 레코드 수 조회 식이 존재하는 경우
                if(totalCountExpression != null)
                    // 전체 레코드 수 쿼리 수행
                    totalCount = source.select(totalCountExpression).fetchFirst();

                // 페이징을 적용하여 목록 응답 객체 생성
                result = new QueryResults<>(sourceList
                        , totalCount, skip, countPerPage, 20);

                // 콜백 메서드를 호출해준다.
                this.m_queryDSLProviderCallback.afterCreatedQueryResultsCallback( name,isUseOnlyProjections,skip,countPerPage, orderFields, orderDirections, searchFields, searchValues, searchPeriodFields,searchStartDates,searchEndDates,result, clazz);
            }
        } catch (Exception e) {
            MoreExceptionHandler.Log(e);
        }

        return result;
    }

    /**
     * Projections 과 쿼리 그리고 응답 클래스 정보로 부터 Where 쿼리를 가져온다.
     * @param projectionArgs 프로젝션 정보
     * @param query JPA 쿼리 클래스
     * @param clazz 응답 클래스 정보
     * @return JPA쿼리 객체
     */
    @Override
    public <T> JPAQuery<T> getWhereQueries(Object[] projectionArgs, JPAQuery<T> query, Class clazz) {
        try {
            // 추가할 검색 조건 목록이 존재하는 경우
            if(predicates != null){
                // 모든 조건에 대해 처리한다
                for (Predicate item : predicates) {
                    // null 이 아닌경우
                    if(item != null)
                        // 조건 추가
                        query.where(item);
                }
            }

            // 사용자의 검색정보를 가져온다.
            List<KeywordSearchItem> keywordSearchItems = this.getSearchFields(clazz,projectionArgs);

            // Enum으로 변환할 검색 목록
            List<KeywordSearchItem> enumConvertKeywordSearchItems = keywordSearchItems.stream().filter((value) -> value.getKeywordSearchType() == EnumFieldSearchType.Enum).collect(Collectors.toList());

            // 모든 변환 검색에 대해서 처리
            for(KeywordSearchItem item: enumConvertKeywordSearchItems) {
                List<String> enumValueList = new ArrayList<>();
                for(String value: item.getKeywords()) {
                    value = value.trim();
                    // Enum 값을 가져온다.
                    Enum enumValue = Enum.valueOf(item.getConvertTypeClass(), value);
                    // 해당 값으로 대체
                    enumValueList.add(enumValue.toString());
                }
                item.setKeywords(enumValueList);
            }

            List<Predicate> keywordSearchPredicates = new ArrayList<>();

            // 모든 항목에 대해서 처리
            for(KeywordSearchItem item: keywordSearchItems) {
                String[] tableAndField = item.getField().split("[.]");

                switch (item.getKeywordSearchType()) {
                    case Number:
                        // clazz가 null인 경우
                        if(clazz == null) {
                            keywordSearchPredicates.add(Expressions.numberPath(item.getConvertTypeClass(), item.getField()).like(item.getKeywords().get(0)));
                        }
                        // clazz가 지정된 경우
                        else {
                            if(tableAndField.length >= 2) {
                                PathBuilder pathBuilder = new PathBuilder(clazz, tableAndField[0]);
                                keywordSearchPredicates.add(pathBuilder.get(Expressions.numberPath(item.getConvertTypeClass(), tableAndField[1])).like(item.getKeywords().get(0)));
                            }
                        }
                        break;
                    case Boolean:
                        // clazz가 null인 경우
                        if(clazz == null) {
                            keywordSearchPredicates.add(Expressions.booleanPath(item.getField()).eq(BooleanUtils.parseBoolean(item.getKeywords().get(0))));
                        }
                        // clazz가 지정된 경우
                        else {
                            if(tableAndField.length >= 2) {
                                PathBuilder pathBuilder = new PathBuilder(clazz, tableAndField[0]);
                                keywordSearchPredicates.add(pathBuilder.get(Expressions.booleanPath(tableAndField[1])).eq(BooleanUtils.parseBoolean(item.getKeywords().get(0))));
                            }
                        }
                        break;
                    case Enum:
                    case List:
                        // clazz가 null인 경우
                        if(clazz == null) {
                            keywordSearchPredicates.add(Expressions.stringPath(item.getField()).in(item.getKeywords()));
                        }
                        // clazz가 지정된 경우
                        else {
                            if(tableAndField.length >= 2) {
                                PathBuilder pathBuilder = new PathBuilder(clazz, tableAndField[0]);
                                keywordSearchPredicates.add(pathBuilder.get(Expressions.stringPath(tableAndField[1])).in(item.getKeywords()));
                            }
                        }
                        break;
                    case BooleanList:
                        // clazz가 null인 경우
                        if(clazz == null) {
                            keywordSearchPredicates.add(Expressions.booleanPath(item.getField()).in(item.getKeywords().stream().map(BooleanUtils::parseBoolean).collect(Collectors.toList())));
                        }
                        // clazz가 지정된 경우
                        else {
                            if(tableAndField.length >= 2) {
                                PathBuilder pathBuilder = new PathBuilder(clazz, tableAndField[0]);
                                keywordSearchPredicates.add(pathBuilder.get(Expressions.booleanPath(tableAndField[1])).in(item.getKeywords().stream().map(BooleanUtils::parseBoolean).collect(Collectors.toList())));
                            }
                        }
                        break;
                    case StringEqual:
                        // clazz가 null인 경우
                        if(clazz == null) {
                            keywordSearchPredicates.add(Expressions.stringPath(item.getField()).eq(item.getKeywords().get(0)));
                        }
                        // clazz가 지정된 경우
                        else {
                            if(tableAndField.length >= 2) {
                                PathBuilder pathBuilder = new PathBuilder(clazz, tableAndField[0]);
                                keywordSearchPredicates.add(pathBuilder.get(Expressions.stringPath(tableAndField[1])).eq(item.getKeywords().get(0)));
                            }
                        }
                        break;
                    case PhoneNo:
                        // clazz가 null인 경우
                        if(clazz == null) {
                            keywordSearchPredicates.add(Expressions.stringPath(item.getField()).like("%" + item.getKeywords().get(0).replaceAll("-", "") + "%"));
                        }
                        // clazz가 지정된 경우
                        else {
                            if(tableAndField.length >= 2) {
                                PathBuilder pathBuilder = new PathBuilder(clazz, tableAndField[0]);
                                keywordSearchPredicates.add(pathBuilder.get(Expressions.stringPath(tableAndField[1])).like("%" + item.getKeywords().get(0).replaceAll("-", "") + "%"));
                            }
                        }
                        break;
                    default:
                        // clazz가 null인 경우
                        if(clazz == null) {
                            keywordSearchPredicates.add(Expressions.stringPath(item.getField()).like("%" + item.getKeywords().get(0) + "%"));
                        }
                        // clazz가 지정된 경우
                        else {
                            if(tableAndField.length >= 2) {
                                PathBuilder pathBuilder = new PathBuilder(clazz, tableAndField[0]);
                                keywordSearchPredicates.add(pathBuilder.get(Expressions.stringPath(tableAndField[1])).like("%" + item.getKeywords().get(0) + "%"));
                            }
                        }
                        break;
                }
            }

            // 키워드 검색 조건이 존재하는 경우
            if(keywordSearchPredicates.size() > 0)
                query.where(new BooleanBuilder().orAllOf(keywordSearchPredicates.toArray(new Predicate[0])));

            // 기간 검색 목록을 가져온다.
            List<PeriodSearchItem> periodSearchItems = this.getPeriodSearchFields(clazz,projectionArgs);
            List<Predicate> periodSearchPredicates = new ArrayList<>();

            // 모든 항목에 대해서 처리
            for(PeriodSearchItem item: periodSearchItems) {
                periodSearchPredicates.add(
                        Expressions.datePath(Instant.class, item.getField()).goe(item.getSearchStartDate().toInstant())
                                .and(Expressions.datePath(Instant.class, item.getField()
                                ).lt(item.getSearchEndDate().toInstant()))
                );
            }

            // 기간 검색 조건이 존재하는 경우
            if(periodSearchPredicates.size() > 0)
                query.where(new BooleanBuilder().orAllOf(periodSearchPredicates.toArray(new Predicate[0])));

        } catch (Exception e) {
            MoreExceptionHandler.Log(e);
        }
        return query;
    }

    /**
     * Projections 과 쿼리 그리고 응답 클래스 정보로 부터 OrderBy 쿼리를 가져온다.
     * @param projectionArgs 프로젝션 정보
     * @param query JPA 쿼리 클래스
     * @return JPA쿼리 객체
     */
    @Override
    public <T> JPAQuery<T> getOrderByQueries(Object[] projectionArgs, JPAQuery<T> query) {

        // 정렬하고자 하는 필드가 없는 경우
        if(this.orderFields != null) {
            // 정렬 방향 목록이 지정되지 않은 경우
            if(this.orderDirections == null || this.orderDirections.length == 0) {
                // 모든 정렬 필드에 대하서 처리
                for (String orderField: orderFields) {
                    this.sorts.add(new SortItem(orderField, "asc"));
                }
            }
            // 정렬 방향 정보가 정렬 필드보다 적은 경우
            else if(orderFields.length > this.orderDirections.length){
                // 모든 정렬 필드에 대하서 처리
                for (int index = 0; index < this.orderFields.length; index++) {
                    // 기본 정렬 방향으로 객체 생성
                    SortItem sortItem = new SortItem(this.orderFields[index], "asc");
                    // 정렬 방향 정보가 존재하는 경우, 정렬 방향 수정
                    if(index < this.orderDirections.length)
                        sortItem.setDirection(this.orderDirections[index]);
                    // 데이터 추가
                    this.sorts.add(sortItem);
                }
            }
            // 그 외
            else {
                // 모든 정렬 필드에 대하서 처리
                for (int index = 0; index < this.orderFields.length; index++) {
                    // 정렬 필드와 정렬 방향으로 객체 생성
                    SortItem sortItem = new SortItem(this.orderFields[index], this.orderDirections[index]);
                    // 데이터 추가
                    this.sorts.add(sortItem);
                }
            }
        }

        // 전체 소팅에 대해 처리한다.
        for (SortItem sort : this.sorts) {
            // table.field 된 셀렉트구문 을 사용자가 요청한 소트 정보로 일치하는 정보를 찾는다.
            String statement = "";
            statement = findStatementForDot(projectionArgs,sort);

            // 찾지못한 경우 as 로 정의된 정보를 찾는다.
            if(!StringUtils.hasText(statement))
                statement = findStatementForAs(projectionArgs,sort);

            // 찾지못한 경우 테이블 정보속에서 전체 검색한다.
            if(!StringUtils.hasText(statement))
                statement = findWhereStatementFromTable(sort.getField());

            // 찾은 경우
            if(StringUtils.hasText(statement)){
                // 정렬 방향별로 처리한다.
                if(sort.getDirection().equalsIgnoreCase("desc"))
                    query = query.orderBy(Expressions.stringPath(statement).desc());
                else
                    query = query.orderBy(Expressions.stringPath(statement).asc());
            }
        }

        return query;
    }

    /**
     * 프로젝션 속에서 table.field 로 되어있는 구문을 찾는다.
     * @param projectionArgs 프로젝션 정보
     * @param sort Sort 아이템
     */
    private String findStatementForDot(Object[] projectionArgs, SortItem sort){
        String statement = "";
        try {
            // 가능성높은 키워드를 만든다.
            String dotWithField = String.format(".%s",sort.getField());

            // 쿼리안에서 키워드와 일치하는 셀렉트 구문을 찾는다.
            List<String> foundKeywords = Arrays.stream(projectionArgs).filter(i -> i.toString().contains(dotWithField)).map(Objects::toString).collect(Collectors.toList());

            // orderBy 가능한 필드를 찾았다면
            if(foundKeywords.size() > 0) {
                // 대상을 가져온다.
                String target = foundKeywords.get(0);

                // . 으로 분리
                String table = Arrays.stream(target.split("\\.")).findFirst().orElse("");

                // 공백 제거
                table = table.trim();

                // 유효한 텍스트가 아닌경우
                if(!StringUtils.hasText(table))
                    return "";

                statement = String.format("%s.%s", table , sort.getField()).replace("+","");
            }
        } catch (Exception ex) {
            MoreExceptionHandler.Log(ex);
        }

        return statement;
    }

    /**
     * 프로젝션 속에서 table.field 로 되어있는 구문을 찾는다.
     * @param fieldName 필드명
     */
    private String findWhereStatementFromTable(String fieldName){
        String statement = "";
        try {
            // 모든 테이블 정보에서 찾는다.
            for (JoinExpression joinExpression : this.queryTables) {
                // Q queryDsL 클래스로 리플렉션을 시도한다.
                Class QClass = joinExpression.getTarget().getClass();
                List<Field> fields = Arrays.stream(QClass.getFields()).collect(Collectors.toList());

                // 필드명을 찾는다.
                Optional<Field> optional = fields.stream().filter(i -> i.getName().contains(fieldName)).findFirst();

                // 찾은 경우
                if(optional.isPresent()) {
                    statement = String.format("%s.%s", joinExpression.getTarget() , fieldName);
                    break;
                }
            }
        } catch (Exception ex) {
            MoreExceptionHandler.Log(ex);
        }

        return statement;
    }

    /**
     * 테이블 정보 속에서 구문을 찾는다.
     * @param projectionArgs 프로젝션 정보
     * @param sort Sort 아이템
     */
    private String findStatementForAs(Object[] projectionArgs, SortItem sort){
        String statement = "";
        try {
            // AS 로 된 키워드를 만든다.
            String asWithField = String.format("as %s",sort.getField());

            // 쿼리안에서 키워드와 일치하는 셀렉트 구문을 찾는다.
            List<String> foundKeywords = Arrays.stream(projectionArgs).filter(i -> i.toString().contains(asWithField)).map(Objects::toString).collect(Collectors.toList());

            // orderBy 가능한 필드를 찾았다면
            if(foundKeywords.size() > 0) {
                // 대상을 가져온다.
                String target = foundKeywords.get(0);

                // as 으로 분리
                String field = target.split("as ")[target.split("as ").length - 1];

                // 유효한 텍스트가 아닌경우
                if(!StringUtils.hasText(field))
                    return "";
            }
        } catch (Exception ex) {
            MoreExceptionHandler.Log(ex);
        }

        return statement;
    }


    /**
     * 요청에 해당 검색어가 포함되어있는지 확인한다.
     * @param key 검색어 키값
     * @return 포함되어있는경우 : true , 포함되어있지않은 경우 : false
     */
    @Override
    public Boolean hasKey(String key) {
        return this.searchFieldAndValues.containsKey(key.toLowerCase());
    }

    /**
     * 해당 검색어의 검색값을 가져온다.
     * @param key 검색어 키값
     * @return 값
     */
    @Override
    public List<String> getValues(String key) {
        List<String> result = this.searchFieldAndValues.get(key.toLowerCase());
        return Objects.requireNonNullElseGet(result, ArrayList::new);
    }

    /**
     * 해당 검색어의 검색값 리스트중 첫번째 검색값을 가져온다.
     * @param key 검색어 키값
     * @return 값
     */
    @Override
    public String getFirstValue(String key) {
        // 검색값을 가져온다.
        List<String> result = this.getValues(key.toLowerCase());
        if(result.size() > 0)
            return result.get(0);
        else
            return "";
    }

    /**
     * 기본 정렬 아이템과 기본 정렬 방향을 정한다.
     * @param column 컬럼 명
     * @param orderBy asc/desc
     */
    @Override
    public void setBaseSort(String column, String orderBy) {
        // 사용자가 지정한 OrderBy 구문이 없는경우
        if(this.orderFields == null || this.orderFields.length == 0)
            this.sorts.add(new SortItem(column,orderBy));
    }

    /**
     * 정렬아이템을 등록한다.
     * @param sortItems SortItem 가변인자 전달
     */
    @Override
    public void setBaseSort(SortItem... sortItems) {
        // 정렬 정보가 존재하는 경우
        if(sortItems[0] != null)
            // 모든 기본 항목에 대해서 처리
            sorts.addAll(Arrays.asList(sortItems));
    }

    /**
     * 주어진 정렬 필드 및 정렬 방향 목록으로 정렬 정보 목록을 반환한다.
     * @param defaultTable 기본 테이블명
     * @param tableAndColumns 테이블과 컬럼 매핑 목록
     * @param orderFields 정렬 필드 목록
     * @param orderDirections 정렬 방향 목록 (asc / desc)
     * @param defaultItems 기본 정렬 필드 목록
     * @return 정렬 정보 목록
     */
    @Override
    public List<SortItem> getSortOrders(String defaultTable, Map<String, Object[]> tableAndColumns, String[] orderFields, String[] orderDirections, SortItem... defaultItems)
    {
        return getSortOrders(null, defaultTable, tableAndColumns, orderFields, orderDirections, defaultItems);
    }

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
    @Override
    public List<SortItem> getSortOrders(Class clazz, String defaultTable, Map<String, Object[]> tableAndColumns, String[] orderFields, String[] orderDirections, SortItem... defaultItems)
    {
        List<SortItem> result = new ArrayList<>();

        // 정렬 필드가 지정되지 않은 경우, 기본 정렬 필드 설정
        if (orderFields == null || orderFields.length == 0) {
            // 모든 기본 항목에 대해서 처리
            for (SortItem sortItem : defaultItems) {
                result.add(new SortItem(getFieldPath(defaultTable, tableAndColumns, sortItem.getField()), sortItem.getDirection(), clazz));
            }
        }
        // 정렬 필드가 지정되어 있는 경우
        else {
            // 정렬 방향 목록이 지정되지 않은 경우
            if(orderDirections == null || orderDirections.length == 0) {
                // 모든 정렬 필드에 대하서 처리
                for (String orderField: orderFields) {
                    // 기본 정렬 방향으로 객체 생성
                    SortItem sortItem = new SortItem(getFieldPath(defaultTable, tableAndColumns, orderField), "asc", clazz);
                    // 데이터 추가
                    result.add(sortItem);
                }
            }
            // 정렬 방향 정보가 정렬 필드보다 적은 경우
            else if(orderFields.length > orderDirections.length){
                // 모든 정렬 필드에 대하서 처리
                for (int index = 0; index < orderFields.length; index++) {
                    // 기본 정렬 방향으로 객체 생성
                    SortItem sortItem = new SortItem(getFieldPath(defaultTable, tableAndColumns, orderFields[index]), "asc", clazz);
                    // 정렬 방향 정보가 존재하는 경우, 정렬 방향 수정
                    if(index < orderDirections.length)
                        sortItem.setDirection(orderDirections[index]);
                    // 데이터 추가
                    result.add(sortItem);
                }
            }
            // 그 외
            else {
                // 모든 정렬 필드에 대하서 처리
                for (int index = 0; index < orderFields.length; index++) {
                    // 정렬 필드와 정렬 방향으로 객체 생성
                    SortItem sortItem = new SortItem(getFieldPath(defaultTable, tableAndColumns, orderFields[index]), orderDirections[index], clazz);
                    // 데이터 추가
                    result.add(sortItem);
                }
            }
        }

        return result;
    }

    /**
     * 필요한 임의의 조건 목록을 추가한다.
     * @param predicates 조건 목록
     */
    @Override
    public void addOtherConditions(Predicate... predicates) {
        this.predicates.clear();

        // 조건이 존재 하는경우
        if(predicates[0] != null)
            this.predicates.addAll(Arrays.asList(predicates));
    }

    /**
     * 사용자가 검색 요청하는 값을 기준으로 , 셀렉트 메타정보속에서 일치하는 쿼리 정보를 찾아낸다.
     * @param possibleKey 찾고자 시도하는 값
     * @param projectionArgs 프로젝션 정보
     */
    private String findWhereStatement(String possibleKey, Object[] projectionArgs) {
        String result = "";
        try {
            // 가능성높은 키워드를 만든다.
            String dotWithField = String.format(".%s",possibleKey);

            // 쿼리안에서 키워드와 일치하는 셀렉트 구문을 찾는다.
            List<String> foundKeywords = Arrays.stream(projectionArgs).filter(i -> i.toString().contains(dotWithField)).map(Objects::toString).collect(Collectors.toList());

            // where 가능한 필드를 찾았다면
            if(foundKeywords.size() > 0) {
                // 대상을 가져온다.
                String target = foundKeywords.get(0);

                // . 으로 분리
                String table = Arrays.stream(target.split("\\.")).findFirst().orElse("");

                // 공백 제거
                table = table.trim();

                // 유효한 텍스트가 아닌경우
                if(!StringUtils.hasText(table))
                    return "";

                return String.format("%s.%s", table , possibleKey).replace("+","");
            }
        }catch (Exception ex) {
            MoreExceptionHandler.Log(ex);
        }
        return result;
    }

    /**
     * 실제 필드명을 테이블 이름과 함께 반환한다.
     * @param defaultTable 기본 테이블명
     * @param tableAndColumns 테이블과 컬럼 매핑 목록
     * @return 테이블명
     */
    protected String getFieldPath(String defaultTable, Map<String, Object[]> tableAndColumns, String field) {
        // 매핑 및 필드명이 존재하는 경우
        if(tableAndColumns != null && tableAndColumns.size() > 0 && StringUtils.hasText(field)) {
            for(String table: tableAndColumns.keySet()) {
                // 문자열에 대한 배열인 경우
                if(tableAndColumns.get(table).getClass().getName().endsWith("String;")) {
                    // 문자열 배열에 해당 이름이 있는 경우
                    if (Arrays.asList(tableAndColumns.get(table)).contains(field)) {
                        if(StringUtils.hasText(table))
                            return table + "." + field;
                        else
                            return field;
                    }
                }
                // 컬럼에 대한 별칭인 경우
                else if(tableAndColumns.get(table).getClass().getName().endsWith("ColumnAlias;")) {
                    // 별칭에서 해당 이름이 있는 객체를 가져온다.
                    ColumnAlias columnAlias = (ColumnAlias) Arrays.stream(tableAndColumns.get(table)).filter((value) -> ((ColumnAlias) value).getAliasName().equals(field)).findFirst().orElse(null);
                    // 별칭에서 해당 이름이 있는 경우
                    if (columnAlias != null) {
                        if(StringUtils.hasText(table))
                            return table + "." + columnAlias.getFieldName();
                        else
                            return columnAlias.getFieldName();
                    }
                }
            }
        }

        return defaultTable + "." + field;
    }

    /**
     * 사용자의 요청 정보와 프로젝션 정보로 검색 키워드 리스트정보로 변환하여 리턴한다.
     * @param clazz 응답 클래스 정보
     * @param projectionArgs 프로젝션 정보
     * @return List<KeywordSearchItem>
     */
    protected List<KeywordSearchItem> getSearchFields(Class clazz, Object[] projectionArgs)
    {
        List<KeywordSearchItem> result = new ArrayList<>();

        // 검색 필드 및 검색할 값이 존재하는 경우
        if(searchFields != null && searchFields.length > 0 && searchValues != null && searchValues.length > 0 && searchFields.length == searchValues.length)
        {
            Map<String, KeywordSearchItem> searchFieldMap = new HashMap<>();

            // 실제 스트링패스
            String columnPath = "";

            // 검색 필드 목록을 모두 소문자로 변환
            for(int index = 0; index < searchFields.length; index++) {
                String[] values = searchValues[index].split(";");

                // 검색할 필드명을 가져온다.
                String searchField = searchFields[index];

                // 필터정보
                SearchFilterImplement foundFilter = null;

                // 응답 클래스 형 으로부터 찾는다., 즉 필터는 응답클래스를 우선시한다.
                foundFilter = findSearchFilterInClass(clazz,searchField);
                Field foundFilterField = null;
                // 찾지 못한경우
                if(foundFilter == null){
                    foundFilterField = getFieldFromTable(searchField);

                    // 리플렉션으로 찾아온 필드가 있다면
                    if(foundFilterField != null){
                        // 메타정보 로부터 찾는다.
                        foundFilter = findSearchFilterInFromTableMetas(foundFilterField);
                    }
                }

                // 메타 정보로부터도 찾지못한경우
                if(foundFilter == null){
                    // 기본 필터정보로 세팅한다.
                    foundFilter = new SearchFilterImplement(foundFilterField !=null? foundFilterField.getType() : String.class,"",EnumFieldSearchType.StringLike,true);
                }

                // 프로젝션 정보에서 ColumnPath 를 찾는다.
                columnPath = foundColumnPathInProjection(searchField,projectionArgs);

                // 프로젝션 정보에서 찾지 못한경우
                if(!StringUtils.hasText(columnPath)) {
                    // 필드 정보가 존재할경우
                    if(foundFilterField != null) {
                        // 클래스 정보를 이용하여 검색 필드 정보를 만든다.
                        String[] splited = foundFilterField.getDeclaringClass().getName().split("\\.");
                        columnPath = String.format("%s.%s",lowercaseFirstLetter(splited[splited.length-1]), searchField);
                    }
                }

                // 해당 검색 필드가 존재하지 않는 경우
                if(!searchFieldMap.containsKey(columnPath))
                    searchFieldMap.put(columnPath, new KeywordSearchItem(searchField, columnPath, List.of(values), foundFilter.searchType(), foundFilter.getType()));
            }

            result.addAll(searchFieldMap.values());
        }

        return result;
    }

    String lowercaseFirstLetter(String input) {
        if (input.isEmpty()) {
            return input;
        }

        char firstChar = Character.toLowerCase(input.charAt(0));
        return firstChar + input.substring(1);
    }

    /**
     *
     * @param searchField
     * @return
     */
    private String foundColumnPathInTableMetas(String searchField) {
        String result = "";
        try {
            // 모든 테이블 정보에서 찾는다.
            for (JoinExpression joinExpression : this.queryTables) {
                // Q queryDsL 클래스로 리플렉션을 시도한다.
                Class EntityClass = joinExpression.getTarget().getClass().getSuperclass();
                List<Field> fields = Arrays.stream(EntityClass.getFields()).collect(Collectors.toList());

                // 필드명을 찾는다.
                Optional<Field> optional = fields.stream().filter(i -> i.getName().contains(searchField)).findFirst();

                // 찾은 경우
                if(optional.isPresent()) {
                    result = String.format("%s.%s", joinExpression.getTarget() , searchField);
                    break;
                }
            }
        }catch (Exception ex){
            MoreExceptionHandler.Log(ex);
        }
        return result;
    }

    /**
     *
     * @param searchField
     * @param projectionArgs
     * @return
     */
    private String foundColumnPathInProjection(String searchField, Object[] projectionArgs) {
        String result = "";
        try {
            // StringPath 데이터를 가져온다.
            result = findWhereStatement(searchField,projectionArgs);
        }catch (Exception ex){
            MoreExceptionHandler.Log(ex);
        }
        return result;
    }

    private Field getFieldFromTable(String searchField) {
        Field result = null;

        // 모든 테이블 정보에서 찾는다.
        for (JoinExpression joinExpression : this.queryTables) {
            // Q queryDsL 클래스로 리플렉션을 시도한다.
            ParameterizedType parameterizedTypes = ((ParameterizedType)joinExpression.getTarget().getClass().getGenericSuperclass());
            Class<?> entityClass = (Class<?>) parameterizedTypes.getActualTypeArguments()[0];
            List<Field> fields = List.of(entityClass.getDeclaredFields());

            // 필드명을 찾는다.
            Optional<Field> optional = fields.stream().filter(i -> i.getName().contains(searchField)).findFirst();
            if(optional.isPresent()){
                return optional.get();
            }
        }
        return null;
    }

    /**
     * 메타테이블 정보로부터 필터정보를 찾는다.
     */
    private SearchFilterImplement findSearchFilterInFromTableMetas(Field field) {
        SearchFilterImplement searchFilter = null;
        try {
            SearchFilter filter = field.getAnnotation(SearchFilter.class);

            // 필터가 존재하는경우
            if(filter != null){
                searchFilter = new SearchFilterImplement(field.getDeclaringClass(),"",filter.searchType(),filter.isDateOnly());
            }

            return searchFilter;
        }catch (Exception ex) {
            MoreExceptionHandler.Log(ex);
        }
        return searchFilter;
    }

    /**
     * 리스폰스 클래스로부터 필터정보를 찾는다.
     * @param clazz 리스폰스 클래스
     * @param searchField 검색할 필드정보
     */
    private SearchFilterImplement findSearchFilterInClass(Class clazz, String searchField) {
        SearchFilterImplement searchFilter = null;
        try {
            // 모든 클래스의 필드를 가져온다.
            Field[] fields = clazz.getDeclaredFields();

            // 필드 정보를 찾는다.
            Optional<Field> foundOptional = Arrays.stream(fields).filter(field -> field.getName().equalsIgnoreCase(searchField)).findFirst();

            // 찾지 못한경우
            if(foundOptional.isEmpty())
                return null;

            // 필드정보를 가져온다.
            Field field = foundOptional.get();
            SearchFilter filter = field.getAnnotation(SearchFilter.class);

            // 필터가 존재하는경우
            if(filter != null){
                searchFilter = new SearchFilterImplement(field.getType(),"",filter.searchType(),filter.isDateOnly());
            }

            return searchFilter;
        }catch (Exception ex) {
            MoreExceptionHandler.Log(ex);
        }
        return searchFilter;
    }

    /**
     * [리플렉션] 테이블 메타 정보안에서 Field 정보를 찾아서 가져온다
     * @param searchField 찾고자 하는 필드명
     * @return Field 리플렉션 값
     */
    private Field findWhereFieldInTargetTable(String searchField) {
        Field field = null;
        try {
            // 모든 테이블 정보에서 찾는다.
            for (JoinExpression joinExpression : this.queryTables) {
                // Q queryDsL 클래스로 리플렉션을 시도한다.
                Class QClass = joinExpression.getTarget().getClass();
                List<Field> fields = Arrays.stream(QClass.getFields()).collect(Collectors.toList());

                // 필드명을 찾는다.
                Optional<Field> optional = fields.stream().filter(i -> i.getName().contains(searchField)).findFirst();

                // 찾은 경우
                if(optional.isPresent()) {
                    return optional.get();
                }
            }
        }catch (Exception ex) {
            MoreExceptionHandler.Log(ex);
        }
        return field;
    }

    /**
     * 사용자의 요청 정보와 프로젝션 정보로 검색 기간정보로 리턴한다.
     * @param clazz 응답 클래스 정보
     * @param projectionArgs 프로젝션 정보
     * @return List<PeriodSearchItem>
     */
    protected List<PeriodSearchItem> getPeriodSearchFields(Class clazz, Object[] projectionArgs)
    {
        List<PeriodSearchItem> result = new ArrayList<>();

        // 기간 검색 필드 및 검색 시작/종료 일시 항목이 존재하는 경우
        if(searchPeriodFields != null && searchPeriodFields.length > 0 && searchStartDates != null && searchStartDates.length > 0 && searchEndDates != null && searchEndDates.length > 0
                && searchFields.length <= searchStartDates.length && searchFields.length <= searchEndDates.length)
        {
            Map<String, PeriodSearchItem> searchFieldMap = new HashMap<>();

            String columnPath = "";

            // 클래스의 모든  필드를 가져온다.
            Field[] fields = clazz.getDeclaredFields();

            // 부모 클래스의 모든 필드를 가져온다.
            Field[] superFields = clazz.getSuperclass().getDeclaredFields();

            // 검색 필드 목록을 모두 소문자로 변환
            for(int index = 0; index < searchPeriodFields.length; index++) {
                // 검색할 필드명을 가져온다.
                String searchField = searchPeriodFields[index];

                // 필드 정보를 찾는다.
                Optional<Field> foundOptional = Arrays.stream(fields).filter(field -> field.getName().equalsIgnoreCase(searchField)).findFirst();

                // 클래스안에 검색 가능한 프러퍼티가 없는 경우
                if(foundOptional.isEmpty()){
                    // 부모클래스에서 찾는다.
                    foundOptional = Arrays.stream(superFields).filter(field -> field.getName().equalsIgnoreCase(searchField)).findFirst();
                }

                // 그래도 없을경우
                if(foundOptional.isEmpty()){
                    continue;
                }

                Field foundField = foundOptional.get();

                // StringPath 데이터를 가져온다.
                columnPath = findWhereStatement(searchField,projectionArgs);

                // 결과 Enum 타입
                SearchFilter searchFilter = foundField.getAnnotation(SearchFilter.class);

                // Date 형이 아닌경우
                if(searchFilter.searchType() != EnumFieldSearchType.Date)
                    return result;

                // 해당 필드가 존재하는 경우
                if(!searchFieldMap.containsKey(columnPath))
                    searchFieldMap.put(columnPath, new PeriodSearchItem(columnPath, searchStartDates[index], searchEndDates[index], searchFilter.isDateOnly()));
            }

            result.addAll(searchFieldMap.values());
        }

        return result;
    }

    /**
     * 주어진 쿼리 원본으로 부터 전체 레코드가 담긴 QueryResults 객체를 반환한다.
     * @param source JPAQuery<T> 객체
     * @param <T> 원본 타입
     * @return QueryResults<D> 객체
     */
    @Override
    public  <T> QueryResults<T> createQueryResults(JPAQuery<T> source) {
        return this.createQueryResults(source, null);
    }

    /**
     * 주어진 쿼리 원본으로 부터 전체 레코드가 담긴 QueryResults 객체를 반환한다.
     * @param source JPAQuery<T> 객체
     * @param orders 정렬필드 정보 목록
     * @param <T> 원본 타입
     * @return QueryResults<D> 객체
     */
    @Override
    public <T> QueryResults<T> createQueryResults(JPAQuery<T> source, List<SortItem> orders) {
        QueryResults<T> result = new QueryResults<>();
        List<T> sourceList;

        try {
            if (source != null)
            {
                // 정렬 적용
                source = orderByWithDirection(source, orders);

                // 쿼리 저장
                JPAQuery<T> query = source;

                // 페이징을 적용한 목록을 가져온다.
                sourceList = query.fetch();

                // 목록 수 저장 (전체 레코드를 가져오므로 별도 전체 레코드 수 조회 필요 없음)
                long totalCount = sourceList == null ? 0 : sourceList.size();

                // 페이징을 적용하여 목록 응답 객체 생성
                result = new QueryResults<>(sourceList
                        , totalCount, 0, Integer.MAX_VALUE, 20);
            }
        } catch (Exception e) {
            MoreExceptionHandler.Log(e);
        }

        return result;
    }


    /**
     * 주어진 쿼리 원본으로 부터 전체 레코드를 가져와 형 변환 후 QueryResults 객체를 반환한다.
     * @param source JPAQuery<T> 객체
     * @param orders 정렬필드 정보 목록
     * @param destClazz 대상 타입 클래스 객체
     * @param <T> 원본 타입
     * @param <D> 대상 타입
     * @return QueryResults<D> 객체
     */
    @Override
    public   <T, D> QueryResults<D> createQueryResults(JPAQuery<T> source, List<SortItem> orders, Class<D> destClazz) {
        QueryResults<D> result = new QueryResults<>();
        List<T> sourceList;
        List<D> destinationList = new ArrayList<>();

        try {
            if (source != null)
            {
                // 정렬 적용
                source = orderByWithDirection(source, orders);

                // 쿼리 저장
                JPAQuery<T> query = source;

                // 페이징을 적용한 목록을 가져온다.
                sourceList = query.fetch();

                // 데이터가 존재하는 경우
                if (sourceList != null && sourceList.size() > 0)
                {
                    // 목록의 모든 데이터에 대해서 처리
                    for (T item : sourceList)
                    {
                        // 대상 객체 생성 및 복사
                        D destinationItem = ObjectUtils.createAndCopy(item, destClazz);

                        // 대상 객체 목록에 추가
                        destinationList.add(destinationItem);
                    }
                }

                // 목록 수 저장 (전체 레코드를 가져오므로 별도 전체 레코드 수 조회 필요 없음)
                long totalCount = sourceList == null ? 0 : sourceList.size();

                // 페이징을 적용하여 목록 응답 객체 생성
                result = new QueryResults<>(destinationList
                        , totalCount, 0, Integer.MAX_VALUE, 20);
            }
        } catch (Exception e) {
            MoreExceptionHandler.Log(e);
        }

        return result;
    }


    /**
     * 필드명 및 정렬 방향 문자열로 쿼리를 작성한다.
     * @param source 쿼리 소스
     * @param orders 정렬 정보 목록
     * @param <T> 대상 클래스 타입
     * @return JPAQuery<T> 객체
     */
    @Override
    public <T> JPAQuery<T> orderByWithDirection(JPAQuery<T> source, List<SortItem> orders) {
        JPAQuery<T> result = source;

        try {
            if(source != null && orders != null) {
                for(SortItem order : orders) {
                    if(order != null) {
                        result = orderByWithDirection(result, order.getTable(), order.getField(), order.getDirection());
                    }
                }
            }
        } catch (Exception e) {
            MoreExceptionHandler.Log(e);
        }

        return result;
    }

    /**
     * 필드명 및 정렬 방향 문자열로 쿼리를 작성한다.
     * @param source 쿼리 소스
     * @param table 테이블명
     * @param field 필드명
     * @param direction 정렬 방향
     * @param <T> 대상 클래스 타입
     * @return JPAQuery<T> 객체
     */
    @Override
    public <T> JPAQuery<T> orderByWithDirection(JPAQuery<T> source, String table, String field, String direction) {
        JPAQuery<T> result = source;

        try {
            if(source != null && StringUtils.hasText(field) && StringUtils.hasText(direction)) {
                if (direction.equalsIgnoreCase("desc"))
                    result = source.orderBy(Expressions.stringPath(StringUtils.hasText(table) ? table + "." + field : field).desc());
                else
                    result = source.orderBy(Expressions.stringPath(StringUtils.hasText(table) ? table + "." + field : field).asc());
            }
        } catch (Exception e) {
            MoreExceptionHandler.Log(e);
        }

        return result;
    }
}
