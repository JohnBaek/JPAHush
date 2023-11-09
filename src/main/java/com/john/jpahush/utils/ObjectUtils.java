package com.john.jpahush.utils;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 객체 유틸리티 클래
 */
@SuppressWarnings("unused")
public class ObjectUtils {

    /**
     * 객체를 생성하고 값을 복사한다.
     * @param source 원본 값 객체
     * @param clazz 생성할 타입에 대한 클래스 객체
     * @param <T> 생성할 타입
     * @return 생성된 객체
     */
    @SuppressWarnings("unchecked")
    public static <T> T createAndCopy(Object source, Class<T> clazz) {
        T result = null;

        try {
            try {
                // 객체를 생성
                result = clazz.getConstructor(source.getClass()).newInstance(source);
            } catch (Exception e) {
                // 객체를 생성
                result = clazz.getConstructor().newInstance();

                // 원본 객체가 목록인 경우
                if(result instanceof List && source instanceof List) {
                    for(Object item: (List<Object>)source) {
                        ((List<Object>)result).add(item);
                    }
                }
                // 원본 객체가 목록이 아닌 경우
                else {
                    // 값 복사
                    BeanUtils.copyProperties(source, result);
                }
            }
        } catch (Exception e) {
            MoreExceptionHandler.Log(e);
        }

        return result;
    }

    /**
     * 객체의 값을 복사한다.
     * @param source 원본 값 객체
     * @param target 값을 복사할 대상 객체
     * @param <T> 생성할 타입
     * @return 생성된 객체
     */
    @SuppressWarnings("unchecked")
    public static <T> T copy(Object source, T target) {

        try {
            // 원본 객체가 목록인 경우
            if(target instanceof List && source instanceof List) {
                for(Object item: (List<Object>)source) {
                    ((List<Object>)target).add(item);
                }
            }
            // 원본 객체가 목록이 아닌 경우
            else {
                // 값 복사
                BeanUtils.copyProperties(source, target);
            }
        } catch (Exception e) {
            MoreExceptionHandler.Log(e);
        }

        return target;
    }

    /**
     * 특정 객체에서 해당 속성명의 값을 가져온다.
     * @param obj 값을 가져올 객체
     * @param property 프로퍼티명
     * @return 해당 프로퍼티의 값
     */
    public static Object getProperty(Object obj, String property) {
        Object returnValue = null;

        try {
            String methodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
            Method method = obj.getClass().getMethod(methodName);
            returnValue = method.invoke(obj);
        }
        catch (Exception e) {
            // Do nothing, we'll return the default value
        }

        return returnValue;
    }

    /**
     * 특정 값으로 중복항목을 제거한다.
     * @param keyExtractor 키 추출 객체
     * @param <T> 타입
     * @return 중복항목 제거 결과
     */
    public static <T> Predicate<T> distinctBy(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }


    /**
     * 스트링에 패딩을 추가한다.
     * @param inputString 스트링값
     * @param length      최종적으로 보여질 길이
     * @return 패딩정보 추가된 반환값
     */
    public static String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
}
