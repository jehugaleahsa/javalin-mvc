package com.truncon.javalin.mvc.annotations.processing;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public final class TypeUtils {
    private final Elements elementUtils;
    private final Types typeUtils;

    public TypeUtils(Elements elementUtils, Types typeUtils) {
        this.elementUtils = elementUtils;
        this.typeUtils = typeUtils;
    }

    public TypeElement getTypeElement(String name) {
        return elementUtils.getTypeElement(name);
    }

    public TypeElement getTypeElement(Name name) {
        return elementUtils.getTypeElement(name);
    }

    public TypeElement getTypeElement(TypeMirror typeMirror) {
        return (TypeElement) typeUtils.asElement(typeMirror);
    }

    public boolean isSameType(TypeMirror type1, TypeMirror type2) {
        return typeUtils.isSameType(type1, type2);
    }

    public boolean isSameType(TypeMirror parameterType, Class<?> type) {
        if (type == null) {
            return false;
        } else if (parameterType.getKind() == TypeKind.ARRAY) {
            ArrayType arrayType = (ArrayType) parameterType;
            return type.isArray() && isSameType(arrayType.getComponentType(), type.getComponentType());
        } else if (type.isPrimitive()) {
            Class<?> primitiveType = getPrimitiveType(parameterType);
            return primitiveType == type;
        } else if (!type.isArray()) {
            TypeElement typeElement = toElement(type);
            if (typeElement == null) {
                return false;
            }
            TypeMirror checkType = typeElement.asType();
            return typeUtils.isSameType(parameterType, checkType);
        } else {
            return false;
        }
    }

    private static Class<?> getPrimitiveType(TypeMirror parameterType) {
        switch (parameterType.getKind()) {
            case BOOLEAN:
                return boolean.class;
            case BYTE:
                return byte.class;
            case CHAR:
                return char.class;
            case DOUBLE:
                return double.class;
            case FLOAT:
                return float.class;
            case INT:
                return int.class;
            case LONG:
                return long.class;
            case SHORT:
                return short.class;
            default:
                return void.class;
        }
    }

    public boolean isSubtype(TypeMirror type1, TypeMirror type2) {
        return typeUtils.isSubtype(type1, type2);
    }

    public TypeMirror erasure(TypeMirror type) {
        return typeUtils.erasure(type);
    }

    public Element asElement(TypeMirror type) {
        return typeUtils.asElement(type);
    }

    private TypeElement toElement(Class<?> clz) {
        return elementUtils.getTypeElement(clz.getCanonicalName());
    }

    public TypeMirror toType(Class<?> clz) {
        TypeElement element = toElement(clz);
        return element == null ? null : element.asType();
    }

    public TypeMirror getCollectionComponentType(TypeMirror collectionType) {
        TypeElement iterableType = toElement(Iterable.class);
        if (!typeUtils.isAssignable(typeUtils.erasure(collectionType), iterableType.asType())) {
            return null; // This is not a collection type
        }
        if (!(collectionType instanceof DeclaredType)) {
            return null; // Not a declared type
        }
        DeclaredType declaredType = (DeclaredType) collectionType;
        if (declaredType.getTypeArguments().size() != 1) {
            return null; // Maybe a Map? or custom collection type?
        }
        return declaredType.getTypeArguments().get(0);
    }

    public TypeMirror getArrayComponentType(TypeMirror type) {
        if (type.getKind() != TypeKind.ARRAY) {
            return null;
        }
        ArrayType arrayType = (ArrayType) type;
        return arrayType.getComponentType();
    }
}
