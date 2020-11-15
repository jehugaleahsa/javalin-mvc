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

    public boolean isType(TypeMirror type1, TypeMirror type2) {
        return typeUtils.isSameType(type1, type2);
    }

    public boolean isType(TypeMirror parameterType, Class<?> type) {
        if (type == null) {
            return false;
        } else if (parameterType.getKind() == TypeKind.ARRAY) {
            ArrayType arrayType = (ArrayType) parameterType;
            return type.isArray() && isType(arrayType.getComponentType(), type.getComponentType());
        } else if (type.isPrimitive()) {
            Class<?> primitiveType = getPrimitiveType(parameterType);
            return primitiveType == type;
        } else if (!type.isArray()) {
            TypeElement typeElement = elementUtils.getTypeElement(type.getCanonicalName());
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

    public static Class<?> getArrayClass(Class<?> type) {
        try {
            String name = getArrayClassName(type);
            ClassLoader classLoader = type.getClassLoader();
            if (classLoader == null) {
                return Class.forName(name);
            } else {
                return classLoader.loadClass(name);
            }
        } catch (ClassNotFoundException exception) {
            return null;
        }
    }

    private static String getArrayClassName(Class<?> type) {
        if (type.isArray()) {
            return "[" + type.getName();
        }
        if (type == boolean.class) {
            return "[Z";
        } else if (type == byte.class) {
            return "[B";
        } else if (type == char.class) {
            return "[C";
        } else if (type == double.class) {
            return "[D";
        } else if (type == float.class) {
            return "[F";
        } else if (type == int.class) {
            return "[I";
        } else if (type == long.class) {
            return "[J";
        } else if (type == short.class) {
            return "[S";
        } else {
            return "[L" + type.getName() + ";";
        }
    }

    public boolean isSubtype(TypeMirror type1, TypeMirror type2) {
        return typeUtils.isSubtype(type1, type2);
    }

    public DeclaredType getDeclaredType(TypeElement element, TypeMirror... types) {
        return typeUtils.getDeclaredType(element, types);
    }

    public TypeMirror getType(DeclaredType declaredType) {
        return elementUtils.getTypeElement(declaredType.toString()).asType();
    }

    public TypeMirror erasure(TypeMirror type) {
        return typeUtils.erasure(type);
    }

    public Element asElement(TypeMirror type) {
        return typeUtils.asElement(type);
    }
}
