package com.john.jpahush.data.commondata.queries;

import com.john.jpahush.data.commondata.enums.EnumFieldSearchType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 필드 검색 타입 정보 클래스
 */
@Getter
@Setter
@ToString
public class FieldSearchType {

    /**
     * 생성자 (문자열 검색 타입을 생성한다.)
     * @param field 검색 필드명
     */
    public FieldSearchType(String field) {
        this.field = field;
        this.fieldSearchType = EnumFieldSearchType.StringLike;
    }

    /**
     * 생성자 (Date 검색 타입을 생성한다.)
     * @param field 검색 필드명
     * @param dateOnly 날짜만 사용할지 여부
     */
    public FieldSearchType(String field, boolean dateOnly) {
        this.field = field;
        this.fieldSearchType = EnumFieldSearchType.Date;
        this.dateOnly = dateOnly;
    }

    /**
     * 생성자
     * @param field 검색 필드명
     * @param keywordSearchType 검색 타입
     */
    public FieldSearchType(String field, EnumFieldSearchType keywordSearchType) {
        this.field = field;
        this.fieldSearchType = keywordSearchType;
    }

    /**
     * 생성자
     * @param field 검색 필드명
     * @param keywordSearchType 검색 타입
     * @param convertTypeClass 변환할 타입 클래스
     */
    @SuppressWarnings("rawtypes")
    public FieldSearchType(String field, EnumFieldSearchType keywordSearchType, Class convertTypeClass) {
        this.field = field;
        this.fieldSearchType = keywordSearchType;
        this.convertTypeClass = convertTypeClass;
    }

    /**
     * 검색 필드명
     */
    @ApiModelProperty(value = "검색 필드명", position = 1)
    String field;

    /**
     * 필드 검색 타입
     */
    @ApiModelProperty(value = "필드 검색 타입", position = 2)
    EnumFieldSearchType fieldSearchType;

    /**
     * 변환할 타입 클래스
     */
    @SuppressWarnings("rawtypes")
    Class convertTypeClass;

    /**
     * 날짜만 사용할지 여부
     */
    boolean dateOnly;
}
