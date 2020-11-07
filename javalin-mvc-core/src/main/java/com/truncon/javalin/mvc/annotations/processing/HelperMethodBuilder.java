package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class HelperMethodBuilder {
    public static final Map<Class<?>, Helper> HELPER_LOOKUP = getHelperLookup();

    private static Map<Class<?>, Helper> getHelperLookup() {
        Map<Class<?>, Helper> lookup = new HashMap<>();
        lookup.put(byte.class, new PrimitiveByteHelper());
        lookup.put(short.class, new PrimitiveShortHelper());
        lookup.put(int.class, new PrimitiveIntegerHelper());
        lookup.put(long.class, new PrimitiveLongHelper());
        lookup.put(float.class, new PrimitiveFloatHelper());
        lookup.put(double.class, new PrimitiveDoubleHelper());
        lookup.put(boolean.class, new PrimitiveBooleanHelper());
        lookup.put(char.class, new PrimitiveCharacterHelper());
        lookup.put(Byte.class, new BoxedByteHelper());
        lookup.put(Short.class, new BoxedShortHelper());
        lookup.put(Integer.class, new BoxedIntegerHelper());
        lookup.put(Long.class, new BoxedLongHelper());
        lookup.put(Float.class, new BoxedFloatHelper());
        lookup.put(Double.class, new BoxedDoubleHelper());
        lookup.put(Boolean.class, new BoxedBooleanHelper());
        lookup.put(Character.class, new BoxedCharacterHelper());
        lookup.put(Date.class, null);
        lookup.put(Instant.class, null);
        lookup.put(ZonedDateTime.class, null);
        lookup.put(OffsetDateTime.class, null);
        lookup.put(LocalDateTime.class, null);
        lookup.put(LocalDate.class, null);
        lookup.put(YearMonth.class, null);
        lookup.put(Year.class, null);
        lookup.put(BigInteger.class, null);
        lookup.put(BigDecimal.class, null);
        lookup.put(UUID.class, null);
        return lookup;
    }

    private final Types typeUtils;
    private final Elements elementUtils;
    private final TypeSpec.Builder registryTypeBuilder;
    private final Set<Class<?>> addedHelpers = new HashSet<>();

    public HelperMethodBuilder(
            Types typeUtils,
            Elements elementUtils,
            TypeSpec.Builder registryTypeBuilder) {
        this.typeUtils = typeUtils;
        this.elementUtils = elementUtils;
        this.registryTypeBuilder = registryTypeBuilder;
    }

    public String addConvertToMethod(Class<?> parameterClass) {
        Helper singletonHelper = HELPER_LOOKUP.get(parameterClass);
        if (singletonHelper != null) {
            if (!addedHelpers.contains(parameterClass)) {
                singletonHelper.buildSingletonHelper(registryTypeBuilder);
                addedHelpers.add(parameterClass);
            }
            return singletonHelper.getSingletonName();
        }
        if (parameterClass.isArray()) {
            Class<?> primitiveClass = parameterClass.getComponentType();
            Helper arrayHelper = HELPER_LOOKUP.get(primitiveClass);
            if (arrayHelper != null) {
                if (!addedHelpers.contains(parameterClass)) {
                    arrayHelper.buildArrayHelper(registryTypeBuilder);
                    addedHelpers.add(parameterClass);
                }
                return arrayHelper.getArrayName();
            }
        }
        return null;
    }

    private boolean isType(TypeMirror parameterType, Class<?> type) {
        if (type == null) {
            return false;
        } else if (parameterType.getKind() == TypeKind.ARRAY) {
            return type.isArray() && isType(((ArrayType) parameterType).getComponentType(), type.getComponentType());
        } else {
            return !type.isArray() && typeUtils.isSameType(parameterType, elementUtils.getTypeElement(type.getCanonicalName()).asType());
        }
    }

    private static Class<?> getArrayClass(Class<?> type) {
        try {
            return Class.forName("[L" + type.getCanonicalName() + ";");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static abstract class Helper {
        public abstract Class<?> getSingletonType();
        public abstract String getSingletonName();
        public abstract String getArrayName();

        public void buildSingletonHelper(TypeSpec.Builder typeBuilder) {
            MethodSpec method = MethodSpec.methodBuilder(getSingletonName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(getSingletonType())
                .addParameter(String.class, "value", Modifier.FINAL)
                .addCode(getSingletonMethodBody())
                .build();
            typeBuilder.addMethod(method);
        }

        protected abstract CodeBlock getSingletonMethodBody();

        public void buildArrayHelper(TypeSpec.Builder typeBuilder) {
            MethodSpec method = MethodSpec.methodBuilder(getArrayName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(getArrayClass(getSingletonType()))
                .addParameter(String[].class, "values", Modifier.FINAL)
                .addCode(getArrayMethodBody())
                .build();
            typeBuilder.addMethod(method);
        }

        protected abstract CodeBlock getArrayMethodBody();
    }

    private static final class PrimitiveByteHelper extends Helper {
        @Override
        public Class<?> getSingletonType() {
            return byte.class;
        }

        @Override
        public String getSingletonName() {
            return "toPrimitiveByte";
        }

        @Override
        public String getArrayName() {
            return "toPrimitiveByteArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody() {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return Byte.parseByte(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return (byte) 0")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody() {
            return CodeBlock.builder()
                .addStatement("byte[] results = new byte[values.length];")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .addStatement("result[i] = Byte.parseByte(value)")
                .nextControlFlow("catch ($T)", NumberFormatException.class)
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class PrimitiveShortHelper extends Helper {
        @Override
        public Class<?> getSingletonType() {
            return short.class;
        }

        @Override
        public String getSingletonName() {
            return "toPrimitiveShort";
        }

        @Override
        public String getArrayName() {
            return "toPrimitiveShortArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody() {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return Short.parseShort(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return (short) 0")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody() {
            return CodeBlock.builder()
                .addStatement("short[] results = new short[values.length];")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .addStatement("result[i] = Short.parseShort(value)")
                .nextControlFlow("catch ($T)", NumberFormatException.class)
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class PrimitiveIntegerHelper extends Helper {
        @Override
        public Class<?> getSingletonType() {
            return int.class;
        }

        @Override
        public String getSingletonName() {
            return "toPrimitiveInteger";
        }

        @Override
        public String getArrayName() {
            return "toPrimitiveIntegerArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody() {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return Integer.parseInt(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return 0")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody() {
            return CodeBlock.builder()
                .addStatement("int[] results = new int[values.length];")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .addStatement("result[i] = Integer.parseInt(value)")
                .nextControlFlow("catch ($T)", NumberFormatException.class)
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class PrimitiveLongHelper extends Helper {
        @Override
        public Class<?> getSingletonType() {
            return long.class;
        }

        @Override
        public String getSingletonName() {
            return "toPrimitiveLong";
        }

        @Override
        public String getArrayName() {
            return "toPrimitiveLongArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody() {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return Long.parseLong(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return 0L")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody() {
            return CodeBlock.builder()
                .addStatement("long[] results = new long[values.length];")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .addStatement("result[i] = Long.parseLong(value)")
                .nextControlFlow("catch ($T)", NumberFormatException.class)
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class PrimitiveFloatHelper extends Helper {
        @Override
        public Class<?> getSingletonType() {
            return float.class;
        }

        @Override
        public String getSingletonName() {
            return "toPrimitiveFloat";
        }

        @Override
        public String getArrayName() {
            return "toPrimitiveFloatArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody() {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return Float.parseFloat(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return 0.0f")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody() {
            return CodeBlock.builder()
                .addStatement("float[] results = new float[values.length];")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .addStatement("result[i] = Float.parseFloat(value)")
                .nextControlFlow("catch ($T)", NumberFormatException.class)
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class PrimitiveDoubleHelper extends Helper {
        @Override
        public Class<?> getSingletonType() {
            return double.class;
        }

        @Override
        public String getSingletonName() {
            return "toPrimitiveDouble";
        }

        @Override
        public String getArrayName() {
            return "toPrimitiveDoubleArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody() {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return Double.parseDouble(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return 0.0")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody() {
            return CodeBlock.builder()
                .addStatement("double[] results = new double[values.length];")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .addStatement("result[i] = Double.parseDouble(value)")
                .nextControlFlow("catch ($T)", NumberFormatException.class)
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class PrimitiveBooleanHelper extends Helper {
        @Override
        public Class<?> getSingletonType() {
            return boolean.class;
        }

        @Override
        public String getSingletonName() {
            return "toPrimitiveBoolean";
        }

        @Override
        public String getArrayName() {
            return "toPrimitiveBooleanArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody() {
            return CodeBlock.builder()
                .addStatement("return Boolean.parseBoolean(value)")
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody() {
            return CodeBlock.builder()
                .addStatement("boolean[] results = new boolean[values.length];")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values[i]")
                .addStatement("result[i] = Boolean.parseBoolean(value)")
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class PrimitiveCharacterHelper extends Helper {
        @Override
        public Class<?> getSingletonType() {
            return char.class;
        }

        @Override
        public String getSingletonName() {
            return "toPrimitiveCharacter";
        }

        @Override
        public String getArrayName() {
            return "toPrimitiveCharacterArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody() {
            return CodeBlock.builder()
                .addStatement("return value != null && value.length() == 1 ? value.charAt(0) : '\\0'")
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody() {
            return CodeBlock.builder()
                .addStatement("char[] results = new char[values.length];")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null && value.length() == 1)")
                .addStatement("result[i] = value.charAt(0)")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BoxedByteHelper extends Helper {
        @Override
        public Class<?> getSingletonType() {
            return Byte.class;
        }

        @Override
        public String getSingletonName() {
            return "toBoxedByte";
        }

        @Override
        public String getArrayName() {
            return "toBoxedByteArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody() {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Byte.parseByte(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody() {
            return CodeBlock.builder()
                .addStatement("Byte[] results = new Byte[values.length];")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("result[i] = Byte.parseByte(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T)", NumberFormatException.class)
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BoxedShortHelper extends Helper {
        @Override
        public Class<?> getSingletonType() {
            return Short.class;
        }

        @Override
        public String getSingletonName() {
            return "toBoxedShort";
        }

        @Override
        public String getArrayName() {
            return "toBoxedShortArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody() {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Short.parseShort(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody() {
            return CodeBlock.builder()
                .addStatement("Short[] results = new Short[values.length];")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("result[i] = Short.parseShort(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T)", NumberFormatException.class)
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BoxedIntegerHelper extends Helper {
        @Override
        public Class<?> getSingletonType() {
            return Integer.class;
        }

        @Override
        public String getSingletonName() {
            return "toBoxedInteger";
        }

        @Override
        public String getArrayName() {
            return "toBoxedIntegerArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody() {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Integer.parseInt(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody() {
            return CodeBlock.builder()
                .addStatement("Integer[] results = new Integer[values.length];")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("result[i] = Integer.parseInt(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T)", NumberFormatException.class)
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BoxedLongHelper extends Helper {
        @Override
        public Class<?> getSingletonType() {
            return Long.class;
        }

        @Override
        public String getSingletonName() {
            return "toBoxedLong";
        }

        @Override
        public String getArrayName() {
            return "toBoxedLongArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody() {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Long.parseLong(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody() {
            return CodeBlock.builder()
                .addStatement("Long[] results = new Long[values.length];")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("result[i] = Long.parseLong(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T)", NumberFormatException.class)
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BoxedFloatHelper extends Helper {
        @Override
        public Class<?> getSingletonType() {
            return Float.class;
        }

        @Override
        public String getSingletonName() {
            return "toBoxedFloat";
        }

        @Override
        public String getArrayName() {
            return "toBoxedFloatArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody() {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Float.parseFloat(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody() {
            return CodeBlock.builder()
                .addStatement("Float[] results = new Float[values.length];")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("result[i] = Float.parseFloat(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T)", NumberFormatException.class)
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BoxedDoubleHelper extends Helper {
        @Override
        public Class<?> getSingletonType() {
            return Double.class;
        }

        @Override
        public String getSingletonName() {
            return "toBoxedDouble";
        }

        @Override
        public String getArrayName() {
            return "toBoxedDoubleArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody() {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Double.parseDouble(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody() {
            return CodeBlock.builder()
                .addStatement("Double[] results = new Double[values.length];")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("result[i] = Double.parseDouble(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T)", NumberFormatException.class)
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BoxedBooleanHelper extends Helper {
        @Override
        public Class<?> getSingletonType() {
            return Boolean.class;
        }

        @Override
        public String getSingletonName() {
            return "toBoxedBoolean";
        }

        @Override
        public String getArrayName() {
            return "toBoxedBooleanArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody() {
            return CodeBlock.builder()
                .addStatement("return value == null ? null : Boolean.parseBoolean(value)")
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody() {
            return CodeBlock.builder()
                .addStatement("Boolean[] results = new Boolean[values.length];")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("result[i] = Boolean.parseBoolean(value)")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BoxedCharacterHelper extends Helper {
        @Override
        public Class<?> getSingletonType() {
            return Character.class;
        }

        @Override
        public String getSingletonName() {
            return "toBoxedCharacter";
        }

        @Override
        public String getArrayName() {
            return "toBoxedCharacterArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody() {
            return CodeBlock.builder()
                .addStatement("return value != null && value.length() == 1 ? value.charAt(0) : null")
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody() {
            return CodeBlock.builder()
                .addStatement("Character[] results = new Character[values.length];")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null && value.length() == 1)")
                .addStatement("result[i] = value.charAt(0)")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }
}
