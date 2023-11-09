package com.john.jpahush.data.commondata.queries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 필드명과 필드명 별칭 정보 클래스
 */
@Getter
@Setter
@AllArgsConstructor
public class ColumnAlias {

	/**
	 * 별칭
	 */
	private String aliasName;

	/**
	 * 실제 필드명
	 */
	private String fieldName;
}
