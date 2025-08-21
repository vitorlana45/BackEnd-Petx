package org.ong.pet.pex.backendpetx.controllers.helper;

import org.ong.pet.pex.backendpetx.controllers.bean.PageInfoBean;
import org.ong.pet.pex.backendpetx.controllers.bean.ActionButtonDTO;
import org.ong.pet.pex.backendpetx.controllers.bean.StatsDTO;
import org.ong.pet.pex.backendpetx.controllers.bean.SmartHeaderConfig;
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
    public static void setupHome(Model model, Long totalAnimais, Long totalTutores, Long totalConsumo) {
        // Configuração moderna com SmartHeaderConfig
        String value = String.valueOf(totalAnimais != null ? totalAnimais : 0);
//        SmartHeaderConfig config = SmartHeaderConfig.forDashboard("Home")
//                .subtitle("Bem-vindo ao sistema de gestão da ONG PetX")
//                .addStat(value, "Total de Animais", "fas fa-paw", "primary")
//                .addStat("0", "Adoções Realizadas", "fas fa-heart", "success")
//                .addStat(value, "Tutores Cadastrados", "fas fa-users", "info")
//                .addStat("0", "Boletins de Resgate", "fas fa-clipboard-list", "warning");
//
//        model.addAttribute("smartHeaderConfig", config);
        
        // Manter compatibilidade com versão antiga
        PageInfoBean pageInfo = PageInfoBean.builder()
                .title("Home")
                .subtitle("Bem-vindo ao sistema de gestão da ONG PetX")
                .icon("fas fa-heart");
        model.addAttribute("pageInfo", pageInfo);

        List<StatsDTO> stats = Arrays.asList(
            StatsDTO.primary(value, "Total de Animais", "fas fa-paw"),
            StatsDTO.success("0", "Adoções Realizadas", "fas fa-heart"),
            StatsDTO.info(String.valueOf(totalTutores != null ? totalTutores : 0), "Tutores Cadastrados", "fas fa-users"),
            StatsDTO.warning("0", "Boletins de Resgate", "fas fa-clipboard-list")
        );
        model.addAttribute("statsData", stats);
    }

    /**
     * Configura uma página de listagem de animais
     */
    public static void setupAnimalsPage(Model model) {
        // Configuração moderna
        SmartHeaderConfig config = SmartHeaderConfig.forListPage(
                "Gestão de Animais",
                "Administre e acompanhe todos os animais da ONG",
                "fas fa-paw",
                "Novo Animal",
                "/animais/novo"
        ).addBreadcrumb("Lista de Animais");

        model.addAttribute("smartHeaderConfig", config);

        // Manter compatibilidade - usando LISTA para múltiplos botões
        PageInfoBean pageInfo = PageInfoBean.builder()
                .title("Gestão de Animais")
                .subtitle("Administre e acompanhe todos os animais da ONG")
                .icon("fas fa-paw")
                .addBreadcrumb("Animais");

        model.addAttribute("pageInfo", pageInfo);

        // SUPORTE A MÚLTIPLOS BOTÕES
        // Se não foi passado actionButtons pelo controller, cria um padrão
        if (!model.containsAttribute("actionButtons")) {
            ActionButtonDTO actionButton = ActionButtonDTO.primary("Novo Animal", "/animais/novo", "fas fa-plus");
            // Always use list for consistency
            List<ActionButtonDTO> buttons = Arrays.asList(actionButton);
            model.addAttribute("actionButtons", buttons);
        }
    }

    /**
     * Configuração moderna apenas com SmartHeaderConfig
     */
    public static void setupAnimalsPageModern(Model model) {
        SmartHeaderConfig config = SmartHeaderConfig.forListPage(
                "Gestão de Animais",
                "Administre e acompanhe todos os animais da ONG",
                "fas fa-paw",
                "Novo Animal",
                "/animais/novo"
        ).addBreadcrumb("Animais");

        model.addAttribute("config", config);
    }


    public static void setupPageWithSingleButton(Model model, String title, String subtitle, String icon, 
                                                 String buttonText, String buttonUrl, String buttonIcon) {
        PageInfoBean pageInfo = PageInfoBean.builder()
                .title(title)
                .subtitle(subtitle) 
                .icon(icon);
        model.addAttribute("pageInfo", pageInfo);

        ActionButtonDTO button = ActionButtonDTO.primary(buttonText, buttonUrl, buttonIcon);
        // Always use list for consistency
        List<ActionButtonDTO> buttons = Arrays.asList(button);
        model.addAttribute("actionButtons", buttons);
    }

    public static void setupPageWithMultipleButtons(Model model, String title, String subtitle, String icon, 
                                                   List<ActionButtonDTO> buttons) {
        PageInfoBean pageInfo = PageInfoBean.builder()
                .title(title)
                .subtitle(subtitle)
                .icon(icon);
        model.addAttribute("pageInfo", pageInfo);
        
        model.addAttribute("actionButtons", buttons); // Lista de objetos
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
        // Always use list for consistency  
        List<ActionButtonDTO> buttons = Arrays.asList(actionButton);
        model.addAttribute("actionButtons", buttons);
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
        // Always use list for consistency
        List<ActionButtonDTO> buttons = Arrays.asList(actionButton);
        model.addAttribute("actionButtons", buttons);
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
        // Always use list for consistency
        List<ActionButtonDTO> buttons = Arrays.asList(actionButton);
        model.addAttribute("actionButtons", buttons);
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
