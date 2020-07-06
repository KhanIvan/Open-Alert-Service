package com.khaniv.openalert.errors.handlers;

import com.khaniv.openalert.errors.DocumentNotFoundException;
import com.khaniv.openalert.errors.MatchAlreadyExistsException;
import com.khaniv.openalert.errors.MaxCountExcessException;
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
        response.sendError(HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler(MaxCountExcessException.class)
    public void handleMaxCountExcessException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.PAYLOAD_TOO_LARGE.value());
    }
}
