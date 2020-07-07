package com.khaniv.openalert.errors.exceptions;

import com.khaniv.openalert.documents.BaseDocument;

import java.text.MessageFormat;

public class DocumentDuplicatesException extends IllegalArgumentException {
    private final static String MESSAGE = "Collection of {0} contains duplicates!";

    public DocumentDuplicatesException(Class<? extends BaseDocument> documentClass) {
        super(MessageFormat.format(MESSAGE, documentClass.getSimpleName()));
    }
}
