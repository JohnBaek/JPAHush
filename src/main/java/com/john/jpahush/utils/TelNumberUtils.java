package com.john.jpahush.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 전화 번호 관련 유틸리티 클래스
 */
@SuppressWarnings("unused")
public class TelNumberUtils {

	/**
	 * 전화 번호에서 '-'를 제거한다.
	 * @param phoneNumber 전화번호
	 * @return '-'를 제거한 전화번호
	 */
	public static String removeDash(String phoneNumber) {

		// 전화번호가 존재하는 경우
		if(StringUtils.hasText(phoneNumber)) {
			return phoneNumber.replaceAll("-", "").trim();
		}
		return "";
	}

	/**
	 * 전화번호 항목들로 정형화된 전화번호를 만든다.
	 * @param phoneNumber1 전화번호 항목 1
	 * @param phoneNumber2 전화번호 항목 2
	 * @param phoneNumber3 전화번호 항목 3
	 * @return 정형화된 전화번호
	 */
	public static String make(String phoneNumber1, String phoneNumber2, String phoneNumber3) {
		if(phoneNumber1 == null)
			phoneNumber1 = "";
		if(phoneNumber2 == null)
			phoneNumber2 = "";
		if(phoneNumber3 == null)
			phoneNumber3 = "";
		return getFormatted(phoneNumber1 + phoneNumber2 + phoneNumber3);
	}

	/**
	 * 휴대폰 번호로 유효한지 검사한다.
	 * @param phoneNumber 전화번호
	 * @return 휴대폰 번호로 유효한지 여부
	 */
	public static boolean isInvalidCellNo(String phoneNumber) {
		String numberOnlyPhoneNumber = removeDash(phoneNumber);
		Pattern pattern = Pattern.compile("(^01(?:0|1|[6-9]))(\\d{3}|\\d{4})(\\d{4})$");
		Matcher matcher = pattern.matcher(numberOnlyPhoneNumber);
		return !matcher.matches();
	}

	/**
	 * 전화번호를 '-'가 포함된 전화번호로 변경하여 반환한다.
	 * @param phoneNumber 전화번호
	 * @return 정형화된 전화번호를 반환한다.
	 */
	public static String getFormatted(String phoneNumber) {

		String result = phoneNumber;

		// 전화번호가 존재하는 경우
		if(StringUtils.hasText(phoneNumber)) {

			// 숫자로만 이루어 지지 않은 경우
			if(!phoneNumber.matches("^[0-9]+$"))
				phoneNumber = removeDash(phoneNumber);

			// 해당되는 지역 번호
			String selectedAreaCode = "";

			// 지역번호가 존재하는 경우
			if(phoneNumber.length() > 8) {
				// 지역 번호 목록
				String[] areaCodes = {"050", "010", "011", "016", "017", "018", "019", "02", "031", "032", "033", "041", "042", "043", "044", "051", "052", "053", "054", "055", "061", "062", "063", "064"};

				// 모든 지역 번호 목록에 대해서 검사
				for(String areaCode : areaCodes) {
					// 지역 번호로 시작하는 경우
					if(phoneNumber.startsWith(areaCode))
					{
						selectedAreaCode = areaCode;
						break;
					}
				}

				// 해당되는 지역 번호가 존재하는 경우
				if(StringUtils.hasText(selectedAreaCode)) {
					phoneNumber = phoneNumber.substring(selectedAreaCode.length());
				}
			}

			// 지역번호가 없는 7자리 번호인 경우
			if(phoneNumber.length() == 7) {
				if(!StringUtils.hasText(selectedAreaCode))
					result = String.format("%s-%s", phoneNumber.substring(0, 3), phoneNumber.substring(3, 7));
				else
					result = String.format("%s-%s-%s", selectedAreaCode, phoneNumber.substring(0, 3), phoneNumber.substring(3, 7));
			}
			// 지역번호가 없는 8자리 번호인 경우
			else if(phoneNumber.length() == 8) {
				if(!StringUtils.hasText(selectedAreaCode))
					result = String.format("%s-%s", phoneNumber.substring(0, 4), phoneNumber.substring(4, 8));
				else
					result = String.format("%s-%s-%s", selectedAreaCode, phoneNumber.substring(0, 4), phoneNumber.substring(4, 8));
			}
		}
		// 전화번호가 존재하지 않는 경우
		else
			result = null;

		return result;
	}

	/**
	 * 전화번호 마스킹(가운데 숫자 4자리 마스킹)
	 * @param telNo 마스킹할 전화번호 문자열
	 * @param maskingGroupIndexes 마스킹할 전화번호 부분 인덱스 목록
	 * @return 마스킹한 전화번호 문자열
	 */
	public static String masking(String telNo, Integer[] maskingGroupIndexes) {
		return MaskingUtils.maskingTelNo(telNo, maskingGroupIndexes);
	}

	/**
	 * 전화번호 중 마지막 네자리를 가져온다.
	 * @param phoneNumber 전화번호
	 * @return 마지막 네자리
	 */
	public static String getLast4Number(String phoneNumber) {

		String result = "";

		if(phoneNumber.length() >= 4)
			result = phoneNumber.substring(phoneNumber.length() - 4);

		return result;
	}

	/**
	 * 전화번호를 "-"로 분리하여 배열로 반환한다.
	 * @param phoneNumber 전화번호
	 * @return 전화번호를 "-"로 분리한 배열
	 */
	public static String[] getItems(String phoneNumber) {
		String[] result = new String[0];

		// 전화번호를 정형화된 번호로 변경한다.
		String formatted = getFormatted(phoneNumber);

		// 정형화된 전화번호가 존재하는 경우
		if(StringUtils.hasText(formatted))
			// "-"로 분리
			result = formatted.split("-");

		return result;
	}
}
