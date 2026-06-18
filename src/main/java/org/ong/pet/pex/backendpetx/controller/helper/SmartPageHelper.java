package org.ong.pet.pex.backendpetx.controller.helper;

import org.ong.pet.pex.backendpetx.controller.bean.PageInfoBean;
import org.ong.pet.pex.backendpetx.controller.bean.ActionButtonDTO;
import org.ong.pet.pex.backendpetx.controller.bean.StatsDTO;
import org.ong.pet.pex.backendpetx.controller.bean.SmartHeaderConfig;
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
    public static void setupHome(Model model, String username) {
        // Configuração moderna com SmartHeaderConfig
        StringBuilder sb = new StringBuilder();
        if (username != null && !username.isEmpty()) {
            sb.append("Bem-vindo, ").append(username).append("!");
        } else {
            sb.append("Bem-vindo ao sistema de gestão da ONG PetX");
        }

        // Manter compatibilidade com versão antiga
        PageInfoBean pageInfo = PageInfoBean.builder()
                .subtitle(sb.toString())
                .icon("fas fa-heart");
        model.addAttribute("pageInfo", pageInfo);

    }

    /**
     * Configura uma página de listagem de animais
     */
    public static void setupAnimalsPage(Model model) {

        // Manter compatibilidade - usando LISTA para múltiplos botões
        PageInfoBean pageInfo = PageInfoBean.builder()
                .title("Gestão de Animais")
                .subtitle("Administre e acompanhe todos os animais da ONG")
                .icon("fas fa-paw")
                .addBreadcrumb("Animais", "/animais");

        model.addAttribute("pageInfo", pageInfo);

        // SUPORTE A MÚLTIPLOS BOTÕES
        // Se não foi passado actionButtons pelo controller, cria um padrão
        if (!model.containsAttribute("actionButtons")) {
            ActionButtonDTO actionButton = ActionButtonDTO.primary("Novo Animal", "/animais/form", "fas fa-plus");
            // Always use list for consistency
            List<ActionButtonDTO> buttons = Arrays.asList(actionButton);
            model.addAttribute("actionButtons", buttons);
        }
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
                "/tutores/form",
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
