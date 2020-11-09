package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.FromQuery;

public class NestedFieldModel {
    @FromQuery
    public String field;
}
