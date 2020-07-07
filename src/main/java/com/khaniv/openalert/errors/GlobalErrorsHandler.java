package com.khaniv.openalert.errors;

import com.khaniv.openalert.errors.exceptions.DocumentDuplicatesException;
import com.khaniv.openalert.errors.exceptions.DocumentNotFoundException;
import com.khaniv.openalert.errors.exceptions.MatchAlreadyExistsException;
import com.khaniv.openalert.errors.exceptions.MaxCountExcessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
@SuppressWarnings("unused")
public class GlobalErrorsHandler {
    @ExceptionHandler(DocumentNotFoundException.class)
    public void handleDocumentNotFoundException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(MatchAlreadyExistsException.class)
    public void handleMatchAlreadyExistsException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(MaxCountExcessException.class)
    public void handleMaxCountExcessException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.PAYLOAD_TOO_LARGE.value());
    }

    @ExceptionHandler(DocumentDuplicatesException.class)
    public void handleDocumentDuplicatesException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
