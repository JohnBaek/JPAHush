package com.john.jpahush.data.commondata.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * 등록자 정보가 포함된 응답 데이터 클래스
 */
@Getter
@Setter
public class ResponseWithCreatedBy implements IRegistrant {
	/**
	 * 등록자 아이디
	 */
	@ApiModelProperty(value = "등록자 아이디")
	private String regId;

	/**
	 * 등록자명
	 */
	@ApiModelProperty(value = "등록자명")
	private String regName;

	/**
	 * 등록일시
	 */
	@ApiModelProperty(value = "등록일시")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private Instant regDate;
}
