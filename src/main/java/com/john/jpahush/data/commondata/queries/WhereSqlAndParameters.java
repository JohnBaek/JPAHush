package com.john.jpahush.data.commondata.queries;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * WHERE 절에 사용할 SQL 파라미터 구문과 파라미터 이름/값을 가지는 클래스
 */
@Getter
@Setter
@Builder
public class WhereSqlAndParameters {
	/**
	 * WHERE 절에 사용할 SQL 파라미터 구문
	 */
	private String whereSql;

	/**
	 * 파라미터 이름/값
	 */
	private Map<String, Object> parameters;
}
