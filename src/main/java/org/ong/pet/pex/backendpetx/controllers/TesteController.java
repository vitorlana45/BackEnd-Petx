package org.ong.pet.pex.backendpetx.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ong.pet.pex.backendpetx.controllers.exceptions.setup.AppException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teste")
public class TesteController {

    @GetMapping("/excecao")
    public String testarExcecao(HttpServletRequest req, HttpServletResponse res, Model model) {
        // Simula uma exceção de chip duplicado
        throw AppException.chipDuplicado("TESTE123");
    }

    @PostMapping("/excecao-htmx")
    public String testarExcecaoHtmx(HttpServletRequest req, HttpServletResponse res, Model model) {
        // Simula uma exceção de chip duplicado via HTMX
        throw AppException.chipDuplicado("TESTE456");
    }

    @GetMapping("/pagina-teste")
    public String paginaTeste(Model model) {
        return "teste/pagina-teste";
    }
}
