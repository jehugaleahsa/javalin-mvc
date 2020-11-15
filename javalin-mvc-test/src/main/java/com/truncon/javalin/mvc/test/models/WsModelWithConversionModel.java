package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.UseConverter;

public final class WsModelWithConversionModel {
    @UseConverter("static-model-converter-ws-context")
    public ConversionModel field;
    private ConversionModel setter1;
    private ConversionModel setter2;

    public ConversionModel getModel1() {
        return setter1;
    }

    @UseConverter("static-model-converter-ws-context")
    public void setModel1(ConversionModel model) {
        this.setter1 = model;
    }

    public ConversionModel getModel2() {
        return setter2;
    }

    public void setModel2(@UseConverter("static-model-converter-ws-context") ConversionModel model) {
        this.setter2 = model;
    }
}
