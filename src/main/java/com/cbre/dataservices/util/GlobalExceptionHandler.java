package com.cbre.dataservices.util;

import com.cbre.dataservices.models.SubmissionStatus;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public @ResponseBody SubmissionStatus defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception{

        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null){
            throw e;
        }

        System.out.println("[EXCEPTION]");
        System.out.println(e.getMessage().toString());
        System.out.println(e.getStackTrace().toString());

        SubmissionStatus resp = new SubmissionStatus(500, e.getMessage());
        return resp;
    }
}
