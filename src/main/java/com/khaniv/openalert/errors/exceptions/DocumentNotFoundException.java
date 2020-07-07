package com.khaniv.openalert.errors.exceptions;

import com.khaniv.openalert.documents.BaseDocument;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.UUID;

public class DocumentNotFoundException extends IllegalArgumentException {
    private final static String MESSAGE = "No {0} found by ID: {1}!";
    private final static String COLLECTION_MESSAGE = "No {0}s found by IDs: {1}!";

    public DocumentNotFoundException(Class<? extends BaseDocument> documentClass, UUID id) {
        super(MessageFormat.format(MESSAGE, documentClass.getSimpleName(), id));
    }

    public DocumentNotFoundException(Class<? extends BaseDocument> documentClass, Collection<UUID> id) {
        super(MessageFormat.format(COLLECTION_MESSAGE, documentClass.getSimpleName(), id));
    }

    public DocumentNotFoundException(String message) {
        super(message);
    }
}
