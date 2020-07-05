package com.khaniv.openalert.errors;

import com.khaniv.openalert.documents.BaseDocument;

import java.text.MessageFormat;

public class MaxCountExcessException extends IllegalArgumentException {
    private final static String MESSAGE = "Collection of {0}s is too big! Maximum allowed size is {1}.";

    public MaxCountExcessException(Class<? extends BaseDocument> documentClass, int maxCount) {
        super(MessageFormat.format(MESSAGE, documentClass.getSimpleName(), maxCount));
    }
}
