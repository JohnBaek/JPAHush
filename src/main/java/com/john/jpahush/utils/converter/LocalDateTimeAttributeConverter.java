package com.john.jpahush.utils.converter;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * JPA timestamp 타입을 LocalDateTime 으로 변환하기 위한 컨버터
 */
@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    /**
     * LocalDateTime 에서 Timestamp 로
     * @param localDateTime LocalDateTime 데이터
     * @return TimeStamp
     */
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
        return localDateTime == null ? null : Timestamp.valueOf(localDateTime);
    }

    /**
     * TimeStamp 에서 LocalDateTime 로
     * @param timestamp Timestamp 데이터
     * @return LocalDateTime
     */
    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}
