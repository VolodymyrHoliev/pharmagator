package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.dataproviders.dto.aptslav.ResponseBodyIsNullException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class PharmagatorControllerAdvice {

    @ExceptionHandler(ResponseBodyIsNullException.class)
    public void handleAptslavApiException(ResponseBodyIsNullException exception) {
        log.error(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handle(IllegalArgumentException e, Model model) {
        log.error(e.getMessage());

        model.addAttribute("errorMessage", e.getMessage());

        return new ModelAndView("error-page");
    }
}
