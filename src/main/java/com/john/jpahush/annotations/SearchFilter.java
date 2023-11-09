package com.john.jpahush.annotations;


import com.john.jpahush.data.commondata.enums.EnumFieldSearchType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 엔티티에 검색 타입을 명시한다.
 * 클래스 프러퍼티에 사용 가능.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface SearchFilter {
    /**
     * 클래스타입
     */
    Class type = null;

    /**
     * 스트링 패스 데이터
     */
    String stringPath() default "";

    /**
     * 검색 타입 ( 기본 : 스트링 Like 검색 )
     */
    EnumFieldSearchType searchType() default EnumFieldSearchType.StringLike;


    /**
     * 시간을 제외한 날짜만 사용할지 여부
     * @return
     */
    boolean isDateOnly() default true;
}
