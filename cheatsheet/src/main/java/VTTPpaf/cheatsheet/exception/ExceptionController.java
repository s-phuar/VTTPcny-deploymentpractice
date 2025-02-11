package VTTPpaf.cheatsheet.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(ResourceNotFoundException.class)
       public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request, HttpServletResponse response){
        //customise ApiError object to throw whatever you want
        // ApiError apiError = new ApiError(response.getStatus(), ex.getMessage(), new Date());
        ApiError apiError = new ApiError(404, ex.getMessage(), new Date());

        return new ResponseEntity<ApiError>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(exception = {IllegalAccessError.class}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleIllegalAccess(IllegalAccessError ex) {
        JsonObject payload = Json.createObjectBuilder()
            .add("message", ex.getMessage())
            .build();
        return ResponseEntity.status(403).body(payload.toString());
    }

    @ExceptionHandler(exception = {IllegalArgumentException.class})
    public ModelAndView handleException(IllegalArgumentException ex) {
        System.err.println(">>>>> In ExceptionHandler");
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", "Handler by ExceptionHandler");
        mav.addObject("message", "The message occurs on %s".formatted(new Date().toString()));
        mav.setStatus(HttpStatusCode.valueOf(400));
        return mav;
    }    

}
