package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.truncon.javalin.mvc.api.FromCookie;
import com.truncon.javalin.mvc.api.FromForm;
import com.truncon.javalin.mvc.api.FromHeader;
import com.truncon.javalin.mvc.api.FromJson;
import com.truncon.javalin.mvc.api.FromPath;
import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.HttpContext;
import com.truncon.javalin.mvc.api.HttpRequest;
import com.truncon.javalin.mvc.api.Named;
import com.truncon.javalin.mvc.api.UseConverter;
import com.truncon.javalin.mvc.api.ValueSource;
import com.truncon.javalin.mvc.api.ws.WsContext;
import com.truncon.javalin.mvc.api.ws.WsMessageContext;
import com.truncon.javalin.mvc.api.ws.WsRequest;
import com.truncon.javalin.mvc.api.ws.WsValueSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
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
import java.util.TimeZone;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class HelperMethodBuilder {
    public static final Map<Class<?>, ConversionHelper> CONVERSION_HELPER_LOOKUP = getConversionHelperLookup();
    private static final Map<ValueSource, SourceHelper> SOURCE_HELPER_LOOKUP = getSourceHelperLookup();
    private static final Map<WsValueSource, WsSourceHelper> WS_SOURCE_HELPER_LOOKUP = getWsSourceHelperLookup();
    private static final String JSON_METHOD_NAME = "toJson";

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
        lookup.put(String.class, new StringHelper());
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

    private static Map<WsValueSource,WsSourceHelper> getWsSourceHelperLookup() {
        // Order for security reasons
        Map<WsValueSource, WsSourceHelper> lookup = new LinkedHashMap<>();
        lookup.put(WsValueSource.Header, new WsHeaderHelper());
        lookup.put(WsValueSource.Cookie, new WsCookieHelper());
        lookup.put(WsValueSource.Path, new WsPathHelper());
        lookup.put(WsValueSource.QueryString, new WsQueryStringHelper());
        lookup.put(WsValueSource.Message, new WsMessageHelper());
        lookup.put(WsValueSource.Any, new WsAnyHelper());
        return lookup;
    }

    private final ContainerSource container;
    private final Map<String, ConverterBuilder> converterLookup;
    private final TypeSpec.Builder typeBuilder;
    private final Set<Class<?>> addedSingletonConversionHelpers = new HashSet<>();
    private final Set<Class<?>> addedArrayConversionHelpers = new HashSet<>();
    private final Set<ValueSource> addedSingletonSourceHelpers = new HashSet<>();
    private final Set<ValueSource> addedArraySourceHelpers = new HashSet<>();
    private final Set<ImmutablePair<WsValueSource, Class<?>>> addedSingletonWsSourceHelpers = new HashSet<>();
    private final Set<ImmutablePair<WsValueSource, Class<?>>> addedArrayWsSourceHelpers = new HashSet<>();
    private final Map<ImmutablePair<ValueSource, String>, String> complexConversionLookup = new HashMap<>();
    private final Map<ImmutableTriple<WsValueSource, String, String>, String> complexWsConversionLookup = new HashMap<>();
    private final Map<String, Integer> complexConversionCounts = new HashMap<>();
    private final Set<Class<?>> jsonMethods = new HashSet<>();

    public HelperMethodBuilder(
            ContainerSource container,
            Map<String, ConverterBuilder> converterLookup,
            TypeSpec.Builder typeBuilder) {
        this.container = container;
        this.converterLookup = converterLookup;
        this.typeBuilder = typeBuilder;
    }

    public ContainerSource getContainer() {
        return container;
    }

    public Class<?> getParameterClass(TypeMirror parameterType) {
        TypeUtils typeUtils = container.getTypeUtils();
        for (Class<?> parameterClass : HelperMethodBuilder.CONVERSION_HELPER_LOOKUP.keySet()) {
            if (typeUtils.isType(parameterType, parameterClass)) {
                return parameterClass;
            } else if (parameterType.getKind() == TypeKind.ARRAY) {
                Class<?> arrayClass = TypeUtils.getArrayClass(parameterClass);
                if (typeUtils.isType(parameterType, arrayClass)) {
                    return arrayClass;
                }
            }
        }
        return null;
    }

    private Class<?> getParameterClass(Element memberElement) {
        return getParameterClass(getParameterType(memberElement));
    }

    private static TypeMirror getParameterType(Element memberElement) {
        if (memberElement.getKind() == ElementKind.FIELD) {
            return memberElement.asType();
        } else {
            ExecutableElement method = (ExecutableElement) memberElement;
            VariableElement parameter = method.getParameters().get(0);
            return parameter.asType();
        }
    }

    public String addConversionMethod(Class<?> parameterClass, boolean isArray) {
        if (isArray) {
            ConversionHelper arrayHelper = CONVERSION_HELPER_LOOKUP.get(parameterClass);
            if (arrayHelper != null) {
                arrayHelper.buildArrayHelper(this);
                return arrayHelper.getArrayName();
            }
        } else {
            ConversionHelper singletonHelper = CONVERSION_HELPER_LOOKUP.get(parameterClass);
            if (singletonHelper != null) {
                singletonHelper.buildSingletonHelper(this);
                return singletonHelper.getSingletonName();
            }
        }
        return null;
    }

    public static ValueSource getDefaultFromBinding(Element element) {
        if (element.getAnnotation(FromPath.class) != null) {
            return ValueSource.Path;
        }
        if (element.getAnnotation(FromQuery.class) != null) {
            return ValueSource.QueryString;
        }
        if (element.getAnnotation(FromHeader.class) != null) {
            return ValueSource.Header;
        }
        if (element.getAnnotation(FromCookie.class) != null) {
            return ValueSource.Cookie;
        }
        if (element.getAnnotation(FromForm.class) != null) {
            return ValueSource.FormData;
        }
        return ValueSource.Any;
    }

    public String addConversionMethod(TypeElement element, ValueSource defaultSource) {
        ImmutablePair<ValueSource, String> key = ImmutablePair.of(defaultSource, element.getQualifiedName().toString());
        String methodName = complexConversionLookup.get(key);
        if (methodName != null) {
            return methodName;
        }
        CodeBlock.Builder methodBodyBuilder = CodeBlock.builder();
        Name modelName = container.isFound() ? container.getDependencyName(element) : null;
        if (modelName == null) {
            methodBodyBuilder.addStatement("$T model = new $T()", element.asType(), element.asType());
        } else {
            methodBodyBuilder.addStatement("$T model = injector.$L()", element.asType(), modelName);
        }
        Collection<Element> memberElements = getBoundMemberElements(
            element,
            false,
            e -> defaultSource != ValueSource.Any || hasFromAnnotation(e) || hasMemberBinding(e)
        ).collect(Collectors.toList());
        for (Element memberElement : memberElements) {
            if (addConverterSetter(memberElement, methodBodyBuilder, defaultSource)) {
                continue;
            }
            if (addJsonSetter(memberElement, methodBodyBuilder)) {
                continue;
            }
            if (addPrimitiveSetter(memberElement, methodBodyBuilder, defaultSource)) {
                continue;
            }
            ValueSource defaultSubSource = getDefaultFromBinding(memberElement);
            if (defaultSubSource != ValueSource.Any || hasMemberBinding(memberElement)) {
                TypeElement subElement = container.getTypeUtils().getTypeElement(getParameterType(memberElement));
                String conversionMethod = addConversionMethod(subElement, defaultSource);
                String valueExpression = CodeBlock.builder()
                    .add("$N($N)", conversionMethod, "context")
                    .build()
                    .toString();
                setMember(memberElement, methodBodyBuilder, valueExpression);
            }
        }
        methodBodyBuilder.addStatement("return model");

        String simpleName = element.getSimpleName().toString();
        int count = complexConversionCounts.getOrDefault(simpleName, 0);
        methodName = "to" + simpleName + (count + 1);
        MethodSpec method = MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PRIVATE)
            .addModifiers(Modifier.STATIC)
            .returns(TypeName.get(element.asType()))
            .addParameter(HttpContext.class, "context", Modifier.FINAL)
            .addCode(methodBodyBuilder.build())
            .build();
        typeBuilder.addMethod(method);
        complexConversionLookup.put(key, methodName);
        complexConversionCounts.put(simpleName, count + 1);
        return methodName;
    }

    private boolean isValidBindTarget(Element memberElement, boolean isWebSockets) {
        if (memberElement.getKind() == ElementKind.FIELD) {
            if (memberElement.getModifiers().contains(Modifier.PUBLIC)) {
                return true;
            }
            if ((!isWebSockets && hasFromAnnotation(memberElement))
                    || (isWebSockets && hasWsFromAnnotation(memberElement))) {
                // TODO - if there is an explicit @From* annotation on a private field, raise an error
            }
            return false;
        } else if (memberElement.getKind() == ElementKind.METHOD) {
            if (memberElement.getModifiers().contains(Modifier.PUBLIC)) {
                ExecutableElement method = (ExecutableElement) memberElement;
                if (!method.getModifiers().contains(Modifier.STATIC) && method.getParameters().size() == 1) {
                    return true;
                }
            }
            if ((!isWebSockets && hasFromAnnotation(memberElement))
                    || (isWebSockets && hasWsFromAnnotation(memberElement))) {
                // TODO - if there is an explicit @From* annotation on a private field, raise an error
            }
            return false;
        } else {
            return false;
        }
    }

    private boolean addConverterSetter(
            Element memberElement,
            CodeBlock.Builder methodBodyBuilder,
            ValueSource defaultSource) {
        String converterName = getConverterName(memberElement);
        if (converterName == null) {
            return false;
        }
        ConverterBuilder converter = converterLookup.get(converterName);
        if (converter == null) {
            String message = "No converter named '"
                + converterName
                + "' exists.";
            throw new ProcessingException(message, memberElement);
        }
        TypeMirror parameterType = getParameterType(memberElement);
        if (!converter.isExpectedType(parameterType)) {
            String message = "The conversion method '"
                + converterName
                + "' does not return a type compatible with '"
                + parameterType.toString()
                + "'.";
            throw new ProcessingException(message, memberElement);
        }

        String memberName = getMemberName(memberElement);
        ValueSource valueSource = getMemberValueSource(memberElement, defaultSource);
        if (converter.hasContextOrRequestType(HttpContext.class)) {
            String call = converter.getConverterCall(container, "context", memberName, valueSource).toString();
            setMember(memberElement, methodBodyBuilder, call);
            return true;
        } else if (converter.hasContextOrRequestType(HttpRequest.class)) {
            String requestName = "context.getRequest()";
            String call = converter.getConverterCall(container, requestName, memberName, valueSource).toString();
            setMember(memberElement, methodBodyBuilder, call);
            return true;
        } else {
            String message = "The conversion method '"
                + converterName
                + "' cannot be used with HTTP action methods.";
            throw new ProcessingException(message, memberElement);
        }
    }

    private static String getConverterName(Element memberElement) {
        // We look in three places for @UseConverter annotations. In order or precedence:
        // 1) On the setter method parameter
        // 2) On the member declaration
        // 3) On the member type definition
        if (memberElement.getKind() == ElementKind.METHOD) {
            ExecutableElement method = (ExecutableElement) memberElement;
            VariableElement parameter = method.getParameters().get(0); // We already checked there's one parameter
            UseConverter parameterConverter = parameter.getAnnotation(UseConverter.class);
            if (parameterConverter != null) {
                return parameterConverter.value();
            }
        }
        UseConverter memberConverter = memberElement.getAnnotation(UseConverter.class);
        if (memberConverter != null) {
            return memberConverter.value();
        }
        TypeMirror parameterType = getParameterType(memberElement);
        if (parameterType.getKind() == TypeKind.DECLARED) {
            DeclaredType declaredType = (DeclaredType) parameterType;
            Element element = declaredType.asElement();
            UseConverter declarationConverter = element.getAnnotation(UseConverter.class);
            if (declarationConverter != null) {
                return declarationConverter.value();
            }
        }
        UseConverter typeConverter = parameterType.getAnnotation(UseConverter.class);
        return typeConverter == null ? null : typeConverter.value();
    }

    private boolean addJsonSetter(Element memberElement, CodeBlock.Builder methodBodyBuilder) {
        if (!hasFromJsonAnnotation(memberElement)) {
            return false;
        }
        String jsonMethod = addJsonMethod();
        String valueExpression = CodeBlock
            .of("$N($N, $T.class)", jsonMethod, "context", getParameterType(memberElement))
            .toString();
        setMember(memberElement, methodBodyBuilder, valueExpression);
        return true;
    }

    private static boolean hasFromJsonAnnotation(Element element) {
        FromJson annotation = element.getAnnotation(FromJson.class);
        if (annotation != null) {
            return true;
        }
        if (element.getKind() == ElementKind.METHOD) {
            ExecutableElement method = (ExecutableElement) element;
            VariableElement parameter = method.getParameters().get(0); // We already checked there's one parameter
            FromJson parameterAnnotation = parameter.getAnnotation(FromJson.class);
            return parameterAnnotation != null;
        }
        return false;
    }

    private boolean addPrimitiveSetter(
            Element memberElement,
            CodeBlock.Builder methodBodyBuilder,
            ValueSource defaultSource) {
        Class<?> parameterClass = getParameterClass(memberElement);
        if (parameterClass == null) {
            return false;
        }
        Class<?> actualClass = parameterClass.isArray()
            ? parameterClass.getComponentType()
            : parameterClass;
        String conversionMethod = addConversionMethod(actualClass, parameterClass.isArray());
        if (conversionMethod == null) {
            return false;
        }
        String memberName = getMemberName(memberElement);
        if (memberName == null) {
            return false;
        }
        ValueSource valueSource = getMemberValueSource(memberElement, defaultSource);
        String sourceMethod = addSourceMethod(valueSource, parameterClass.isArray());
        String valueExpression = getValueExpression(conversionMethod, sourceMethod, "context", memberName);
        return setMember(memberElement, methodBodyBuilder, valueExpression);
    }

    private boolean setMember(Element member, CodeBlock.Builder methodBodyBuilder, String valueExpression) {
        if (member.getKind() == ElementKind.FIELD) {
            methodBodyBuilder.addStatement("model.$N = " + valueExpression, member.getSimpleName());
            return true;
        } else if (member.getKind() == ElementKind.METHOD) {
            methodBodyBuilder.addStatement("model.$N(" + valueExpression + ")", member.getSimpleName());
            return true;
        } else {
            return false;
        }
    }

    private String getValueExpression(String conversionMethod, String sourceMethod, String context, String key) {
        return CodeBlock.builder()
            .add("$N($N($N, $S))", conversionMethod, sourceMethod, context, key)
            .build()
            .toString();
    }

    private static ValueSource getMemberValueSource(Element memberElement, ValueSource defaultSource) {
        if (memberElement.getAnnotation(FromHeader.class) != null) {
            return ValueSource.Header;
        }
        if (memberElement.getAnnotation(FromCookie.class) != null) {
            return ValueSource.Cookie;
        }
        if (memberElement.getAnnotation(FromPath.class) != null) {
            return ValueSource.Path;
        }
        if (memberElement.getAnnotation(FromQuery.class) != null) {
            return ValueSource.QueryString;
        }
        if (memberElement.getAnnotation(FromForm.class) != null) {
            return ValueSource.FormData;
        }
        return defaultSource;
    }

    private static String getMemberName(Element memberElement) {
        Named named = memberElement.getAnnotation(Named.class);
        if (named != null) {
            return named.value();
        }
        if (memberElement.getKind() == ElementKind.FIELD) {
            return memberElement.getSimpleName().toString();
        } else if (memberElement.getKind() == ElementKind.METHOD) {
            String name = memberElement.getSimpleName().toString();
            return StringUtils.uncapitalize(StringUtils.removeStart(name, "set"));
        } else {
            return null;
        }
    }

    public boolean hasMemberBinding(Element element) {
        return hasMemberBinding(element, HelperMethodBuilder::hasFromAnnotation);
    }

    private boolean hasMemberBinding(Element element, Function<Element, Boolean> hasAnnotation) {
        if (element.getKind() == ElementKind.PARAMETER) {
            TypeElement typeElement = container.getTypeUtils().getTypeElement(element.asType());
            return hasMemberBinding(typeElement, hasAnnotation);
        } else if (element.getKind() == ElementKind.FIELD) {
            TypeElement typeElement = container.getTypeUtils().getTypeElement(element.asType());
            return hasMemberBinding(typeElement, hasAnnotation);
        } else if (element.getKind() == ElementKind.METHOD) {
            ExecutableElement method = (ExecutableElement) element;
            if (!method.getModifiers().contains(Modifier.STATIC) && method.getParameters().size() == 1) {
                VariableElement parameterElement = method.getParameters().get(0);
                TypeElement typeElement = container.getTypeUtils().getTypeElement(parameterElement.asType());
                return hasMemberBinding(typeElement, hasAnnotation);
            }
        }
        return false;
    }

    private boolean hasMemberBinding(TypeElement element, Function<Element, Boolean> hasAnnotation) {
        if (element == null) {
            return false;
        }
        boolean hasBinding = element.getEnclosedElements().stream()
            .filter(e -> e.getKind() == ElementKind.FIELD || e.getKind() == ElementKind.METHOD)
            .anyMatch(hasAnnotation::apply);
        if (hasBinding) {
            return true;
        }
        TypeMirror superType = element.getSuperclass();
        if (superType.getKind() == TypeKind.NONE || container.getTypeUtils().isType(superType, Object.class)) {
            return false;
        }
        TypeElement superTypeElement = container.getTypeUtils().getTypeElement(superType);
        return hasMemberBinding(superTypeElement, hasAnnotation);
    }

    public static boolean hasFromAnnotation(Element element) {
        return element.getAnnotation(FromPath.class) != null
            || element.getAnnotation(FromQuery.class) != null
            || element.getAnnotation(FromHeader.class) != null
            || element.getAnnotation(FromCookie.class) != null
            || element.getAnnotation(FromForm.class) != null;
    }

    public static WsValueSource getDefaultWsFromBinding(Element element) {
        if (element.getAnnotation(FromPath.class) != null) {
            return WsValueSource.Path;
        }
        if (element.getAnnotation(FromQuery.class) != null) {
            return WsValueSource.QueryString;
        }
        if (element.getAnnotation(FromHeader.class) != null) {
            return WsValueSource.Header;
        }
        if (element.getAnnotation(FromCookie.class) != null) {
            return WsValueSource.Cookie;
        }
        return WsValueSource.Any;
    }

    public String addConversionMethod(
            TypeElement element,
            WsValueSource defaultSource,
            Class<? extends WsContext> contextType) {
        ImmutableTriple<WsValueSource, String, String> key = ImmutableTriple.of(
            defaultSource,
            element.getQualifiedName().toString(),
            contextType.getSimpleName());
        String methodName = complexWsConversionLookup.get(key);
        if (methodName != null) {
            return methodName;
        }
        CodeBlock.Builder methodBodyBuilder = CodeBlock.builder();
        Name modelName = container.isFound() ? container.getDependencyName(element) : null;
        if (modelName == null) {
            methodBodyBuilder.addStatement("$T model = new $T()", element.asType(), element.asType());
        } else {
            methodBodyBuilder.addStatement("$T model = injector.$L()", element.asType(), modelName);
        }
        Collection<Element> memberElements = getBoundMemberElements(
            element,
            true,
            e -> defaultSource != WsValueSource.Any || hasWsFromAnnotation(e) || hasMemberBinding(e)
        ).collect(Collectors.toList());
        for (Element memberElement : memberElements) {
            if (addConverterSetter(memberElement, methodBodyBuilder, contextType, defaultSource)) {
                continue;
            }
            if (addJsonSetter(memberElement, methodBodyBuilder, contextType)) {
                continue;
            }
            if (addPrimitiveSetter(memberElement, methodBodyBuilder, defaultSource, contextType)) {
                continue;
            }
            WsValueSource defaultSubSource = getDefaultWsFromBinding(memberElement);
            if (defaultSubSource != WsValueSource.Any || hasWsMemberBinding(memberElement)) {
                TypeElement subElement = container.getTypeUtils().getTypeElement(getParameterType(memberElement));
                String conversionMethod = addConversionMethod(subElement, defaultSource, contextType);
                String valueExpression = CodeBlock.builder()
                    .add("$N($N)", conversionMethod, "context")
                    .build()
                    .toString();
                setMember(memberElement, methodBodyBuilder, valueExpression);
            }
        }
        methodBodyBuilder.addStatement("return model");

        String simpleName = element.getSimpleName().toString();
        int count = complexConversionCounts.getOrDefault(simpleName, 0);
        methodName = "to" + simpleName + (count + 1);
        MethodSpec method = MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PRIVATE)
            .addModifiers(Modifier.STATIC)
            .returns(TypeName.get(element.asType()))
            .addParameter(contextType, "context", Modifier.FINAL)
            .addCode(methodBodyBuilder.build())
            .build();
        typeBuilder.addMethod(method);
        complexWsConversionLookup.put(key, methodName);
        complexConversionCounts.put(simpleName, count + 1);
        return methodName;
    }

    private boolean addConverterSetter(
            Element memberElement,
            CodeBlock.Builder methodBodyBuilder,
            Class<? extends WsContext> contextType,
            WsValueSource defaultSource) {
        String converterName = getConverterName(memberElement);
        if (converterName == null) {
            return false;
        }
        ConverterBuilder converter = converterLookup.get(converterName);
        if (converter == null) {
            String message = "No converter named '"
                + converterName
                + "' exists.";
            throw new ProcessingException(message, memberElement);
        }
        TypeMirror parameterType = getParameterType(memberElement);
        if (!converter.isExpectedType(parameterType)) {
            String message = "The conversion method '"
                + converterName
                + "' does not return a type compatible with '"
                + parameterType.toString()
                + "'.";
            throw new ProcessingException(message, memberElement);
        }

        String memberName = getMemberName(memberElement);
        WsValueSource valueSource = getMemberValueSource(memberElement, defaultSource);
        if (converter.hasContextOrRequestType(WsContext.class) || converter.hasContextOrRequestType(contextType)) {
            String call = converter.getConverterCall(container, "context", memberName, valueSource).toString();
            setMember(memberElement, methodBodyBuilder, call);
            return true;
        } else if (converter.hasContextOrRequestType(WsRequest.class)) {
            String requestName = "context.getRequest()";
            String call = converter.getConverterCall(container, requestName, memberName, valueSource).toString();
            setMember(memberElement, methodBodyBuilder, call);
            return true;
        } else {
            String message = "The conversion method '"
                + converterName
                + "' cannot be used with WebSocket action methods.";
            throw new ProcessingException(message, memberElement);
        }
    }

    private boolean addJsonSetter(Element memberElement, CodeBlock.Builder methodBodyBuilder, Class<? extends WsContext> contextType) {
        if (!hasFromJsonAnnotation(memberElement)) {
            return false;
        }
        if (contextType != WsMessageContext.class) {
            return false;
        }
        String jsonMethod = addWsJsonMethod(contextType);
        String valueExpression = CodeBlock
            .of("$N($N, $T.class)", jsonMethod, "context", getParameterType(memberElement))
            .toString();
        setMember(memberElement, methodBodyBuilder, valueExpression);
        return true;
    }

    private Stream<? extends Element> getBoundMemberElements(TypeElement element, boolean isWebSockets, Function<Element, Boolean> hasBinding) {
        Stream<? extends Element> currentMembers = element.getEnclosedElements().stream()
            .filter(e -> e.getKind() == ElementKind.FIELD || e.getKind() == ElementKind.METHOD)
            .filter(hasBinding::apply)
            .filter(e -> isValidBindTarget(e, isWebSockets));
        TypeMirror superType = element.getSuperclass();
        if (superType.getKind() == TypeKind.NONE || container.getTypeUtils().isType(superType, Object.class)) {
            return currentMembers;
        }
        TypeElement superTypeElement = container.getTypeUtils().getTypeElement(superType);
        Stream<? extends Element> baseMembers = getBoundMemberElements(superTypeElement, isWebSockets, hasBinding);
        return Stream.concat(currentMembers, baseMembers);
    }

    public static boolean hasWsFromAnnotation(Element element) {
        return element.getAnnotation(FromPath.class) != null
            || element.getAnnotation(FromQuery.class) != null
            || element.getAnnotation(FromHeader.class) != null
            || element.getAnnotation(FromCookie.class) != null;
    }

    private boolean addPrimitiveSetter(
            Element memberElement,
            CodeBlock.Builder methodBodyBuilder,
            WsValueSource defaultSource,
            Class<?> contextType) {
        Class<?> parameterClass = getParameterClass(memberElement);
        if (parameterClass == null) {
            return false;
        }
        Class<?> actualClass = parameterClass.isArray()
            ? parameterClass.getComponentType()
            : parameterClass;
        String conversionMethod = addConversionMethod(actualClass, parameterClass.isArray());
        if (conversionMethod == null) {
            return false;
        }
        String memberName = getMemberName(memberElement);
        if (memberName == null) {
            return false;
        }
        WsValueSource valueSource = getMemberValueSource(memberElement, defaultSource);
        String sourceMethod = addSourceMethod(valueSource, contextType, parameterClass.isArray());
        String valueExpression = getValueExpression(conversionMethod, sourceMethod, "context", memberName);
        return setMember(memberElement, methodBodyBuilder, valueExpression);
    }

    private static WsValueSource getMemberValueSource(Element memberElement, WsValueSource defaultSource) {
        if (memberElement.getAnnotation(FromHeader.class) != null) {
            return WsValueSource.Header;
        }
        if (memberElement.getAnnotation(FromCookie.class) != null) {
            return WsValueSource.Cookie;
        }
        if (memberElement.getAnnotation(FromPath.class) != null) {
            return WsValueSource.Path;
        }
        if (memberElement.getAnnotation(FromQuery.class) != null) {
            return WsValueSource.QueryString;
        }
        return defaultSource;
    }

    public boolean hasWsMemberBinding(Element element) {
        return hasMemberBinding(element, HelperMethodBuilder::hasWsFromAnnotation);
    }

    public String addSourceMethod(ValueSource valueSource, boolean isArray) {
        SourceHelper sourceHelper = SOURCE_HELPER_LOOKUP.get(valueSource);
        if (isArray) {
            sourceHelper.buildArrayHelper(this);
            return sourceHelper.getArrayName();
        } else {
            sourceHelper.buildSingletonHelper(this);
            return sourceHelper.getSingletonName();
        }
    }

    public String addSourceMethod(WsValueSource valueSource, Class<?> wrapperType, boolean isArray) {
        // TODO - Check that the wrapper type and value source make sense together
        WsSourceHelper sourceHelper = WS_SOURCE_HELPER_LOOKUP.get(valueSource);
        if (isArray) {
            sourceHelper.buildArrayHelper(this, wrapperType);
            return sourceHelper.getArrayName();
        } else {
            sourceHelper.buildSingletonHelper(this, wrapperType);
            return sourceHelper.getSingletonName();
        }
    }

    public String addJsonMethod() {
        if (jsonMethods.contains(HttpContext.class)) {
            return JSON_METHOD_NAME;
        }
        TypeVariableName typeArgument = TypeVariableName.get("T");
        MethodSpec method = MethodSpec.methodBuilder(JSON_METHOD_NAME)
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
            .addTypeVariable(typeArgument)
            .returns(typeArgument)
            .addParameter(HttpContext.class, "context", Modifier.FINAL)
            .addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), typeArgument), "type", Modifier.FINAL)
            .addCode(CodeBlock.builder()
                .addStatement("$T request = context.getRequest()", HttpRequest.class)
                .addStatement("return request.getBodyFromJson(type)")
                .build()
            )
            .build();
        typeBuilder.addMethod(method);
        jsonMethods.add(HttpContext.class);
        return JSON_METHOD_NAME;
    }

    public String addWsJsonMethod(Class<?> wrapperType) {
        if (jsonMethods.contains(wrapperType)) {
            return JSON_METHOD_NAME;
        }
        TypeVariableName typeArgument = TypeVariableName.get("T");
        MethodSpec method = MethodSpec.methodBuilder(JSON_METHOD_NAME)
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
            .addTypeVariable(typeArgument)
            .returns(typeArgument)
            .addParameter(wrapperType, "context", Modifier.FINAL)
            .addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), typeArgument), "type", Modifier.FINAL)
            .addCode(CodeBlock.builder()
                .addStatement("return context.getMessage(type)")
                .build()
            )
            .build();
        typeBuilder.addMethod(method);
        jsonMethods.add(wrapperType);
        return JSON_METHOD_NAME;
    }

    // region ConversionHelper

    private static abstract class ConversionHelper {
        public abstract Class<?> getSingletonType();
        public abstract Class<?> getArrayType();
        public abstract String getSingletonName();
        public abstract String getArrayName();

        public void buildSingletonHelper(HelperMethodBuilder builder) {
            if (builder.addedSingletonConversionHelpers.contains(getSingletonType())) {
                return;
            }
            MethodSpec method = MethodSpec.methodBuilder(getSingletonName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(getSingletonType())
                .addParameter(String.class, "value", Modifier.FINAL)
                .addCode(getSingletonMethodBody(builder))
                .build();
            builder.typeBuilder.addMethod(method);
            builder.addedSingletonConversionHelpers.add(getSingletonType());
        }

        protected abstract CodeBlock getSingletonMethodBody(HelperMethodBuilder builder);

        public void buildArrayHelper(HelperMethodBuilder builder) {
            if (builder.addedArrayConversionHelpers.contains(getSingletonType())) {
                return;
            }
            MethodSpec method = MethodSpec.methodBuilder(getArrayName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(getArrayType())
                .addParameter(String[].class, "values", Modifier.FINAL)
                .addCode(getArrayMethodBody(builder))
                .build();
            builder.typeBuilder.addMethod(method);
            builder.addedArrayConversionHelpers.add(getSingletonType());
        }

        protected abstract CodeBlock getArrayMethodBody(HelperMethodBuilder builder);

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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? (byte) 0 : Byte.parseByte(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return (byte) 0")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? (short) 0 : Short.parseShort(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return (short) 0")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? 0 : Integer.parseInt(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return 0")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? 0L : Long.parseLong(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return 0L")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? 0.0f : Float.parseFloat(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return 0.0f")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? 0.0 : Double.parseDouble(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return 0.0")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("return Boolean.parseBoolean(value)")
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("return value != null && value.length() == 1 ? value.charAt(0) : '\\0'")
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Byte.parseByte(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Short.parseShort(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Integer.parseInt(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Long.parseLong(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Float.parseFloat(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Double.parseDouble(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("return value == null ? null : Boolean.parseBoolean(value)")
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("return value != null && value.length() == 1 ? value.charAt(0) : null")
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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

    private static final class StringHelper extends ConversionHelper {
        @Override
        public Class<?> getSingletonType() {
            return String.class;
        }

        @Override
        public Class<?> getArrayType() {
            return String[].class;
        }

        @Override
        public String getSingletonName() {
            return "toString";
        }

        @Override
        public String getArrayName() {
            return "toStringArray";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("return value")
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("return values")
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            addFormatterField(builder.typeBuilder);
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : DATE_FORMATTER.parse(value)")
                .nextControlFlow("catch ($T exception)", ParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
            addFormatterField(builder.typeBuilder);
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
                MethodSpec method = MethodSpec.methodBuilder("getDateFormatter")
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                    .returns(SimpleDateFormat.class)
                    .addCode(CodeBlock.builder()
                        .addStatement("$T formatter = new $T($S)", SimpleDateFormat.class, SimpleDateFormat.class, "yyyy-MM-dd'T'HH:mm:ss'Z'")
                        .addStatement("formatter.setTimeZone($T.getTimeZone($S))", TimeZone.class, "UTC")
                        .addStatement("return formatter")
                        .build())
                    .build();
                typeBuilder.addMethod(method);
                addField(
                    typeBuilder,
                    "DATE_FORMATTER",
                    SimpleDateFormat.class,
                    "$N()",
                    method.name
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Instant.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : ZonedDateTime.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : OffsetDateTime.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : LocalDateTime.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : LocalDate.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : YearMonth.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Year.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : new BigInteger(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : new BigDecimal(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : UUID.fromString(value)")
                .nextControlFlow("catch ($T exception)", IllegalArgumentException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder) {
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

    // endregion

    // region SourceHelper

    private static abstract class SourceHelper {
        public abstract ValueSource getValueSource();
        public abstract String getSingletonName();
        public abstract String getArrayName();

        public void buildSingletonHelper(HelperMethodBuilder builder) {
            if (builder.addedSingletonSourceHelpers.contains(getValueSource())) {
                return;
            }
            MethodSpec method = MethodSpec.methodBuilder(getSingletonName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(String.class)
                .addParameter(HttpContext.class, "context", Modifier.FINAL)
                .addParameter(String.class, "key", Modifier.FINAL)
                .addCode(getSingletonMethodBody(builder, "context", "key"))
                .build();
            builder.typeBuilder.addMethod(method);
            builder.addedSingletonSourceHelpers.add(getValueSource());
        }

        protected abstract CodeBlock getSingletonMethodBody(HelperMethodBuilder builder, String context, String key);

        public void buildArrayHelper(HelperMethodBuilder builder) {
            if (builder.addedArraySourceHelpers.contains(getValueSource())) {
                return;
            }
            MethodSpec method = MethodSpec.methodBuilder(getArrayName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(String[].class)
                .addParameter(HttpContext.class, "context", Modifier.FINAL)
                .addParameter(String.class, "key", Modifier.FINAL)
                .addCode(getArrayMethodBody(builder,"context", "key"))
                .build();
            builder.typeBuilder.addMethod(method);
            builder.addedArraySourceHelpers.add(getValueSource());
        }

        protected abstract CodeBlock getArrayMethodBody(HelperMethodBuilder builder, String context, String key);

        public abstract CodeBlock getPresenceCheck(String request, String key);
    }

    private static final class PathHelper extends SourceHelper {
        @Override
        public ValueSource getValueSource() {
            return ValueSource.Path;
        }

        @Override
        public String getSingletonName() {
            return "getPathValue";
        }

        @Override
        public String getArrayName() {
            return "getPathValues";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder, String wrapper, String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper)
                .addStatement("return request.getPathParameter($N)", key)
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder, String wrapper, String key) {
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
        public ValueSource getValueSource() {
            return ValueSource.QueryString;
        }

        @Override
        public String getSingletonName() {
            return "getQueryValue";
        }

        @Override
        public String getArrayName() {
            return "getQueryValues";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder, String wrapper, String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper)
                .addStatement("return request.getQueryParameter($N)", key)
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder, String wrapper, String key) {
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
        public ValueSource getValueSource() {
            return ValueSource.Header;
        }

        @Override
        public String getSingletonName() {
            return "getHeaderValue";
        }

        @Override
        public String getArrayName() {
            return "getHeaderValues";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder, String wrapper, String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper)
                .addStatement("return request.getHeaderValue($N)", key)
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder, String wrapper, String key) {
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
        public ValueSource getValueSource() {
            return ValueSource.Cookie;
        }

        @Override
        public String getSingletonName() {
            return "getCookieValue";
        }

        @Override
        public String getArrayName() {
            return "getCookieValues";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder, String wrapper, String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper)
                .addStatement("return request.getCookieValue($N)", key)
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder, String wrapper, String key) {
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
        public ValueSource getValueSource() {
            return ValueSource.FormData;
        }

        @Override
        public String getSingletonName() {
            return "getFormDataValue";
        }

        @Override
        public String getArrayName() {
            return "getFormDataValues";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder, String wrapper, String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper)
                .addStatement("return request.getFormValue($N)", key)
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder, String wrapper, String key) {
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
        public ValueSource getValueSource() {
            return ValueSource.Any;
        }

        @Override
        public String getSingletonName() {
            return "getValue";
        }

        @Override
        public String getArrayName() {
            return "getValues";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(HelperMethodBuilder builder, String wrapper, String key) {
            // Build the source helpers for all the other source types
            for (Map.Entry<ValueSource, SourceHelper> entry : SOURCE_HELPER_LOOKUP.entrySet()) {
                ValueSource source = entry.getKey();
                if (source != ValueSource.Any) {
                    SourceHelper helper = entry.getValue();
                    helper.buildSingletonHelper(builder);
                }
            }

            CodeBlock.Builder codeBuilder = CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper);
            for (Map.Entry<ValueSource, SourceHelper> entry : SOURCE_HELPER_LOOKUP.entrySet()) {
                ValueSource source = entry.getKey();
                if (source != ValueSource.Any) {
                    SourceHelper helper = entry.getValue();
                    CodeBlock check = helper.getPresenceCheck("request", key);
                    if (check != null) {
                        codeBuilder.beginControlFlow("if (" + check.toString() + ")");
                        codeBuilder.addStatement("return $N($N, $N)", helper.getSingletonName(), wrapper, key);
                        codeBuilder.endControlFlow();
                    }
                }
            }
            codeBuilder.addStatement("return null");
            return codeBuilder.build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(HelperMethodBuilder builder, String wrapper, String key) {
            // Build the source helpers for all the other source types
            for (Map.Entry<ValueSource, SourceHelper> entry : SOURCE_HELPER_LOOKUP.entrySet()) {
                ValueSource source = entry.getKey();
                if (source != ValueSource.Any) {
                    SourceHelper helper = entry.getValue();
                    helper.buildArrayHelper(builder);
                }
            }

            CodeBlock.Builder codeBuilder = CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper);
            for (Map.Entry<ValueSource, SourceHelper> entry : SOURCE_HELPER_LOOKUP.entrySet()) {
                ValueSource source = entry.getKey();
                if (source != ValueSource.Any) {
                    SourceHelper helper = entry.getValue();
                    CodeBlock check = helper.getPresenceCheck("request", key);
                    if (check != null) {
                        codeBuilder.beginControlFlow("if (" + check.toString() + ")");
                        codeBuilder.addStatement("return $N($N, $N)", helper.getArrayName(), wrapper, key);
                        codeBuilder.endControlFlow();
                    }
                }
            }
            codeBuilder.addStatement("return new String[0]");
            return codeBuilder.build();
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return null;
        }
    }

    // endregion

    // region WsSourceHelper

    private static abstract class WsSourceHelper {
        public abstract WsValueSource getValueSource();
        public abstract String getSingletonName();
        public abstract String getArrayName();

        public void buildSingletonHelper(HelperMethodBuilder builder, Class<?> wrapperType) {
            ImmutablePair<WsValueSource, Class<?>> key = ImmutablePair.of(getValueSource(), wrapperType);
            if (builder.addedSingletonWsSourceHelpers.contains(key)) {
                return;
            }
            MethodSpec method = MethodSpec.methodBuilder(getSingletonName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(String.class)
                .addParameter(wrapperType, "context", Modifier.FINAL)
                .addParameter(String.class, "key", Modifier.FINAL)
                .addCode(getSingletonMethodBody(builder, wrapperType, "context", "key"))
                .build();
            builder.typeBuilder.addMethod(method);
            builder.addedSingletonWsSourceHelpers.add(key);
        }

        protected abstract CodeBlock getSingletonMethodBody(
            HelperMethodBuilder builder,
            Class<?> contextType,
            String context,
            String key);

        public void buildArrayHelper(HelperMethodBuilder builder, Class<?> wrapperType) {
            ImmutablePair<WsValueSource, Class<?>> key = ImmutablePair.of(getValueSource(), wrapperType);
            if (builder.addedArrayWsSourceHelpers.contains(key)) {
                return;
            }
            MethodSpec method = MethodSpec.methodBuilder(getArrayName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(String[].class)
                .addParameter(wrapperType, "context", Modifier.FINAL)
                .addParameter(String.class, "key", Modifier.FINAL)
                .addCode(getArrayMethodBody(builder, wrapperType, "context", "key"))
                .build();
            builder.typeBuilder.addMethod(method);
            builder.addedArrayWsSourceHelpers.add(key);
        }

        protected abstract CodeBlock getArrayMethodBody(
            HelperMethodBuilder builder,
            Class<?> contextType,
            String context,
            String key);

        public abstract CodeBlock getPresenceCheck(String context, String key);
    }

    private static final class WsPathHelper extends WsSourceHelper {
        @Override
        public WsValueSource getValueSource() {
            return WsValueSource.Path;
        }

        @Override
        public String getSingletonName() {
            return "getWsPathValue";
        }

        @Override
        public String getArrayName() {
            return "getWsPathValues";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(
                HelperMethodBuilder builder,
                Class<?> wrapperType,
                String wrapper,
                String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", WsRequest.class, wrapper)
                .addStatement("return request.getPathParameter($N)", key)
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(
                HelperMethodBuilder builder,
                Class<?> wrapperType,
                String wrapper,
                String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", WsRequest.class, wrapper)
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

    private static final class WsQueryStringHelper extends WsSourceHelper {
        @Override
        public WsValueSource getValueSource() {
            return WsValueSource.QueryString;
        }

        @Override
        public String getSingletonName() {
            return "getWsQueryValue";
        }

        @Override
        public String getArrayName() {
            return "getWsQueryValues";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(
                HelperMethodBuilder builder,
                Class<?> wrapperType,
                String wrapper,
                String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", WsRequest.class, wrapper)
                .addStatement("return request.getQueryParameter($N)", key)
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(
                HelperMethodBuilder builder,
                Class<?> wrapperType,
                String wrapper,
                String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", WsRequest.class, wrapper)
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

    private static final class WsHeaderHelper extends WsSourceHelper {
        @Override
        public WsValueSource getValueSource() {
            return WsValueSource.Header;
        }

        @Override
        public String getSingletonName() {
            return "getWsHeaderValue";
        }

        @Override
        public String getArrayName() {
            return "getWsHeaderValues";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(
                HelperMethodBuilder builder,
                Class<?> wrapperType,
                String wrapper,
                String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", WsRequest.class, wrapper)
                .addStatement("return request.getHeaderValue($N)", key)
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(
                HelperMethodBuilder builder,
                Class<?> wrapperType,
                String wrapper,
                String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", WsRequest.class, wrapper)
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

    private static final class WsCookieHelper extends WsSourceHelper {
        @Override
        public WsValueSource getValueSource() {
            return WsValueSource.Cookie;
        }

        @Override
        public String getSingletonName() {
            return "getWsCookieValue";
        }

        @Override
        public String getArrayName() {
            return "getWsCookieValues";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(
                HelperMethodBuilder builder,
                Class<?> wrapperType,
                String wrapper,
                String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", WsRequest.class, wrapper)
                .addStatement("return request.getCookieValue($N)", key)
                .build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(
                HelperMethodBuilder builder,
                Class<?> wrapperType,
                String wrapper,
                String key) {
            return CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", WsRequest.class, wrapper)
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

    private static final class WsMessageHelper extends WsSourceHelper {
        @Override
        public WsValueSource getValueSource() {
            return WsValueSource.Message;
        }

        @Override
        public String getSingletonName() {
            return "getWsMessageValue";
        }

        @Override
        public String getArrayName() {
            return "getWsMessageValues";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(
                HelperMethodBuilder builder,
                Class<?> wrapperType,
                String wrapper,
                String key) {
            if (wrapperType == WsMessageContext.class) {
                return CodeBlock.builder().addStatement("return $N.getMessage()", wrapper).build();
            } else {
                return CodeBlock.builder().addStatement("return null").build();
            }
        }

        @Override
        protected CodeBlock getArrayMethodBody(
                HelperMethodBuilder builder,
                Class<?> wrapperType,
                String wrapper,
                String key) {
            if (wrapperType == WsMessageContext.class) {
                return CodeBlock.builder()
                    .addStatement("return new String[] { $N.getMessage() }", wrapper)
                    .build();
            } else {
                return CodeBlock.builder().addStatement("return new String[0]").build();
            }
        }

        @Override
        public CodeBlock getPresenceCheck(String context, String key) {
            return null;
        }
    }

    private static final class WsAnyHelper extends WsSourceHelper {
        @Override
        public WsValueSource getValueSource() {
            return WsValueSource.Any;
        }

        @Override
        public String getSingletonName() {
            return "getWsValue";
        }

        @Override
        public String getArrayName() {
            return "getWsValues";
        }

        @Override
        protected CodeBlock getSingletonMethodBody(
                HelperMethodBuilder builder,
                Class<?> wrapperType,
                String wrapper,
                String key) {
            // Build the source helpers for all the other source types
            for (Map.Entry<WsValueSource, WsSourceHelper> entry : WS_SOURCE_HELPER_LOOKUP.entrySet()) {
                WsValueSource source = entry.getKey();
                if (source != WsValueSource.Any) {
                    WsSourceHelper helper = entry.getValue();
                    helper.buildSingletonHelper(builder, wrapperType);
                }
            }

            CodeBlock.Builder codeBuilder = CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", WsRequest.class, wrapper);
            for (Map.Entry<WsValueSource, WsSourceHelper> entry : WS_SOURCE_HELPER_LOOKUP.entrySet()) {
                WsValueSource source = entry.getKey();
                if (source != WsValueSource.Any) {
                    WsSourceHelper helper = entry.getValue();
                    CodeBlock check = helper.getPresenceCheck("request", key);
                    if (check != null) {
                        codeBuilder.beginControlFlow("if (" + check.toString() + ")");
                        codeBuilder.addStatement("return $N($N, $N)", helper.getSingletonName(), wrapper, key);
                        codeBuilder.endControlFlow();
                    }
                }
            }
            WsSourceHelper messageHelper = WS_SOURCE_HELPER_LOOKUP.get(WsValueSource.Message);
            codeBuilder.addStatement("return $N($N, $N)", messageHelper.getSingletonName(), wrapper, key);
            return codeBuilder.build();
        }

        @Override
        protected CodeBlock getArrayMethodBody(
                HelperMethodBuilder builder,
                Class<?> wrapperType,
                String wrapper,
                String key) {
            // Build the source helpers for all the other source types
            for (Map.Entry<WsValueSource, WsSourceHelper> entry : WS_SOURCE_HELPER_LOOKUP.entrySet()) {
                WsValueSource source = entry.getKey();
                if (source != WsValueSource.Any) {
                    WsSourceHelper helper = entry.getValue();
                    helper.buildArrayHelper(builder, wrapperType);
                }
            }

            CodeBlock.Builder codeBuilder = CodeBlock.builder()
                .addStatement("$T request = $N.getRequest()", WsRequest.class, wrapper);
            for (Map.Entry<WsValueSource, WsSourceHelper> entry : WS_SOURCE_HELPER_LOOKUP.entrySet()) {
                WsValueSource source = entry.getKey();
                if (source != WsValueSource.Any) {
                    WsSourceHelper helper = entry.getValue();
                    CodeBlock check = helper.getPresenceCheck(wrapper, key);
                    if (check != null) {
                        codeBuilder.beginControlFlow("if (" + check.toString() + ")");
                        codeBuilder.addStatement("return $N($N, $N)", helper.getArrayName(), wrapper, key);
                        codeBuilder.endControlFlow();
                    }
                }
            }
            WsSourceHelper messageHelper = WS_SOURCE_HELPER_LOOKUP.get(WsValueSource.Message);
            codeBuilder.addStatement("$N($N, $N)", messageHelper.getArrayName(), wrapper, key);
            return codeBuilder.build();
        }

        @Override
        public CodeBlock getPresenceCheck(String context, String key) {
            return null;
        }
    }

    // endregion
}
