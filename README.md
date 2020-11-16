# Javalin MVC
Build Javalin route handlers at compile time using controllers and action methods.

## Javalin
When I started working in Java, I took a look at the Java ecosystem for popular web libraries and frameworks. To be honest, I was disappointed to see the most popular options are still monolithic frameworks. Coming from a .NET background, I got to watch as ASP.NET MVC evolved from a massive, opinionated framework to the fast and flexible ASP.NET MVC Core it has become. Coming to Java, using a monolithic framework felt like a step backwards, so I started looking at other alternatives.

That's when I found [Javalin](https://javalin.io): it's a light-weight web library similar to Node.js' Express, where you simply provide routes and route handlers (lambdas) to process requests. You literally have running code within 5 minutes of creating your project. A quick scroll through their documentation and you already know almost everything you will ever need to know. It's simple: the way it should be.

## Why Javalin MVC?
While Javalin is simple and amazing, you might find yourself wishing it had a couple niceties. For one, you'll probably find yourself doing a lot of conversions from `string` to `int` or writing the same code to deserialize your JSON. If you build anything substantial in size, you might also find having all your route handlers in one file is a bit much to sift through. How can we simplify role-based authentication, logging, etc.?

Coincidentally, I was learning about Java's [annotation processing](https://medium.com/@jintin/annotation-processing-in-java-3621cb05343a) around the same time I started playing with Javalin. It occurred to me I could build routes automatically by inspecting annotations and generating the code at compile time. Javalin MVC just translates controller classes and "action methods" into Javalin routes at compile time.

One of the major benefits to Javalin MVC being a compile time tool is that there's absolutely no runtime overhead for using this library. It's as fast as if you wrote all the Javalin route handlers by hand. Not only that, but it's a compile-time error if you try to register two HTTP methods/routes multiple times -- with raw Javalin you'd only discover that error at runtime!

## Recent News (11/15/2020)
Big improvements are in the works! A few weeks ago, I was staring at this code and I started thinking. In version 1.0.x, I was using a `ModelBinder` class to bind values. This class was pretty optimal so there was little runtime overhead. But my thought was, "Why should there be any runtime overhead at all?". Almost everything I need to perform binding I already know at compile time. Really, the only thing I *don't* know is the actual value. That means I can generate helper methods to perform primitive parsing and avoid any overhead of runtime type casting, auto-boxing, etc.

I also now support binding values into model classes. This means you can pull values from query strings, headers, cookies, etc. and place them inside of a class now, rather than having a bunch of action method parameters.

For the ultimate flexibility, I added support for specifying your own converters. You can now point to a static method or instance method and it will be used to parse your types.

I also greatly increasing my unit test code coverage, so I have addressed several issues in the past few weeks. Along with this, I have improved the compile-time error messages. I am also going to investigate how I can better support incremental builds, but I will probably do a release prior to this.

## Installation
The following dependencies are needed in your web project:

```xml
<!-- Javalin, of course -->
<dependency>
    <groupId>io.javalin</groupId>
    <artifactId>javalin</artifactId>
    <version>3.6.0</version>
</dependency>
<!-- Dependency Injection via Dagger. Optional!!! -->
<dependency>
    <groupId>com.google.dagger</groupId>
    <artifactId>dagger</artifactId>
    <version>2.25.2</version>
</dependency>
<!-- Javalin MVC -->
<dependency>
    <groupId>com.truncon</groupId>
    <artifactId>javalin-mvc-api</artifact>
    <version>1.0.2</version>
</dependency>
<dependency>
    <groupId>com.truncon</groupId>
    <artifactId>javalin-mvc-core</artifact>
    <version>1.0.2</version>
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

Javalin MVC uses annotation processing (more on this later) so must be setup in your web project's `pom.xml` in order to be run at compile time. If you do not want to use Dagger, you can exclude the dagger-compiler configuration below: 

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
                        <version>1.0.2</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

I have no idea how to configure Javalin MVC to run with Gradle, although it should mirror closely how Dagger is configured. Feel free to submit a PR with the steps listed here if you get this working and you're feeling generous. 😁

## Defining a controller
A controller is a class decorated with the `@Controller` annotation. It can have one or more methods annotated with `@HttpGet`, `@HttpPost`, `@HttpPut`, `@HttpPatch`, `@HttpDelete`, `@HttpHead`, or `@HttpOptions`. Each method is associated with a route and will cause a Javalin route handler to be generated. The route handler simply creates an instance of the controller, then calls the method. Simple!

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

The route handler generation happens at compile time, so there's no runtime overhead. It's as if you wrote it all by hand.

## Action methods
Action method parameters can be bound to values coming from your request headers, route parameters, query strings, form fields (url encoded) or the request body (JSON, etc.). By default, the method parameter and the request parameter are matched by name, but the `@Named` annotation can used to override this behavior. Furthermore, you can say explicitly where to bind a parameter from using the `@FromPath`, `@FromQuery`, `@FromHeader` or `@FromCookie`, or `@FromForm` annotations.

Consider this example:

```Java
@HttpGet(route="/")
public ActionResult getGreeting(String name, Integer age) { 
    return new ContentResult("Hello " + name + "! You are " + age + " years old!");
 }
