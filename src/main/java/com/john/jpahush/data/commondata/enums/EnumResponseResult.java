package com.john.jpahush.data.commondata.enums;

import lombok.Getter;

/**
 * 응답 결과
 */
public enum EnumResponseResult {
    /**
     * 에러
     */
    Error(-1),
    /**
     * 경고
     */
    Warning(0),
    /**
     * 성공
     */
    Success(1);

    /**
     * 정수 값
     */
    @Getter
    private final int value;

    /**
     * 생성자
     * @param value 초기화 값
     */
    EnumResponseResult(int value) {
        this.value = value;
    }
}
