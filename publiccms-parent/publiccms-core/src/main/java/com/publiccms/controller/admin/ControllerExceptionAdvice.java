package com.publiccms.controller.admin;

import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.publiccms.common.constants.CommonConstants;

@ControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(ValidationException.class)
    public ModelAndView handleException(ConstraintViolationException e) {
        ModelAndView modelAndView = new ModelAndView(CommonConstants.TEMPLATE_ERROR);
        modelAndView.addObject(CommonConstants.ERROR,
                e.getConstraintViolations().stream()
                        .map(cv -> StringUtils.join(cv.getPropertyPath().toString(), ":", cv.getMessage()))
                        .collect(Collectors.joining(";")));
        modelAndView.addObject("fields", e.getConstraintViolations().stream().map(cv -> cv.getPropertyPath().toString())
                .collect(Collectors.joining(CommonConstants.COMMA_DELIMITED)));
        return modelAndView;
    }
}
