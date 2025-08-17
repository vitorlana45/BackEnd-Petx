//package org.ong.pet.pex.backendpetx.controllers;
//
//import org.ong.pet.pex.backendpetx.enums.*;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
///**
// * Controller para gerenciar as views do Boletim
// */
//@Controller
//@RequestMapping("/boletim")
//@PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
//public class BoletimViewController {
//
//    /**
//     * Exibe o formulário de cadastro de boletim
//     */
//    @GetMapping("/cadastro")
//    public String exibirFormularioCadastro(Model model) {
//        // Adicionar enums para os selects do formulário
//        model.addAttribute("especies", EspecieEnum.values());
//        model.addAttribute("portes", PorteEnum.values());
//        model.addAttribute("statusEnum", StatusEnum.values());
//        model.addAttribute("maturidades", MaturidadeEnum.values());
//        model.addAttribute("origens", OrigemAnimalEnum.values());
//        model.addAttribute("sexos", SexoEnum.values());
//        model.addAttribute("destinos", Destino.values());
//
//        return "boletim/cadastro";
//    }
//}
