package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.truncon.javalin.mvc.api.HttpContext;
import com.truncon.javalin.mvc.api.HttpRequest;
import com.truncon.javalin.mvc.api.ValueSource;

import javax.lang.model.element.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class HelperMethodBuilder {
    public static final Map<Class<?>, ConversionHelper> CONVERSION_HELPER_LOOKUP = getConversionHelperLookup();
    private static final Map<ValueSource, SourceHelper> SOURCE_HELPER_LOOKUP = getSourceHelperLookup();

    private static Map<Class<?>, ConversionHelper> getConversionHelperLookup() {
        Map<Class<?>, ConversionHelper> lookup = new HashMap<>();
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
        lookup.put(Date.class, new DateHelper());
        lookup.put(Instant.class, new InstantHelper());
        lookup.put(ZonedDateTime.class, new ZonedDateTimeHelper());
        lookup.put(OffsetDateTime.class, new OffsetDateTimeHelper());
        lookup.put(LocalDateTime.class, new LocalDateTimeHelper());
        lookup.put(LocalDate.class, new LocalDateHelper());
        lookup.put(YearMonth.class, new YearMonthHelper());
        lookup.put(Year.class, new YearHelper());
        lookup.put(BigInteger.class, new BigIntegerHelper());
        lookup.put(BigDecimal.class, new BigDecimalHelper());
        lookup.put(UUID.class, new UUIDHelper());
        return lookup;
    }

    private static Map<ValueSource, SourceHelper> getSourceHelperLookup() {
        // Ordered for security reasons
        Map<ValueSource, SourceHelper> lookup = new LinkedHashMap<>();
        lookup.put(ValueSource.Header, new HeaderHelper());
        lookup.put(ValueSource.Cookie, new CookieHelper());
        lookup.put(ValueSource.Path, new PathHelper());
        lookup.put(ValueSource.QueryString, new QueryStringHelper());
        lookup.put(ValueSource.FormData, new FormDataHelper());
        lookup.put(ValueSource.Any, new AnyHelper());
        return lookup;
    }

    private final TypeSpec.Builder typeBuilder;
    private final Set<Class<?>> addedSingletonConversionHelpers = new HashSet<>();
    private final Set<Class<?>> addedArrayConversionHelpers = new HashSet<>();
    private final Set<ValueSource> addedSingletonSourceHelpers = new HashSet<>();
    private final Set<ValueSource> addedArraySourceHelpers = new HashSet<>();

    public HelperMethodBuilder(TypeSpec.Builder typeBuilder) {
        this.typeBuilder = typeBuilder;
    }

    public String addConversionMethod(Class<?> parameterClass, boolean isArray) {
        if (isArray) {
            ConversionHelper arrayHelper = CONVERSION_HELPER_LOOKUP.get(parameterClass);
            if (arrayHelper != null) {
                if (!addedArrayConversionHelpers.contains(parameterClass)) {
                    arrayHelper.buildArrayHelper(typeBuilder);
                    addedArrayConversionHelpers.add(parameterClass);
                }
                return arrayHelper.getArrayName();
            }
        } else {
            ConversionHelper singletonHelper = CONVERSION_HELPER_LOOKUP.get(parameterClass);
            if (singletonHelper != null) {
                if (!addedSingletonConversionHelpers.contains(parameterClass)) {
                    singletonHelper.buildSingletonHelper(typeBuilder);
                    addedSingletonConversionHelpers.add(parameterClass);
                }
                return singletonHelper.getSingletonName();
            }
        }
        return null;
    }

    public String addSourceMethod(ValueSource valueSource, String key, boolean isArray) {
        SourceHelper sourceHelper = SOURCE_HELPER_LOOKUP.get(valueSource);
        if (isArray) {
            if (!addedArraySourceHelpers.contains(ValueSource.Any) && !addedArraySourceHelpers.contains(valueSource)) {
                sourceHelper.buildArrayHelper(typeBuilder, key);
                addedArraySourceHelpers.add(valueSource);
            }
            return sourceHelper.getArrayName();
        } else {
            if (!addedSingletonSourceHelpers.contains(ValueSource.Any) && !addedSingletonSourceHelpers.contains(valueSource)) {
                sourceHelper.buildSingletonHelper(typeBuilder, key);
                addedSingletonSourceHelpers.add(valueSource);
            }
            return sourceHelper.getSingletonName();
        }
    }

    private static abstract class ConversionHelper {
        public abstract Class<?> getSingletonType();
        public abstract Class<?> getArrayType();
        public abstract String getSingletonName();
        public abstract String getArrayName();

        public void buildSingletonHelper(TypeSpec.Builder typeBuilder) {
            MethodSpec method = MethodSpec.methodBuilder(getSingletonName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(getSingletonType())
                .addParameter(String.class, "value", Modifier.FINAL)
                .addCode(getSingletonMethodBody(typeBuilder))
                .build();
            typeBuilder.addMethod(method);
        }

        protected abstract CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder);

        public void buildArrayHelper(TypeSpec.Builder typeBuilder) {
            MethodSpec method = MethodSpec.methodBuilder(getArrayName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(getArrayType())
                .addParameter(String[].class, "values", Modifier.FINAL)
                .addCode(getArrayMethodBody(typeBuilder))
                .build();
            typeBuilder.addMethod(method);
        }

        protected abstract CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder);

        protected void addField(
                TypeSpec.Builder typeBuilder,
                String name,
                Class<?> type,
                String initializer,
                Object... args) {
            FieldSpec field = FieldSpec.builder(type, name, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer(initializer, args)
                .build();
            typeBuilder.addField(field);
        }
    }

    private static final class PrimitiveByteHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return byte.class;
        }

        @Override
        public Class<?> getArrayType() {
            return byte[].class;
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
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? (byte) 0 : Byte.parseByte(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return (byte) 0")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("byte[] results = new byte[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("results[i] = Byte.parseByte(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class PrimitiveShortHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return short.class;
        }

        @Override
        public Class<?> getArrayType() {
            return short[].class;
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
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? (short) 0 : Short.parseShort(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return (short) 0")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("short[] results = new short[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("results[i] = Short.parseShort(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class PrimitiveIntegerHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return int.class;
        }

        @Override
        public Class<?> getArrayType() {
            return int[].class;
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
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? 0 : Integer.parseInt(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return 0")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("int[] results = new int[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("results[i] = Integer.parseInt(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class PrimitiveLongHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return long.class;
        }

        @Override
        public Class<?> getArrayType() {
            return long[].class;
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
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? 0L : Long.parseLong(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return 0L")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("long[] results = new long[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("results[i] = Long.parseLong(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class PrimitiveFloatHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return float.class;
        }

        @Override
        public Class<?> getArrayType() {
            return float[].class;
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
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? 0.0f : Float.parseFloat(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return 0.0f")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("float[] results = new float[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("results[i] = Float.parseFloat(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class PrimitiveDoubleHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return double.class;
        }

        @Override
        public Class<?> getArrayType() {
            return double[].class;
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
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? 0.0 : Double.parseDouble(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return 0.0")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("double[] results = new double[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("results[i] = Double.parseDouble(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class PrimitiveBooleanHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return boolean.class;
        }

        @Override
        public Class<?> getArrayType() {
            return boolean[].class;
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
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("return Boolean.parseBoolean(value)")
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("boolean[] results = new boolean[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values[i]")
                .addStatement("results[i] = Boolean.parseBoolean(value)")
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class PrimitiveCharacterHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return char.class;
        }

        @Override
        public Class<?> getArrayType() {
            return char[].class;
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
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("return value != null && value.length() == 1 ? value.charAt(0) : '\\0'")
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("char[] results = new char[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null && value.length() == 1)")
                .addStatement("results[i] = value.charAt(0)")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BoxedByteHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return Byte.class;
        }

        @Override
        public Class<?> getArrayType() {
            return Byte[].class;
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
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Byte.parseByte(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("Byte[] results = new Byte[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("results[i] = Byte.parseByte(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BoxedShortHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return Short.class;
        }

        @Override
        public Class<?> getArrayType() {
            return Short[].class;
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
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Short.parseShort(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("Short[] results = new Short[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("results[i] = Short.parseShort(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BoxedIntegerHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return Integer.class;
        }

        @Override
        public Class<?> getArrayType() {
            return Integer[].class;
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
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Integer.parseInt(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("Integer[] results = new Integer[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("results[i] = Integer.parseInt(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BoxedLongHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return Long.class;
        }

        @Override
        public Class<?> getArrayType() {
            return Long[].class;
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
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Long.parseLong(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("Long[] results = new Long[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("results[i] = Long.parseLong(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BoxedFloatHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return Float.class;
        }

        @Override
        public Class<?> getArrayType() {
            return Float[].class;
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
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Float.parseFloat(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("Float[] results = new Float[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("results[i] = Float.parseFloat(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BoxedDoubleHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return Double.class;
        }

        @Override
        public Class<?> getArrayType() {
            return Double[].class;
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
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Double.parseDouble(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("Double[] results = new Double[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("results[i] = Double.parseDouble(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BoxedBooleanHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return Boolean.class;
        }

        @Override
        public Class<?> getArrayType() {
            return Boolean[].class;
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
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("return value == null ? null : Boolean.parseBoolean(value)")
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("Boolean[] results = new Boolean[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("results[i] = Boolean.parseBoolean(value)")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BoxedCharacterHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return Character.class;
        }

        @Override
        public Class<?> getArrayType() {
            return Character[].class;
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
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("return value != null && value.length() == 1 ? value.charAt(0) : null")
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("Character[] results = new Character[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null && value.length() == 1)")
                .addStatement("results[i] = value.charAt(0)")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class DateHelper extends ConversionHelper {
        private boolean hasField;

        @Override
        public Class<?> getSingletonType() {
            return Date.class;
        }

        @Override
        public Class<?> getArrayType() {
            return Date[].class;
        }

        @Override
        public String getSingletonName() {
            return "toDate";
        }

        @Override
        public String getArrayName() {
            return "toDateArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            addFormatterField(typeBuilder);
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : DATE_FORMATTER.parse(value)")
                .nextControlFlow("catch ($T exception)", ParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            addFormatterField(typeBuilder);
            return CodeBlock.builder()
                .addStatement("Date[] results = new Date[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                    .addStatement("String value = values[i]")
                    .beginControlFlow("if (value != null)")
                        .beginControlFlow("try")
                            .addStatement("results[i] = DATE_FORMATTER.parse(value)")
                        .nextControlFlow("catch ($T exception)", ParseException.class)
                        .endControlFlow()
                    .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }

        private void addFormatterField(TypeSpec.Builder typeBuilder) {
            if (!hasField) {
                addField(
                    typeBuilder,
                    "DATE_FORMATTER",
                    SimpleDateFormat.class,
                    "new $T($S)",
                    SimpleDateFormat.class,
                    "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
                );
                hasField = true;
            }
        }
    }

    private static final class InstantHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return Instant.class;
        }

        @Override
        public Class<?> getArrayType() {
            return Instant[].class;
        }

        @Override
        public String getSingletonName() {
            return "toInstant";
        }

        @Override
        public String getArrayName() {
            return "toInstantArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Instant.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("Instant[] results = new Instant[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                    .addStatement("String value = values[i]")
                    .beginControlFlow("if (value != null)")
                        .beginControlFlow("try")
                            .addStatement("results[i] = Instant.parse(value)")
                        .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                        .endControlFlow()
                    .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class ZonedDateTimeHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return ZonedDateTime.class;
        }

        @Override
        public Class<?> getArrayType() {
            return ZonedDateTime[].class;
        }

        @Override
        public String getSingletonName() {
            return "toZonedDateTime";
        }

        @Override
        public String getArrayName() {
            return "toZonedDateTimeArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : ZonedDateTime.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("ZonedDateTime[] results = new ZonedDateTime[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .beginControlFlow("try")
                .addStatement("results[i] = ZonedDateTime.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .endControlFlow()
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class OffsetDateTimeHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return OffsetDateTime.class;
        }

        @Override
        public Class<?> getArrayType() {
            return OffsetDateTime[].class;
        }

        @Override
        public String getSingletonName() {
            return "toOffsetDateTime";
        }

        @Override
        public String getArrayName() {
            return "toOffsetDateTimeArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : OffsetDateTime.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("OffsetDateTime[] results = new OffsetDateTime[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .beginControlFlow("try")
                .addStatement("results[i] = OffsetDateTime.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .endControlFlow()
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class LocalDateTimeHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return LocalDateTime.class;
        }

        @Override
        public Class<?> getArrayType() {
            return LocalDateTime[].class;
        }

        @Override
        public String getSingletonName() {
            return "toLocalDateTime";
        }

        @Override
        public String getArrayName() {
            return "toLocalDateTimeArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : LocalDateTime.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("LocalDateTime[] results = new LocalDateTime[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .beginControlFlow("try")
                .addStatement("results[i] = LocalDateTime.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .endControlFlow()
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class LocalDateHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return LocalDate.class;
        }

        @Override
        public Class<?> getArrayType() {
            return LocalDate[].class;
        }

        @Override
        public String getSingletonName() {
            return "toLocalDate";
        }

        @Override
        public String getArrayName() {
            return "toLocalDateArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : LocalDate.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("LocalDate[] results = new LocalDate[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .beginControlFlow("try")
                .addStatement("results[i] = LocalDate.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .endControlFlow()
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class YearMonthHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return YearMonth.class;
        }

        @Override
        public Class<?> getArrayType() {
            return YearMonth[].class;
        }

        @Override
        public String getSingletonName() {
            return "toYearMonth";
        }

        @Override
        public String getArrayName() {
            return "toYearMonthArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : YearMonth.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("YearMonth[] results = new YearMonth[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .beginControlFlow("try")
                .addStatement("results[i] = YearMonth.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .endControlFlow()
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class YearHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return Year.class;
        }

        @Override
        public Class<?> getArrayType() {
            return Year[].class;
        }

        @Override
        public String getSingletonName() {
            return "toYear";
        }

        @Override
        public String getArrayName() {
            return "toYearArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Year.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("Year[] results = new Year[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .beginControlFlow("try")
                .addStatement("results[i] = Year.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .endControlFlow()
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BigIntegerHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return BigInteger.class;
        }

        @Override
        public Class<?> getArrayType() {
            return BigInteger[].class;
        }

        @Override
        public String getSingletonName() {
            return "toBigInteger";
        }

        @Override
        public String getArrayName() {
            return "toBigIntegerArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : new BigInteger(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("BigInteger[] results = new BigInteger[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("results[i] = new BigInteger(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BigDecimalHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return BigDecimal.class;
        }

        @Override
        public Class<?> getArrayType() {
            return BigDecimal[].class;
        }

        @Override
        public String getSingletonName() {
            return "toBigDecimal";
        }

        @Override
        public String getArrayName() {
            return "toBigDecimalArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : new BigDecimal(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("BigDecimal[] results = new BigDecimal[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("results[i] = new BigDecimal(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class UUIDHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return UUID.class;
        }

        @Override
        public Class<?> getArrayType() {
            return UUID[].class;
        }

        @Override
        public String getSingletonName() {
            return "toUUID";
        }

        @Override
        public String getArrayName() {
            return "toUUIDArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : UUID.fromString(value)")
                .nextControlFlow("catch ($T exception)", IllegalArgumentException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder) {
            return CodeBlock.builder()
                .addStatement("UUID[] results = new UUID[values.length]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values[i]")
                .beginControlFlow("if (value != null)")
                .addStatement("results[i] = UUID.fromString(value)")
                .endControlFlow()
                .nextControlFlow("catch ($T exception)", IllegalArgumentException.class)
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static abstract class SourceHelper {
        public abstract String getSingletonName();
        public abstract String getArrayName();

        public void buildSingletonHelper(TypeSpec.Builder typeBuilder, String key) {
            MethodSpec method = MethodSpec.methodBuilder(getSingletonName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(String.class)
                .addParameter(HttpContext.class, "context", Modifier.FINAL)
                .addParameter(String.class, "key", Modifier.FINAL)
                .addCode(getSingletonMethodBody(typeBuilder, "context", "key"))
                .build();
            typeBuilder.addMethod(method);
        }

        protected abstract CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder, String context, String key);

        public void buildArrayHelper(TypeSpec.Builder typeBuilder, String key) {
            MethodSpec method = MethodSpec.methodBuilder(getArrayName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(String[].class)
                .addParameter(HttpContext.class, "context", Modifier.FINAL)
                .addParameter(String.class, "key", Modifier.FINAL)
                .addCode(getArrayMethodBody(typeBuilder,"context", "key"))
                .build();
            typeBuilder.addMethod(method);
        }

        protected abstract CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder, String context, String key);

        public abstract CodeBlock getPresenceCheck(String request, String key);
    }

    private static final class PathHelper extends SourceHelper {
        @Override
        public String getSingletonName() {
            return "getPathValue";
        }

        @Override
        public String getArrayName() {
            return "getPathValues";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder, String wrapper, String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper)
                .addStatement("return request.getPathParameter($N)", key)
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder, String wrapper, String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper)
                .addStatement("$T<String> values = request.getPathLookup().get($N)", Collection.class, key)
                .addStatement("return values == null ? new String[0] : values.toArray(new String[0])")
                .build();
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return CodeBlock.builder()
                .add("$N.hasPathParameter($N)", request, key)
                .build();
        }
    }

    private static final class QueryStringHelper extends SourceHelper {
        @Override
        public String getSingletonName() {
            return "getQueryValue";
        }

        @Override
        public String getArrayName() {
            return "getQueryValues";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder, String wrapper, String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper)
                .addStatement("return request.getQueryParameter($N)", key)
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder, String wrapper, String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper)
                .addStatement("$T<String> values = request.getQueryLookup().get($N)", Collection.class, key)
                .addStatement("return values == null ? new String[0] : values.toArray(new String[0])")
                .build();
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return CodeBlock.builder()
                .add("$N.hasQueryParameter($N)", request, key)
                .build();
        }
    }

    private static final class HeaderHelper extends SourceHelper {
        @Override
        public String getSingletonName() {
            return "getHeaderValue";
        }

        @Override
        public String getArrayName() {
            return "getHeaderValues";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder, String wrapper, String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper)
                .addStatement("return request.getHeaderValue($N)", key)
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder, String wrapper, String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper)
                .addStatement("$T<String> values = request.getHeaderLookup().get($N)", Collection.class, key)
                .addStatement("return values == null ? new String[0] : values.toArray(new String[0])")
                .build();
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return CodeBlock.builder()
                .add("$N.hasHeader($N)", request, key)
                .build();
        }
    }

    private static final class CookieHelper extends SourceHelper {
        @Override
        public String getSingletonName() {
            return "getCookieValue";
        }

        @Override
        public String getArrayName() {
            return "getCookieValues";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder, String wrapper, String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper)
                .addStatement("return request.getCookieValue($N)", key)
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder, String wrapper, String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper)
                .addStatement("$T<String> values = request.getCookieLookup().get($N)", Collection.class, key)
                .addStatement("return values == null ? new String[0] : values.toArray(new String[0])")
                .build();
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return CodeBlock.builder()
                .add("$N.hasCookie($N)", request, key)
                .build();
        }
    }

    private static final class FormDataHelper extends SourceHelper {
        @Override
        public String getSingletonName() {
            return "getFormDataValue";
        }

        @Override
        public String getArrayName() {
            return "getFormDataValues";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder, String wrapper, String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper)
                .addStatement("return request.getFormValue($N)", key)
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder, String wrapper, String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper)
                .addStatement("$T<String> values = request.getFormLookup().get($N)", Collection.class, key)
                .addStatement("return values == null ? new String[0] : values.toArray(new String[0])")
                .build();
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return CodeBlock.builder()
                .add("$N.hasFormParameter($N)", request, key)
                .build();
        }
    }

    private static final class AnyHelper extends SourceHelper {
        @Override
        public String getSingletonName() {
            return "getValue";
        }

        @Override
        public String getArrayName() {
            return "getValues";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(TypeSpec.Builder typeBuilder, String wrapper, String key) {
            // Build the source helpers for all the other source types
            for (Map.Entry<ValueSource, SourceHelper> entry : SOURCE_HELPER_LOOKUP.entrySet()) {
                ValueSource source = entry.getKey();
                if (source != ValueSource.Any) {
                    SourceHelper helper = entry.getValue();
                    helper.buildSingletonHelper(typeBuilder, key);
                }
            }

            CodeBlock.Builder builder = CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper);
            for (Map.Entry<ValueSource, SourceHelper> entry : SOURCE_HELPER_LOOKUP.entrySet()) {
                ValueSource source = entry.getKey();
                if (source != ValueSource.Any) {
                    SourceHelper helper = entry.getValue();
                    CodeBlock check = helper.getPresenceCheck("request", key);
                    if (check != null) {
                        builder.beginControlFlow("if (" + check.toString() + ")");
                        builder.addStatement("return $N($N, $N)", helper.getSingletonName(), wrapper, key);
                        builder.endControlFlow();
                    }
                }
            }
            builder.addStatement("return null");
            return builder.build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(TypeSpec.Builder typeBuilder, String wrapper, String key) {
            // Build the source helpers for all the other source types
            for (Map.Entry<ValueSource, SourceHelper> entry : SOURCE_HELPER_LOOKUP.entrySet()) {
                ValueSource source = entry.getKey();
                if (source != ValueSource.Any) {
                    SourceHelper helper = entry.getValue();
                    helper.buildArrayHelper(typeBuilder, key);
                }
            }

            CodeBlock.Builder builder = CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper);
            for (Map.Entry<ValueSource, SourceHelper> entry : SOURCE_HELPER_LOOKUP.entrySet()) {
                ValueSource source = entry.getKey();
                if (source != ValueSource.Any) {
                    SourceHelper helper = entry.getValue();
                    CodeBlock check = helper.getPresenceCheck("request", key);
                    if (check != null) {
                        builder.beginControlFlow("if (" + check.toString() + ")");
                        builder.addStatement("return $N($N, $N)", helper.getArrayName(), wrapper, key);
                        builder.endControlFlow();
                    }
                }
            }
            builder.addStatement("return new String[0]");
            return builder.build();
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return null;
        }
    }
}
