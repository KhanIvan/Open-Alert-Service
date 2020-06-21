package com.khaniv.openalert.converters;

import com.khaniv.openalert.converters.utility.ZonedDateTimeConstants;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.ZonedDateTime;

@Component
@WritingConverter
public class ZonedDateTimeWritingConverter implements Converter<ZonedDateTime, Document> {

    @Override
    public Document convert(ZonedDateTime source) {
        Document document = new Document();
        document.put(ZonedDateTimeConstants.DATE_TIME, Date.from(source.toInstant()));
        document.put(ZonedDateTimeConstants.ZONE, source.getZone().toString());
        return document;
    }
}
