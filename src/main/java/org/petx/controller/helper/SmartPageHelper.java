package org.petx.controller.helper;

import org.petx.dto.PageInfoBean;
import org.petx.dto.ActionButtonDTO;
import org.petx.dto.StatsDTO;
import org.springframework.ui.Model;
import java.util.Arrays;
import java.util.List;

/**
 * Helper para criação inteligente de páginas
 * Centraliza a lógica de montagem de cabeçalhos, breadcrumbs e estatísticas
 */
public class SmartPageHelper {

    /**
     * Configura uma página de dashboard com estatísticas
     */
    public static void setupDashboard(Model model, Long totalAnimais, Long totalTutores, Long totalConsumo) {
        // Página base
        PageInfoBean pageInfo = PageInfoBean.builder()
                .title("Dashboard")
                .subtitle("Bem-vindo ao sistema de gestão da ONG PetX")
                .icon("fas fa-tachometer-alt");
        model.addAttribute("pageInfo", pageInfo);

        // Estatísticas dinâmicas
        List<StatsDTO> stats = Arrays.asList(
            StatsDTO.primary(
                String.valueOf(totalAnimais != null ? totalAnimais : 0), 
                "Total de Animais", 
                "fas fa-paw"
            ),
            StatsDTO.success("0", "Adoções Realizadas", "fas fa-heart"),
            StatsDTO.info(
                String.valueOf(totalTutores != null ? totalTutores : 0), 
                "Tutores Cadastrados", 
                "fas fa-users"
            ),
            StatsDTO.warning("0", "Boletins de Resgate", "fas fa-clipboard-list")
        );
        model.addAttribute("statsData", stats);
    }

    /**
     * Configura uma página de listagem de animais
     */
    public static void setupAnimalsPage(Model model) {
        // Página com breadcrumb
        PageInfoBean pageInfo = PageInfoBean.builder()
                .title("Gestão de Animais")
                .subtitle("Administre e acompanhe todos os animais da ONG")
                .icon("fas fa-paw")
                .addBreadcrumb("Animais");
        model.addAttribute("pageInfo", pageInfo);

        // Botão de ação principal
        ActionButtonDTO actionButton = ActionButtonDTO.primary(
                "Novo Animal", 
                "/animais/novo", 
                "fas fa-plus"
        );
        model.addAttribute("actionButton", actionButton);
    }

    /**
     * Configura uma página de listagem de tutores
     */
    public static void setupTutorsPage(Model model) {
        PageInfoBean pageInfo = PageInfoBean.builder()
                .title("Gestão de Tutores")
                .subtitle("Administre e acompanhe todos os tutores cadastrados")
                .icon("fas fa-users")
                .addBreadcrumb("Tutores");
        model.addAttribute("pageInfo", pageInfo);

        ActionButtonDTO actionButton = ActionButtonDTO.primary(
                "Novo Tutor", 
                "/tutores/novo", 
                "fas fa-user-plus"
        );
        model.addAttribute("actionButton", actionButton);
    }

    /**
     * Configura uma página de detalhes de animal
     */
    public static void setupAnimalDetails(Model model, String animalNome) {
        PageInfoBean pageInfo = PageInfoBean.builder()
                .title("Detalhes do Animal")
                .subtitle("Informações completas sobre " + animalNome)
                .icon("fas fa-paw")
                .addBreadcrumb("Animais", "/animais")
                .addBreadcrumb(animalNome);
        model.addAttribute("pageInfo", pageInfo);

        ActionButtonDTO actionButton = ActionButtonDTO.warning(
                "Editar Animal", 
                "/animais/" + animalNome + "/editar", 
                "fas fa-edit"
        );
        model.addAttribute("actionButton", actionButton);
    }

    /**
     * Configura página de formulário (novo/editar)
     */
    public static void setupFormPage(Model model, String entityType, String action, String backUrl) {
        String title = action.equals("novo") ? "Novo " + entityType : "Editar " + entityType;
        String subtitle = action.equals("novo") ? 
                "Preencha os dados para cadastrar" : 
                "Atualize as informações necessárias";

        PageInfoBean pageInfo = PageInfoBean.builder()
                .title(title)
                .subtitle(subtitle)
                .icon("fas fa-edit")
                .addBreadcrumb(entityType + "s", "/" + entityType.toLowerCase() + "s")
                .addBreadcrumb(action.equals("novo") ? "Novo" : "Editar");
        model.addAttribute("pageInfo", pageInfo);

        ActionButtonDTO actionButton = ActionButtonDTO.success(
                "Voltar", 
                backUrl, 
                "fas fa-arrow-left"
        );
        model.addAttribute("actionButton", actionButton);
    }

    /**
     * Método universal para páginas customizadas
     */
    public static PageInfoBean customPage(String title, String subtitle, String icon) {
        return PageInfoBean.builder()
                .title(title)
                .subtitle(subtitle)
                .icon(icon);
    }
}
