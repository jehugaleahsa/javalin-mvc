package io.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import io.javalin.mvc.DefaultModelBinder;
import io.javalin.mvc.api.*;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

final class ParameterGenerator {
    private final Types typeUtils;
    private final Elements elementUtils;
    private final VariableElement parameter;

    private ParameterGenerator(Types typeUtils, Elements elementUtils, VariableElement parameter) {
        this.typeUtils = typeUtils;
        this.elementUtils = elementUtils;
        this.parameter = parameter;
    }

    public static ParameterGenerator getParameterGenerator(RouteGenerator route, VariableElement parameter) {
        return new ParameterGenerator(route.getTypeUtils(), route.getElementUtils(), parameter);
    }

    public VariableElement getParameter() {
        return parameter;
    }

    public String generateParameter(String contextName) {
        TypeMirror parameterType = parameter.asType();
        if (isType(parameterType, HttpContext.class)) {
            return contextName;
        }
        if (isType(parameterType, HttpRequest.class)) {
            return contextName + ".getRequest()";
        }
        if (isType(parameterType, HttpResponse.class)) {
            return contextName + ".getResponse()";
        }
        String parameterName = getParameterName();
        if (isType(parameterType, FileUpload.class)) {
            return CodeBlock.of(contextName + ".getRequest().getFile($S)", parameterName).toString();
        }
        ValueSource valueSource = getValueSource(parameter);
        for (Class<?> parameterClass : DefaultModelBinder.SUPPORTED_TYPES) {
            if (isType(parameterType, parameterClass)) {
                return bindParameter(parameterName, parameterClass, valueSource);
            } else {
                Class<?> arrayClass = getArrayClass(parameterClass);
                if (isType(parameterType, arrayClass)) {
                    return bindParameter(parameterName, arrayClass, valueSource);
                }
            }
        }
        return CodeBlock.of(
                "($T)binder.getValue($S, $T.class, $T.$L)",
                parameterType,
                parameterName,
                parameterType,
                ValueSource.class,
                valueSource).toString();
    }

    private boolean isType(TypeMirror parameterType, Class<?> type) {
        if (type == null) {
            return false;
        } else if (parameterType.getKind() == TypeKind.ARRAY) {
            return type.isArray() && isType(((ArrayType)parameterType).getComponentType(), type.getComponentType());
        } else {
            return !type.isArray() && typeUtils.isSameType(parameterType, elementUtils.getTypeElement(type.getCanonicalName()).asType());
        }
    }

    private Class<?> getArrayClass(Class<?> type) {
        try {
            return Class.forName("[L" + type.getCanonicalName() + ";");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public String getParameterName() {
        Named annotation = parameter.getAnnotation(Named.class);
        if (annotation == null) {
            return parameter.getSimpleName().toString();
        }
        String annotatedName = annotation.value();
        return annotatedName.trim();
    }

    private ValueSource getValueSource(VariableElement parameter) {
        if (parameter.getAnnotation(FromHeader.class) != null) {
            return ValueSource.Header;
        }
        if (parameter.getAnnotation(FromCookie.class) != null) {
            return ValueSource.Cookie;
        }
        if (parameter.getAnnotation(FromPath.class) != null) {
            return ValueSource.Path;
        }
        if (parameter.getAnnotation(FromQuery.class) != null) {
            return ValueSource.QueryString;
        }
        if (parameter.getAnnotation(FromForm.class) != null) {
            return ValueSource.FormData;
        }
        return ValueSource.Any;
    }

    private static String bindParameter(String parameterName, Class<?> parameterClass, ValueSource valueSource) {
        return CodeBlock.of(
                "($T)binder.getValue($S, $T.class, $T.$L)",
                parameterClass,
                parameterName,
                parameterClass,
                ValueSource.class,
                valueSource).toString();
    }
}
