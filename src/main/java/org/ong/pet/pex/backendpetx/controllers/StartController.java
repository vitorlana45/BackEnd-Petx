package org.ong.pet.pex.backendpetx.controllers;

import lombok.extern.slf4j.Slf4j;
import org.ong.pet.pex.backendpetx.controllers.bean.StatsDTO;
import org.ong.pet.pex.backendpetx.controllers.bean.PageInfoBean;
import org.ong.pet.pex.backendpetx.controllers.bean.ActionButtonDTO;
import org.ong.pet.pex.backendpetx.service.StatisticService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.*;

@Slf4j
@Controller
public class StartController {

    private final StatisticService statisticService;

    public StartController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/home")
    public String init(Model model, Principal principal) {
        try {
            long totalAnimais = statisticService.getQuantidadeAnimais();
            long totalTutores = statisticService.getQuantidadeTutores();
            long totalAdocoes = statisticService.getQuantidadeAdocoes();

            StatsDTO statsAnimais = StatsDTO.primary(String.valueOf(totalAnimais), "Animais", "fas fa-paw");
            StatsDTO statsTutores = StatsDTO.success(String.valueOf(totalTutores), "Tutores", "fas fa-user");
            StatsDTO statsAdocoes = StatsDTO.info(String.valueOf(totalAdocoes), "Adoções", "fas fa-home");

            // mocks (considere mover para service)
            StatsDTO crescimentoMensal = StatsDTO.warning("+15%", "Crescimento Mensal", "fas fa-chart-line");
            StatsDTO totalBoletins = StatsDTO.info("32", "Boletins", "fas fa-clipboard-list");

            model.addAttribute("totalAnimais", statsAnimais);
            model.addAttribute("totalTutores", statsTutores);
            model.addAttribute("totalAdocoes", statsAdocoes);
            model.addAttribute("totalBoletins", totalBoletins);
            model.addAttribute("crescimentoMensal", crescimentoMensal);

            Map<String, Integer> animaisPorStatus = new LinkedHashMap<>();
            animaisPorStatus.put("Disponível", 24);
            animaisPorStatus.put("Adotado", 35);
            animaisPorStatus.put("Em tratamento", 18);
            animaisPorStatus.put("Doente", 4);

            model.addAttribute("animaisPorStatus", animaisPorStatus);

            String username = (principal != null) ? principal.getName() : "visitante";

            // Se tiver PageInfoBean, use-o; caso contrário, mantenha Map.
            PageInfoBean pageInfo = new PageInfoBean();
            pageInfo.setTitle("Home");
            pageInfo.setSubtitle("Visão geral do sistema");
            pageInfo.setIcon("fas fa-tachometer-alt");
            pageInfo.setSubtitle("Bem-vindo de volta, " + username + "!");

            List<PageInfoBean.BreadcrumbItem> breadcrumbs = new ArrayList<>();
            breadcrumbs.add(new PageInfoBean.BreadcrumbItem("Home", "/home"));
            pageInfo.setBreadcrumbs(breadcrumbs);

            model.addAttribute("pageInfo", pageInfo);

            // actionButtons (plural)
            List<ActionButtonDTO> actionButtons = List.of(
                    new ActionButtonDTO("Novo Animal", "ANIMAIS.FORM", "fas fa-plus","btn-primary")
            );
            model.addAttribute("actionButtons", actionButtons);

            log.info("Dashboard carregado com sucesso para {}", username);
            return "index";
        } catch (Exception e) {
            log.error("Erro ao carregar dashboard", e);
            model.addAttribute("error", "Ocorreu um erro ao carregar o dashboard: " + e.getMessage());
            return "error/generic";
        }
    }
}
