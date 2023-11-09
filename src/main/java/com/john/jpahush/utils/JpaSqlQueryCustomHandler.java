package com.john.jpahush.utils;


import com.querydsl.jpa.HibernateHandler;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.InstantType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import javax.persistence.Query;


/**
 * JPA SQL Query(native query) 형변환 문제 처리를 위한 핸들러
 */
public class JpaSqlQueryCustomHandler  extends HibernateHandler {
    @Override
    public void addScalar(Query query, String alias, Class<?> type) {
        // 형변환을 처리한다.
        switch (type.getTypeName().toLowerCase()){
            // Instant 시간/날짜 데이터인경우
            case "java.time.instant":
            case "instant":
                query.unwrap(NativeQuery.class).addScalar(alias, new InstantType());
                break;
            // 스트링 또는 문자형 데이터인경우
            case "java.lang.string":
            case "string":
            case "java.lang.character":
                query.unwrap(NativeQuery.class).addScalar(alias, new StringType());
                break;
            // 정수형 데이터인경우
            case "java.lang.integer":
            case "integer":
                query.unwrap(NativeQuery.class).addScalar(alias, new IntegerType());
                break;
            default:
                query.unwrap(NativeQuery.class).addScalar(alias);
                break;
        }
    }
}
