package com.john.jpahush.annotations;

import com.john.jpahush.data.commondata.enums.EnumFieldSearchType;

import java.lang.annotation.Annotation;

public class SearchFilterImplement implements SearchFilter{
    public SearchFilterImplement(Class type, String stringPath, EnumFieldSearchType searchType, boolean isDateOnly) {
        this.type = type;
        this.stringPath = stringPath;
        this.searchType = searchType;
        this.isDateOnly = isDateOnly;
    }

    public Class getType() {
        return type;
    }

    /**
     * 클래스타입
     */
    Class type = null;

    /**
     * 스트링 패스
     */
    String stringPath = "";

    /**
     * 검색 타입 ( 기본 : 스트링 Like 검색 )
     */
    EnumFieldSearchType searchType = EnumFieldSearchType.StringLike;


    /**
     * 시간을 제외한 날짜만 사용할지 여부
     * @return
     */
    boolean isDateOnly = true;

    @Override
    public String stringPath() {
        return this.stringPath;
    }

    @Override
    public EnumFieldSearchType searchType() {
        return this.searchType;
    }

    @Override
    public boolean isDateOnly() {
        return this.isDateOnly;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return this.annotationType();
    }


    public void setType(Class type) {
        this.type = type;
    }

    public void setStringPath(String stringPath) {
        this.stringPath = stringPath;
    }

    public void setSearchType(EnumFieldSearchType searchType) {
        this.searchType = searchType;
    }

    public void setDateOnly(boolean dateOnly) {
        isDateOnly = dateOnly;
    }
}
