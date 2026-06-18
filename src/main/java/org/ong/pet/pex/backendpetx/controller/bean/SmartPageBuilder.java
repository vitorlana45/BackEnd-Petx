package org.ong.pet.pex.backendpetx.controllers.bean;

import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder inteligente para construção de páginas
 * Permite criar cabeçalhos, breadcrumbs, estatísticas e ações de forma fluente
 */
public class SmartPageBuilder {
    private String title;
    private String subtitle;
    private String icon;
    private List<String[]> breadcrumbs = new ArrayList<>();
    private ActionButtonDTO actionButton;
    private List<StatsDTO> stats = new ArrayList<>();

    private SmartPageBuilder() {}

    public static SmartPageBuilder create() {
        return new SmartPageBuilder();
    }

    // === CONFIGURAÇÕES BÁSICAS DA PÁGINA ===
    public SmartPageBuilder title(String title) {
        this.title = title;
        return this;
    }

    public SmartPageBuilder subtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public SmartPageBuilder icon(String icon) {
        this.icon = icon;
        return this;
    }

    // === BREADCRUMB ===
    public SmartPageBuilder addBreadcrumb(String name, String url) {
        this.breadcrumbs.add(new String[]{name, url});
        return this;
    }

    public SmartPageBuilder addBreadcrumb(String name) {
        this.breadcrumbs.add(new String[]{name, null});
        return this;
    }

    // === BOTÃO DE AÇÃO PRINCIPAL ===
    public SmartPageBuilder primaryAction(String text, String url, String icon) {
        this.actionButton = ActionButtonDTO.primary(text, url, icon);
        return this;
    }

    // === ESTATÍSTICAS ===
    public SmartPageBuilder addPrimaryStat(String value, String label, String icon) {
        this.stats.add(StatsDTO.primary(value, label, icon));
        return this;
    }

    public SmartPageBuilder addSuccessStat(String value, String label, String icon) {
        this.stats.add(StatsDTO.success(value, label, icon));
        return this;
    }

    public SmartPageBuilder addWarningStat(String value, String label, String icon) {
        this.stats.add(StatsDTO.warning(value, label, icon));
        return this;
    }

    public SmartPageBuilder addInfoStat(String value, String label, String icon) {
        this.stats.add(StatsDTO.info(value, label, icon));
        return this;
    }

    // === BUILDERS PRÉ-CONFIGURADOS PARA PÁGINAS COMUNS ===

    /**
     * Configuração padrão para página de listagem de animais
     */
    public static SmartPageBuilder animalsListPage() {
        return create()
                .title("Gestão de Animais")
                .subtitle("Administre e acompanhe todos os animais da ONG")
                .icon("fas fa-paw")
                .addBreadcrumb("Animais")
                .primaryAction("Novo Animal", "/animais/form", "fas fa-plus");
    }

    /**
     * Configuração padrão para dashboard
     */
    public static SmartPageBuilder homePage() {
        return create()
                .title("Home")
                .subtitle("Bem-vindo ao sistema de gestão da ONG PetX")
                .icon("fas fa-tachometer-alt");
    }

    /**
     * Configuração padrão para página de tutores
     */
    public static SmartPageBuilder tutorsListPage() {
        return create()
                .title("Gestão de Tutores")
                .subtitle("Administre e acompanhe todos os tutores cadastrados")
                .icon("fas fa-users")
                .addBreadcrumb("Tutores")
                .primaryAction("Novo Tutor", "/tutores/form", "fas fa-user-plus");
    }

    // === MÉTODO DE BUILD ===
    public void applyToModel(Model model) {
        // Criar PageInfoDTO usando método estático
        var pageInfo = PageInfoBean.builder()
                .title(title)
                .subtitle(subtitle)
                .icon(icon);

        // Adicionar breadcrumbs
        for (String[] crumb : breadcrumbs) {
            if (crumb[1] != null) {
                pageInfo.addBreadcrumb(crumb[0], crumb[1]);
            } else {
                pageInfo.addBreadcrumb(crumb[0]);
            }
        }

        model.addAttribute("pageInfo", pageInfo);

        if (actionButton != null) {
            model.addAttribute("actionButton", actionButton);
        }

        if (!stats.isEmpty()) {
            model.addAttribute("statsData", stats);
        }
    }
}
