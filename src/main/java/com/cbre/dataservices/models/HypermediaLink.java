package com.cbre.dataservices.models;

public class HypermediaLink {

    private String rel;
    private String href;
    private String title;
    private String method;

    public HypermediaLink(String relation, String href, String title, String method){
        this.rel = relation;
        this.href = href;
        this.title = title;
        this.method = method;
    }

    public String getRel() { return rel; }
    public String getHref() { return href; }
    public String getTitle() { return title; }
    public String getMethod() { return method; }
}
