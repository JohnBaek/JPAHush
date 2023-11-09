package com.john.jpahush.utils;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class StringArrayUtils {

	/**
	 * 주어진 regex로 문자열을 분리하여 반환한다.
	 * @param source 원본 문자열
	 * @param regex 검사할 정규식 문자열
	 * @param doTrim 분리된 문자열에 trim을 적용할지 여부
	 * @param removeBlankItem 공백 항목을 제외할지 여부
	 * @return 분리된 문자열 배멸
	 */
	public static String[] Split(String source, String regex, boolean doTrim, boolean removeBlankItem) {
		String[] result = new String[0];

		try {
			List<String> items = Arrays.asList(source.split(regex));

			// 공백 항목을 제외하는 경우
			if(removeBlankItem) {
				// 빈문자열을 제외한 목록을 저장한다.
				items = items.stream().filter(StringUtils::hasText).collect(toList());
			}

			// 분리된 문자열이 존재하는 경우
			if(items.size() > 0)
			{
				// 결과로 저장
				result = items.toArray(result);

				// trim을 수행해야 하는 경우
				if(doTrim)
				{
					// 모든 항목에 대해서 trim을 수행
					for(int index = 0; index < result.length; index++)
						result[index] = result[index].trim();
				}
			}
		} catch (Exception e) {
			MoreExceptionHandler.Log(e);
		}

		return result;
	}
}
