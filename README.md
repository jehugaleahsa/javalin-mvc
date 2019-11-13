# Javalin MVC
Build Javalin route handlers at compile time using controllers and action methods.

## Javalin
This MVC library utilizes [Javalin](http://javalin.io). It is a very lightweight Java REST API framework that avoids the overhead and complexity of more full-blown web frameworks. That makes it more appropriate for small services that run exclusively behind a reverse proxy (e.g., NGinX). It can also be built with Maven, so can exist nicely with other Java projects.

### Controllers are awesome!
While Javalin is pretty straight-forward, it does lack some niceties that improve developer productivity. For one, you don't want to have to manually extract values from URL parameters, query strings, headers and the request body (e.g., JSON). You also don't want to manually deal with different responses. You don't want to have a 1,000 line block of code at the top of your app registering all of the routes. You don't want to have to deal with dependency injection, request filtering, error handling, logging, etc. That's why this project exists.

Basically, it's a compile-time tool (via Java's [annotation processing](https://medium.com/@jintin/annotation-processing-in-java-3621cb05343a)) that converts decorated classes into Javalin route handlers. The annotation processing tool is implemented in the `javalin-mvc-core` project. The `javalin-mvc-api` project provides only interfaces and annotations (and classes implemented in terms of those interfaces). The `javalin-mvc-core` project takes care of implementing those interfaces and including them in the generated code.

## Installation
The following dependencies are needed in your web project:

```xml
<!-- Javalin, of course -->
<dependency>
    <groupId>io.javalin</groupId>
    <artifactId>javalin</artifactId>
    <version>3.6.0</version>
</dependency>
<!-- Dependency Injection -->
<dependency>
    <groupId>com.google.dagger</groupId>
    <artifactId>dagger</artifactId>
    <version>2.25.2</version>
</dependency>
<!-- Javalin MVC -->
<dependency>
    <groupId>com.truncon</groupId>
    <artifactId>javalin-mvc-api</artifact>
    <version>1.0.1</version>
</dependency>
<dependency>
    <groupId>com.truncon</groupId>
    <artifactId>javalin-mvc-core</artifact>
    <version>1.0.1</version>
</dependency>
<!-- The rest of these dependencies are for OpenAPI support. Optional!!! -->
<dependency>
    <groupId>io.swagger.core.v3</groupId>
    <artifactId>swagger-core</artifactId>
    <version>2.0.8</version>
</dependency>
<dependency>
    <groupId>cc.vileda</groupId>
    <artifactId>kotlin-openapi3-dsl</artifactId>
    <version>0.20.1</version>
</dependency>
<dependency>
    <groupId>org.webjars</groupId>
    <artifactId>swagger-ui</artifactId>
    <version>3.23.8</version>
</dependency>
<dependency>
    <groupId>io.github.classgraph</groupId>
    <artifactId>classgraph</artifactId>
    <version>4.8.34</version>
</dependency>
```

Javalin MVC uses annotation processing (more on this later) so must be setup in your web project's `pom.xml` in order to be run at compile time:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <annotationProcessorPaths>
                    <path>
                        <groupId>com.google.dagger</groupId>
                        <artifactId>dagger-compiler</artifactId>
                        <version>2.25.2</version>
                    </path>
                    <path>
                        <groupId>com.truncon</groupId>
                        <artifactId>javalin-mvc-core</artifactId>
                        <version>1.0.1</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

#### Defining a controller
A controller is a class decorated with the `@Controller` annotation. Any methods associated with a route will cause a Javalin route configuration to be generated which creates an instance of the controller, then calls the method. Simple!

So this:

```Java
@Controller
public final class HomeController {
    @HttpGet(route="/")
    public ActionResult index() {
        return JsonResult(new IndexModel());
    }
} 
```

is *essentially* converted to this:

```Java
Javalin app = Javalin.create();
app.get("/", ctx -> {
    HomeController controller = new HomeController();
    ActionResult result = controller.index();
    result.execute(ctx);
});
app.start(5000);
```

#### Controller Actions
If a controller method is found that's decorated with `@HttpGet`, `@HttpPost`, etc., the processor will add a Javalin route that instantiates the controller and calls the method automatically. The route handler generation happens at compile time, so there's almost no runtime overhead. It's as if you wrote it all by hand.

Action method parameters can be bound to values coming from your request headers, route parameters, query strings, form fields (url encoded) or the request body (JSON, etc.). By default, the method parameter and the request parameter are matched by name, but the `@Named` annotation can used to override this behavior. Furthermore, you can say explicitly where to bind a parameter from using the `@FromForm`, `@FromHeader`, `@FromPath` or `@FromQuery` annotations.

Consider this example:

```Java
@HttpGet(route="/")
public ActionResult getGreeting(String name, Integer age) { 
    return new ContentResult("Hello " + name + "! You are " + age + " years old!");
 }
```

In the example above, the first route parameter, query string parameter, etc. matching the name `name` or `age` will be passed to the `getGreeting` method. In the case of `age`, the value with be automatically converted from a `String` to an `Integer`.  

You can also inject the `HttpContext`, the `HttpRequest` and/or the `HttpResponse` objects into action methods, as well. This is useful for grabbing other information about the request or manually specifying the response. 

Your action methods should return instances of `ActionResult`. An `ActionResult` exists for each type of response (JSON, plain text, status codes, redirects, etc.). You can also return `void`, in which case you must provide a response via `HttpResponse`.

#### Pending Features
Here is a list of supported and/or desired features. An `x` means it is already supported. Feel free to submit an issue for feature requests!!!

* [x] Specify controllers via `@Controller`
* [x] Specify routes via `@HttpGet`, `@HttpPost`, etc.
* [x] Bind parameters from headers, cookies, URL parameters, query strings, and form data by name.
    * [x] Strings
    * [x] Integer (reference type only)
    * [x] Boolean (reference type only)
    * [x] Long (reference type only)
    * [x] Short (reference type only)
    * [x] Byte (reference type only)
    * [x] Double (reference type only)
    * [x] Float (reference type only)
    * [x] BigInteger
    * [x] BigDecimal
    * [x] Dates
        * [x] Date
        * [x] Instant
        * [x] OffsetDateTime
        * [x] ZonedDateTime
        * [x] LocalDateTime
        * [x] LocalDate
    * [x] UUID
    * [x] Arrays
    * [x] File uploads
* [x] Bind Java object from request body (JSON)
* [ ] Bind Java object from other sources
    * [x] Support `Named` annotation on fields and setter methods
    * [x] Support setting int, short, byte, char, String, Date, etc.
    * [x] Support setting arrays of int, short, byte, char, String, Date, etc.
    * [x] Support binding values from headers, cookies, URL parameters, query strings, and form data
    * [ ] Support overriding binding source using `From*` annotations on a specific member.
* [x] Override where parameters are bound from.
* [x] Support returning `ActionResult` implementations
    * [x] ContentResult - return plain strings
    * [x] JsonResult - return Object as JSON
    * [x] StatusCodeResult - return HTTP status code (no body)
    * [x] RedirectResult - indicate client to redirect
    * [x] FileStreamResult - send file contents
* [x] Support returning non-`ActionResult` values
    * [x] void
    * [x] Primitives, Strings, Dates, UUIDs, etc.
    * [x] Objects using JsonResult
* [x] Support parameter naming flexibility
* [x] Support custom/alternative parameter name bindings
* [x] Support pre-execution interceptor
* [x] Support post-execution interceptor
* [x] Support async operations
* [x] Support dependency injection
    * [x] Inject controller dependencies
    * [x] Inject injector (self-injection)
    * [x] Inject context/request/response objects
* [x] Open API/Swagger Annotations
    * [x] Now uses built-in Javalin OpenAPI annotations
* [ ] WebSockets
    * [x] Specify routes via WsController
    * [x] Support WsConnect, WsDisconnect, WsError, WsMessage and WsBinaryMessage annotations
    * [x] Support for data binding
    * [ ] Support for @Before and @After handlers (does this make sense?)

## Dagger
Dependency injection is at the core of modern software projects. It supports switching between implementations at runtime and promotes testability. Historically, dependency injection has utilized runtime reflection to instantiate objects and inject them. However, waiting to perform injection until runtime comes with the risk of missing bindings that will lead to system failure. There's also the overhead of constructing objects using reflection. However, the [Dagger](https://google.github.io/dagger/) project uses annotation processing to provide compile-time dependency injection. This provides all the benefits of using an inversion of control (IoC) container without the risk of missing bindings causing runtime failures. There's also minimal overhead because there's no reflection involved.

Dagger is integrated into `javalin-mvc-core`, somewhat dictating the use of Dagger (although, I'm working on making it optional some day). Dagger, being a compile-time DI library, has a somewhat different API than other DI libraries. Instead of having a global `injector.get(Class<?> clz)` method that can be used to retrieve every type of object, there are specific methods for each dependency. Ideally, a generic DI interface could be provided so `javalin-mvc-core` could work against *any* DI library, but this dramatic difference in API makes that infeasible. It was a tough decision, but I ended up choosing Dagger. Technically, you can wire in your own choice of DI library on top of Dagger.

The `javalin-mvc-core` project needs to know how to instantiate objects with Dagger; to do this, you must mark your Dagger container with the `ControllerComponent` annotation. Your Dagger container will, minimally, look like this:

```java
@Component
@ControllerComponent
public interface WebContainer {
}

```

This allows Javalin MVC to instantiate objects as needed. If any of this is missing, Javalin MVC will assume your controllers have default constructors.

## An example main

An example `main` method might look like this:

```java
public static void main(String[] args) throws IOException {
    Javalin app = Javalin.create(config -> {
        // Remove the following line to disable Open API annotation processing
        config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
        // This example is using the new SPA feature
        config.addStaticFiles("./public", Location.EXTERNAL);
        config.addSinglePageRoot("/", "./public/index.html", Location.EXTERNAL);
    });

    // Provide method of constructing a new DI container
    // Dagger prepends "Dagger" automatically at compile time
    Supplier<WebContainer> scopeFactory = () -> DaggerWebContainer.builder().build();
    // Javalin MVC generates "com.truncon.javalin.mvc.ControllerRegistry" automatically at compile time
    ControllerRegistry registry = new ControllerRegistry(scopeFactory);
    registry.register(app);

    // Prevent unhandled exceptions from taking down the web server
    app.exception(Exception.class, (e, ctx) -> {
        logger.error("Encountered an unhandled exception.", e);
        ctx.status(500);
    });

    app.start(5000);
}

private static OpenApiOptions getOpenApiOptions() {
    return new OpenApiOptions(new Info()
            .version("1.0")
            .description("My API"))
        .path("/swagger")
        .swagger(new SwaggerOptions("/swagger-ui")
            .title("My API Documentation"));
}
```

If you have access to the generated sources, you can inspect the generated `ControllerRegistry.java` file. If you do, you will see most of the file is comprised of calls to `app.get(...)`, `app.post(...)`, etc.

## Before and After Handlers
One or more `@Before` annotations can be put on an action method. You pass it a `Class<?>` to specify which class will be used. The class must implement the `BeforeActionHandler` interface, overriding a method with the following signature:

```java
boolean executeBefore(HttpContext context, String[] arguments);
```

If `false` is returned, the request is cancelled. If you cancel a request, you should set the response. The `HttpRequest` and `HttpResponse` objects are retrieved from the `HttpContext` argument. The `String[]` argument contains any contextual information you want to provide to the processor.

One or more `@After` annotations can be put on an action method. You pass it a `Class<?>` to specify which class will be used. The class must implement the `AfterActionHandler` interface, overriding a method with the following signature:

```java
Exception executeAfter(HttpContext context, String[] arguments, Exception exception);
```

If executing a controller action results in an `Exception` being thrown, it will be passed as the last argument. The method can indicate that the exception has been handled by returning `null`. Otherwise, returning an exception indicates that the error should continue on to the next `@After` handler. Throwing an exception within an `@After` handler immediately jumps out of the request processing logic. Again, the `String[]` argument contains any contextual information you want to provide to the processor.

Here's an example `Log` handler that logs before and after an action fires:

```java
public final class Log implements BeforeActionHandler, AfterActionHandler {
    @Override
    public boolean executeBefore(HttpContext context, String[] arguments) {
        System.out.println("Before: " + String.join(",", arguments));
        return true; // Continue processing the request.
    }

    @Override
    public Exception executeAfter(HttpContext context, String[] arguments, Exception exception) {
        System.out.println("After: " + String.join(", ", arguments));
        if (exception != null) {
            System.err.println(exception.toString());
        }
        return exception; // Allow further exception processing, if needed.
    }
}
```

You might use `Log` like this:

```java
@HttpPost(route="/customers")
@Before(handler = Log.class, arguments = { "CustomerController.update" }))
@After(handler = Log.class, arguments = { "CustomerController.update" }))
public ActionResult update(Customer customer) {
    // ...
}
```

You can have as many `@Before` and `@After` annotations on a single action method as you need. They will be executed in the order they appear (top-down).

## OpenAPI/Swagger Support
You can directly use Javalin OpenAPI annotations on controller methods and they will appear in swagger/Swagger-UI. You must first configure Javalin to use swagger (see the example main above). Below is an absurd example demonstrating the majority of the annotations you can use.

```java
@HttpGet(route="/api/pickles")
@OpenApi(
    requestBody = @OpenApiRequestBody(
        content = @OpenApiContent(from = byte[].class),
        description = "Get all the pickles",
        required = true
    ),
    description = "Get all the pickles",
    summary = "Get all the pickles",
    responses = {
        @OpenApiResponse(
            status = "200",
            description = "Successfully retrieved all the pickles",
            content = @OpenApiContent(from = PickleModel[].class)
        )
    },
    tags = { "pickles", "all" },
    fileUploads = {
        @OpenApiFileUpload(name = "file", required = true, description = "A file to upload")
    },
    queryParams = {
        @OpenApiParam(name = "q", deprecated = true, description = "The search parameter"),
        @OpenApiParam(name = "e", description = "Search exclusions")
    },
    headers = {
        @OpenApiParam(name = "Content-Type", description = "What sort of data is passed.")
    },
    cookies = {
        @OpenApiParam(name = "Num num, me eat", required = true, description = "A cookie")
    }
)
public ActionResult index() {
    // ... crazy API implementation
}
```

One caveat is that you must ensure method names in your controllers are unique; otherwise, which documentation goes to which controller action becomes ambiguous. This is a good practice anyway.

## WebSockets
WebSockets are handled by marking classes with the `WsController` annotation. Unlike HTTP controllers, a WebSocket controller only handles a single route. Each WebSocket controller can process client connections, disconnections, errors, and messages (text or binary). The route the controller will handle is passed as a parameter to the `WsController` annotation. The methods within the controller can be marked with the `WsConnect`, `WsDisconnect`, `WsError`, `WsMessage`, or `WsBinaryMessage` annotations. Only one instance of each annotation can appear within a class; however, the same method can have multiple annotations.

Similar to HTTP controllers, method parameters can be bound from query strings, path parameters, headers, and cookies. However, there is no such thing as form data in WebSockets. If you want to explicitly bind a value from a particular source, you can use the same `From*` annotations for HTTP. In addition, you can use the `FromMessage` to binary parameters directly from content of messages. The `FromMessage` annotation works for `String` as well as JSON objects. You can also use `FromMessage` to bind binary messages to `byte[]` or `ByteBuffer` parameters.

If a method accepts a `WsContext` object, it will have direct access to the context object. Similarly, you can bind to `WsRequest` and `WsResponse` objects. A method-specific sub-interface exists for each method, so there is a `WsConnectContext`, `WsDisconnectContext`, `WsErrorContext`, `WsMessageContext`, and `WsBinaryMessageContext` that can be used as parameters, as well; however, these will only be initialize if used on the appropriate method.

You can send responses to the client using the `WsResponse.send` methods; however, you can also return an instance of `WsActionResult` from the `WsMessage` handler. Currently, the only support result types are `WsContentResult` for sending plain text, `WsJsonResult` for sending JSON results, and `WsByteArrayResult` and `WsByteBufferResult` for sending binary results. Similar to HTTP controllers, you can also just return from your method and it will be serialized appropriately.

```java
@WsController(route="/ws/pickles")
public final class WsPickleController {
    @Inject
    public WsPickleController() {
    }

    @WsConnect
    public void onConnect(WsConnectContext context) {
    }

    @WsDisconnect
    public void onDisconnect(WsDisconnectContext context) {
    }

    @WsError
    public void onError(WsErrorContext context) {
    }

    @WsMessage
    public WsActionResult onMessage(@FromMessage Payload payload) {
        return new WsJsonResult(payload);
    }
}
```
