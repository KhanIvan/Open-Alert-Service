package com.khaniv.openalert.converters;

import com.khaniv.openalert.converters.utility.ZonedDateTimeConstants;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@ReadingConverter
public class ZonedDateTimeReadingConverter implements Converter<Document, ZonedDateTime> {
    @Override
    public ZonedDateTime convert(Document source) {
        return source.getDate(ZonedDateTimeConstants.DATE_TIME)
                .toInstant()
                .atZone(ZoneId.of(source.getString(ZonedDateTimeConstants.ZONE)));
    }
}
