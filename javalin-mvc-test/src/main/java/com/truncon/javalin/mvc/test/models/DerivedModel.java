package com.truncon.javalin.mvc.test.models;

import java.util.UUID;

public final class DerivedModel extends BaseModel {
    private UUID derivedUuid;
    public double derivedAmount;

    public UUID getDerivedUuid() {
        return derivedUuid;
    }

    public void setDerivedUuid(UUID uuid) {
        this.derivedUuid = uuid;
    }
}
