package africa.bookcatelog.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBookNotFoundException(BookNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "Timestamp", LocalDateTime.now(),
                        "Status", 404,
                        "Error", "Not_Found",
                        "Message", exception.getMessage()
                ));

    }

        @ExceptionHandler(ExternalApiException.class)
        public ResponseEntity<Map<String ,Object>>handleExternalApi(ExternalApiException exception){

            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of(
                            "Timestamp", LocalDateTime.now(),
                            "Status", 503,
                            "Error", "Service Unavailable",
                            "Message", exception.getMessage()
                    ));

        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<Map <String , Object >> handleInvalidInput(IllegalArgumentException exception){

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "Timestamp", LocalDateTime.now(),
                            "Status", 400,
                            "Error", "Bad Request",
                            "Message", exception.getMessage()
                    ));
        }

    }

