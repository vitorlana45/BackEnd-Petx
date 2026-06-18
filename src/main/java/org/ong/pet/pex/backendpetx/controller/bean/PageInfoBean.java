package org.ong.pet.pex.backendpetx.controller.bean;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
public class PageInfoBean {
    // Getters e Setters
    private String title;
    private String subtitle;
    private String icon;
    private List<BreadcrumbItem> breadcrumbs = new ArrayList<>();

    public PageInfoBean(String title, String subtitle, String icon) {
        this.title = title;
        this.subtitle = subtitle;
        this.icon = icon;
    }

    public static PageInfoBean builder() {
        return new PageInfoBean();
    }

    public PageInfoBean title(String title) {
        this.title = title;
        return this;
    }

    public PageInfoBean subtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public PageInfoBean icon(String icon) {
        this.icon = icon;
        return this;
    }

    public PageInfoBean addBreadcrumb(String name, String url) {
        this.breadcrumbs.add(new BreadcrumbItem(name, url));
        return this;
    }

    public PageInfoBean addBreadcrumb(String name) {
        this.breadcrumbs.add(new BreadcrumbItem(name, null));
        return this;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class BreadcrumbItem {
        private String name;
        private String url;

        public BreadcrumbItem(String name, String url) {
            this.name = name;
            this.url = url;
        }

    }
}
