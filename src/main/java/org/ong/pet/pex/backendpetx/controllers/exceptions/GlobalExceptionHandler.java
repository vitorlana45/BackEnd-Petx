//package org.ong.pet.pex.backendpetx.controllers.exceptions;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.ong.pet.pex.backendpetx.controllers.exceptions.setup.BaseApplicationError;
//import org.ong.pet.pex.backendpetx.service.exceptions.UsuarioNaoAutenticado;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.MessageSource;
//import org.springframework.http.HttpStatus;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindException;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    private final MessageSource messages;
//    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//    public GlobalExceptionHandler(MessageSource messages) {
//        this.messages = messages;
//    }
//
//    /**
//     * Verifica se a requisição é HTMX
//     */
//    private boolean isHtmx(HttpServletRequest req) {
//        return req.getHeader("HX-Request") != null;
//    }
//
//    /**
//     * Resolve a mensagem usando MessageSource
//     */
//    private String resolveMsg(Locale locale, BaseApplicationError ex) {
//        try {
//            // Acessa os campos usando reflection para evitar problemas com Lombok
//            String messageKey = (String) ex.getClass().getMethod("getMessageKey").invoke(ex);
//            Object[] messageArgs = (Object[]) ex.getClass().getMethod("getMessageArgs").invoke(ex);
//
//            if (messageKey == null) return "Ocorreu um erro.";
//            return messages.getMessage(messageKey, messageArgs, messageKey, locale);
//        } catch (Exception e) {
//            log.warn("Erro ao resolver mensagem: {}", e.getMessage());
//            return "Ocorreu um erro inesperado.";
//        }
//    }
//
//    /**
//     * Obtém o status HTTP da exceção
//     */
//    private HttpStatus getStatus(BaseApplicationError ex) {
//        try {
//            return (HttpStatus) ex.getClass().getMethod("getStatus").invoke(ex);
//        } catch (Exception e) {
//            log.warn("Erro ao obter status: {}", e.getMessage());
//            return HttpStatus.BAD_REQUEST;
//        }
//    }
//
//    @ExceptionHandler(BaseApplicationError.class)
//    public Object handleBase(BaseApplicationError ex,
//                             HttpServletRequest req,
//                             HttpServletResponse res,
//                             Model model,
//                             RedirectAttributes ra,
//                             Locale locale) {
//        String msg = resolveMsg(locale, ex);
//        HttpStatus status = ex.getStatus() != null ? ex.getStatus() : HttpStatus.BAD_REQUEST;
//
//        // HTMX (modal): renderiza só o fragmento de alerta do modal
//        if (req.getHeader("HX-Request") != null) {
//            res.setStatus(status.value());
//            model.addAttribute("mensagemErro", msg);
//            return "fragmentos/messages :: modalMessages";
//        }
//
//        // Full page: usa flash + redirect (sem query ?erro=)
//        ra.addFlashAttribute("mensagemErro", msg);
//        String referer = req.getHeader("Referer");
//        return "redirect:" + (referer != null ? referer : "/animais");
//    }
//
//    /**
//     * Trata erros de validação de formulário
//     */
//    @ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
//    public Object handleValidation(Exception ex,
//                                   HttpServletRequest req,
//                                   HttpServletResponse res,
//                                   Model model) {
//        List<String> erros = new ArrayList<>();
//        BindingResult br = ex instanceof MethodArgumentNotValidException manv
//                ? manv.getBindingResult()
//                : ((BindException) ex).getBindingResult();
//
//        br.getAllErrors().forEach(e -> erros.add(e.getDefaultMessage()));
//
//        if (isHtmx(req)) {
//            res.setStatus(422);
//            model.addAttribute("mensagemErro", "Corrija os campos indicados.");
//            model.addAttribute("mensagemErroDetalhe", String.join("\n", erros));
//            return "fragmentos/messages :: modalMessages";
//        }
//
//        // Para requisições normais, redireciona para a página de origem
//        String referer = req.getHeader("Referer");
//        if (referer != null) {
//            return "redirect:" + referer + "?erro=" + java.net.URLEncoder.encode("Corrija os campos indicados.", java.nio.charset.StandardCharsets.UTF_8);
//        }
//        return "redirect:/animais?erro=" + java.net.URLEncoder.encode("Corrija os campos indicados.", java.nio.charset.StandardCharsets.UTF_8);
//    }
//
//    /**
//     * Fallback para qualquer exceção não mapeada
//     */
//    @ExceptionHandler(Exception.class)
//    public Object handleUnexpected(Exception ex,
//                                   HttpServletRequest req,
//                                   HttpServletResponse res,
//                                   Model model) {
//        log.error("Erro inesperado", ex);
//        String msg = "Ocorreu um erro inesperado. Tente novamente.";
//
//        if (isHtmx(req)) {
//            res.setStatus(500);
//            model.addAttribute("mensagemErro", msg);
//            return "fragmentos/messages :: modalMessages";
//        }
//
//        // Para requisições normais, redireciona para a página de origem
//        String referer = req.getHeader("Referer");
//        if (referer != null) {
//            String separator = referer.contains("?") ? "&" : "?";
//            return "redirect:" + referer + separator + "erro=" + java.net.URLEncoder.encode(msg, java.nio.charset.StandardCharsets.UTF_8);
//        }
//        return "redirect:/animais?erro=" + java.net.URLEncoder.encode(msg, java.nio.charset.StandardCharsets.UTF_8);
//    }
//
//    // TODO: fazer a tela de erro de autenticação
//    @ExceptionHandler(UsuarioNaoAutenticado.class)
//    public Object handleUsuarioNaoAutenticado(UsuarioNaoAutenticado ex,
//                                   HttpServletRequest req,
//                                   HttpServletResponse res,
//                                   Model model) {
//        log.error("Usuário não autenticado", ex);
//        String msg = "Usuário não autenticado. Faça login novamente.";
//
//        if (isHtmx(req)) {
//            res.setStatus(401);
//            model.addAttribute("mensagemErro", msg);
//            return "fragmentos/messages :: modalMessages";
//        }
//
//        // Para requisições normais, redireciona para a página de login
//        return "redirect:/login?erro=" + java.net.URLEncoder.encode(msg, java.nio.charset.StandardCharsets.UTF_8);
//    }
//
//}
