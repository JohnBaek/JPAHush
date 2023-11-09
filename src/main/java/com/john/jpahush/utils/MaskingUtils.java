package com.john.jpahush.utils;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class MaskingUtils {

	/**
	 * 전화번호 마스킹(가운데 숫자 4자리 마스킹)
	 * @param telNo 마스킹할 전화번호 문자열
	 * @param maskingGroupIndexes 마스킹할 전화번호 부분 인덱스 목록
	 * @return 마스킹한 전화번호 문자열
	 */
	public static String maskingTelNo(String telNo, Integer[] maskingGroupIndexes) {
		String regex = "(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$";

		try {
			// 전화번호가 존재하지 않는 경우, 그대로 반환 처리
			if(!StringUtils.hasText(telNo)) return telNo;

			Matcher matcher = Pattern.compile(regex).matcher(TelNumberUtils.getFormatted(telNo));
			if(matcher.find()) {

				// 마스킹할 전화번호 부분 인덱스 리스트
				List<Integer> maskingGroupIndexList = Arrays.asList(maskingGroupIndexes);

				// 결과를 저장할 목록
				List<String> replacedItems = Arrays.asList("", "", "");

				// 정규식 매칭 결과의 모든 그룹 중 첫번째를 제외한 모든 그룹에 대해서 처리
				for(int i = 1; i <= matcher.groupCount(); i++) {

					// 해당 그룹의 문자열을 가져온다.
					String replaceTarget = matcher.group(i);

					// 마스킹할 그룹인 경우
					if(maskingGroupIndexList.contains(i - 1)) {
						char[] c = new char[replaceTarget.length()];
						Arrays.fill(c, '*');

						replacedItems.set(i - 1, String.valueOf(c));
					}
					// 마스킹할 그룹이 아닌 경우
					else
						replacedItems.set(i - 1, replaceTarget);
				}

				return String.join("-", replacedItems);
			}
		}
		catch(Exception ignored) {

		}
		return telNo;
	}

	/**
	 * 이름 마스킹(이름 첫자와 마지막 글자를 제외한 나머지)
	 * @param name 마스킹할 이름 문자열
	 * @return 마스킹한 이름 문자열
	 */
	public static String maskingName(String name) {

		try {
			// 이름이 존재하지 않는 경우, 그대로 반환 처리
			if(!StringUtils.hasText(name)) return name;

			String pattern = "^(.)(.+)(.)$";
			if(name.length() == 2)
				pattern = "^(.)(.+)$";

			Matcher matcher = Pattern.compile(pattern).matcher(name);

			if(matcher.matches()) {
				StringBuilder replacedResult = new StringBuilder();

				// 정규식 매칭 결과의 모든 그룹에 대해서 처리
				for(int i = 1; i <= matcher.groupCount(); i++) {

					// 해당 그룹의 문자열을 가져온다.
					String replaceTarget = matcher.group(i);

					// 두번째 그룹인 경우
					if(i == 2) {
						char[] c = new char[replaceTarget.length()];
						Arrays.fill(c, '*');

						replacedResult.append(String.valueOf(c));
					} else
						replacedResult.append(replaceTarget);
				}

				return replacedResult.toString();
			}
		}
		catch(Exception ignored) {

		}
		return name;
	}

	/**
	 * 주민번호 마스킹(주민번호 뒷자리와 요청에 따라 생년월일 마스킹)
	 * @param juminNo 마스킹할 주민번호 문자열
	 * @return 마스킹한 주민번호 문자열
	 */
	public static String maskingJuminNo(String juminNo) {
		return maskingJuminNo(juminNo, false);
	}

	/**
	 * 주민번호 마스킹(주민번호 뒷자리와 요청에 따라 생년월일 마스킹)
	 * @param juminNo 마스킹할 주민번호 문자열
	 * @param includeBirthDate 생년월일도 마스킹할지 여부
	 * @return 마스킹한 주민번호 문자열
	 */
	public static String maskingJuminNo(String juminNo, boolean includeBirthDate) {

		try {

			StringBuilder juminNo1 = new StringBuilder();
			StringBuilder juminNo2 = new StringBuilder();

			// 주민번호 문자열이 존재하는 경우
			if(StringUtils.hasText(juminNo)) {
				// '-'가 포함된 전체 주민번호인 경우
				if(juminNo.length() == 14) {
					juminNo1 = new StringBuilder(juminNo.substring(0, 6));
					juminNo2 = new StringBuilder(juminNo.substring(7));
				}
				// '-'가 빠진 전체 주민번호인 경우
				else if(juminNo.length() == 13) {
					juminNo1 = new StringBuilder(juminNo.substring(0, 6));
					juminNo2 = new StringBuilder(juminNo.substring(6));
				}
				// 주민번호 뒷자리만 있는 경우
				else if(juminNo.length() == 7)
					juminNo2 = new StringBuilder(juminNo);
				// 주민번호 앞자리만 있는 경우
				else if(juminNo.length() == 6)
					juminNo1 = new StringBuilder(juminNo);
			}

			// 주민번호 앞자리 혹은 뒷자리가 존재하는 경우
			if(StringUtils.hasText(juminNo1.toString()) || StringUtils.hasText(juminNo2.toString())) {

				// 주민번호 앞자리가 존재하고 생년월일도 마스킹을 원하는 경우
				if(StringUtils.hasText(juminNo1.toString()) && includeBirthDate) {
					String regex = "(\\d{2})?(\\d{2})?(\\d{2})$";

					// 생년월일 6자리에 대해서 정규식 검사
					Matcher matcher = Pattern.compile(regex).matcher(juminNo1.toString());
					if(matcher.find()) {

						juminNo1 = new StringBuilder();

						// 정규식 매칭 결과의 모든 그룹에 대해서 처리
						for(int i = 1; i <= matcher.groupCount(); i++) {

							// 해당 그룹의 문자열을 가져온다.
							String replaceTarget = matcher.group(i);

							// 두번째 그룹인 경우
							if(i >= 2) {
								char[] c = new char[replaceTarget.length()];
								Arrays.fill(c, '*');

								juminNo1.append(String.valueOf(c));
							} else
								juminNo1.append(replaceTarget);
						}
					}
				}

				// 주민번호 뒷자리가 존재하는 경우
				if(StringUtils.hasText(juminNo2.toString())) {
					String regex = "(\\d)(\\d{6})$";

					// 주민번호 뒷자리에 대해서 정규식 검사
					Matcher matcher = Pattern.compile(regex).matcher(juminNo2.toString());
					if(matcher.find()) {

						juminNo2 = new StringBuilder();

						// 정규식 매칭 결과의 모든 그룹에 대해서 처리
						for(int i = 1; i <= matcher.groupCount(); i++) {

							// 해당 그룹의 문자열을 가져온다.
							String replaceTarget = matcher.group(i);

							// 두번째 그룹인 경우
							if(i == 2) {
								char[] c = new char[replaceTarget.length()];
								Arrays.fill(c, '*');

								juminNo2.append(String.valueOf(c));
							} else
								juminNo2.append(replaceTarget);
						}
					}
				}

				if(StringUtils.hasText(juminNo1) && StringUtils.hasText(juminNo2))
					return juminNo1 + "-" + juminNo2;
				else if(StringUtils.hasText(juminNo1))
					return juminNo1.toString();
				else if(StringUtils.hasText(juminNo2))
					return juminNo2.toString();
			}

		}
		catch(Exception ignored) {

		}
		return juminNo;
	}
}
