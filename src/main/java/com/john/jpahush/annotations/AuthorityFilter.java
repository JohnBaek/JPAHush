package com.john.jpahush.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 클래스 프러퍼티에 권한을 명기한다.
 * 클래스 프러퍼티에 사용 가능.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface AuthorityFilter {
    /**
     * 권한 목록 , 콤마 구분
     */
    String[] autorities() default "";
}
