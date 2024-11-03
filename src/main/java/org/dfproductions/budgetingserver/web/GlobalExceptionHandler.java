package org.dfproductions.budgetingserver.web;

import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<String> handleInvalidJwtSignature(SignatureException ex) {
        return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
    }
}
