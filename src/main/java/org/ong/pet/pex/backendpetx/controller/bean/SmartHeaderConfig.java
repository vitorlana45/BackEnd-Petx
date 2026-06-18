package org.ong.pet.pex.backendpetx.controller.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO para configuração completa do Smart Header Component
 * Permite máxima flexibilidade na criação de cabeçalhos
 */
public class SmartHeaderConfig {
    
    private String title;
    private String subtitle;
    private String icon;
    private String variant; // default, compact, detailed, minimal
    private List<BreadcrumbItem> breadcrumbs;
    private ActionButtonDTO actionButton;
    private List<StatsDTO> stats;
    
    public SmartHeaderConfig() {
        this.breadcrumbs = new ArrayList<>();
        this.stats = new ArrayList<>();
        this.variant = "default";
    }
    
    // Builder Pattern
    public static SmartHeaderConfig builder() {
        return new SmartHeaderConfig();
    }
    
    public SmartHeaderConfig title(String title) {
        this.title = title;
        return this;
    }
    
    public SmartHeaderConfig subtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }
    
    public SmartHeaderConfig icon(String icon) {
        this.icon = icon;
        return this;
    }
    
    public SmartHeaderConfig variant(String variant) {
        this.variant = variant;
        return this;
    }
    
    public SmartHeaderConfig addBreadcrumb(String name) {
        this.breadcrumbs.add(new BreadcrumbItem(name, null));
        return this;
    }
    
    public SmartHeaderConfig addBreadcrumb(String name, String url) {
        this.breadcrumbs.add(new BreadcrumbItem(name, url));
        return this;
    }
    
    public SmartHeaderConfig actionButton(ActionButtonDTO actionButton) {
        this.actionButton = actionButton;
        return this;
    }
    
    public SmartHeaderConfig actionButton(String text, String url, String icon) {
        this.actionButton = ActionButtonDTO.primary(text, url, icon);
        return this;
    }
    
    public SmartHeaderConfig addStat(StatsDTO stat) {
        this.stats.add(stat);
        return this;
    }
    
    public SmartHeaderConfig addStat(String value, String label, String icon, String type) {
        this.stats.add(new StatsDTO(value, label, icon, type));
        return this;
    }
    
    // Factory Methods para casos comuns
    public static SmartHeaderConfig forPage(String title, String subtitle, String icon) {
        return builder()
                .title(title)
                .subtitle(subtitle)
                .icon(icon);
    }
    
    public static SmartHeaderConfig forListPage(String title, String subtitle, String icon, String addButtonText, String addButtonUrl) {
        return builder()
                .title(title)
                .subtitle(subtitle)
                .icon(icon)
                .actionButton(addButtonText, addButtonUrl, "fas fa-plus");
    }
    
    public static SmartHeaderConfig forDashboard(String title) {
        return builder()
                .title(title)
                .subtitle("Bem-vindo ao sistema de gestão")
                .icon("fas fa-tachometer-alt")
                .variant("detailed");
    }
    
    public static SmartHeaderConfig compact(String title, String icon) {
        return builder()
                .title(title)
                .icon(icon)
                .variant("compact");
    }
    
    public static SmartHeaderConfig minimal(String title, String subtitle) {
        return builder()
                .title(title)
                .subtitle(subtitle)
                .variant("minimal");
    }
    
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public String getVariant() { return variant; }
    public void setVariant(String variant) { this.variant = variant; }
    
    public List<BreadcrumbItem> getBreadcrumbs() { return breadcrumbs; }
    public void setBreadcrumbs(List<BreadcrumbItem> breadcrumbs) { this.breadcrumbs = breadcrumbs; }
    
    public ActionButtonDTO getActionButton() { return actionButton; }
    public void setActionButton(ActionButtonDTO actionButton) { this.actionButton = actionButton; }
    
    public List<StatsDTO> getStats() { return stats; }
    public void setStats(List<StatsDTO> stats) { this.stats = stats; }
    
    // Classe interna para Breadcrumb
    public static class BreadcrumbItem {
        private String name;
        private String url;
        
        public BreadcrumbItem(String name, String url) {
            this.name = name;
            this.url = url;
        }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }
}
