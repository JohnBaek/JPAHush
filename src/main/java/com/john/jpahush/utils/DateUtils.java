package com.john.jpahush.utils;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("unused")
public class DateUtils {

	/**
	 * 주어진 시간의 Calendar 객체를 생성하여 반환한다.
	 * @param source Date 객체
	 * @return Calendar 객체
	 */
	public static Calendar getCalendar(Date source) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(source);

		return calendar;
	}

	/**
	 * 주어진 날짜 객체에 특정 연/월/일 등의 값을 더한 날짜 객체를 반환한다.
	 * @param source 원본 날짜 객체
	 * @param part 값을 적용할 부분 (Calendar.YEAR, Calendar.MONTH, Calendar.DATE ...)
	 * @param values 더할 값
	 * @return 더한 결과 날짜 객체
	 */
	public static Date add(Date source, int part, int values) {
		if(source == null)
			source = new Date();

		Calendar calendar = DateUtils.getCalendar(source);
		calendar.add(part, values);

		return calendar.getTime();
	}

	/**
	 * 주어진 날짜 객체에 특정 연 수를 더한 날짜 객체를 반환한다.
	 * @param source 원본 날짜 객체
	 * @param years 더할 일 수
	 * @return 더한 결과 날짜 객체
	 */
	public static Date addYears(Date source, int years) {
		return DateUtils.add(source, Calendar.YEAR, years);
	}

	/**
	 * 주어진 날짜 객체에 특정 월 수를 더한 날짜 객체를 반환한다.
	 * @param source 원본 날짜 객체
	 * @param months 더할 일 수
	 * @return 더한 결과 날짜 객체
	 */
	public static Date addMonths(Date source, int months) {
		return DateUtils.add(source, Calendar.MONTH, months);
	}

	/**
	 * 주어진 날짜 객체에 특정 일 수를 더한 날짜 객체를 반환한다.
	 * @param source 원본 날짜 객체
	 * @param days 더할 일 수
	 * @return 더한 결과 날짜 객체
	 */
	public static Date addDays(Date source, int days) {
		return DateUtils.add(source, Calendar.DATE, days);
	}

	/**
	 * 주어진 날짜 객체에 특정 시간을 더한 날짜 객체를 반환한다.
	 * @param source 원본 날짜 객체
	 * @param hours 더할 시간
	 * @return 더한 결과 날짜 객체
	 */
	public static Date addHours(Date source, int hours) {
		return DateUtils.add(source, Calendar.HOUR, hours);
	}

	/**
	 * 주어진 날짜 객체에 특정 분을 더한 날짜 객체를 반환한다.
	 * @param source 원본 날짜 객체
	 * @param minutes 더할 분
	 * @return 더한 결과 날짜 객체
	 */
	public static Date addMinutes(Date source, int minutes) {
		return DateUtils.add(source, Calendar.MINUTE, minutes);
	}

	/**
	 * 주어진 날짜 객체에 특정 초를 더한 날짜 객체를 반환한다.
	 * @param source 원본 날짜 객체
	 * @param seconds 더할 초
	 * @return 더한 결과 날짜 객체
	 */
	public static Date addSeconds(Date source, int seconds) {
		return DateUtils.add(source, Calendar.SECOND, seconds);
	}

	/**
	 * 날짜를 지정된 포맷의 문자열로 변환한다.
	 * @param source 원본 날짜 객체
	 * @param format 변환할 문자열 포맷
	 * @return 날짜 문자열
	 */
	public static String toString(Date source, String format) {
		SimpleDateFormat transFormat = new SimpleDateFormat(format);
		return transFormat.format(source);
	}

	/**
	 * 날짜를 지정된 포맷의 문자열로 변환한다.
	 * @param source 원본 날짜 객체
	 * @param format 변환할 문자열 포맷
	 * @return 날짜 문자열
	 */
	public static String toString(Instant source, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format)
				.withZone(ZoneId.of("Asia/Seoul"));
		return formatter.format(source);
	}

	/**
	 * 날짜를 지정된 포맷의 문자열로 변환한다.
	 * @param source 원본 날짜 객체
	 * @param format 변환할 문자열 포맷
	 * @return 날짜 문자열
	 */
	public static String toString(LocalDate source, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format)
				.withZone(ZoneId.of("Asia/Seoul"));
		return formatter.format(source);
	}

	/**
	 * 날짜를 지정된 포맷의 문자열로 변환한다.
	 * @param source 원본 날짜 객체
	 * @param format 변환할 문자열 포맷
	 * @return 날짜 문자열
	 */
	public static String toString(LocalDateTime source, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format)
				.withZone(ZoneId.of("Asia/Seoul"));
		return formatter.format(source);
	}

	/**
	 * 날짜 문자열을 Instant 타입으로 변환한다.
	 * @param source 원본 날짜 문자열
	 * @param format 변환할 문자열 포맷
	 * @return Instant 객체
	 */
	public static Instant toInstant(String source, String format) {
		if(StringUtils.hasText(source)) {
			if((source.length() == 10 && format.length() == 10)
					|| (source.length() == 8 && format.length() == 8)) {
				source = source + " 00:00";
				format = format + " HH:mm";
			}
		}

		return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(format))
				.atZone(ZoneId.of( "Asia/Seoul" ))
				.toInstant();
	}

	/**
	 * 날짜 문자열을 LocalDate 타입으로 변환한다.
	 * @param source 원본 날짜 문자열
	 * @param format 변환할 문자열 포맷
	 * @return LocalDate 객체
	 */
	public static LocalDate toLocalDate(String source, String format) {
		return LocalDate.parse(source, DateTimeFormatter.ofPattern(format));
	}

	/**
	 * 날짜 문자열을 LocalDateTime 타입으로 변환한다.
	 * @param source 원본 날짜 문자열
	 * @param format 변환할 문자열 포맷
	 * @return LocalDateTime 객체
	 */
	public static LocalDateTime toLocalDateTime(String source, String format) {
		if(StringUtils.hasText(source)) {
			if((source.length() == 10 && format.length() == 10)
			|| (source.length() == 8 && format.length() == 8)) {
				source = source + " 00:00";
				format = format + " HH:mm";
			}
		}
		return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(format));
	}

	/**
	 * 날짜 문자열을 Date 타입으로 변환한다.
	 * @param source 원본 날짜 문자열
	 * @param format 변환할 문자열 포맷
	 * @return Date 객체
	 */
	public static Date toDate(String source, String format) {

		return Date.from(toInstant(source, format));
	}

	/**
	 * 날짜 객체의 시간을 버리고 날짜만 가지는 날짜 객체를 반환한다.
	 * @param source 원본 날짜 객체
	 * @return Date 객체
	 */
	public static Date toDateOnly(Date source) {
		Date result = null;

		try {
			if(source != null) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				result = formatter.parse(formatter.format(source));
			}
		}
		catch(Exception e) {
			MoreExceptionHandler.Log(e);
		}

		return result;
	}

	/**
	 * 날짜 문자열을 YearMonth 타입으로 변환한다.
	 * @param source 원본 날짜 문자열
	 * @param format 변환할 문자열 포맷
	 * @return YearMonth 객체
	 */
	public static YearMonth toYearMonth(String source, String format) {
		return YearMonth.parse(source, DateTimeFormatter.ofPattern(format));
	}

	/**
	 * 지정된 '일' 로 날짜를 변환한다.
	 * @param source Date 소스
	 * @param days   변환할 날짜
	 * @return 변환된 Date 객체
	 */
	public static Date toChangeDays(Date source, int days){
		// 캘린더 객체를 가져온다.
		Calendar calendar = Calendar.getInstance();

		// 캘린더 객체를 받아온 소스로 초기화한다.
		calendar.setTime(source);

		// 변환한다.
		calendar.set(Calendar.DAY_OF_MONTH, days);

		return calendar.getTime();
	}

	/**
	 * 지정된 '일' 로 날짜를 변환한다.
	 * @param source Date 소스
	 * @param days   변환할 날짜
	 * @return 변환된 Date 객체
	 */
	public static Date toChangeDays(Date source, String days)  {
		Date result;

		try {
			// 캘린더 객체를 가져온다.
			Calendar calendar = Calendar.getInstance();

			// 캘린더 객체를 받아온 소스로 초기화한다.
			calendar.setTime(source);

			// 변환한다.
			calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(days));

			result = calendar.getTime();
		} catch (Exception e) {
			result = null;
			MoreExceptionHandler.Log(e);
		}

		return result;
	}

	/**
	 * 넘겨받은 Date 객체를 지정한 '월' 로 변경한다.
	 * @param source Date 타겟 소스
	 * @param month  변경할 월
	 * @return 변환된 Date 객체
	 */
	public static Date toChangeMonth(Date source, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(source);
		calendar.set(Calendar.MONTH, month);
		return calendar.getTime();
	}

	/**
	 * 현재 월의 마지막 날짜를 가져온다
	 * @return Date 객체
	 */
	public static Date getLastDateOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	/**
	 * 지정한 월의 첫번째 날짜를 가져온다
	 * @param source Date 타겟 소스
	 * @param month  변경할 월
	 * @return 변환된 Date 객체
	 */
	public static Date toFirstDateOfMonth(Date source, int month) {
		// 지정한 월로 변경한다.
		Date changeMonth = toChangeMonth(source, month - 1);

		// 지정한 일로 변경한다.
		return toChangeDays(changeMonth,1);
	}

	/**
	 * 지정한 월의 마지막 날짜를 가져온다
	 * @param source Date 타겟 소스
	 * @param month  변경할 월
	 * @return 변환된 Date 객체
	 */
	public static Date toLastDateOfMonth(Date source, int month) {
		// 지정한 월로 변경한다.
		Date changeMonth = toChangeMonth(source, month - 1);

		// 지정된 월의 마지막일을 가져온다
		Calendar calendarSource = Calendar.getInstance();
		calendarSource.setTime(changeMonth);

		int lastDay = calendarSource.getActualMaximum(Calendar.DAY_OF_MONTH);

		// 지정한 일로 변경한다.
		return toChangeDays(changeMonth,lastDay);
	}

	/**
	 * 지정한 월의 첫번째 날짜를 가져온다
	 * @param source Date 타겟 소스
	 * @param month  변경할 월
	 * @param format 변환할 문자열 포맷
	 * @return 변환된 Date 객체
	 */
	public static String toLastDateOfMonthWithFormat(Date source, int month, String format) {
		Date change = toLastDateOfMonth(source, month);
		return toString(change, format);
	}

	/**
	 * 지정한 월의 첫번째 날짜를 가져온다
	 * @param source Date 타겟 소스
	 * @param month  변경할 월
	 * @param format 변환할 문자열 포맷
	 * @return Date 객체
	 */
	public static String toFirstDateOfMonthWithFormat(Date source, int month, String format) {
		Date change = toFirstDateOfMonth(source, month);
		return toString(change, format);
	}

	/**
	 * 지정한 월의 첫번째 날짜를 가져온다
	 * @param source Date 타겟 소스
	 * @param month  변경할 월
	 * @param format 변환할 문자열 포맷
	 * @return Date 객체
	 */
	public static Integer toFirstDateOfMonthWithFormatToInt(Date source, int month, String format) {
		Date change = toFirstDateOfMonth(source, month);
		String formatted = toString(change, format);
		return Integer.parseInt(formatted);
	}

	/**
	 * Instant 객체로 부터 날짜의 특정 부분 값을 가져온다.
	 * @param source Instant 객체
	 * @param field 날짜 부분
	 * @return 해당 부분의 값
	 * @throws Exception Instant 객체나 날짜 부분에 대한 명시가 유효하지 않은 경우 발생
	 */
	public static Integer getFrom(Instant source, ChronoField field) throws Exception {

		if(source == null)
			throw new Exception("날짜가 유효하지 않습니다.");

		LocalDateTime dateTime = LocalDateTime.ofInstant(source, ZoneId.of("Asia/Seoul"));

		if(field == ChronoField.YEAR)
			return dateTime.getYear();
		else if(field == ChronoField.MONTH_OF_YEAR)
			return dateTime.getMonthValue();
		else if(field == ChronoField.DAY_OF_MONTH)
			return dateTime.getDayOfMonth();
		else if(field == ChronoField.HOUR_OF_DAY)
			return dateTime.getHour();
		else if(field == ChronoField.MINUTE_OF_HOUR)
			return dateTime.getMinute();
		else if(field == ChronoField.SECOND_OF_MINUTE)
			return dateTime.getSecond();
		else
			throw new Exception("지원하지 않는 형식입니다.");
	}

	/**
	 * Instant 객체로 부터 날짜의 특정 부분 값을 가져온다.
	 * @param source Instant 객체
	 * @param field 날짜 부분
	 * @return 해당 부분의 값
	 * @throws Exception Instant 객체나 날짜 부분에 대한 명시가 유효하지 않은 경우 발생
	 */
	public static Integer getFrom(LocalDateTime source, ChronoField field) throws Exception {

		if(source == null)
			throw new Exception("날짜가 유효하지 않습니다.");

		if(field == ChronoField.YEAR)
			return source.getYear();
		else if(field == ChronoField.MONTH_OF_YEAR)
			return source.getMonthValue();
		else if(field == ChronoField.DAY_OF_MONTH)
			return source.getDayOfMonth();
		else if(field == ChronoField.HOUR_OF_DAY)
			return source.getHour();
		else if(field == ChronoField.MINUTE_OF_HOUR)
			return source.getMinute();
		else if(field == ChronoField.SECOND_OF_MINUTE)
			return source.getSecond();
		else
			throw new Exception("지원하지 않는 형식입니다.");
	}

	/**
	 * LocalDateTime을 Instant로 변환한다.
	 * @param source LocalDateTime 객체
	 * @return Instant 객체
	 */
	public static Instant toInstant(LocalDateTime source) {
		if(source == null) return null;
		return source.atZone(ZoneId.of("Asia/Seoul")).toInstant();
	}

	/**
	 * LocalDate를 Instant로 변환한다.
	 * @param source LocalDate 객체
	 * @return Instant 객체
	 */
	public static Instant toInstant(LocalDate source) {
		if(source == null) return null;
		return source.atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant();
	}

	/**
	 * Date를 Instant로 변환한다.
	 * @param source Date 객체
	 * @return Instant 객체
	 */
	public static Instant toInstant(Date source) {
		if(source == null) return null;
		return source.toInstant();
	}

	/**
	 * Instant를 LocalDate로 변환한다.
	 * @param source Instant 객체
	 * @return LocalDate 객체
	 */
	public static LocalDate toLocalDate(Instant source) {
		if(source == null) return null;
		return LocalDate.ofInstant(source, ZoneId.of("Asia/Seoul"));
	}

	/**
	 * Date를 LocalDate로 변환한다.
	 * @param source Date 객체
	 * @return LocalDate 객체
	 */
	public static LocalDate toLocalDate(Date source) {
		if(source == null) return null;
		return new java.sql.Timestamp(source.getTime()).toLocalDateTime().toLocalDate();
	}

	/**
	 * Instant를 LocalDateTime으로 변환한다.
	 * @param source Instant 객체
	 * @return LocalDateTime 객체
	 */
	public static LocalDateTime toLocalDateTime(Instant source) {
		if(source == null) return null;
		return LocalDateTime.ofInstant(source, ZoneId.of("Asia/Seoul"));
	}

	/**
	 * Instant를 LocalDateTime으로 변환한다.
	 * @param source Instant 객체
	 * @param zoneId ZoneId 객체
	 * @return LocalDateTime 객체
	 */
	public static LocalDateTime toLocalDateTime(Instant source, ZoneId zoneId) {
		if(source == null) return null;
		return LocalDateTime.ofInstant(source, zoneId);
	}

	/**
	 * Instant를 LocalDateTime으로 변환한다.
	 * @param source Instant 객체
	 * @return LocalDateTime 객체
	 */
	public static LocalDateTime toLocalDateTime(Date source) {
		if(source == null) return null;
		return new java.sql.Timestamp(source.getTime()).toLocalDateTime();
	}

	/**
	 * Instant를 Date로 변환한다.
	 * @param source Instant 객체
	 * @return Date 객체
	 */
	public static Date toDate(Instant source) {
		if(source == null) return null;
		return Date.from(source);
	}

	/**
	 * LocalDateTime을 Date로 변환한다.
	 * @param source Instant 객체
	 * @return Date 객체
	 */
	public static Date toDate(LocalDateTime source) {
		if(source == null) return null;
		return  java.sql.Timestamp.valueOf(source);
	}

	/**
	 * LocalDate를 Date로 변환한다.
	 * @param source Instant 객체
	 * @return Date 객체
	 */
	public static Date toDate(LocalDate source) {
		if(source == null) return null;
		return  Date.from(DateUtils.toInstant(source));
	}

	/**
	 * 두 개의 인스턴트 파라미터를 받아 연산하고 , 연산된 결과를 총시간으로 반환한다.
	 * @param startDate 계산될 Instant 오브젝트
	 * @param endDate 	계산될 Instant 오브젝트
	 * @return 연산된 총시간 결과
	 */
	public static long toHoursBetweenTwoDates(Instant startDate, Instant endDate){
		long result = 0;

		try {
			// 시간 보정
			Instant correctStartDate = DateUtils.toDate(DateUtils.toString(startDate, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm").toInstant();
			Instant correctEndDate = DateUtils.toDate(DateUtils.toString(endDate, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm").toInstant();

			// 날자를 연산결과를 초로 가져온다.
			long diffInSeconds = Duration.between(correctStartDate,correctEndDate).getSeconds();

			// 초를 시간으로 변경한다.
			result = diffInSeconds / 3600;
		}
		catch(Exception ignored) {

		}

		// 날자를 연산결과를 초로 가져온다.
		return result;
	}

	/**
	 * 두 개의 인스턴트 파라미터를 받아 연산하고 , 연산된 결과를 총시간으로 반환한다.
	 * @param startDate 계산될 Instant 오브젝트
	 * @param endDate 	계산될 Instant 오브젝트
	 * @return 연산된 총시간 결과
	 */
	public static long toSecondsBetweenTwoDates(Instant startDate, Instant endDate){
		long result = 0;

		try {
			// 시간 보정
			Instant correctStartDate = DateUtils.toDate(DateUtils.toString(startDate, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss").toInstant();
			Instant correctEndDate = DateUtils.toDate(DateUtils.toString(endDate, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss").toInstant();

			result = Duration.between(correctStartDate,correctEndDate).getSeconds();
		}
		catch(Exception ignored) {

		}

		// 날자를 연산결과를 초로 가져온다.
		return result;
	}

	/**
	 * 두 개의 인스턴트 파라미터를 받아 연산하고 , 연산된 결과를 총시간으로 반환한다.
	 * @param startDate 계산될 Instant 오브젝트
	 * @param endDate 	계산될 Instant 오브젝트
	 * @return 연산된 총시간 결과
	 */
	public static long toDaysBetweenTwoDates(Instant startDate, Instant endDate){
		long result = 0;

		try {
			// 날자를 연산결과를 시간으로로 가져온다.
			result = toHoursBetweenTwoDates(startDate,endDate);

			// 초를 시간으로 변경한다.
			result =  result / 24;
		}
		catch(Exception ignored) {

		}

		// 날자를 연산결과를 초로 가져온다.
		return result;
	}

	/**
	 * 분으로 이뤄진 데이터를 시간:분 표시로 변경한다.
	 * @param totalMinute 변경할 분
	 * @return 변경된 데이터 문자열
	 */
	public static String toHourFromTotalMinutes(int totalMinute) {
		try {
			if (totalMinute == 0)
				return "";

			// value를 시간과 분으로 분리합니다.
			long hours = (long) Math.floor(totalMinute / 60);  // 시간 계산
			long minutes = totalMinute % 60;            // 분 계산

			// 시간과 분이 모두 0일 경우 표시하지 않습니다.
			if (hours == 0 && minutes == 0) {
				return "";
			}

			// 분이 0일 경우 시간만 반환합니다.
			if (minutes == 0) {
				return hours + "시간";
			}

			// 시간이 0이고 분이 0이 아닐 경우 분만 반환합니다.
			if (hours == 0 && minutes != 0) {
				return minutes + "분";
			}

			// 시간과 분을 문자열로 변환하여 반환합니다.
			return hours + "시간" + minutes + "분";
		}
		catch (Exception ex) {
			//ignore
		}
		return "";
	}
}
