package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.AnnotationSpec;

import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public final class AnnotationUtils {
    private AnnotationUtils() {
    }

    /**
     * Clones an annotation by recursively navigating through the members, returning
     * and {@link AnnotationSpec}.
     * @param annotation The annotation to clone.
     * @return An {@link AnnotationSpec} that clones the given {@link Annotation}.
     * @implNote This code is copied from Javapoet's {@link AnnotationSpec#get(Annotation)} method.
     * This was necessary because the annotation was implemented in Kotlin. When the Javapoet
     * implementation encountered a nested annotation, it would just try to call toString() on it.
     * However, when Kotlin's KClass printed itself out, it would not include the .class.
     */
    public static AnnotationSpec cloneAnnotation(Annotation annotation) {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(annotation.annotationType());
        try {
            Method[] methods = annotation.annotationType().getDeclaredMethods();
            for (Method method : methods) {
                Object value = getValue(annotation, method);
                if (!Objects.deepEquals(value, method.getDefaultValue())) {
                    if (value.getClass().isArray()) {
                        for(int i = 0; i < Array.getLength(value); ++i) {
                            addMemberForValue(builder, method.getName(), Array.get(value, i));
                        }
                    } else if (value instanceof Annotation) {
                        builder.addMember(method.getName(), "$L", cloneAnnotation((Annotation) value));
                    } else {
                        addMemberForValue(builder, method.getName(), value);
                    }
                }
            }
        } catch (Exception exception) {
            throw new ProcessingException("Reflecting " + annotation + " failed!", exception);
        }
        return builder.build();
    }

    /**
     * @apiNote Calling {@link Method#invoke(Object, Object...)} on the top-level annotation seems
     * to work fine. When navigating through nested annotations, though, an {@link InvocationTargetException}
     * is thrown. This is likely the result of Kotlin using proxy classes.
     */
    private static Object getValue(Annotation annotation, Method method) throws IllegalAccessException, InvocationTargetException {
        try {
            return method.invoke(annotation);
        } catch (InvocationTargetException ex) {
            if (ex.getTargetException() instanceof MirroredTypeException) {
                MirroredTypeException mirroredException = (MirroredTypeException) ex.getTargetException();
                return mirroredException.getTypeMirror();
            } else {
                throw ex;
            }
        }
    }

    private static void addMemberForValue(AnnotationSpec.Builder builder, String memberName, Object value) {
        if (value instanceof Class || value instanceof TypeMirror) {
            builder.addMember(memberName, "$T.class", value);
        } else if (value instanceof Enum) {
            builder.addMember(memberName, "$T.$L", value.getClass(), ((Enum<?>) value).name());
        } else if (value instanceof String) {
            builder.addMember(memberName, "$S", value);
        } else if (value instanceof Float) {
            builder.addMember(memberName, "$Lf", value);
        } else if (value instanceof Character) {
            builder.addMember(memberName, "'$L'", characterLiteralWithoutSingleQuotes((Character) value));
        } else if (value instanceof Annotation) {
            builder.addMember(memberName, "$L", cloneAnnotation((Annotation) value));
        } else {
            builder.addMember(memberName, "$L", value);
        }
    }

    private static String characterLiteralWithoutSingleQuotes(char value) {
        switch (value) {
            case '\b':
                return "\\b";
            case '\t':
                return "\\t";
            case '\n':
                return "\\n";
            case '\f':
                return "\\f";
            case '\r':
                return "\\r";
            case '"':
                return "\"";
            case '\'':
                return "\\'";
            case '\\':
                return "\\\\";
            default:
                if (Character.isISOControl(value)) {
                    return String.format("\\u%04x", (int) value);
                } else {
                    return Character.toString(value);
                }
        }
    }
}
