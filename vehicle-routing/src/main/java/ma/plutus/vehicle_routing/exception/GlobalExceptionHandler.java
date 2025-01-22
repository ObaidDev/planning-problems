package ma.plutus.vehicle_routing.exception;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import ma.plutus.vehicle_routing.dto.ErrorResponse;


@Slf4j
@Hidden
@RestControllerAdvice  
public class GlobalExceptionHandler {


    @ExceptionHandler(RoutePlanNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoRoutePlanFoundException(RoutePlanNotFoundException ex) {
        log.error("NoRoutePlanFoundException: {}", ex.getMessage());

        ErrorResponse.ErrorDetail errorDetail = new ErrorResponse.ErrorDetail();
        errorDetail.setField("general"); // Default field
        errorDetail.setMessage(ex.getMessage());

        // Create the error response
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(false);
        errorResponse.setMessage("Processing failed");
        errorResponse.setErrors(List.of(errorDetail));


        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){


        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(false);
        errorResponse.setMessage("Validation failed");

        // Extract validation errors from the exception
        List<ErrorResponse.ErrorDetail> validationErrors = ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> {
                ErrorResponse.ErrorDetail errorDetail = new ErrorResponse.ErrorDetail();
                errorDetail.setField(fieldError.getField()); // Get the field name
                errorDetail.setMessage(fieldError.getDefaultMessage()); // Get the error message
                return errorDetail;
            })
            .collect(Collectors.toList());

        errorResponse.setErrors(validationErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

}
