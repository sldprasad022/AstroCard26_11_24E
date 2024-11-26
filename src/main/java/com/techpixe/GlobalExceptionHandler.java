//package com.techpixe;


//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("status", ex.getStatusCode().value());
//        response.put("error", ((HttpStatus) ex.getStatusCode()).getReasonPhrase());
//        response.put("message", ex.getReason());
//
//        return new ResponseEntity<>(response, ex.getStatusCode());
//    }
//}

package com.techpixe;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import com.techpixe.exception.CustomStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle ResponseStatusException
//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
//        return buildResponse((HttpStatus) ex.getStatusCode(), ex.getReason());
//    }


      
    @ExceptionHandler(CustomStatusException.class)
    public ResponseEntity<Map<String, Object>> handleCustomStatusException(CustomStatusException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status",ex.getStatusCode());
        response.put("error", "Custom Error");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }
    

    // Generic exception handler for unexpected errors
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
//        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
//    }

    // Helper method to build a response entity
    @ExceptionHandler(ResponseStatusException.class)
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);

        return new ResponseEntity<>(response, status);
    }
}





