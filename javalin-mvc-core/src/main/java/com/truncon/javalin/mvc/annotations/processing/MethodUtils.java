package com.truncon.javalin.mvc.annotations.processing;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ws.WsActionResult;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.concurrent.CompletableFuture;

final class MethodUtils {
    private final TypeUtils typeUtils;

    public MethodUtils(TypeUtils typeUtils) {
        this.typeUtils = typeUtils;
    }

    public boolean hasVoidReturnType(ExecutableElement method) {
        TypeMirror returnType = method.getReturnType();
        return returnType.getKind() == TypeKind.VOID;
    }

    public boolean hasActionResultReturnType(ExecutableElement method) {
        return hasActionResultReturnType(method, ActionResult.class);
    }

    public boolean hasWsActionResultReturnType(ExecutableElement method) {
        return hasActionResultReturnType(method, WsActionResult.class);
    }

    private boolean hasActionResultReturnType(ExecutableElement method, Class<?> actionResultType) {
        TypeMirror returnType = method.getReturnType();
        TypeMirror resultType = typeUtils.getTypeElement(actionResultType.getCanonicalName()).asType();
        return typeUtils.isSubtype(returnType, resultType);
    }

    public boolean hasFutureActionResultReturnType(ExecutableElement method) {
        return hasFutureActionResultReturnType(method, ActionResult.class);
    }

    public boolean hasFutureWsActionResultReturnType(ExecutableElement method) {
        return hasFutureActionResultReturnType(method, WsActionResult.class);
    }

    private boolean hasFutureActionResultReturnType(ExecutableElement method, Class<?> actionResultType) {
        TypeMirror returnType = method.getReturnType();
        TypeMirror resultType = typeUtils.getTypeElement(actionResultType.getCanonicalName()).asType();
        TypeElement futureType = typeUtils.getTypeElement(CompletableFuture.class.getCanonicalName());
        DeclaredType futureResultType = typeUtils.getDeclaredType(futureType, resultType);
        return typeUtils.isSubtype(returnType, futureResultType);
    }

    public boolean hasFutureSimpleReturnType(ExecutableElement method) {
        TypeMirror returnType = typeUtils.erasure(method.getReturnType());
        TypeMirror futureType = typeUtils.erasure(
            typeUtils.getTypeElement(CompletableFuture.class.getCanonicalName()).asType());
        return typeUtils.isSubtype(returnType, futureType);
    }
}
