package org.ong.pet.pex.backendpetx.controllers.bean;

/**
 * DTO para estatísticas do cabeçalho
 */
public class StatsDTO {
    private String value;
    private String label;
    private String icon;
    private String type;

    public StatsDTO() {}

    public StatsDTO(String value, String label, String icon, String type) {
        this.value = value;
        this.label = label;
        this.icon = icon;
        this.type = type;
    }

    public static StatsDTO primary(String value, String label, String icon) {
        return new StatsDTO(value, label, icon, "primary");
    }

    public static StatsDTO success(String value, String label, String icon) {
        return new StatsDTO(value, label, icon, "success");
    }

    public static StatsDTO warning(String value, String label, String icon) {
        return new StatsDTO(value, label, icon, "warning");
    }

    public static StatsDTO info(String value, String label, String icon) {
        return new StatsDTO(value, label, icon, "info");
    }

    // Getters e Setters
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
