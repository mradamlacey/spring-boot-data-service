package com.cbre.dataservices.models;

public class Property {

    private final long id;
    private final String content;

    public Property(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
