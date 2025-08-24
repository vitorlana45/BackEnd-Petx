package org.ong.pet.pex.backendpetx.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@Configuration
public class GlobalModelAttributes {

    @ModelAttribute
    public void addCurrentPage(HttpServletRequest request, Model model) {
        String uri = request.getRequestURI();
        model.addAttribute("currentPage", uri);
    }

}
