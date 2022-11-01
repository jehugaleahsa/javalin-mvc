package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.truncon.javalin.mvc.api.DefaultValue;
import com.truncon.javalin.mvc.api.FromBody;
import com.truncon.javalin.mvc.api.FromCookie;
import com.truncon.javalin.mvc.api.FromForm;
import com.truncon.javalin.mvc.api.FromHeader;
import com.truncon.javalin.mvc.api.FromJson;
import com.truncon.javalin.mvc.api.FromPath;
import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.HttpContext;
import com.truncon.javalin.mvc.api.HttpRequest;
import com.truncon.javalin.mvc.api.Named;
import com.truncon.javalin.mvc.api.NoBinding;
import com.truncon.javalin.mvc.api.UseConverter;
import com.truncon.javalin.mvc.api.ValueSource;
import com.truncon.javalin.mvc.api.ws.FromBinary;
import com.truncon.javalin.mvc.api.ws.WsBinaryMessageContext;
import com.truncon.javalin.mvc.api.ws.WsContext;
import com.truncon.javalin.mvc.api.ws.WsMessageContext;
import com.truncon.javalin.mvc.api.ws.WsRequest;
import com.truncon.javalin.mvc.api.ws.WsValueSource;
import io.javalin.http.Context;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class HelperMethodBuilder {
    private static final Map<Class<?>, Function<Boolean, ConversionHelper>> CONVERSION_HELPER_LOOKUP = getConversionHelperLookup();
    private static final Map<Class<?>, Function<ConversionHelper, ConversionHelper>> COLLECTION_HELPER_LOOKUP = getCollectionHelperLookup();
    private static final Map<ValueSource, SourceHelper> SOURCE_HELPER_LOOKUP = getSourceHelperLookup();
    private static final Map<WsValueSource, WsSourceHelper> WS_SOURCE_HELPER_LOOKUP = getWsSourceHelperLookup();
    private static final String JSON_METHOD_NAME = "toJson";
    private static final String BINARY_BYTE_ARRAY_METHOD_NAME = "toBinaryByteArray";
    private static final String BINARY_BYTE_BUFFER_METHOD_NAME = "toBinaryByteBuffer";
    private static final String BINARY_INPUT_STREAM_METHOD_NAME = "toInputStream";

    private static Map<Class<?>, Function<Boolean, ConversionHelper>> getConversionHelperLookup() {
        Map<Class<?>, Function<Boolean, ConversionHelper>> lookup = new HashMap<>();
        lookup.put(byte.class, PrimitiveByteHelper::new);
        lookup.put(short.class, PrimitiveShortHelper::new);
        lookup.put(int.class, PrimitiveIntegerHelper::new);
        lookup.put(long.class, PrimitiveLongHelper::new);
        lookup.put(float.class, PrimitiveFloatHelper::new);
        lookup.put(double.class, PrimitiveDoubleHelper::new);
        lookup.put(boolean.class, PrimitiveBooleanHelper::new);
        lookup.put(char.class, PrimitiveCharacterHelper::new);
        lookup.put(Byte.class, BoxedByteHelper::new);
        lookup.put(Short.class, BoxedShortHelper::new);
        lookup.put(Integer.class, BoxedIntegerHelper::new);
        lookup.put(Long.class, BoxedLongHelper::new);
        lookup.put(Float.class, BoxedFloatHelper::new);
        lookup.put(Double.class, BoxedDoubleHelper::new);
        lookup.put(Boolean.class, BoxedBooleanHelper::new);
        lookup.put(Character.class, BoxedCharacterHelper::new);
        lookup.put(String.class, StringHelper::new);
        lookup.put(Date.class, DateHelper::new);
        lookup.put(Instant.class, InstantHelper::new);
        lookup.put(ZonedDateTime.class, ZonedDateTimeHelper::new);
        lookup.put(OffsetDateTime.class, OffsetDateTimeHelper::new);
        lookup.put(LocalDateTime.class, LocalDateTimeHelper::new);
        lookup.put(LocalDate.class, LocalDateHelper::new);
        lookup.put(YearMonth.class, YearMonthHelper::new);
        lookup.put(Year.class, YearHelper::new);
        lookup.put(BigInteger.class, BigIntegerHelper::new);
        lookup.put(BigDecimal.class, BigDecimalHelper::new);
        lookup.put(UUID.class, UUIDHelper::new);
        return lookup;
    }

    private static Map<Class<?>, Function<ConversionHelper, ConversionHelper>> getCollectionHelperLookup() {
        Map<Class<?>, Function<ConversionHelper, ConversionHelper>> lookup = new HashMap<>();
        lookup.put(Iterable.class, IterableHelper::new);
        lookup.put(Collection.class, CollectionHelper::new);
        lookup.put(List.class, ListHelper::new);
        lookup.put(Set.class, SetHelper::new);
        lookup.put(SortedSet.class, SortedSetHelper::new);
        lookup.put(ArrayList.class, ArrayListHelper::new);
        lookup.put(LinkedList.class, LinkedListHelper::new);
        lookup.put(HashSet.class, HashSetHelper::new);
        lookup.put(LinkedHashSet.class, LinkedHashSetHelper::new);
        lookup.put(TreeSet.class, TreeSetHelper::new);
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

    private static Map<WsValueSource, WsSourceHelper> getWsSourceHelperLookup() {
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
    private final Set<String> addedCollectionConversionHelpers = new HashSet<>();
    private final Set<ScalarSourceKey> addedScalarSourceHelpers = new HashSet<>();
    private final Set<CollectionSourceKey> addedCollectionSourceHelpers = new HashSet<>();
    private final Set<String> addedFields = new HashSet<>();
    private final Set<WsScalarSourceKey> addedWsScalarSourceHelpers = new HashSet<>();
    private final Set<WsCollectionSourceKey> addedCollectionWsSourceHelpers = new HashSet<>();
    private final Map<ConversionTypeKey, String> complexConversionLookup = new HashMap<>();
    private final Map<WsConversionTypeKey, String> complexWsConversionLookup = new HashMap<>();
    private final Map<String, Integer> complexConversionCounts = new HashMap<>();
    private final Set<Class<?>> jsonMethods = new HashSet<>();
    private final Set<Class<?>> binaryMethods = new HashSet<>();

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

    public ConversionHelper getConversionHelper(TypeMirror parameterType) {
        TypeUtils typeUtils = container.getTypeUtils();

        // Handle the parameter being an array type
        if (parameterType.getKind() == TypeKind.ARRAY) {
            TypeMirror componentType = typeUtils.getArrayComponentType(parameterType);
            for (Map.Entry<Class<?>, Function<Boolean, ConversionHelper>> entry : HelperMethodBuilder.CONVERSION_HELPER_LOOKUP.entrySet()) {
                Class<?> parameterClass = entry.getKey();
                if (typeUtils.isSameType(componentType, parameterClass)) {
                    Function<Boolean, ConversionHelper> factory = entry.getValue();
                    return factory.apply(true);
                }
            }
            return null;
        }

        // Handle the parameter being a collection type
        TypeMirror componentType = typeUtils.getCollectionComponentType(parameterType);
        if (componentType != null) {
            // We force array here, so we get the collection-oriented conversion helper
            ConversionHelper componentHelper = getConversionHelper(componentType);
            return componentHelper == null ? null : getCollectionHelper(parameterType, componentHelper);
        }

        // Handle the parameter being a scalar type
        for (Map.Entry<Class<?>, Function<Boolean, ConversionHelper>> entry : HelperMethodBuilder.CONVERSION_HELPER_LOOKUP.entrySet()) {
            Class<?> parameterClass = entry.getKey();
            if (typeUtils.isSameType(parameterType, parameterClass)) {
                Function<Boolean, ConversionHelper> factory = entry.getValue();
                return factory.apply(false);
            }
        }

        return null;
    }

    private ConversionHelper getCollectionHelper(TypeMirror collectionType, ConversionHelper componentHelper) {
        TypeUtils typeUtils = container.getTypeUtils();
        TypeMirror erased = typeUtils.erasure(collectionType);
        for (Map.Entry<Class<?>, Function<ConversionHelper, ConversionHelper>> entry : COLLECTION_HELPER_LOOKUP.entrySet()) {
            TypeMirror entryType = typeUtils.erasure(typeUtils.toType(entry.getKey()));
            if (typeUtils.isSameType(erased, entryType)) {
                Function<ConversionHelper, ConversionHelper> factory = entry.getValue();
                return factory.apply(componentHelper);
            }
        }
        return null;
    }

    private ConversionHelper getConversionHelper(Element memberElement) {
        return getConversionHelper(getParameterType(memberElement));
    }

    private static TypeMirror getParameterType(Element memberElement) {
        if (memberElement.getKind() == ElementKind.FIELD) {
            return memberElement.asType();
        } else {
            ExecutableElement method = (ExecutableElement) memberElement;
            VariableElement parameter = method.getParameters().get(0); // We already checked it's a setter
            return parameter.asType();
        }
    }

    public static ValueSource getDefaultFromBinding(Element element) {
        if (element.getAnnotation(FromPath.class) != null) {
            return ValueSource.Path;
        }
        if (element.getAnnotation(jakarta.ws.rs.PathParam.class) != null) {
            return ValueSource.Path;
        }
        if (element.getAnnotation(FromQuery.class) != null) {
            return ValueSource.QueryString;
        }
        if (element.getAnnotation(jakarta.ws.rs.QueryParam.class) != null) {
            return ValueSource.QueryString;
        }
        if (element.getAnnotation(FromHeader.class) != null) {
            return ValueSource.Header;
        }
        if (element.getAnnotation(jakarta.ws.rs.HeaderParam.class) != null) {
            return ValueSource.Header;
        }
        if (element.getAnnotation(FromCookie.class) != null) {
            return ValueSource.Cookie;
        }
        if (element.getAnnotation(jakarta.ws.rs.CookieParam.class) != null) {
            return ValueSource.Cookie;
        }
        if (element.getAnnotation(FromForm.class) != null) {
            return ValueSource.FormData;
        }
        if (element.getAnnotation(jakarta.ws.rs.FormParam.class) != null) {
            return ValueSource.FormData;
        }
        return ValueSource.Any;
    }

    public ConversionMethodResult addConversionMethod(TypeElement element, ValueSource defaultSource) {
        // We prevent infinite recursion by making sure we don't try to recursively
        // bind sub-members of the same type.
        Set<String> visitedTypes = new HashSet<>();
        visitedTypes.add(element.getQualifiedName().toString());
        return addConversionMethodInternal(element, defaultSource, visitedTypes);
    }

    private ConversionMethodResult addConversionMethodInternal(
            TypeElement element,
            ValueSource defaultSource,
            Set<String> visitedTypes) {
        ConversionTypeKey key = ConversionTypeKey.of(defaultSource, element);
        String methodName = complexConversionLookup.get(key);
        if (methodName != null) {
            return new ConversionMethodResult(methodName, false);
        }
        CodeBlock.Builder methodBodyBuilder = CodeBlock.builder();
        boolean injectorNeeded = addModelBuilderCreation(methodBodyBuilder, element);

        Collection<Element> memberElements = getBoundMemberElements(
            element,
            e -> defaultSource != ValueSource.Any || hasFromAnnotation(e) || hasMemberBinding(e)
        ).collect(Collectors.toList());
        for (Element memberElement : memberElements) {
            if (hasAnnotation(memberElement, NoBinding.class)) {
                continue;
            }
            ConverterSetterResult setterResult = addConverterSetter(memberElement, methodBodyBuilder, defaultSource);
            if (setterResult.isCalled()) {
                injectorNeeded |= setterResult.isInjectorNeeded();
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
                if (subElement != null && !visitedTypes.contains(subElement.getQualifiedName().toString())) {
                    visitedTypes.add(subElement.getQualifiedName().toString());
                    ConversionMethodResult methodResult = addConversionMethodInternal(subElement, defaultSource, visitedTypes);
                    CodeBlock value = methodResult.isInjectorNeeded()
                        ? CodeBlock.builder().add("$N($N, $N)", methodResult.getMethod(), "context", "injector").build()
                        : CodeBlock.builder().add("$N($N)", methodResult.getMethod(), "context").build();
                    setMember(memberElement, methodBodyBuilder, value.toString());
                    injectorNeeded |= methodResult.isInjectorNeeded();
                }
            }
        }
        methodBodyBuilder.addStatement("return model");

        String simpleName = element.getSimpleName().toString();
        int count = complexConversionCounts.getOrDefault(simpleName, 0);
        methodName = "to" + simpleName + (count + 1);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PRIVATE)
            .addModifiers(Modifier.STATIC)
            .returns(TypeName.get(element.asType()))
            .addParameter(HttpContext.class, "context")
            .addCode(methodBodyBuilder.build());
        if (injectorNeeded) {
            methodBuilder.addParameter(
                TypeName.get(container.getInjectorType()),
                "injector");
        }
        typeBuilder.addMethod(methodBuilder.build());
        complexConversionLookup.put(key, methodName);
        complexConversionCounts.put(simpleName, count + 1);
        return new ConversionMethodResult(methodName, injectorNeeded);
    }

    private boolean addModelBuilderCreation(CodeBlock.Builder methodBodyBuilder, TypeElement element) {
        InjectionResult result = container.getInstanceCall(element.asType(), "injector");
        methodBodyBuilder.addStatement("$T model = $L", element.asType(), result.getInstanceCall());
        return result.isInjectorNeeded();
    }

    private boolean isValidBindTarget(Element memberElement) {
        if (memberElement.getKind() == ElementKind.FIELD) {
            return memberElement.getModifiers().contains(Modifier.PUBLIC)
                && !memberElement.getModifiers().contains(Modifier.STATIC);
        } else {
            return isSetter(memberElement);
        }
    }

    private ConverterSetterResult addConverterSetter(
            Element memberElement,
            CodeBlock.Builder methodBodyBuilder,
            ValueSource defaultSource) {
        String converterName = getConverterName(memberElement);
        if (converterName == null) {
            return new ConverterSetterResult(false, false);
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
        ValueSource valueSource = getValueSource(memberElement, defaultSource);
        if (converter.hasContextOrRequestType(HttpContext.class)) {
            ConvertCallResult result = converter.getConverterCall(container, "context", memberName, "injector", valueSource);
            setMember(memberElement, methodBodyBuilder, result.getCall());
            return new ConverterSetterResult(true, result.isInjectorNeeded());
        } else if (converter.hasContextOrRequestType(HttpRequest.class)) {
            String requestName = "context.getRequest()";
            ConvertCallResult result = converter.getConverterCall(container, requestName, memberName, "injector", valueSource);
            setMember(memberElement, methodBodyBuilder, result.getCall());
            return new ConverterSetterResult(true, result.isInjectorNeeded());
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
        if (!hasFromBodyAnnotation(memberElement)) {
            return false;
        }
        String jsonMethod = addJsonMethod();
        String valueExpression = CodeBlock
            .of("$N($N, $T.class)", jsonMethod, "context", getParameterType(memberElement))
            .toString();
        setMember(memberElement, methodBodyBuilder, valueExpression);
        return true;
    }

    private static boolean hasFromBodyAnnotation(Element element) {
        //noinspection deprecation
        FromJson json = element.getAnnotation(FromJson.class);
        if (json != null) {
            return true;
        }
        FromBody body = element.getAnnotation(FromBody.class);
        if (body != null) {
            return true;
        }
        if (element.getKind() == ElementKind.METHOD) {
            ExecutableElement method = (ExecutableElement) element;
            VariableElement parameter = method.getParameters().get(0); // We already checked there's one parameter
            return hasFromBodyAnnotation(parameter);
        }
        return false;
    }

    private boolean addPrimitiveSetter(
            Element memberElement,
            CodeBlock.Builder methodBodyBuilder,
            ValueSource defaultSource) {
        ConversionHelper conversionHelper = getConversionHelper(memberElement);
        if (conversionHelper == null) {
            return false;
        }
        String memberName = getMemberName(memberElement);
        if (memberName == null) {
            return false;
        }
        conversionHelper.addConversionMethod(this);
        ValueSource valueSource = getValueSource(memberElement, defaultSource);
        String defaultValue = getDefaultValue(memberElement);
        String sourceMethod = addSourceMethod(valueSource, conversionHelper.isCollectionType(), defaultValue);
        String valueExpression = getValueExpression(conversionHelper, sourceMethod, memberName, defaultValue);
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

    private String getValueExpression(
            ConversionHelper conversionHelper,
            String sourceMethod,
            String key,
            String defaultValue) {
        String sourceCall = defaultValue == null
            ? CodeBlock.of("$N($N, $S)", sourceMethod, "context", key).toString()
            : CodeBlock.of("$N($N, $S, $S)", sourceMethod, "context", key, defaultValue).toString();
        return conversionHelper.getConversionCall(sourceCall);
    }

    public static ValueSource getValueSource(Element memberElement, ValueSource defaultSource) {
        if (memberElement.getAnnotation(FromHeader.class) != null) {
            return ValueSource.Header;
        }
        if (memberElement.getAnnotation(jakarta.ws.rs.HeaderParam.class) != null) {
            return ValueSource.Header;
        }
        if (memberElement.getAnnotation(FromCookie.class) != null) {
            return ValueSource.Cookie;
        }
        if (memberElement.getAnnotation(jakarta.ws.rs.CookieParam.class) != null) {
            return ValueSource.Cookie;
        }
        if (memberElement.getAnnotation(FromPath.class) != null) {
            return ValueSource.Path;
        }
        if (memberElement.getAnnotation(jakarta.ws.rs.PathParam.class) != null) {
            return ValueSource.Path;
        }
        if (memberElement.getAnnotation(FromQuery.class) != null) {
            return ValueSource.QueryString;
        }
        if (memberElement.getAnnotation(jakarta.ws.rs.QueryParam.class) != null) {
            return ValueSource.QueryString;
        }
        if (memberElement.getAnnotation(FromForm.class) != null) {
            return ValueSource.FormData;
        }
        if (memberElement.getAnnotation(jakarta.ws.rs.FormParam.class) != null) {
            return ValueSource.FormData;
        }
        return defaultSource;
    }

    private static String getMemberName(Element memberElement) {
        String specifiedName = getSpecifiedName(memberElement);
        if (specifiedName != null) {
            return specifiedName;
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

    public static String getSpecifiedName(Element element) {
        String builtinName = getNameFromBuiltinAnnotations(element);
        if (builtinName != null) {
            return builtinName;
        }
        String standardName = getNameFromStandardAnnotations(element);
        if (standardName != null) {
            return standardName;
        }

        Named named = element.getAnnotation(Named.class);
        return named == null ? null : StringUtils.stripToNull(named.value());
    }

    private static String getNameFromBuiltinAnnotations(Element element) {
        FromCookie cookie = element.getAnnotation(FromCookie.class);
        String cookieName = cookie == null ? null : StringUtils.stripToNull(cookie.value());
        if (cookieName != null) {
            return cookieName;
        }
        FromForm form = element.getAnnotation(FromForm.class);
        String formName = form == null ? null : StringUtils.stripToNull(form.value());
        if (formName != null) {
            return formName;
        }
        FromHeader header = element.getAnnotation(FromHeader.class);
        String headerName = header == null ? null : StringUtils.stripToNull(header.value());
        if (headerName != null) {
            return headerName;
        }
        FromPath path = element.getAnnotation(FromPath.class);
        String pathName = path == null ? null : StringUtils.stripToNull(path.value());
        if (pathName != null) {
            return pathName;
        }
        FromQuery query = element.getAnnotation(FromQuery.class);
        return query == null ? null : StringUtils.stripToNull(query.value());
    }

    private static String getNameFromStandardAnnotations(Element element) {
        jakarta.ws.rs.CookieParam cookie = element.getAnnotation(jakarta.ws.rs.CookieParam.class);
        String cookieName = cookie == null ? null : StringUtils.stripToNull(cookie.value());
        if (cookieName != null) {
            return cookieName;
        }
        jakarta.ws.rs.FormParam form = element.getAnnotation(jakarta.ws.rs.FormParam.class);
        String formName = form == null ? null : StringUtils.stripToNull(form.value());
        if (formName != null) {
            return formName;
        }
        jakarta.ws.rs.HeaderParam header = element.getAnnotation(jakarta.ws.rs.HeaderParam.class);
        String headerName = header == null ? null : StringUtils.stripToNull(header.value());
        if (headerName != null) {
            return headerName;
        }
        jakarta.ws.rs.PathParam path = element.getAnnotation(jakarta.ws.rs.PathParam.class);
        String pathName = path == null ? null : StringUtils.stripToNull(path.value());
        if (pathName != null) {
            return pathName;
        }
        jakarta.ws.rs.QueryParam query = element.getAnnotation(jakarta.ws.rs.QueryParam.class);
        return query == null ? null : StringUtils.stripToNull(query.value());
    }

    public boolean hasMemberBinding(Element element) {
        return hasMemberBinding(element, this::hasFromAnnotation);
    }

    private boolean hasMemberBinding(Element element, Function<Element, Boolean> hasAnnotation) {
        if (hasAnnotation(element, NoBinding.class)) {
            return false;
        }
        if (element.getKind() == ElementKind.PARAMETER) {
            TypeElement typeElement = container.getTypeUtils().getTypeElement(element.asType());
            return hasMemberBinding(typeElement, hasAnnotation);
        } else if (element.getKind() == ElementKind.FIELD) {
            TypeElement typeElement = container.getTypeUtils().getTypeElement(element.asType());
            return hasMemberBinding(typeElement, hasAnnotation);
        } else if (isSetter(element)) {
            ExecutableElement method = (ExecutableElement) element;
            VariableElement parameterElement = method.getParameters().get(0);
            TypeElement typeElement = container.getTypeUtils().getTypeElement(parameterElement.asType());
            return hasMemberBinding(typeElement, hasAnnotation);
        }
        return false;
    }

    private boolean hasMemberBinding(TypeElement element, Function<Element, Boolean> hasAnnotation) {
        if (element == null) {
            return false;
        }
        boolean hasBinding = element.getEnclosedElements().stream()
            .filter(e -> e.getKind() == ElementKind.FIELD || isSetter(e))
            .filter(e -> e.getModifiers().contains(Modifier.PUBLIC))
            .filter(e -> !e.getModifiers().contains(Modifier.STATIC))
            .anyMatch(hasAnnotation::apply);
        if (hasBinding) {
            return true;
        }
        TypeMirror superType = element.getSuperclass();
        if (superType.getKind() == TypeKind.NONE || container.getTypeUtils().isSameType(superType, Object.class)) {
            return false;
        }
        TypeElement superTypeElement = container.getTypeUtils().getTypeElement(superType);
        return hasMemberBinding(superTypeElement, hasAnnotation);
    }

    public boolean hasFromAnnotation(Element element) {
        if (hasAnnotation(element, NoBinding.class)) {
            return false;
        }
        //noinspection deprecation
        return hasAnnotation(element, FromPath.class)
            || hasAnnotation(element, jakarta.ws.rs.PathParam.class)
            || hasAnnotation(element, FromQuery.class)
            || hasAnnotation(element, jakarta.ws.rs.QueryParam.class)
            || hasAnnotation(element, FromHeader.class)
            || hasAnnotation(element, jakarta.ws.rs.HeaderParam.class)
            || hasAnnotation(element, FromCookie.class)
            || hasAnnotation(element, jakarta.ws.rs.CookieParam.class)
            || hasAnnotation(element, FromForm.class)
            || hasAnnotation(element, jakarta.ws.rs.FormParam.class)
            || hasAnnotation(element, FromBody.class)
            || hasAnnotation(element, FromJson.class);
    }

    public static String getDefaultValue(Element parameter) {
        DefaultValue builtin = parameter.getAnnotation(DefaultValue.class);
        if (builtin != null) {
            return builtin.value();
        }
        jakarta.ws.rs.DefaultValue standard = parameter.getAnnotation(jakarta.ws.rs.DefaultValue.class);
        if (standard != null) {
            return standard.value();
        }
        return null;
    }

    public static WsValueSource getDefaultWsFromBinding(Element element) {
        if (element.getAnnotation(FromHeader.class) != null) {
            return WsValueSource.Header;
        }
        if (element.getAnnotation(jakarta.ws.rs.HeaderParam.class) != null) {
            return WsValueSource.Header;
        }
        if (element.getAnnotation(FromCookie.class) != null) {
            return WsValueSource.Cookie;
        }
        if (element.getAnnotation(jakarta.ws.rs.CookieParam.class) != null) {
            return WsValueSource.Cookie;
        }
        if (element.getAnnotation(FromPath.class) != null) {
            return WsValueSource.Path;
        }
        if (element.getAnnotation(jakarta.ws.rs.PathParam.class) != null) {
            return WsValueSource.Path;
        }
        if (element.getAnnotation(FromQuery.class) != null) {
            return WsValueSource.QueryString;
        }
        if (element.getAnnotation(jakarta.ws.rs.QueryParam.class) != null) {
            return WsValueSource.QueryString;
        }
        return WsValueSource.Any;
    }

    public ConversionMethodResult addConversionMethod(
            TypeElement element,
            WsValueSource defaultSource,
            Class<? extends WsContext> contextType) {
        // We prevent infinite recursion by making sure we don't try to recursively
        // bind sub-members of the same type.
        Set<String> visitedTypes = new HashSet<>();
        visitedTypes.add(element.getQualifiedName().toString());
        return addConversionMethodInternal(element, defaultSource, contextType, visitedTypes);
    }

    private ConversionMethodResult addConversionMethodInternal(
            TypeElement element,
            WsValueSource defaultSource,
            Class<? extends WsContext> contextType,
            Set<String> visitedTypes) {
        WsConversionTypeKey key = WsConversionTypeKey.of(defaultSource, element, contextType);
        String methodName = complexWsConversionLookup.get(key);
        if (methodName != null) {
            return new ConversionMethodResult(methodName, false);
        }

        CodeBlock.Builder methodBodyBuilder = CodeBlock.builder();
        InjectionResult result = container.getInstanceCall(element.asType(), "injector");
        boolean injectorNeeded = result.isInjectorNeeded();
        methodBodyBuilder.addStatement("$T model = $L", element, result.getInstanceCall());
        Collection<Element> memberElements = getBoundMemberElements(
            element,
            e -> defaultSource != WsValueSource.Any || hasWsFromAnnotation(e) || hasWsMemberBinding(e)
        ).collect(Collectors.toList());
        for (Element memberElement : memberElements) {
            if (hasAnnotation(memberElement, NoBinding.class)) {
                continue;
            }
            ConverterSetterResult setterResult = addConverterSetter(
                memberElement, methodBodyBuilder, contextType, defaultSource);
            if (setterResult.isCalled()) {
                injectorNeeded |= setterResult.isInjectorNeeded();
                continue;
            }
            if (addJsonSetter(memberElement, methodBodyBuilder, contextType)) {
                continue;
            }
            if (addBinarySetter(memberElement, methodBodyBuilder, contextType, defaultSource)) {
                continue;
            }
            if (addPrimitiveSetter(memberElement, methodBodyBuilder, defaultSource, contextType)) {
                continue;
            }

            WsValueSource defaultSubSource = getDefaultWsFromBinding(memberElement);
            if (defaultSubSource != WsValueSource.Any || hasWsMemberBinding(memberElement)) {
                TypeElement subElement = container.getTypeUtils().getTypeElement(getParameterType(memberElement));
                if (!visitedTypes.contains(subElement.getQualifiedName().toString())) {
                    visitedTypes.add(subElement.getQualifiedName().toString());
                    ConversionMethodResult methodResult = addConversionMethodInternal(subElement, defaultSource, contextType, visitedTypes);
                    String valueExpression = CodeBlock.builder()
                        .add("$N($N)", methodResult.getMethod(), "context")
                        .build()
                        .toString();
                    setMember(memberElement, methodBodyBuilder, valueExpression);
                    injectorNeeded |= methodResult.isInjectorNeeded();
                }
            }
        }
        methodBodyBuilder.addStatement("return model");

        String simpleName = element.getSimpleName().toString();
        int count = complexConversionCounts.getOrDefault(simpleName, 0);
        methodName = "to" + simpleName + (count + 1);
        MethodSpec method = MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
            .returns(TypeName.get(element.asType()))
            .addParameter(contextType, "context")
            .addCode(methodBodyBuilder.build())
            .build();
        typeBuilder.addMethod(method);
        complexWsConversionLookup.put(key, methodName);
        complexConversionCounts.put(simpleName, count + 1);

        return new ConversionMethodResult(methodName, injectorNeeded);
    }

    private ConverterSetterResult addConverterSetter(
            Element memberElement,
            CodeBlock.Builder methodBodyBuilder,
            Class<? extends WsContext> contextType,
            WsValueSource defaultSource) {
        String converterName = getConverterName(memberElement);
        if (converterName == null) {
            return new ConverterSetterResult(false, false);
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
        WsValueSource valueSource = getValueSource(memberElement, defaultSource);
        if (converter.hasContextOrRequestType(WsContext.class) || converter.hasContextOrRequestType(contextType)) {
            ConvertCallResult result = converter.getConverterCall(container, "context", memberName, "injector", valueSource);
            setMember(memberElement, methodBodyBuilder, result.getCall());
            return new ConverterSetterResult(true, result.isInjectorNeeded());
        } else if (converter.hasContextOrRequestType(WsRequest.class)) {
            String requestName = "context.getRequest()";
            ConvertCallResult result = converter.getConverterCall(container, requestName, memberName, "injector", valueSource);
            setMember(memberElement, methodBodyBuilder, result.getCall());
            return new ConverterSetterResult(true, result.isInjectorNeeded());
        } else {
            String message = "The conversion method '"
                + converterName
                + "' cannot be used with WebSocket action methods.";
            throw new ProcessingException(message, memberElement);
        }
    }

    private boolean addJsonSetter(Element memberElement, CodeBlock.Builder methodBodyBuilder, Class<? extends WsContext> contextType) {
        if (!hasFromBodyAnnotation(memberElement)) {
            return false;
        }
        if (contextType != WsMessageContext.class) {
            return false;
        }
        String jsonMethod = addWsJsonMethod();
        String valueExpression = CodeBlock
            .of("$N($N, $T.class)", jsonMethod, "context", getParameterType(memberElement))
            .toString();
        setMember(memberElement, methodBodyBuilder, valueExpression);
        return true;
    }

    private boolean addBinarySetter(
            Element memberElement,
            CodeBlock.Builder methodBodyBuilder,
            Class<? extends WsContext> contextType,
            WsValueSource defaultSource) {
        // Binary messages only
        if (contextType != WsBinaryMessageContext.class) {
            return false;
        }
        // The parameter type must be byte[] or ByteBuffer
        TypeUtils typeUtils = container.getTypeUtils();
        TypeMirror parameterType = getParameterType(memberElement);
        if (!typeUtils.isSameType(parameterType, byte[].class) && !typeUtils.isSameType(parameterType, ByteBuffer.class)) {
            return false;
        }
        // We only bind from the binary message if there's an explicit FromBinary annotation or
        // there's no other default/explicit source specified.
        //noinspection deprecation
        boolean hasBinaryAnnotation = hasAnnotation(memberElement, FromBinary.class)
            || hasAnnotation(memberElement, FromBody.class);
        if (defaultSource != WsValueSource.Any || !hasBinaryAnnotation) {
            return false;
        }
        // At this point, we know we're dealing with a binary message handler, the parameter type is a byte[] or
        // ByteBuffer, and there's either an explicit FromBinary annotation or there's no alternative binding.
        String binaryMethod = addWsBinaryMethod(parameterType);
        if (binaryMethod != null) {
            String valueExpression = CodeBlock.of("$N(context)", binaryMethod).toString();
            setMember(memberElement, methodBodyBuilder, valueExpression);
            return true;
        }
        return false;
    }

    private static boolean hasAnnotation(Element element, Class<? extends Annotation> annotationClass) {
        if (element.getAnnotation(annotationClass) != null) {
            return true;
        }
        if (isSetter(element)) {
            ExecutableElement method = (ExecutableElement) element;
            VariableElement parameterElement = method.getParameters().get(0);
            return parameterElement.getAnnotation(annotationClass) != null;
        }
        return false;
    }

    private Stream<? extends Element> getBoundMemberElements(TypeElement element, Function<Element, Boolean> hasBinding) {
        Stream<? extends Element> currentMembers = element.getEnclosedElements().stream()
            .filter(e -> e.getKind() == ElementKind.FIELD || e.getKind() == ElementKind.METHOD)
            .filter(hasBinding::apply)
            .filter(this::isValidBindTarget);
        TypeMirror superType = element.getSuperclass();
        if (superType.getKind() == TypeKind.NONE || container.getTypeUtils().isSameType(superType, Object.class)) {
            return currentMembers;
        }
        TypeElement superTypeElement = container.getTypeUtils().getTypeElement(superType);
        Stream<? extends Element> baseMembers = getBoundMemberElements(superTypeElement, hasBinding);
        return Stream.concat(currentMembers, baseMembers);
    }

    public boolean hasWsFromAnnotation(Element element) {
        if (hasAnnotation(element, NoBinding.class)) {
            return false;
        }
        //noinspection deprecation
        return hasAnnotation(element, FromPath.class)
            || hasAnnotation(element, jakarta.ws.rs.PathParam.class)
            || hasAnnotation(element, FromQuery.class)
            || hasAnnotation(element, jakarta.ws.rs.QueryParam.class)
            || hasAnnotation(element, FromHeader.class)
            || hasAnnotation(element, jakarta.ws.rs.HeaderParam.class)
            || hasAnnotation(element, FromCookie.class)
            || hasAnnotation(element, jakarta.ws.rs.CookieParam.class)
            || hasAnnotation(element, FromBody.class)
            || hasAnnotation(element, FromJson.class)
            || hasAnnotation(element, FromBinary.class);
    }

    private boolean addPrimitiveSetter(
            Element memberElement,
            CodeBlock.Builder methodBodyBuilder,
            WsValueSource defaultSource,
            Class<? extends WsContext> contextType) {
        ConversionHelper conversionHelper = getConversionHelper(memberElement);
        if (conversionHelper == null) {
            return false;
        }
        String memberName = getMemberName(memberElement);
        if (memberName == null) {
            return false;
        }
        conversionHelper.addConversionMethod(this);
        WsValueSource valueSource = getValueSource(memberElement, defaultSource);
        String defaultValue = getDefaultValue(memberElement);
        String sourceMethod = addSourceMethod(valueSource, contextType, conversionHelper.isCollectionType(), defaultValue);
        String valueExpression = getValueExpression(conversionHelper, sourceMethod, memberName, defaultValue);
        return setMember(memberElement, methodBodyBuilder, valueExpression);
    }

    private static WsValueSource getValueSource(Element memberElement, WsValueSource defaultSource) {
        if (memberElement.getAnnotation(FromHeader.class) != null) {
            return WsValueSource.Header;
        }
        if (memberElement.getAnnotation(jakarta.ws.rs.HeaderParam.class) != null) {
            return WsValueSource.Header;
        }
        if (memberElement.getAnnotation(FromCookie.class) != null) {
            return WsValueSource.Cookie;
        }
        if (memberElement.getAnnotation(jakarta.ws.rs.CookieParam.class) != null) {
            return WsValueSource.Cookie;
        }
        if (memberElement.getAnnotation(FromPath.class) != null) {
            return WsValueSource.Path;
        }
        if (memberElement.getAnnotation(jakarta.ws.rs.PathParam.class) != null) {
            return WsValueSource.Path;
        }
        if (memberElement.getAnnotation(FromQuery.class) != null) {
            return WsValueSource.QueryString;
        }
        if (memberElement.getAnnotation(jakarta.ws.rs.QueryParam.class) != null) {
            return WsValueSource.QueryString;
        }
        return defaultSource;
    }

    public boolean hasWsMemberBinding(Element element) {
        return hasMemberBinding(element, this::hasWsFromAnnotation);
    }

    public String addSourceMethod(ValueSource valueSource, boolean isCollection, String defaultValue) {
        SourceHelper sourceHelper = SOURCE_HELPER_LOOKUP.get(valueSource);
        if (isCollection) {
            sourceHelper.buildCollectionHelper(this, defaultValue);
            return sourceHelper.getCollectionName();
        } else {
            sourceHelper.buildScalarHelper(this, defaultValue);
            return sourceHelper.getScalarName();
        }
    }

    public String addSourceMethod(
            WsValueSource valueSource,
            Class<? extends WsContext> wrapperType,
            boolean isCollection,
            String defaultValue) {
        // TODO - Check that the wrapper type and value source make sense together
        WsSourceHelper sourceHelper = WS_SOURCE_HELPER_LOOKUP.get(valueSource);
        if (isCollection) {
            sourceHelper.buildCollectionHelper(this, wrapperType, defaultValue);
            return sourceHelper.getCollectionName();
        } else {
            sourceHelper.buildScalarHelper(this, wrapperType, defaultValue);
            return sourceHelper.getScalarName();
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
            .addParameter(HttpContext.class, "context")
            .addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), typeArgument), "type")
            .addCode(CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("$T request = context.getRequest()", HttpRequest.class)
                .addStatement("return request.getBodyFromJson(type)")
                .nextControlFlow("catch (Exception exception)")
                .addStatement("return null")
                .endControlFlow()
                .build()
            )
            .build();
        typeBuilder.addMethod(method);
        jsonMethods.add(HttpContext.class);
        return JSON_METHOD_NAME;
    }

    public String addWsJsonMethod() {
        if (jsonMethods.contains(WsMessageContext.class)) {
            return JSON_METHOD_NAME;
        }
        TypeVariableName typeArgument = TypeVariableName.get("T");
        MethodSpec method = MethodSpec.methodBuilder(JSON_METHOD_NAME)
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
            .addTypeVariable(typeArgument)
            .returns(typeArgument)
            .addParameter(WsMessageContext.class, "context")
            .addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), typeArgument), "type")
            .addCode(CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return context.getMessage(type)")
                .nextControlFlow("catch (Exception exception)")
                .addStatement("return null")
                .endControlFlow()
                .build()
            )
            .build();
        typeBuilder.addMethod(method);
        jsonMethods.add(WsMessageContext.class);
        return JSON_METHOD_NAME;
    }

    public String addWsBinaryMethod(TypeMirror parameterType) {
        if (container.getTypeUtils().isSameType(parameterType, byte[].class)) {
            return addWsByteArrayBinaryMethod();
        } else if (container.getTypeUtils().isSameType(parameterType, ByteBuffer.class)) {
            return addWsByteBufferBinaryMethod();
        } else if (container.getTypeUtils().isSameType(parameterType, InputStream.class)) {
            return addWsInputStreamMethod();
        } else {
            return null;
        }
    }

    private String addWsByteArrayBinaryMethod() {
        if (binaryMethods.contains(byte[].class)) {
            return BINARY_BYTE_ARRAY_METHOD_NAME;
        }
        MethodSpec method = MethodSpec.methodBuilder(BINARY_BYTE_ARRAY_METHOD_NAME)
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
            .returns(byte[].class)
            .addParameter(WsBinaryMessageContext.class, "context")
            .addCode(CodeBlock.builder()
                .addStatement("$T data = context.getData()", byte[].class)
                .addStatement("$T offset = context.getOffset()", int.class)
                .addStatement("$T length = context.getLength()", int.class)
                .beginControlFlow("if (offset == 0 && length == data.length)")
                .addStatement("return data")
                .endControlFlow()
                .addStatement("$T[] result = new $T[length]", byte.class, byte.class)
                .addStatement("$T.arraycopy(data, offset, result, 0, length)", System.class)
                .addStatement("return result")
                .build()
            )
            .build();
        typeBuilder.addMethod(method);
        binaryMethods.add(byte[].class);
        return BINARY_BYTE_ARRAY_METHOD_NAME;
    }

    private String addWsByteBufferBinaryMethod() {
        if (binaryMethods.contains(ByteBuffer.class)) {
            return BINARY_BYTE_BUFFER_METHOD_NAME;
        }
        MethodSpec method = MethodSpec.methodBuilder(BINARY_BYTE_BUFFER_METHOD_NAME)
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
            .returns(ByteBuffer.class)
            .addParameter(WsBinaryMessageContext.class, "context")
            .addCode(CodeBlock.builder()
                .addStatement("return $T.wrap(context.getData(), context.getOffset(), context.getLength())", ByteBuffer.class)
                .build()
            )
            .build();
        typeBuilder.addMethod(method);
        binaryMethods.add(ByteBuffer.class);
        return BINARY_BYTE_BUFFER_METHOD_NAME;
    }

    private String addWsInputStreamMethod() {
        if (binaryMethods.contains(InputStream.class)) {
            return BINARY_INPUT_STREAM_METHOD_NAME;
        }
        MethodSpec method = MethodSpec.methodBuilder(BINARY_INPUT_STREAM_METHOD_NAME)
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
            .returns(InputStream.class)
            .addParameter(WsBinaryMessageContext.class, "context")
            .addCode(
                CodeBlock.builder()
                    .addStatement("return new $T(context.getData(), context.getOffset(), context.getLength())", ByteArrayInputStream.class)
                    .build()
            )
            .build();
        typeBuilder.addMethod(method);
        binaryMethods.add(InputStream.class);
        return BINARY_INPUT_STREAM_METHOD_NAME;
    }

    private static boolean isSetter(Element element) {
        return element.getKind() == ElementKind.METHOD
            && isSetter((ExecutableElement) element);
    }

    private static boolean isSetter(ExecutableElement method) {
        return !method.getModifiers().contains(Modifier.STATIC)
            && method.getParameters().size() == 1
            && StringUtils.startsWith(method.getSimpleName().toString(), "set");
    }

    public void addRouteHandler(String methodName, CodeBlock body) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PRIVATE)
            .returns(void.class)
            .addException(Exception.class)
            .addParameter(Context.class, "ctx")
            .addCode(body);
        typeBuilder.addMethod(methodBuilder.build());
    }

    public void addWsRouteHandler(String methodName, Class<?> contextType, CodeBlock body) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PRIVATE)
            .returns(void.class)
            .addException(Exception.class)
            .addParameter(contextType, "ctx")
            .addCode(body);
        typeBuilder.addMethod(methodBuilder.build());
    }

    // region ConversionHelper

    public interface ConversionHelper {
        boolean isCollectionType();

        Class<?> getScalarType();

        void addConversionMethod(HelperMethodBuilder builder);

        String getConversionCall(String sourceCall);
    }

    private static abstract class MethodBuildingConversionHelper implements ConversionHelper {
        private final boolean isCollectionType;

        protected MethodBuildingConversionHelper(boolean isCollectionType) {
            this.isCollectionType = isCollectionType;
        }

        @Override
        public boolean isCollectionType() {
            return isCollectionType;
        }

        public abstract Class<?> getScalarType();

        protected abstract Class<?> getCollectionType();

        protected abstract String getScalarName();

        protected abstract String getCollectionName();

        @Override
        public void addConversionMethod(HelperMethodBuilder builder) {
            if (isCollectionType) {
                buildCollectionHelper(builder);
            } else {
                buildScalarHelper(builder);
            }
        }

        private void buildScalarHelper(HelperMethodBuilder builder) {
            if (String.class.equals(getScalarType())) {
                return;
            }
            if (builder.addedSingletonConversionHelpers.contains(getScalarType())) {
                return;
            }
            MethodSpec method = MethodSpec.methodBuilder(getScalarName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(getScalarType())
                .addParameter(String.class, "value")
                .addCode(getScalarMethodBody(builder))
                .build();
            builder.typeBuilder.addMethod(method);
            builder.addedSingletonConversionHelpers.add(getScalarType());
        }

        protected abstract CodeBlock getScalarMethodBody(HelperMethodBuilder builder);

        private void buildCollectionHelper(HelperMethodBuilder builder) {
            if (builder.addedArrayConversionHelpers.contains(getScalarType())) {
                return;
            }
            MethodSpec method = MethodSpec.methodBuilder(getCollectionName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(getCollectionType())
                .addParameter(ParameterizedTypeName.get(List.class, String.class), "values")
                .addCode(getCollectionMethodBody(builder))
                .build();
            builder.typeBuilder.addMethod(method);
            builder.addedArrayConversionHelpers.add(getScalarType());
        }

        protected abstract CodeBlock getCollectionMethodBody(HelperMethodBuilder builder);

        protected static void addField(
                TypeSpec.Builder typeBuilder,
                String name,
                TypeName type,
                String initializer,
                Object... args) {
            FieldSpec field = FieldSpec.builder(type, name, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer(initializer, args)
                .build();
            typeBuilder.addField(field);
        }

        @Override
        public String getConversionCall(String sourceCall) {
            // As an optimization, we do not create a converter for strings.
            if (!isCollectionType && String.class.equals(getScalarType())) {
                return sourceCall;
            }
            String methodName = isCollectionType ? getCollectionName() : getScalarName();
            return CodeBlock.of("$L($L)", methodName, sourceCall).toString();
        }
    }

    private static final class PrimitiveByteHelper extends MethodBuildingConversionHelper {
        public PrimitiveByteHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return byte.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return byte[].class;
        }

        @Override
        public String getScalarName() {
            return "toPrimitiveByte";
        }

        @Override
        public String getCollectionName() {
            return "toPrimitiveByteArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? (byte) 0 : Byte.parseByte(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return (byte) 0")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("byte[] results = new byte[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values.get(i)")
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

    private static final class PrimitiveShortHelper extends MethodBuildingConversionHelper {
        public PrimitiveShortHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return short.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return short[].class;
        }

        @Override
        public String getScalarName() {
            return "toPrimitiveShort";
        }

        @Override
        public String getCollectionName() {
            return "toPrimitiveShortArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? (short) 0 : Short.parseShort(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return (short) 0")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("short[] results = new short[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values.get(i)")
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

    private static final class PrimitiveIntegerHelper extends MethodBuildingConversionHelper {
        public PrimitiveIntegerHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return int.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return int[].class;
        }

        @Override
        public String getScalarName() {
            return "toPrimitiveInteger";
        }

        @Override
        public String getCollectionName() {
            return "toPrimitiveIntegerArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? 0 : Integer.parseInt(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return 0")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("int[] results = new int[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values.get(i)")
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

    private static final class PrimitiveLongHelper extends MethodBuildingConversionHelper {
        public PrimitiveLongHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return long.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return long[].class;
        }

        @Override
        public String getScalarName() {
            return "toPrimitiveLong";
        }

        @Override
        public String getCollectionName() {
            return "toPrimitiveLongArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? 0L : Long.parseLong(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return 0L")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("long[] results = new long[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values.get(i)")
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

    private static final class PrimitiveFloatHelper extends MethodBuildingConversionHelper {
        public PrimitiveFloatHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return float.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return float[].class;
        }

        @Override
        public String getScalarName() {
            return "toPrimitiveFloat";
        }

        @Override
        public String getCollectionName() {
            return "toPrimitiveFloatArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? 0.0f : Float.parseFloat(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return 0.0f")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("float[] results = new float[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values.get(i)")
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

    private static final class PrimitiveDoubleHelper extends MethodBuildingConversionHelper {
        public PrimitiveDoubleHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return double.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return double[].class;
        }

        @Override
        public String getScalarName() {
            return "toPrimitiveDouble";
        }

        @Override
        public String getCollectionName() {
            return "toPrimitiveDoubleArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? 0.0 : Double.parseDouble(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return 0.0")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("double[] results = new double[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values.get(i)")
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

    private static final class PrimitiveBooleanHelper extends MethodBuildingConversionHelper {
        public PrimitiveBooleanHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return boolean.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return boolean[].class;
        }

        @Override
        public String getScalarName() {
            return "toPrimitiveBoolean";
        }

        @Override
        public String getCollectionName() {
            return "toPrimitiveBooleanArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("return Boolean.parseBoolean(value)")
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("boolean[] results = new boolean[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values.get(i)")
                .addStatement("results[i] = Boolean.parseBoolean(value)")
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class PrimitiveCharacterHelper extends MethodBuildingConversionHelper {
        public PrimitiveCharacterHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return char.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return char[].class;
        }

        @Override
        public String getScalarName() {
            return "toPrimitiveCharacter";
        }

        @Override
        public String getCollectionName() {
            return "toPrimitiveCharacterArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("return value != null && value.length() == 1 ? value.charAt(0) : '\\0'")
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("char[] results = new char[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values.get(i)")
                .beginControlFlow("if (value != null && value.length() == 1)")
                .addStatement("results[i] = value.charAt(0)")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BoxedByteHelper extends MethodBuildingConversionHelper {
        public BoxedByteHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return Byte.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return Byte[].class;
        }

        @Override
        public String getScalarName() {
            return "toBoxedByte";
        }

        @Override
        public String getCollectionName() {
            return "toBoxedByteArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Byte.parseByte(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("Byte[] results = new Byte[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values.get(i)")
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

    private static final class BoxedShortHelper extends MethodBuildingConversionHelper {
        public BoxedShortHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return Short.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return Short[].class;
        }

        @Override
        public String getScalarName() {
            return "toBoxedShort";
        }

        @Override
        public String getCollectionName() {
            return "toBoxedShortArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Short.parseShort(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("Short[] results = new Short[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values.get(i)")
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

    private static final class BoxedIntegerHelper extends MethodBuildingConversionHelper {
        public BoxedIntegerHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return Integer.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return Integer[].class;
        }

        @Override
        public String getScalarName() {
            return "toBoxedInteger";
        }

        @Override
        public String getCollectionName() {
            return "toBoxedIntegerArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Integer.parseInt(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("Integer[] results = new Integer[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values.get(i)")
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

    private static final class BoxedLongHelper extends MethodBuildingConversionHelper {
        public BoxedLongHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return Long.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return Long[].class;
        }

        @Override
        public String getScalarName() {
            return "toBoxedLong";
        }

        @Override
        public String getCollectionName() {
            return "toBoxedLongArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Long.parseLong(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("Long[] results = new Long[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values.get(i)")
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

    private static final class BoxedFloatHelper extends MethodBuildingConversionHelper {
        public BoxedFloatHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return Float.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return Float[].class;
        }

        @Override
        public String getScalarName() {
            return "toBoxedFloat";
        }

        @Override
        public String getCollectionName() {
            return "toBoxedFloatArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Float.parseFloat(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("Float[] results = new Float[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values.get(i)")
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

    private static final class BoxedDoubleHelper extends MethodBuildingConversionHelper {
        public BoxedDoubleHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return Double.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return Double[].class;
        }

        @Override
        public String getScalarName() {
            return "toBoxedDouble";
        }

        @Override
        public String getCollectionName() {
            return "toBoxedDoubleArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Double.parseDouble(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("Double[] results = new Double[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values.get(i)")
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

    private static final class BoxedBooleanHelper extends MethodBuildingConversionHelper {
        public BoxedBooleanHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return Boolean.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return Boolean[].class;
        }

        @Override
        public String getScalarName() {
            return "toBoxedBoolean";
        }

        @Override
        public String getCollectionName() {
            return "toBoxedBooleanArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("return value == null ? null : Boolean.parseBoolean(value)")
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("Boolean[] results = new Boolean[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values.get(i)")
                .beginControlFlow("if (value != null)")
                .addStatement("results[i] = Boolean.parseBoolean(value)")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class BoxedCharacterHelper extends MethodBuildingConversionHelper {
        public BoxedCharacterHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return Character.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return Character[].class;
        }

        @Override
        public String getScalarName() {
            return "toBoxedCharacter";
        }

        @Override
        public String getCollectionName() {
            return "toBoxedCharacterArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("return value != null && value.length() == 1 ? value.charAt(0) : null")
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("Character[] results = new Character[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values.get(i)")
                .beginControlFlow("if (value != null && value.length() == 1)")
                .addStatement("results[i] = value.charAt(0)")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class StringHelper extends MethodBuildingConversionHelper {
        public StringHelper(boolean isCollectionType) {
            super(isCollectionType);
        }

        @Override
        public Class<?> getScalarType() {
            return String.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return String[].class;
        }

        @Override
        public String getScalarName() {
            return "toString";
        }

        @Override
        public String getCollectionName() {
            return "toStringArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("return value")
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("return values.toArray(new $T[0])", String.class)
                .build();
        }
    }

    private static final class DateHelper extends MethodBuildingConversionHelper {
        private static final String FORMATTER_FIELD_NAME = "SIMPLE_DATE_FORMATTER";

        public DateHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return Date.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return Date[].class;
        }

        @Override
        public String getScalarName() {
            return "toDate";
        }

        @Override
        public String getCollectionName() {
            return "toDateArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            addFormatterField(builder);
            return CodeBlock.builder()
                .beginControlFlow("if (value == null)")
                .addStatement("return null")
                .endControlFlow()
                .beginControlFlow("try")
                .addStatement("return $N.get().parse(value)", FORMATTER_FIELD_NAME)
                .nextControlFlow("catch ($T exception)", ParseException.class)
                .addStatement("// SWALLOW")
                .endControlFlow()
                .addStatement("return null")
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            addFormatterField(builder);
            return CodeBlock.builder()
                .addStatement("Date[] results = new Date[values.size()]")
                .addStatement("$T formatter = $N.get()", SimpleDateFormat.class, FORMATTER_FIELD_NAME)
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values.get(i)")
                .beginControlFlow("if (value == null)")
                .addStatement("continue")
                .endControlFlow()
                .beginControlFlow("try")
                .addStatement("results[i] = $N.parse(value)", "formatter")
                .nextControlFlow("catch ($T exception)", ParseException.class)
                .addStatement("// SWALLOW")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }

        private void addFormatterField(HelperMethodBuilder builder) {
            if (!builder.addedFields.contains(FORMATTER_FIELD_NAME)) {
                TypeSpec.Builder typeBuilder = builder.typeBuilder;
                MethodSpec method = MethodSpec.methodBuilder("getSimpleDateFormatter")
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                    .returns(SimpleDateFormat.class)
                    .addCode(CodeBlock.builder()
                        .addStatement("$T formatter = new $T($S)", SimpleDateFormat.class, SimpleDateFormat.class, "yyyy-MM-dd'T'HH:mm:ssXX")
                        //.addStatement("formatter.setTimeZone($T.getDefault())", TimeZone.class)
                        .addStatement("return formatter")
                        .build())
                    .build();;
                typeBuilder.addMethod(method);
                addField(
                    typeBuilder,
                    FORMATTER_FIELD_NAME,
                    ParameterizedTypeName.get(ThreadLocal.class, SimpleDateFormat.class),
                    "$T.withInitial(JavalinControllerRegistry::$N)",
                    ThreadLocal.class,
                    method.name
                );
                builder.addedFields.add(FORMATTER_FIELD_NAME);
            }
        }
    }

    private static final class InstantHelper extends MethodBuildingConversionHelper {
        public InstantHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return Instant.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return Instant[].class;
        }

        @Override
        public String getScalarName() {
            return "toInstant";
        }

        @Override
        public String getCollectionName() {
            return "toInstantArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Instant.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("Instant[] results = new Instant[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                    .addStatement("String value = values.get(i)")
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

    private static final class ZonedDateTimeHelper extends MethodBuildingConversionHelper {
        public ZonedDateTimeHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return ZonedDateTime.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return ZonedDateTime[].class;
        }

        @Override
        public String getScalarName() {
            return "toZonedDateTime";
        }

        @Override
        public String getCollectionName() {
            return "toZonedDateTimeArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : ZonedDateTime.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("ZonedDateTime[] results = new ZonedDateTime[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values.get(i)")
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

    private static final class OffsetDateTimeHelper extends MethodBuildingConversionHelper {
        public OffsetDateTimeHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return OffsetDateTime.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return OffsetDateTime[].class;
        }

        @Override
        public String getScalarName() {
            return "toOffsetDateTime";
        }

        @Override
        public String getCollectionName() {
            return "toOffsetDateTimeArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : OffsetDateTime.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("OffsetDateTime[] results = new OffsetDateTime[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values.get(i)")
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

    private static final class LocalDateTimeHelper extends MethodBuildingConversionHelper {
        public LocalDateTimeHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return LocalDateTime.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return LocalDateTime[].class;
        }

        @Override
        public String getScalarName() {
            return "toLocalDateTime";
        }

        @Override
        public String getCollectionName() {
            return "toLocalDateTimeArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : $T.parse(value)", LocalDateTime.class)
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("$T[] results = new $T[values.size()]", LocalDateTime.class, LocalDateTime.class)
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values.get(i)")
                .beginControlFlow("if (value != null)")
                .beginControlFlow("try")
                .addStatement("results[i] = $T.parse(value)", LocalDateTime.class)
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .endControlFlow()
                .endControlFlow()
                .endControlFlow()
                .addStatement("return results")
                .build();
        }
    }

    private static final class LocalDateHelper extends MethodBuildingConversionHelper {
        public LocalDateHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return LocalDate.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return LocalDate[].class;
        }

        @Override
        public String getScalarName() {
            return "toLocalDate";
        }

        @Override
        public String getCollectionName() {
            return "toLocalDateArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : LocalDate.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("LocalDate[] results = new LocalDate[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values.get(i)")
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

    private static final class YearMonthHelper extends MethodBuildingConversionHelper {
        public YearMonthHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return YearMonth.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return YearMonth[].class;
        }

        @Override
        public String getScalarName() {
            return "toYearMonth";
        }

        @Override
        public String getCollectionName() {
            return "toYearMonthArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : YearMonth.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("YearMonth[] results = new YearMonth[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values.get(i)")
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

    private static final class YearHelper extends MethodBuildingConversionHelper {
        public YearHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return Year.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return Year[].class;
        }

        @Override
        public String getScalarName() {
            return "toYear";
        }

        @Override
        public String getCollectionName() {
            return "toYearArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : Year.parse(value)")
                .nextControlFlow("catch ($T exception)", DateTimeParseException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("Year[] results = new Year[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .addStatement("String value = values.get(i)")
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

    private static final class BigIntegerHelper extends MethodBuildingConversionHelper {
        public BigIntegerHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return BigInteger.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return BigInteger[].class;
        }

        @Override
        public String getScalarName() {
            return "toBigInteger";
        }

        @Override
        public String getCollectionName() {
            return "toBigIntegerArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : new BigInteger(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("BigInteger[] results = new BigInteger[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values.get(i)")
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

    private static final class BigDecimalHelper extends MethodBuildingConversionHelper {
        public BigDecimalHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return BigDecimal.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return BigDecimal[].class;
        }

        @Override
        public String getScalarName() {
            return "toBigDecimal";
        }

        @Override
        public String getCollectionName() {
            return "toBigDecimalArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : new BigDecimal(value)")
                .nextControlFlow("catch ($T exception)", NumberFormatException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("BigDecimal[] results = new BigDecimal[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values.get(i)")
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

    private static final class UUIDHelper extends MethodBuildingConversionHelper {
        public UUIDHelper(boolean isArrayType) {
            super(isArrayType);
        }

        @Override
        public Class<?> getScalarType() {
            return UUID.class;
        }

        @Override
        public Class<?> getCollectionType() {
            return UUID[].class;
        }

        @Override
        public String getScalarName() {
            return "toUUID";
        }

        @Override
        public String getCollectionName() {
            return "toUUIDArray";
        }

        @Override
        protected CodeBlock getScalarMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("return value == null ? null : UUID.fromString(value)")
                .nextControlFlow("catch ($T exception)", IllegalArgumentException.class)
                .addStatement("return null")
                .endControlFlow()
                .build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            return CodeBlock.builder()
                .addStatement("UUID[] results = new UUID[values.size()]")
                .beginControlFlow("for (int i = 0; i != results.length; ++i)")
                .beginControlFlow("try")
                .addStatement("String value = values.get(i)")
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

    private static abstract class CollectionConversionHelper implements ConversionHelper {
        private final ConversionHelper conversionHelper;

        protected CollectionConversionHelper(ConversionHelper conversionHelper) {
            this.conversionHelper = conversionHelper;
        }

        @Override
        public boolean isCollectionType() {
            return true;
        }

        @Override
        public Class<?> getScalarType() {
            return conversionHelper.getScalarType();
        }

        protected abstract Class<?> getCollectionClass();

        protected abstract Class<?> getImplementationClass();

        protected boolean acceptsCapacity() {
            return true;
        }

        @Override
        public void addConversionMethod(HelperMethodBuilder builder) {
            conversionHelper.addConversionMethod(builder);

            if (isArrayListString()) {
                return;
            }

            String converterName = getConverterName();
            if (builder.addedCollectionConversionHelpers.contains(converterName)) {
                return;
            }
            MethodSpec method = MethodSpec.methodBuilder(converterName)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(ParameterizedTypeName.get(getImplementationClass(), conversionHelper.getScalarType()))
                .addParameter(ParameterizedTypeName.get(List.class, String.class), "values")
                .addCode(getCollectionMethodBody(builder))
                .build();
            builder.typeBuilder.addMethod(method);
            builder.addedCollectionConversionHelpers.add(converterName);
        }

        private CodeBlock getCollectionMethodBody(HelperMethodBuilder builder) {
            Class<?> collectionType = getImplementationClass();
            Class<?> componentType = conversionHelper.getScalarType();
            TypeName typeName = ParameterizedTypeName.get(collectionType, componentType);
            String conversionCall = conversionHelper.getConversionCall("value");
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            if (acceptsCapacity()) {
                bodyBuilder.addStatement("$T results = new $T<>(values.size())", typeName, collectionType);
            } else {
                bodyBuilder.addStatement("$T results = new $T<>()", typeName, collectionType);
            }
            return bodyBuilder.beginControlFlow("for (int i = 0, length = values.size(); i != length; ++i)")
                .addStatement("String value = values.get(i)")
                .addStatement("results.add($L)", conversionCall)
                .endControlFlow()
                .addStatement("return results")
                .build();
        }

        @Override
        public String getConversionCall(String sourceCall) {
            if (isArrayListString()) {
                return CodeBlock.of("new $T<>($L)", ArrayList.class, sourceCall).toString();
            }
            String converterName = getConverterName();
            return CodeBlock.of("$N($L)", converterName, sourceCall).toString();
        }

        private String getConverterName() {
            Class<?> collectionType = getImplementationClass();
            Class<?> componentType = conversionHelper.getScalarType();
            return "to" + componentType.getSimpleName() + collectionType.getSimpleName();
        }

        private boolean isArrayListString() {
            // As an optimization, we do not create a helper method for ArrayList<String>
            return ArrayList.class.equals(getImplementationClass())
                && String.class.equals(conversionHelper.getScalarType());
        }
    }

    private static final class IterableHelper extends CollectionConversionHelper {
        public IterableHelper(ConversionHelper conversionHelper) {
            super(conversionHelper);
        }

        @Override
        protected Class<?> getCollectionClass() {
            return Iterable.class;
        }

        @Override
        protected Class<?> getImplementationClass() {
            return ArrayList.class;
        }
    }

    private static final class CollectionHelper extends CollectionConversionHelper {
        public CollectionHelper(ConversionHelper conversionHelper) {
            super(conversionHelper);
        }

        @Override
        protected Class<?> getCollectionClass() {
            return Collection.class;
        }

        @Override
        protected Class<?> getImplementationClass() {
            return ArrayList.class;
        }
    }

    private static final class ListHelper extends CollectionConversionHelper {
        public ListHelper(ConversionHelper conversionHelper) {
            super(conversionHelper);
        }

        @Override
        protected Class<?> getCollectionClass() {
            return List.class;
        }

        @Override
        protected Class<?> getImplementationClass() {
            return ArrayList.class;
        }
    }

    private static final class SetHelper extends CollectionConversionHelper {
        public SetHelper(ConversionHelper conversionHelper) {
            super(conversionHelper);
        }

        @Override
        protected Class<?> getCollectionClass() {
            return Set.class;
        }

        @Override
        protected Class<?> getImplementationClass() {
            return LinkedHashSet.class;
        }
    }

    private static final class SortedSetHelper extends CollectionConversionHelper {
        public SortedSetHelper(ConversionHelper conversionHelper) {
            super(conversionHelper);
        }

        @Override
        protected Class<?> getCollectionClass() {
            return SortedSet.class;
        }

        @Override
        protected Class<?> getImplementationClass() {
            return TreeSet.class;
        }

        @Override
        protected boolean acceptsCapacity() {
            return false;
        }
    }

    private static final class ArrayListHelper extends CollectionConversionHelper {
        public ArrayListHelper(ConversionHelper conversionHelper) {
            super(conversionHelper);
        }

        @Override
        protected Class<?> getCollectionClass() {
            return ArrayList.class;
        }

        @Override
        protected Class<?> getImplementationClass() {
            return ArrayList.class;
        }
    }

    private static final class LinkedListHelper extends CollectionConversionHelper {
        public LinkedListHelper(ConversionHelper conversionHelper) {
            super(conversionHelper);
        }

        @Override
        protected Class<?> getCollectionClass() {
            return LinkedList.class;
        }

        @Override
        protected Class<?> getImplementationClass() {
            return LinkedList.class;
        }

        @Override
        protected boolean acceptsCapacity() {
            return false;
        }
    }

    private static final class HashSetHelper extends CollectionConversionHelper {
        public HashSetHelper(ConversionHelper conversionHelper) {
            super(conversionHelper);
        }

        @Override
        protected Class<?> getCollectionClass() {
            return HashSet.class;
        }

        @Override
        protected Class<?> getImplementationClass() {
            return HashSet.class;
        }
    }

    private static final class LinkedHashSetHelper extends CollectionConversionHelper {
        public LinkedHashSetHelper(ConversionHelper conversionHelper) {
            super(conversionHelper);
        }

        @Override
        protected Class<?> getCollectionClass() {
            return LinkedHashSet.class;
        }

        @Override
        protected Class<?> getImplementationClass() {
            return LinkedHashSet.class;
        }
    }

    private static final class TreeSetHelper extends CollectionConversionHelper {
        public TreeSetHelper(ConversionHelper conversionHelper) {
            super(conversionHelper);
        }

        @Override
        protected Class<?> getCollectionClass() {
            return TreeSet.class;
        }

        @Override
        protected Class<?> getImplementationClass() {
            return TreeSet.class;
        }

        @Override
        protected boolean acceptsCapacity() {
            return false;
        }
    }

    // endregion

    // region SourceHelper

    private static abstract class SourceHelper {
        public abstract ValueSource getValueSource();
        public abstract String getScalarName();
        public abstract String getCollectionName();

        public void buildScalarHelper(HelperMethodBuilder builder, String defaultValue) {
            ScalarSourceKey key = ScalarSourceKey.of(getValueSource(), defaultValue != null);
            if (builder.addedScalarSourceHelpers.contains(key)) {
                return;
            }
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getScalarName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(String.class)
                .addParameter(HttpContext.class, "context")
                .addParameter(String.class, "key");
            if (defaultValue != null) {
                methodBuilder.addParameter(String.class, "defaultValue");
            }
            methodBuilder.addCode(getScalarMethodBody(builder, defaultValue, "context", "key"));
            builder.typeBuilder.addMethod(methodBuilder.build());
            builder.addedScalarSourceHelpers.add(key);
        }

        protected abstract CodeBlock getScalarMethodBody(
            HelperMethodBuilder builder,
            String defaultValue,
            String context,
            String key);

        public void buildCollectionHelper(HelperMethodBuilder builder, String defaultValue) {
            CollectionSourceKey key = CollectionSourceKey.of(getValueSource(), defaultValue != null);
            if (builder.addedCollectionSourceHelpers.contains(key)) {
                return;
            }
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getCollectionName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(ParameterizedTypeName.get(List.class, String.class))
                .addParameter(HttpContext.class, "context")
                .addParameter(String.class, "key");
            if (defaultValue != null) {
                methodBuilder.addParameter(String.class, "defaultValue");
            }
            methodBuilder.addCode(getCollectionMethodBody(builder, defaultValue, "context", "key"));
            builder.typeBuilder.addMethod(methodBuilder.build());
            builder.addedCollectionSourceHelpers.add(key);
        }

        protected abstract CodeBlock getCollectionMethodBody(
            HelperMethodBuilder builder,
            String defaultValue,
            String context,
            String key);

        public abstract CodeBlock getPresenceCheck(String request, String key);
    }

    private static final class PathHelper extends SourceHelper {
        @Override
        public ValueSource getValueSource() {
            return ValueSource.Path;
        }

        @Override
        public String getScalarName() {
            return "getPathValue";
        }

        @Override
        public String getCollectionName() {
            return "getPathValues";
        }

        @Override
        protected CodeBlock getScalarMethodBody(
                HelperMethodBuilder builder,
                String defaultValue,
                String wrapper,
                String key) {
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            bodyBuilder.addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper);
            if (defaultValue == null) {
                bodyBuilder.addStatement("return request.getPathValue($N)", key);
            } else {
                bodyBuilder.addStatement("return request.getPathValue($N, $N)", key, "defaultValue");
            }
            return bodyBuilder.build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(
                HelperMethodBuilder builder,
                String defaultValue,
                String wrapper,
                String key) {
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            bodyBuilder.addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper);
            if (defaultValue == null) {
                bodyBuilder.addStatement("return request.getPathValues($N)", key);
            } else {
                bodyBuilder.addStatement(
                    "$T values = request.getPathValues($N)",
                    ParameterizedTypeName.get(List.class, String.class),
                    key);
                bodyBuilder.addStatement(
                    "return values.isEmpty() ? $T.singletonList($N) : values",
                    Collections.class,
                    "defaultValue");
            }
            return bodyBuilder.build();
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return CodeBlock.builder()
                .add("$N.hasPathValue($N)", request, key)
                .build();
        }
    }

    private static final class QueryStringHelper extends SourceHelper {
        @Override
        public ValueSource getValueSource() {
            return ValueSource.QueryString;
        }

        @Override
        public String getScalarName() {
            return "getQueryValue";
        }

        @Override
        public String getCollectionName() {
            return "getQueryValues";
        }

        @Override
        protected CodeBlock getScalarMethodBody(
                HelperMethodBuilder builder,
                String defaultValue,
                String wrapper,
                String key) {
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            bodyBuilder.addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper);
            if (defaultValue == null) {
                bodyBuilder.addStatement("return request.getQueryValue($N)", key);
            } else {
                bodyBuilder.addStatement("return request.getQueryValue($N, $N)", key, "defaultValue");
            }
            return bodyBuilder.build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(
                HelperMethodBuilder builder,
                String defaultValue,
                String wrapper,
                String key) {
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            bodyBuilder.addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper);
            if (defaultValue == null) {
                bodyBuilder.addStatement("return request.getQueryValues($N)", key);
            } else {
                bodyBuilder.addStatement(
                    "$T values = request.getQueryValues($N)",
                    ParameterizedTypeName.get(List.class, String.class),
                    key);
                bodyBuilder.addStatement(
                    "return values.isEmpty() ? $T.singletonList($N) : values",
                    Collections.class,
                    "defaultValue");
            }
            return bodyBuilder.build();
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return CodeBlock.builder()
                .add("$N.hasQueryValue($N)", request, key)
                .build();
        }
    }

    private static final class HeaderHelper extends SourceHelper {
        @Override
        public ValueSource getValueSource() {
            return ValueSource.Header;
        }

        @Override
        public String getScalarName() {
            return "getHeaderValue";
        }

        @Override
        public String getCollectionName() {
            return "getHeaderValues";
        }

        @Override
        protected CodeBlock getScalarMethodBody(
                HelperMethodBuilder builder,
                String defaultValue,
                String wrapper,
                String key) {
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            bodyBuilder.addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper);
            if (defaultValue == null) {
                bodyBuilder.addStatement("return request.getHeaderValue($N)", key);
            } else {
                bodyBuilder.addStatement("return request.getHeaderValue($N, $N)", key, "defaultValue");
            }
            return bodyBuilder.build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(
                HelperMethodBuilder builder,
                String defaultValue,
                String wrapper,
                String key) {
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            bodyBuilder.addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper);
            if (defaultValue == null) {
                bodyBuilder.addStatement("return request.getHeaderValues($N)", key);
            } else {
                bodyBuilder.addStatement(
                    "$T values = request.getHeaderValues($N)",
                    ParameterizedTypeName.get(List.class, String.class),
                    key);
                bodyBuilder.addStatement(
                    "return values.isEmpty() ? $T.singletonList($N) : values",
                    Collections.class,
                    "defaultValue");
            }
            return bodyBuilder.build();
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return CodeBlock.builder()
                .add("$N.hasHeaderValue($N)", request, key)
                .build();
        }
    }

    private static final class CookieHelper extends SourceHelper {
        @Override
        public ValueSource getValueSource() {
            return ValueSource.Cookie;
        }

        @Override
        public String getScalarName() {
            return "getCookieValue";
        }

        @Override
        public String getCollectionName() {
            return "getCookieValues";
        }

        @Override
        protected CodeBlock getScalarMethodBody(
                HelperMethodBuilder builder,
                String defaultValue,
                String wrapper,
                String key) {
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            bodyBuilder.addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper);
            if (defaultValue == null) {
                bodyBuilder.addStatement("return request.getCookieValue($N)", key);
            } else {
                bodyBuilder.addStatement("return request.getCookieValue($N, $N)", key, "defaultValue");
            }
            return bodyBuilder.build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(
                HelperMethodBuilder builder,
                String defaultValue,
                String wrapper,
                String key) {
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            bodyBuilder.addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper);
            if (defaultValue == null) {
                bodyBuilder.addStatement("return request.getCookieValues($N)", key);
            } else {
                bodyBuilder.addStatement(
                    "$T values = request.getCookieValues($N)",
                    ParameterizedTypeName.get(List.class, String.class),
                    key);
                bodyBuilder.addStatement(
                    "return values.isEmpty() ? $T.singletonList($N) : values",
                    Collections.class,
                    "defaultValue");
            }
            return bodyBuilder.build();
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return CodeBlock.builder()
                .add("$N.hasCookieValue($N)", request, key)
                .build();
        }
    }

    private static final class FormDataHelper extends SourceHelper {
        @Override
        public ValueSource getValueSource() {
            return ValueSource.FormData;
        }

        @Override
        public String getScalarName() {
            return "getFormDataValue";
        }

        @Override
        public String getCollectionName() {
            return "getFormDataValues";
        }

        @Override
        protected CodeBlock getScalarMethodBody(
                HelperMethodBuilder builder,
                String defaultValue,
                String wrapper,
                String key) {
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            bodyBuilder.addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper);
            if (defaultValue == null) {
                bodyBuilder.addStatement("return request.getFormValue($N)", key);
            } else {
                bodyBuilder.addStatement("return request.getFormValue($N, $N)", key, "defaultValue");
            }
            return bodyBuilder.build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(
                HelperMethodBuilder builder,
                String defaultValue,
                String wrapper,
                String key) {
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            bodyBuilder.addStatement("$T request = $N.getRequest()", HttpRequest.class, wrapper);
            if (defaultValue == null) {
                bodyBuilder.addStatement("return request.getFormValues($N)", key);
            } else {
                bodyBuilder.addStatement(
                    "$T values = request.getFormValues($N)",
                    ParameterizedTypeName.get(List.class, String.class),
                    key);
                bodyBuilder.addStatement(
                    "return values.isEmpty() ? $T.singletonList($N) : values",
                    Collections.class,
                    "defaultValue");
            }
            return bodyBuilder.build();
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return CodeBlock.builder()
                .add("$N.hasFormValue($N)", request, key)
                .build();
        }
    }

    private static final class AnyHelper extends SourceHelper {
        @Override
        public ValueSource getValueSource() {
            return ValueSource.Any;
        }

        @Override
        public String getScalarName() {
            return "getValue";
        }

        @Override
        public String getCollectionName() {
            return "getValues";
        }

        @Override
        protected CodeBlock getScalarMethodBody(
                HelperMethodBuilder builder,
                String defaultValue,
                String wrapper,
                String key) {
            // Build the source helpers for all the other source types
            for (Map.Entry<ValueSource, SourceHelper> entry : SOURCE_HELPER_LOOKUP.entrySet()) {
                ValueSource source = entry.getKey();
                if (source != ValueSource.Any) {
                    SourceHelper helper = entry.getValue();
                    helper.buildScalarHelper(builder, null /* We don't pass defaultValue here */);
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
                        codeBuilder.addStatement("return $N($N, $N)", helper.getScalarName(), wrapper, key);
                        codeBuilder.endControlFlow();
                    }
                }
            }
            if (defaultValue == null) {
                codeBuilder.addStatement("return null");
            } else {
                codeBuilder.addStatement("return $N", "defaultValue");
            }
            return codeBuilder.build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(HelperMethodBuilder builder, String defaultValue, String wrapper, String key) {
            // Build the source helpers for all the other source types
            for (Map.Entry<ValueSource, SourceHelper> entry : SOURCE_HELPER_LOOKUP.entrySet()) {
                ValueSource source = entry.getKey();
                if (source != ValueSource.Any) {
                    SourceHelper helper = entry.getValue();
                    helper.buildCollectionHelper(builder, null /* We don't pass defaultValue here */);
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
                        codeBuilder.addStatement("return $N($N, $N)", helper.getCollectionName(), wrapper, key);
                        codeBuilder.endControlFlow();
                    }
                }
            }
            if (defaultValue == null) {
                codeBuilder.addStatement("return $T.emptyList()", Collections.class);
            } else {
                codeBuilder.addStatement("return $T.singletonList($N)", Collections.class, "defaultValue");
            }
            return codeBuilder.build();
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return null;
        }
    }

    private static final class ScalarSourceKey {
        private final ValueSource valueSource;
        private final boolean hasDefaultValue;

        private ScalarSourceKey(ValueSource valueSource, boolean hasDefaultValue) {
            this.valueSource = valueSource;
            this.hasDefaultValue = hasDefaultValue;
        }

        public static ScalarSourceKey of(ValueSource valueSource, boolean hasDefaultValue) {
            return new ScalarSourceKey(valueSource, hasDefaultValue);
        }

        public ValueSource getValueSource() {
            return valueSource;
        }

        public boolean isHasDefaultValue() {
            return hasDefaultValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ScalarSourceKey that = (ScalarSourceKey) o;
            return hasDefaultValue == that.hasDefaultValue && valueSource == that.valueSource;
        }

        @Override
        public int hashCode() {
            return Objects.hash(valueSource, hasDefaultValue);
        }
    }

    private static final class CollectionSourceKey {
        private final ValueSource valueSource;
        private final boolean hasDefaultValue;

        private CollectionSourceKey(ValueSource valueSource, boolean hasDefaultValue) {
            this.valueSource = valueSource;
            this.hasDefaultValue = hasDefaultValue;
        }

        public static CollectionSourceKey of(ValueSource valueSource, boolean hasDefaultValue) {
            return new CollectionSourceKey(valueSource, hasDefaultValue);
        }

        public ValueSource getValueSource() {
            return valueSource;
        }

        public boolean isHasDefaultValue() {
            return hasDefaultValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CollectionSourceKey that = (CollectionSourceKey) o;
            return hasDefaultValue == that.hasDefaultValue && valueSource == that.valueSource;
        }

        @Override
        public int hashCode() {
            return Objects.hash(valueSource, hasDefaultValue);
        }
    }

    // endregion

    // region WsSourceHelper

    private static abstract class WsSourceHelper {
        public abstract WsValueSource getValueSource();
        public abstract String getScalarName();
        public abstract String getCollectionName();

        public void buildScalarHelper(
                HelperMethodBuilder builder,
                Class<? extends WsContext> wrapperType,
                String defaultValue) {
            WsScalarSourceKey key = WsScalarSourceKey.of(getValueSource(), wrapperType, defaultValue != null);
            if (builder.addedWsScalarSourceHelpers.contains(key)) {
                return;
            }
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getScalarName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(String.class)
                .addParameter(wrapperType, "context")
                .addParameter(String.class, "key");
            if (defaultValue != null) {
                methodBuilder.addParameter(String.class, "defaultValue");
            }
            methodBuilder.addCode(getScalarMethodBody(builder, wrapperType, defaultValue, "context", "key"));
            builder.typeBuilder.addMethod(methodBuilder.build());
            builder.addedWsScalarSourceHelpers.add(key);
        }

        protected abstract CodeBlock getScalarMethodBody(
            HelperMethodBuilder builder,
            Class<? extends WsContext> contextType,
            String defaultValue,
            String context,
            String key);

        public void buildCollectionHelper(
                HelperMethodBuilder builder,
                Class<? extends WsContext> wrapperType,
                String defaultValue) {
            WsCollectionSourceKey key = WsCollectionSourceKey.of(getValueSource(), wrapperType, defaultValue != null);
            if (builder.addedCollectionWsSourceHelpers.contains(key)) {
                return;
            }
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getCollectionName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(ParameterizedTypeName.get(List.class, String.class))
                .addParameter(wrapperType, "context")
                .addParameter(String.class, "key");
            if (defaultValue != null) {
                methodBuilder.addParameter(String.class, "defaultValue");
            }
            methodBuilder.addCode(getCollectionMethodBody(builder, wrapperType, defaultValue, "context", "key"));
            builder.typeBuilder.addMethod(methodBuilder.build());
            builder.addedCollectionWsSourceHelpers.add(key);
        }

        protected abstract CodeBlock getCollectionMethodBody(
            HelperMethodBuilder builder,
            Class<? extends WsContext> contextType,
            String defaultValue,
            String context,
            String key);

        public abstract CodeBlock getPresenceCheck(String request, String key);
    }

    private static final class WsPathHelper extends WsSourceHelper {
        @Override
        public WsValueSource getValueSource() {
            return WsValueSource.Path;
        }

        @Override
        public String getScalarName() {
            return "getWsPathValue";
        }

        @Override
        public String getCollectionName() {
            return "getWsPathValues";
        }

        @Override
        protected CodeBlock getScalarMethodBody(
                HelperMethodBuilder builder,
                Class<? extends WsContext> wrapperType,
                String defaultValue,
                String wrapper,
                String key) {
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            bodyBuilder.addStatement("$T request = $N.getRequest()", WsRequest.class, wrapper);
            if (defaultValue == null) {
                bodyBuilder.addStatement("return request.getPathValue($N)", key);
            } else {
                bodyBuilder.addStatement("return request.getPathValue($N, $N)", key, "defaultValue");
            }
            return bodyBuilder.build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(
                HelperMethodBuilder builder,
                Class<? extends WsContext> wrapperType,
                String defaultValue,
                String wrapper,
                String key) {
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            bodyBuilder.addStatement("$T request = $N.getRequest()", WsRequest.class, wrapper);
            if (defaultValue == null) {
                bodyBuilder.addStatement("return request.getPathValues($N)", key);
            } else {
                bodyBuilder.addStatement(
                    "$T values = request.getPathValues($N)",
                    ParameterizedTypeName.get(List.class, String.class),
                    key);
                bodyBuilder.addStatement(
                    "return values.isEmpty() ? $T.singletonList($N) : values",
                    Collections.class,
                    "defaultValue");
            }
            return bodyBuilder.build();
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return CodeBlock.builder()
                .add("$N.hasPathValue($N)", request, key)
                .build();
        }
    }

    private static final class WsQueryStringHelper extends WsSourceHelper {
        @Override
        public WsValueSource getValueSource() {
            return WsValueSource.QueryString;
        }

        @Override
        public String getScalarName() {
            return "getWsQueryValue";
        }

        @Override
        public String getCollectionName() {
            return "getWsQueryValues";
        }

        @Override
        protected CodeBlock getScalarMethodBody(
                HelperMethodBuilder builder,
                Class<? extends WsContext> wrapperType,
                String defaultValue,
                String wrapper,
                String key) {
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            bodyBuilder.addStatement("$T request = $N.getRequest()", WsRequest.class, wrapper);
            if (defaultValue == null) {
                bodyBuilder.addStatement("return request.getQueryValue($N)", key);
            } else {
                bodyBuilder.addStatement("return request.getQueryValue($N, $N)", key, "defaultValue");
            }
            return bodyBuilder.build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(
                HelperMethodBuilder builder,
                Class<? extends WsContext> wrapperType,
                String defaultValue,
                String wrapper,
                String key) {
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            bodyBuilder.addStatement("$T request = $N.getRequest()", WsRequest.class, wrapper);
            if (defaultValue == null) {
                bodyBuilder.addStatement("return request.getQueryValues($N)", key);
            } else {
                bodyBuilder.addStatement(
                    "$T values = request.getQueryValues($N)",
                    ParameterizedTypeName.get(List.class, String.class),
                    key);
                bodyBuilder.addStatement(
                    "return values.isEmpty() ? $T.singletonList($N) : values",
                    Collections.class,
                    "defaultValue");
            }
            return bodyBuilder.build();
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return CodeBlock.builder()
                .add("$N.hasQueryValue($N)", request, key)
                .build();
        }
    }

    private static final class WsHeaderHelper extends WsSourceHelper {
        @Override
        public WsValueSource getValueSource() {
            return WsValueSource.Header;
        }

        @Override
        public String getScalarName() {
            return "getWsHeaderValue";
        }

        @Override
        public String getCollectionName() {
            return "getWsHeaderValues";
        }

        @Override
        protected CodeBlock getScalarMethodBody(
                HelperMethodBuilder builder,
                Class<? extends WsContext> wrapperType,
                String defaultValue,
                String wrapper,
                String key) {
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            bodyBuilder.addStatement("$T request = $N.getRequest()", WsRequest.class, wrapper);
            if (defaultValue == null) {
                bodyBuilder.addStatement("return request.getHeaderValue($N)", key);
            } else {
                bodyBuilder.addStatement("return request.getHeaderValue($N, $N)", key, "defaultValue");
            }
            return bodyBuilder.build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(
                HelperMethodBuilder builder,
                Class<? extends WsContext> wrapperType,
                String defaultValue,
                String wrapper,
                String key) {
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            bodyBuilder.addStatement("$T request = $N.getRequest()", WsRequest.class, wrapper);
            if (defaultValue == null) {
                bodyBuilder.addStatement("return request.getHeaderValues($N)", key);
            } else {
                bodyBuilder.addStatement(
                    "$T values = request.getHeaderValues($N)",
                    ParameterizedTypeName.get(List.class, String.class),
                    key);
                bodyBuilder.addStatement(
                    "return values.isEmpty() ? $T.singletonList($N) : values",
                    Collections.class,
                    "defaultValue");
            }
            return bodyBuilder.build();
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return CodeBlock.builder()
                .add("$N.hasHeaderValue($N)", request, key)
                .build();
        }
    }

    private static final class WsCookieHelper extends WsSourceHelper {
        @Override
        public WsValueSource getValueSource() {
            return WsValueSource.Cookie;
        }

        @Override
        public String getScalarName() {
            return "getWsCookieValue";
        }

        @Override
        public String getCollectionName() {
            return "getWsCookieValues";
        }

        @Override
        protected CodeBlock getScalarMethodBody(
                HelperMethodBuilder builder,
                Class<? extends WsContext> wrapperType,
                String defaultValue,
                String wrapper,
                String key) {
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            bodyBuilder.addStatement("$T request = $N.getRequest()", WsRequest.class, wrapper);
            if (defaultValue == null) {
                bodyBuilder.addStatement("return request.getCookieValue($N)", key);
            } else {
                bodyBuilder.addStatement("return request.getCookieValue($N, $N)", key, "defaultValue");
            }
            return bodyBuilder.build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(
                HelperMethodBuilder builder,
                Class<? extends WsContext> wrapperType,
                String defaultValue,
                String wrapper,
                String key) {
            CodeBlock.Builder bodyBuilder = CodeBlock.builder();
            bodyBuilder.addStatement("$T request = $N.getRequest()", WsRequest.class, wrapper);
            if (defaultValue == null) {
                bodyBuilder.addStatement("return request.getCookieValues($N)", key);
            } else {
                bodyBuilder.addStatement(
                    "$T values = request.getCookieValues($N)",
                    ParameterizedTypeName.get(List.class, String.class),
                    key);
                bodyBuilder.addStatement(
                    "return values.isEmpty() ? $T.singletonList($N) : values",
                    Collections.class,
                    "defaultValue");
            }
            return bodyBuilder.build();
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return CodeBlock.builder()
                .add("$N.hasCookieValue($N)", request, key)
                .build();
        }
    }

    private static final class WsMessageHelper extends WsSourceHelper {
        @Override
        public WsValueSource getValueSource() {
            return WsValueSource.Message;
        }

        @Override
        public String getScalarName() {
            return "getWsMessageValue";
        }

        @Override
        public String getCollectionName() {
            return "getWsMessageValues";
        }

        @Override
        protected CodeBlock getScalarMethodBody(
                HelperMethodBuilder builder,
                Class<? extends WsContext> wrapperType,
                String defaultValue, // Unused?
                String wrapper,
                String key) {
            if (wrapperType == WsMessageContext.class) {
                return CodeBlock.builder().addStatement("return $N.getMessage()", wrapper).build();
            } else {
                return CodeBlock.builder().addStatement("return null").build();
            }
        }

        @Override
        protected CodeBlock getCollectionMethodBody(
                HelperMethodBuilder builder,
                Class<? extends WsContext> wrapperType,
                String defaultValue,
                String wrapper,
                String key) {
            if (wrapperType == WsMessageContext.class) {
                return CodeBlock.builder()
                    .addStatement("return $T.singletonList($N.getMessage())", Collections.class, wrapper)
                    .build();
            } else {
                return CodeBlock.builder()
                    .addStatement("return $T.emptyList()", Collections.class)
                    .build();
            }
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return null;
        }
    }

    private static final class WsAnyHelper extends WsSourceHelper {
        @Override
        public WsValueSource getValueSource() {
            return WsValueSource.Any;
        }

        @Override
        public String getScalarName() {
            return "getWsValue";
        }

        @Override
        public String getCollectionName() {
            return "getWsValues";
        }

        @Override
        protected CodeBlock getScalarMethodBody(
                HelperMethodBuilder builder,
                Class<? extends WsContext> wrapperType,
                String defaultValue,
                String wrapper,
                String key) {
            // Build the source helpers for all the other source types
            for (Map.Entry<WsValueSource, WsSourceHelper> entry : WS_SOURCE_HELPER_LOOKUP.entrySet()) {
                WsValueSource source = entry.getKey();
                if (source != WsValueSource.Any) {
                    WsSourceHelper helper = entry.getValue();
                    helper.buildScalarHelper(builder, wrapperType, null /* We don't pass defaultValue here */);
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
                        codeBuilder.addStatement("return $N($N, $N)", helper.getScalarName(), wrapper, key);
                        codeBuilder.endControlFlow();
                    }
                }
            }
            if (defaultValue == null) {
                WsSourceHelper messageHelper = WS_SOURCE_HELPER_LOOKUP.get(WsValueSource.Message);
                codeBuilder.addStatement("return $N($N, $N)", messageHelper.getScalarName(), wrapper, key);
            } else  {
                codeBuilder.addStatement("return $N", "defaultValue");
            }
            return codeBuilder.build();
        }

        @Override
        protected CodeBlock getCollectionMethodBody(
                HelperMethodBuilder builder,
                Class<? extends WsContext> wrapperType,
                String defaultValue,
                String wrapper,
                String key) {
            // Build the source helpers for all the other source types
            for (Map.Entry<WsValueSource, WsSourceHelper> entry : WS_SOURCE_HELPER_LOOKUP.entrySet()) {
                WsValueSource source = entry.getKey();
                if (source != WsValueSource.Any) {
                    WsSourceHelper helper = entry.getValue();
                    helper.buildCollectionHelper(builder, wrapperType, null /* We do not pass defaultValue here */);
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
                        codeBuilder.addStatement("return $N($N, $N)", helper.getCollectionName(), wrapper, key);
                        codeBuilder.endControlFlow();
                    }
                }
            }
            if (defaultValue == null) {
                WsSourceHelper messageHelper = WS_SOURCE_HELPER_LOOKUP.get(WsValueSource.Message);
                codeBuilder.addStatement("return $N($N, $N)", messageHelper.getCollectionName(), wrapper, key);
            } else {
                codeBuilder.addStatement("return $T.singletonList($N)", Collections.class, "defaultValue");
            }
            return codeBuilder.build();
        }

        @Override
        public CodeBlock getPresenceCheck(String request, String key) {
            return null;
        }
    }

    private static final class WsScalarSourceKey {
        private final WsValueSource source;
        private final Class<? extends WsContext> contextType;
        private final boolean hasDefaultValue;

        private WsScalarSourceKey(
                WsValueSource source,
                Class<? extends WsContext> contextType,
                boolean hasDefaultValue) {
            this.source = source;
            this.contextType = contextType;
            this.hasDefaultValue = hasDefaultValue;
        }

        public static WsScalarSourceKey of(
                WsValueSource source,
                Class<? extends WsContext> contextType,
                boolean hasDefaultValue) {
            return new WsScalarSourceKey(source, contextType, hasDefaultValue);
        }

        public WsValueSource getSource() {
            return source;
        }

        public Class<? extends WsContext> getContextType() {
            return contextType;
        }

        public boolean isHasDefaultValue() {
            return hasDefaultValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            WsScalarSourceKey that = (WsScalarSourceKey) o;
            return hasDefaultValue == that.hasDefaultValue
                && source == that.source
                && contextType.equals(that.contextType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, contextType, hasDefaultValue);
        }
    }

    private static final class WsCollectionSourceKey {
        private final WsValueSource valueSource;
        private final Class<? extends WsContext> contextType;
        private final boolean hasDefaultValue;

        private WsCollectionSourceKey(
                WsValueSource valueSource,
                Class<? extends WsContext> contextType,
                boolean hasDefaultValue) {
            this.valueSource = valueSource;
            this.contextType = contextType;
            this.hasDefaultValue = hasDefaultValue;
        }

        public static WsCollectionSourceKey of(
                WsValueSource valueSource,
                Class<? extends WsContext> contextType,
                boolean hasDefaultValue) {
            return new WsCollectionSourceKey(valueSource, contextType, hasDefaultValue);
        }

        public WsValueSource getValueSource() {
            return valueSource;
        }

        public Class<? extends WsContext> getContextType() {
            return contextType;
        }

        public boolean isHasDefaultValue() {
            return hasDefaultValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            WsCollectionSourceKey that = (WsCollectionSourceKey) o;
            return hasDefaultValue == that.hasDefaultValue && valueSource == that.valueSource && contextType.equals(that.contextType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(valueSource, contextType, hasDefaultValue);
        }
    }

    // endregion

    private static final class ConversionTypeKey {
        private final ValueSource source;
        private final TypeElement element;
        
        private ConversionTypeKey(ValueSource source, TypeElement element) {
            this.source = source;
            this.element = element;
        }

        public static ConversionTypeKey of(ValueSource source, TypeElement element) {
            return new ConversionTypeKey(source, element);
        }

        public ValueSource getSource() {
            return source;
        }

        public TypeElement getElement() {
            return element;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ConversionTypeKey that = (ConversionTypeKey) o;
            return source.equals(that.source)
                && element.getQualifiedName().toString().equals(that.element.getQualifiedName().toString());
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, element.getQualifiedName().toString());
        }
    }

    private static final class WsConversionTypeKey {
        private final WsValueSource source;
        private final TypeElement element;
        private final Class<? extends WsContext> contextType;

        private WsConversionTypeKey(
                WsValueSource source,
                TypeElement element,
                Class<? extends WsContext> contextType) {
            this.source = source;
            this.element = element;
            this.contextType = contextType;
        }

        public static WsConversionTypeKey of(
                WsValueSource source,
                TypeElement element,
                Class<? extends WsContext> contextType) {
            return new WsConversionTypeKey(source, element, contextType);
        }

        public WsValueSource getSource() {
            return source;
        }

        public TypeElement getElement() {
            return element;
        }

        public Class<? extends WsContext> getContextType() {
            return contextType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            WsConversionTypeKey that = (WsConversionTypeKey) o;
            return source.equals(that.source)
                && element.getQualifiedName().toString().equals(that.element.getQualifiedName().toString())
                && contextType.getSimpleName().equals(that.contextType.getSimpleName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(
                source,
                element.getQualifiedName().toString(),
                contextType.getSimpleName()
            );
        }
    }
}
