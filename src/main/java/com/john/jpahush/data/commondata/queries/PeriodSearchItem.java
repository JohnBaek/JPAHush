package com.john.jpahush.data.commondata.queries;

import com.john.jpahush.utils.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 기간 검색 정보 클래스
 */
@Getter
@Setter
public class PeriodSearchItem {

    /**
     * 생성자
     */
    @SuppressWarnings("unused")
    public PeriodSearchItem() {

    }

    /**
     * 생성자
     * @param field 검색 필드명
     * @param searchStartDate 검색 시작 일시
     * @param searchEndDate 검색 종료 일시
     * @param dateOnly 날짜만 사용할지 여부
     */
    public PeriodSearchItem(String field, Date searchStartDate, Date searchEndDate, boolean dateOnly) {
        this.field = field;
        this.dateOnly = dateOnly;
        // 날짜만 사용하는 경우
        if(dateOnly) {
            if(searchStartDate != null)
                this.searchStartDate = DateUtils.toDateOnly(searchStartDate);
            if(searchEndDate != null)
                this.searchEndDate = DateUtils.addDays(DateUtils.toDateOnly(searchEndDate), 1);
        }
        // 시간도 사용하는 경우
        else {
            if(searchStartDate != null)
               this.searchStartDate = searchStartDate;
            if(searchEndDate != null)
                this.searchEndDate = DateUtils.addSeconds(searchEndDate, 1);
        }
    }

    /**
     * 검색 필드명
     */
    @ApiModelProperty(value = "검색 필드명", position = 1)
    String field;

    /**
     * 날짜만 사용할지 여부
     */
    @Setter(AccessLevel.NONE)
    boolean dateOnly;

    /**
     * 검색 시작 일시
     */
    @ApiModelProperty(value = "검색 시작 일시", position = 2)
    Date searchStartDate;

    /**
     * 검색 종료 일시
     */
    @ApiModelProperty(value = "검색 종료 일시", position = 2)
    Date searchEndDate;

    /**
     * 문자열로 변환
     * @return 객체 내용이 담겨있는 문자열
     */
    @Override
    public String toString() {
        return String.format("%s='%s' ~ '%s'", field,
                dateOnly ? DateUtils.toString(searchStartDate, "yyyy-MM-dd")
                        : DateUtils.toString(searchStartDate, "yyyy-MM-dd HH:mm:ss"),
                dateOnly ? DateUtils.toString(DateUtils.addDays(searchEndDate, -1), "yyyy-MM-dd")
                        : DateUtils.toString(DateUtils.addSeconds(searchEndDate, -1), "yyyy-MM-dd HH:mm:ss")
        );
    }
}
