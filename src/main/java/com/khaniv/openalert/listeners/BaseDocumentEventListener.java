package com.khaniv.openalert.listeners;

import com.khaniv.openalert.documents.BaseDocument;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class BaseDocumentEventListener extends AbstractMongoEventListener<BaseDocument> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<BaseDocument> event) {
        ZonedDateTime now = ZonedDateTime.now();
        if (event.getSource().getCreatedAt() == null)
            event.getSource().setCreatedAt(now);

        event.getSource().setUpdatedAt(now);

        super.onBeforeConvert(event);
    }
}