```

In the example above, the first route parameter, query string parameter, etc. matching the name `name` or `age` will be passed to the `getGreeting` method. In the case of `age`, the value with be automatically converted from a `String` to an `Integer`.

You can also inject the `HttpContext`, the `HttpRequest` and/or the `HttpResponse` objects into action methods, as well. This is useful for grabbing other information about the request or manually specifying the response. 

Your action methods should return instances of `ActionResult`. An `ActionResult` exists for each type of response (JSON, plain text, status codes, redirects, etc.). You can also return `void`, in which case you must provide a response via `HttpResponse`. If you return a primitive or object, it will be serialized as JSON with a 200 OK response.

### ❗ Performance/Security Note
For the best performance and for added security, you should *always* explicitly specify where your values are coming from, using one of the `@From*` annotations. Otherwise, the route handle must look for values at runtime. Furthermore, you wouldn't want someone accidentally/maliciously overwriting an authentication header with a query string! 😬

## An example main
An example `main` method might look like this:

```java
public static void main(String[] args) throws IOException {
    Javalin app = Javalin.create(config -> {
        // Remove the following line to disable Open API annotation processing
        config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
        // This example is using the SPA feature
        config.addStaticFiles("./public", Location.EXTERNAL);
        config.addSinglePageRoot("/", "./public/index.html", Location.EXTERNAL);
    });

    // Provide method of constructing a new DI container
    // Dagger prepends "Dagger" automatically at compile time
    Supplier<WebContainer> scopeFactory = () -> DaggerWebContainer.builder().build();
    // Javalin MVC generates "com.truncon.javalin.mvc.JavalinControllerRegistry" automatically at compile time
    ControllerRegistry registry = new JavalinControllerRegistry(scopeFactory);
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

If you have access to the generated sources, you can inspect the generated `JavalinControllerRegistry.java` file. If you do, you will see most of the file is comprised of calls to `app.get(...)`, `app.post(...)`, etc.

## Supported Features
Here is a list of supported and/or desired features. An `x` means it is already supported. Feel free to submit an issue for feature requests!!!

* [x] Specify controllers via `@Controller`
    * [ ] Allow specifying common route prefix for all action methods.
* [x] Specify routes via `@HttpGet`, `@HttpPost`, etc.
* [x] Bind parameters from headers, cookies, URL parameters, query strings, and form data by name.
    * [x] String
    * [x] Integer/int
    * [x] Boolean/boolean
    * [x] Long/long
    * [x] Short/short
    * [x] Byte/byte
    * [x] Double/double
    * [x] Float/float
    * [x] BigInteger
    * [x] BigDecimal
    * [x] Dates
        * [x] Date
        * [x] Instant
        * [x] OffsetDateTime
        * [x] ZonedDateTime
        * [x] LocalDateTime
        * [x] LocalDate
        * [x] YearMonth
        * [x] Year
    * [x] UUID
    * [x] Arrays
    * [ ] Collection types (`List<T>`, `Set<T>`, `Map<K, V>`, etc.) 
    * [x] File uploads
* [x] Bind Java object from request body (JSON)
* [x] Bind Java object from other sources
    * [x] Support `@Named` annotation on fields and setter methods
    * [x] Support setting int, short, byte, char, String, Date, etc.
    * [x] Support setting arrays of int, short, byte, char, String, Date, etc.
    * [x] Support binding values from headers, cookies, URL parameters, query strings, and form data
    * [x] Support overriding binding source using `@From*` annotations on a specific member.
* [x] Override where parameters are bound from using `@From*` annotations.
* [x] Support returning `ActionResult` implementations
    * [x] `ContentResult` - return plain strings
    * [x] `JsonResult` - return Object as JSON
    * [x] `StatusCodeResult` - return HTTP status code (no body)
    * [x] `RedirectResult` - tell the browser to redirect to another URL
    * [x] `StreamResult` - respond with a stream of bytes (think images, movies, etc.)
    * [x] `DownloadResult` - respond so the browser prompts to download the response
* [x] Support returning non-`ActionResult` values
    * [x] void (a response must be provided via `HttpResponse` manually)
    * [x] Primitives, Strings, Dates, UUIDs, etc.
    * [x] Objects using JsonResult
* [x] Support parameter naming flexibility via `@Named` annotation
* [x] Support custom/alternative parameter name bindings via `@Named`
* [x] Support pre-execution interceptor via `@Before`
* [x] Support post-execution interceptor via `@After`
* [x] Support async operations (by returning `CompletableFuture<T>`)
* [x] Support Dagger dependency injection
    * [x] Inject controller dependencies
    * [x] Inject injector (self-injection)
    * [x] Inject context/request/response objects
* [x] Open API/Swagger Annotations
    * [x] Now uses built-in Javalin OpenAPI annotations
* [ ] WebSockets
    * [x] Specify routes via `@WsController`
    * [x] Support `@WsConnect`, `@WsDisconnect`, `@WsError`, `@WsMessage` and WsBinaryMessage annotations
    * [x] Support for data binding
    * [ ] Support for @Before and @After handlers
* [x] Custom conversion methods
    * [x] Support `@Converter` on static methods
    * [x] Support `@Converter` on instance methods
    * [x] Support `@UseConverter` annotations on types
    * [x] Support `@UseConverter` annotations on parameters
    * [x] Support `@UseConverter` annotations on fields/setters
    * [x] Support `@UseConverter` annotations on setter parameters.
    * [x] Support HTTP and WebSocket conversions

## Dagger (Optional)
Dependency injection is at the core of modern software projects. It supports switching between implementations at runtime and promotes testability. Historically, dependency injection has utilized runtime reflection to instantiate objects and inject them. However, waiting to perform injection until runtime comes with the risk of missing bindings that will lead to system failure. There's also the overhead of constructing objects using reflection. However, the [Dagger](https://google.github.io/dagger/) project uses annotation processing to provide compile-time dependency injection. This provides all the benefits of using an inversion of control (IoC) container without the risk of missing bindings causing runtime failures. There's also minimal overhead because there's no reflection involved.

Dagger is integrated into `javalin-mvc-core`, making Dagger the default DI; however, your code will still compile if you choose not to use it. Dagger, being a compile-time DI library, has a somewhat different API than other DI libraries. Instead of having a global `injector.get(Class<?> clz)` method that can be used to retrieve every type of object, there are specific methods for each dependency. Ideally, a generic DI interface could be provided so `javalin-mvc-core` could work against *any* DI library, but this dramatic difference in API makes that infeasible. It was a tough decision, but I ended up choosing Dagger. Technically, you can wire in your own choice of DI library on top of Dagger.

The `javalin-mvc-core` project needs to know how to instantiate objects with Dagger; to do this, you must mark your Dagger container with the `ControllerComponent` annotation. Your Dagger container will, minimally, look like this:

```java
@Component
@ControllerComponent
public interface WebContainer {
}

```

This allows Javalin MVC to instantiate objects as needed. If any of this is missing, Javalin MVC will assume your controllers have default constructors.

One final note about dependency injection with Dagger: by default, the lifetime (a.k.a., the scope) of the dependencies is managed at the request level. This means `@Singleton`s only live for the life of a request. You can easily support longer lifetimes using `@Provides` and returning pre-constructed objects. 

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
@Before(handler = Log.class, arguments = { "CustomerController.update" })
@After(handler = Log.class, arguments = { "CustomerController.update" })
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

## Known Limitations
At the time of this writing, Javalin MVC does not support incremental compilation. This means that all of your classes must be compiled in the same `javac` command in order for the full `JavalinControllerRegistry.java` file to be generated. This is the default behavior using Maven (e.g., `mvn compile`) but if you are working in an IDE, such as IntelliJ, you might need to use the "Rebuild" command or run Maven as a separate step.

A common alternative workflow is to temporarily enable annotation processing and run `mvn compile` from the command line. Then simply copy the generated `/target/generated-sources/annotations/com/truncon/javalin/mvc/JavalinControllerRegistry.java` file into your codebase and disable the annotation processor again until you make another change. This is a bit of a pain but is useful for people who only intend to use this project to save themselves typing route handlers by hand.

Also, note some IDEs incorrectly run the Dagger and Javalin MVC annotation processors out of order or at random. Dagger should always run first - otherwise Javalin MVC can't see that a dependency is provided. This often manifests itself as Javalin MVC trying to default-construct a class without a default constructor. 😬 

## Submitting Issues/Feature Requests
Please let me know if you encounter any issues using Javalin MVC by submitting an issue. Also feel free to create issues for new feature requests. Also feel free to submit PRs for bug fixes and feature requests. Any help will be appreciated!

## License
The MIT License

Copyright (c) 2020 Travis Wayne Parks. http://www.truncon.com

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
