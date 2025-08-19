package org.petx.dto;

/**
 * DTO para botões de ação no cabeçalho
 */
public class ActionButtonDTO {
    private String text;
    private String url;
    private String icon;
    private String cssClass;

    public ActionButtonDTO() {}

    public ActionButtonDTO(String text, String url, String icon, String cssClass) {
        this.text = text;
        this.url = url;
        this.icon = icon;
        this.cssClass = cssClass;
    }

    public static ActionButtonDTO primary(String text, String url, String icon) {
        return new ActionButtonDTO(text, url, icon, "btn-primary");
    }

    public static ActionButtonDTO success(String text, String url, String icon) {
        return new ActionButtonDTO(text, url, icon, "btn-success");
    }

    public static ActionButtonDTO warning(String text, String url, String icon) {
        return new ActionButtonDTO(text, url, icon, "btn-warning");
    }

    // Getters e Setters
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getCssClass() { return cssClass; }
    public void setCssClass(String cssClass) { this.cssClass = cssClass; }
}
