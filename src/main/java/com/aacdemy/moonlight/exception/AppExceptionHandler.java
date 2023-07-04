package com.aacdemy.moonlight.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@ControllerAdvice
public class AppExceptionHandler {


    /**
     * thrown when trying to delete admin user.
     */
    @ExceptionHandler(AdminDeleteException.class)
    public void handlerAdminDeleteException(AdminDeleteException ex, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write("{\"error\": \"" + ex.getMessage() + "\"}");
    }

    /**
     * thrown when authentication is failed.
     */
    @ExceptionHandler(AuthenticationException.class)
    public void handleAuthenticationException(Exception ex, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"Authentication failed\"}");
    }

    /*
     * thrown when jwt token missing in preauthorized request
     * example: @PreAuthorize("#phoneNumber == authentication.getPrincipal().phoneNumber
     */
    @ExceptionHandler(SpelEvaluationException.class)
    public void handleSpelEvaluationException(SpelEvaluationException ex, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"JWT token missing in the request\"}");
    }

    /**
     * thrown when trying to access forbidden resources.
     */
    @ExceptionHandler(AccessDeniedException.class)
    // just existing, not working even with .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler)
    void handleAccessDeniedException(HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"You do not have permission to access this resource\"}");
    }

    /**
     * custom exception thrown when entity is not found.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    void handlerEntityNotFoundException(EntityNotFoundException ex, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"" + ex.getMessage() + "\"}");
    }

    /**
     * thrown when the provided passwords do not match during a password change request.
     */
    @ExceptionHandler(PasswordMismatchException.class)
    void handlePasswordMismatchException(PasswordMismatchException ex, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"" + ex.getMessage() + "\"}");
    }

    /**
     * thrown when there is a validation error while processing user input, such as incorrect or missing fields..
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public void handleValidationException(ValidationException e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(Map.of("errors", e.getMessage()));
        response.getWriter().write(json);
    }


    /**
     * thrown when illegal arguments are used such as invalid character found in method name
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    void handleIllegalArgumentException(IllegalArgumentException ex, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(Map.of("errors:", ex.getMessage()));
        response.getWriter().write(json);
    }

    /**
     * thrown when a constraint violation occurs, such as a unique key constraint violation
     * or a foreign key constraint violation
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    void handleConstraintViolationException(ConstraintViolationException ex, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        List<String> errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).toList();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(Map.of("errors", errors));
        response.getWriter().write(json);
    }

    /**
     * thrown when there is a violation of a database integrity constraint
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseBody
    void handleDataIntegrityViolationException(SQLIntegrityConstraintViolationException ex, HttpServletResponse response) throws IOException {
        String errorMessage = ex.getMessage();
        //removes the name of the field in the response  "Duplicate entry 'test30@gmail.com' for key 'users.UK_6dotkott2kjsp8vw4d0m25fb7'"
        int index = errorMessage.indexOf("for key");
        if (index != -1) {
            errorMessage = errorMessage.substring(0, index).trim();

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(Map.of("errors", errorMessage));
            response.getWriter().write(json);
        }
    }

    /**
     * thrown when validation of request body fails
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    void handleValidationExceptions2(MethodArgumentNotValidException ex, HttpServletResponse response) throws IOException {
        BindingResult result = ex.getBindingResult();
        List<String> errorMessages = result.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(Map.of("errors:", errorMessages));
        response.getWriter().write(json);
    }

    /**
     * thrown when a required parameter is missing in the request
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    void handleMissingParameterException(MissingServletRequestParameterException e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"Required parameter \"" + e.getParameterName() + "\" is missing!\"}");
    }

    /**
     * thrown when a required part is missing in the request
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    void handleMissingPartException(MissingServletRequestPartException e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"Required part \"" + e.getRequestPartName() + "\" is missing!\"}");

    }

    /**
     * thrown when a required request header is missing in the request
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    void handleMissingHeaderException(MissingRequestHeaderException e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"Required logged user!\"}");
    }

    /**
     * thrown when an argument in the request does not match the expected type
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    void handleMismatchInputsException(MethodArgumentTypeMismatchException e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"Invalid input data. Please check " + e.getParameter().getParameterName() + " and try again.\"}");
    }

    @ExceptionHandler(DateTimeParseException.class)
    public void handleDateTimeParseException(DateTimeParseException exception, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"Invalid input data. Please check input " + exception.getParsedString() + " and try again with format yyyy-MM-dd.\"}");
    }
}
