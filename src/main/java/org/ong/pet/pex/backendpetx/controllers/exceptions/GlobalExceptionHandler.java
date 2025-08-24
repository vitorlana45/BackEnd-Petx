package org.ong.pet.pex.backendpetx.controllers.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.ong.pet.pex.backendpetx.controllers.exceptions.setup.BaseApplicationError;
import org.ong.pet.pex.backendpetx.service.exceptions.PetXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messages;
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private boolean isHtmx(HttpServletRequest req) {
        return req.getHeader("HX-Request") != null;
    }

    private String resolveMsg(Locale locale, BaseApplicationError ex) {
        if (ex.getMessageKey() == null) return "Ocorreu um erro.";
        return messages.getMessage(ex.getMessageKey(),
                ex.getMessageArgs(),
                ex.getMessageKey(), // fallback: mostra a key
                locale);
    }

    /** 1) TODAS as exceções de negócio do app caem aqui */
    @ExceptionHandler(BaseApplicationError.class)
    public Object handleBase(BaseApplicationError ex,
                             HttpServletRequest req,
                             HttpServletResponse res,
                             Model model,
                             Locale locale) {
        log.warn("Erro de negócio: {}", ex.getMessage(), ex);

        String msg = resolveMsg(locale, ex);
        HttpStatus status = ex.getStatus() != null ? ex.getStatus() : HttpStatus.BAD_REQUEST;

        if (isHtmx(req)) {
            res.setStatus(status.value());
            model.addAttribute("mensagemErro", msg);
            return "fragmentos/messages :: messages"; // preenche o container do modal
        }

        model.addAttribute("mensagemErro", msg);
        return "error/app"; // view genérica; sem redirect
    }

    /** 2) Validação (opcional, se você usa @Valid/BindingResult) */
    @ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
    public Object handleValidation(Exception ex,
                                   HttpServletRequest req,
                                   HttpServletResponse res,
                                   Model model) {
        List<String> erros = new ArrayList<>();
        BindingResult br = ex instanceof MethodArgumentNotValidException manv
                ? manv.getBindingResult()
                : ((BindException) ex).getBindingResult();

        br.getAllErrors().forEach(e -> erros.add(e.getDefaultMessage()));

        if (isHtmx(req)) {
            res.setStatus(422);
            model.addAttribute("mensagemErro", "Corrija os campos indicados.");
            // seu fragmento já lista erros se houver __thymeleafFields/#fields, mas
            // se quiser pode mandar uma string agregada:
            model.addAttribute("mensagemErroDetalhe", String.join("\n", erros));
            return "fragmentos/messages :: messages";
        }

        model.addAttribute("mensagemErro", "Corrija os campos indicados.");
        model.addAttribute("mensagemErroDetalhe", String.join("\n", erros));
        return "error/app";
    }

    /** 3) Fallback para qualquer coisa não mapeada */
    @ExceptionHandler(Exception.class)
    public Object handleUnexpected(Exception ex,
                                   HttpServletRequest req,
                                   HttpServletResponse res,
                                   Model model) {
        log.error("Erro inesperado", ex);
        String msg = "Ocorreu um erro inesperado. Tente novamente.";

        if (isHtmx(req)) {
            res.setStatus(500);
            model.addAttribute("mensagemErro", msg);
            return "fragmentos/messages :: messages";
        }

        model.addAttribute("mensagemErro", msg);
        return "error/app";
    }
}