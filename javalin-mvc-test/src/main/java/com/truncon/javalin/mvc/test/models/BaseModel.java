package com.truncon.javalin.mvc.test.models;

public class BaseModel {
    private String baseName;
    public int baseId;

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String name) {
        this.baseName = name;
    }
}
