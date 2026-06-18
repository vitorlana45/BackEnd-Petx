package org.ong.pet.pex.backendpetx.controller.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para botões de ação no cabeçalho
 */
@Setter
@Getter
public class ActionButtonDTO {
    // Getters e Setters
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

    public static ActionButtonDTO createCustom(String text, String url, String icon, String cssClass) {
        return new ActionButtonDTO(text, url, icon, cssClass);
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

}
