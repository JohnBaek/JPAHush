package com.john.jpahush.data.commondata.queries;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * 정렬 정보 클래스
 */
@SuppressWarnings({"rawtypes"})
@Getter
@Setter
public class SortItem {

    /**
     * 생성자
     * @param table 테이블명
     * @param field 정렬 필드명
     * @param direction 정렬 방향
     */
    public SortItem(String table, String field, String direction) {
        this(table, field, direction, null);
    }

    /**
     * 생성자
     * @param table 테이블명
     * @param field 정렬 필드명
     * @param direction 정렬 방향
     * @param clazz 사용할 클래스
     */
    public SortItem(String table, String field, String direction, Class clazz) {
        this.clazz = clazz;
        this.table = table;
        this.field = field;
        this.direction = direction;
    }

    /**
     * 생성자
     * @param field 정렬 필드명
     * @param direction 정렬 방향
     */
    public SortItem(String field, String direction) {
        this(field, direction, (Class) null);
    }

    /**
     * 생성자
     * @param field 정렬 필드명
     * @param direction 정렬 방향
     * @param clazz 사용할 클래스
     */
    public SortItem(String field, String direction, Class clazz) {
        if(StringUtils.hasText(field)) {
            String[] fieldItems = field.split("[.]");
            if(fieldItems.length == 2) {
                this.table = fieldItems[0];
                this.field = fieldItems[1];
            }
            else if(fieldItems.length == 1)
                this.field = field;
            else if(fieldItems.length == 3){
                this.table = fieldItems[0];
                this.field = fieldItems[1] + "." + fieldItems[2];
            }
        }
        this.direction = direction;
        this.clazz = clazz;
    }

    /**
     * 사용할 클래스 (JPASQLQuery 사용시만)
     */
    @ApiModelProperty(value = "테이블명 (필요시만 사용)", position = 1)
    Class clazz;

    /**
     * 테이블명
     */
    @ApiModelProperty(value = "테이블명 (필요시만 사용)", position = 1)
    String table;

    /**
     * 정렬 필드명
     */
    @ApiModelProperty(value = "정렬 필드명", position = 2, required = true)
    String field;

    /**
     * 정렬 방향
     */
    @ApiModelProperty(value = "정렬 방향 (asc / desc)", position = 3, required = true)
    String direction;

    /**
     * 문자열 변환
     */
    @Override
    public String toString() {
        if(StringUtils.hasText(table))
            return String.format("%s.%s %s", table, field, direction);
        else
            return String.format("%s %s", field, direction);
    }
}

