package com.khaniv.openalert.errors.exceptions;

import java.text.MessageFormat;
import java.util.UUID;

public class MatchAlreadyExistsException extends IllegalArgumentException {
    private final static String MESSAGE = "Match between lost person '{'ID = {0}'}'" +
            " and seen person '{'ID = {1}'}' already exists!";

    public MatchAlreadyExistsException(UUID lostPersonId, UUID seenPersonId) {
        super(MessageFormat.format(MESSAGE, lostPersonId, seenPersonId));
    }
}
