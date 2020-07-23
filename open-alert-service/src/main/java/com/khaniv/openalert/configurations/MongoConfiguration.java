package com.khaniv.openalert.configurations;

import com.khaniv.openalert.converters.ZonedDateTimeReadingConverter;
import com.khaniv.openalert.converters.ZonedDateTimeWritingConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoConfiguration {
    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new ZonedDateTimeReadingConverter());
        converters.add(new ZonedDateTimeWritingConverter());
        return new MongoCustomConversions(converters);
    }
}
