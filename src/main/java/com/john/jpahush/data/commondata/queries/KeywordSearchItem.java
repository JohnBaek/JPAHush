package com.john.jpahush.data.commondata.queries;

import com.john.jpahush.data.commondata.enums.EnumFieldSearchType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 키워드 검색 정보 클래스
 */
@Getter
@Setter
@ToString
public class KeywordSearchItem {

    /**
     * 생성자
     */
    @SuppressWarnings("unused")
    public KeywordSearchItem() {

    }

    /**
     * 생성자
     * @param aliasName 별칭
     * @param field 검색 필드명
     * @param keywords 검색어 목록
     * @param keywordSearchType 검색 타입
     * @param convertTypeClass 변환할 타입 클래스
     */
    @SuppressWarnings("rawtypes")
    public KeywordSearchItem(String aliasName, String field, List<String> keywords, EnumFieldSearchType keywordSearchType, Class convertTypeClass) {
        this.aliasName = aliasName;
        this.field = field;
        this.keywords = keywords;
        this.keywordSearchType = keywordSearchType;
        this.convertTypeClass = convertTypeClass;
    }

    /**
     * 별칭
     */
    @ApiModelProperty(value = "별칭", position = 0)
    private String aliasName;

    /**
     * 검색 필드명
     */
    @ApiModelProperty(value = "검색 필드명", position = 1)
    String field;

    /**
     * 검색어 목록
     */
    @ApiModelProperty(value = "검색어 목록", position = 2)
    List<String> keywords;

    /**
     * 검색 타입
     */
    @ApiModelProperty(value = "검색 타입", position = 3)
    EnumFieldSearchType keywordSearchType;

    /**
     * 변환할 타입 클래스
     */
    @SuppressWarnings("rawtypes")
    Class convertTypeClass;
}
