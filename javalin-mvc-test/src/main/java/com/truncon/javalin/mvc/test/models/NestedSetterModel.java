package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.FromQuery;

public class NestedSetterModel {
    @FromQuery
    public String setter;
}
