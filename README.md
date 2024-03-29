# Javalin MVC
Build Javalin route handlers at compile time using controllers and action methods.

> Upgrade Note: Javalin released version 5.x. Read this [summary](./what-is-new-in-javalin-mvc-5.md) to see what changed! Use the 4.x version of this library to continue using Javalin 4.x. Use 5.x to use Javalin 5.x!

## Javalin
We need something simple, we need something fast. [Javalin](https://javalin.io) is a very simple, very fast HTTP/WebSocket routing middleware library that runs on Jetty. If you haven't learned about Javalin yet, go do that first!

## Why Javalin MVC?
While Javalin is simple and amazing, you might find yourself wishing it had a couple niceties. For one, you'll probably find yourself doing a lot of conversions from `string` to `int` or writing the same code to deserialize your JSON. If you build anything substantial, you might also find having all your route handlers in one file is a bit much to sift through. How can we simplify role-based authentication, logging, etc.?

Javalin MVC just translates controller classes and "action methods" into Javalin routes at *compile time*. Because it's compile time, there's absolutely no runtime overhead for using Javalin MVC over plain Javalin. It's as fast as if you wrote all the Javalin route handlers by hand! Not only that, but it's a compile-time error if you try to register the same route multiple times. Importantly, Javalin MVC doesn't try to hide Javalin, so you can choose just how much you want to use Javalin MVC and adopt it over time.

## Quick Example
If you just want to get started quickly, you can view the Javalin MVC quick start example, found [here](https://github.com/jehugaleahsa/javalin-mvc-starter).

## Installation
The following dependencies are needed in your web project:

```xml
<dependencies>
  <!-- Javalin -->
  <dependency>
    <groupId>io.javalin</groupId>
    <artifactId>javalin</artifactId>
    <version>5.1.3</version>
  </dependency>
  <!-- Javalin MVC -->
  <dependency>
    <groupId>com.truncon</groupId>
    <artifactId>javalin-mvc-api</artifactId>
    <version>5.0.1</version>
  </dependency>
  <dependency>
    <groupId>com.truncon</groupId>
    <artifactId>javalin-mvc-core</artifactId>
    <version>5.0.1</version>
  </dependency>
</dependencies>
```

Javalin MVC uses annotation processing, so must be setup in your web project's `pom.xml` in order to be run at compile time: 

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.10.1</version>
            <configuration>
                <annotationProcessorPaths>
                    <!-- Required -->
                    <path>
                        <groupId>com.truncon</groupId>
                        <artifactId>javalin-mvc-generator</artifactId>
                        <version>5.0.1</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

I have no idea how to configure Javalin MVC to run with Gradle. Feel free to submit a PR with the steps listed here if you get this working, and you're feeling generous. 😁

## Defining a controller
A controller is a class decorated with the `@Controller` annotation. It can have one or more methods annotated with `@HttpGet`, `@HttpPost`, `@HttpPut`, `@HttpPatch`, `@HttpDelete`, `@HttpHead`, or `@HttpOptions`. These annotated methods are known as "action methods", and each are associated with a route that will cause a Javalin route handler to be generated. The route handler simply creates an instance of the controller, then calls the method. Simple!

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

is *essentially* turned into this:

```java
app.get("/", ctx -> {
    HomeController controller = new HomeController();
    ActionResult result = controller.index();
    result.execute(ctx);
});
```

Remember, the route handler generation happens at compile time, so there's no runtime overhead.

## Action methods
Action method parameters can be bound to the values coming from your request headers, route parameters, query strings, form fields (url encoded) or the request body (JSON, etc.). By default, the method parameter and the request parameter are matched by name, but the `@Named` annotation can be used to override this behavior. Furthermore, you can say explicitly where to bind a parameter by using the `@FromPath`, `@FromQuery`, `@FromHeader` or `@FromCookie`, or `@FromForm` annotations (this is *highly* recommended).

Consider this example:

```java
@HttpGet(route="/")
public ActionResult getGreeting(String name, Integer age) { 
    return new ContentResult("Hello " + name + "! You are " + age + " years old!");
 }
```

In the example above, the first route parameter, query string parameter, etc. named `name` or `age` will be passed to the `getGreeting` method. In the case of `age`, the value with be automatically converted from a `String` to an `Integer`.

You can also inject the `HttpContext`, the `HttpRequest` and/or the `HttpResponse` objects into action methods, as well. This is useful for grabbing other information about the request or manually specifying the response. 

Your action methods should return instances of `ActionResult`. An `ActionResult` exists for each type of response (JSON, plain text, status codes, redirects, etc.). You can also return `void`, in which case you must provide a response via `HttpResponse`. If you return a primitive or object, it will be serialized as JSON with a `200 OK` response.

### ❗ Performance/Security Note
For the best performance and for added security, you should *always* explicitly specify where your values are coming from, using one of the `@From*` annotations. Otherwise, the route handle must look for values at runtime. Furthermore, you wouldn't want someone accidentally/maliciously overwriting an authentication header with a query string! 😬

## An example main
An example `main` method might look like this:

```java
import com.truncon.javalin.mvc.ControllerRegistry;
import com.truncon.javalin.mvc.JavalinControllerRegistry;
// etc...

public final class App {
    public static void main(String[] args) throws IOException {
        Javalin app = Javalin.create(config -> {
            // Remove the following line to disable Open API annotation processing
            config.plugins.register(new OpenApiPlugin(getOpenApiOptions()));
            config.plugins.register(new SwaggerPlugin(new SwaggerConfiguration()));
            
            // Register you JSON mapper with whatever library you want
            ObjectMapper objectMapper = new ObjectMapper(); // customize as needed
            JsonMapper jsonMapper = new JavalinJackson(objectMapper);
            config.jsonMapper(jsonMapper);
            
            // This example is using the SPA feature with static files
            config.addStaticFiles("./public", Location.EXTERNAL);
            config.addSinglePageRoot("/", "./public/index.html", Location.EXTERNAL);
        });

        // Javalin MVC generates "com.truncon.javalin.mvc.JavalinControllerRegistry" automatically at compile time
        ControllerRegistry registry = new JavalinControllerRegistry();
        registry.register(app);

        // Prevent unhandled exceptions from taking down the web server
        app.exception(Exception.class, (e, ctx) -> {
            logger.error("Encountered an unhandled exception.", e);
            ctx.status(500);
        });

        app.start(5000);
    }

    private static OpenApiConfiguration getOpenApiOptions() {
        OpenApiConfiguration configuration = new OpenApiConfiguration();
        OpenApiInfo info = configuration.getInfo();
        info.setTitle("My API");
        info.setVersion("1.0");
        return configuration;
    }
}
```

If you have access to the generated sources, you can inspect the generated `JavalinControllerRegistry.java` file. If you do, you will see most of the file consists of calls to `app.get(...)`, `app.post(...)`, etc.

## Feature Overview
Here is a list of supported and/or desired features. An `x` means it is already supported. Feel free to submit an issue for feature requests!!!

* [x] Specify controllers via `@Controller`
    * [x] Allow specifying common route prefix for all action methods.
* [x] Specify routes for action methods
    * [x] `@HttpGet` 
    * [x] `@HttpPost`
    * [x] `@HttpPut`
    * [x] `@HttpPatch`
    * [x] `@HttpDelete`
    * [x] `@HttpHead`
    * [x] `@HttpOptions`
    * [ ] `@HttpConnect` (Not supported by Javalin)
    * [ ] `@HttpTrace` (Not supported by Javalin)
* [x] Bind parameters from headers, cookies, URL parameters, query strings, and form data by name.
    * [x] `String`
    * [x] `Integer`/`int`
    * [x] `Boolean`/`boolean`
    * [x] `Long`/`long`
    * [x] `Short`/`short`
    * [x] `Byte`/`byte`
    * [x] `Double`/`double`
    * [x] `Float`/`float`
    * [x] `BigInteger`
    * [x] `BigDecimal`
    * [x] Dates
        * [x] `Date`
        * [x] `Instant`
        * [x] `OffsetDateTime`
        * [x] `ZonedDateTime`
        * [x] `LocalDateTime`
        * [x] `LocalDate`
        * [x] `YearMonth`
        * [x] `Year`
    * [x] `UUID`
    * [x] Arrays
    * [x] Collection types
      * [x] `Iterable<T>`
      * [x] `Collection<T>`
      * [x] `List<T>`
      * [x] `Set<T>`
      * [x] `ArrayList<T>`
      * [x] `LinkedList<T>`
      * [x] `HashSet<T>`
      * [x] `LinkedHashSet<T>`
    * [x] File uploads
* [x] Bind Java object from request body (JSON)
* [x] Bind Java object from other sources
    * [x] Support `@Named` annotation on fields and setter methods
    * [x] Support setting int, short, byte, char, String, Date, etc.
    * [x] Support setting arrays of int, short, byte, char, String, Date, etc.
    * [x] Support binding values from headers, cookies, URL parameters, query strings, and form data
    * [x] Support overriding binding source using `@From*` annotations on a specific member.
* [x] Bind `InputStream` from request body
* [ ] Bind `byte[]` from request body (currently accessible via `HttpRequest`)
* [x] Override where parameters are bound from using `@From*` annotations.
* [x] Support binding default values using `@DefaultValue`
* [x] Support returning `ActionResult` implementations
    * [x] `ContentResult` - return plain strings
    * [x] `JsonResult` - return Object as JSON
    * [x] `StatusCodeResult` - return HTTP status code (without a body)
    * [x] `RedirectResult` - tell the browser to redirect to another URL
    * [x] `StreamResult` - respond with a stream of bytes (think images, movies, etc.)
    * [x] `DownloadResult` - respond so the browser does a file download
* [x] Support returning non-`ActionResult` values
    * [x] void (a response must be provided via `HttpResponse` manually)
    * [x] Primitives, Strings, Dates, UUIDs, etc.
    * [x] Objects using `JsonResult`
* [x] Support parameter naming flexibility via `@Named` annotation
* [x] Support pre-execution interceptor via `@Before`
* [x] Support post-execution interceptor via `@After`
* [x] Support async operations (by returning `CompletableFuture<T>`)
* [x] Support Dagger dependency injection
    * [x] Inject into controllers
    * [x] Inject into bound models
    * [x] Inject into converter classes
    * [x] Inject into `@Before` and `@After` handlers
* [x] Support runtime dependency injection (Guice, Weld, etc.)
    * [x] Inject into controllers
    * [x] Inject into bound models
    * [x] Inject into converter classes
    * [x] Inject into `@Before` and `@After` handlers
* [x] Partial [JAX-RS](https://projects.eclipse.org/projects/ee4j.rest) Support (`javax.ws.rs.*` only)
  * [x] `@Path`
  * [x] `@GET`, `@POST`, `@PUT`, `@PATCH`, `@DELETE`, etc.
  * [x] `@PathParam`, `@QueryParam`, `@HeaderParam`, `@CookieParam`, `@FormParam`
  * [x] `@DefaultValue`
* [x] Open API/Swagger Annotations
    * [x] Uses built-in Javalin OpenAPI annotations
* [x] WebSockets
    * [x] Specify routes via `@WsController`
    * [x] Support `@WsConnect`, `@WsClose`, `@WsError`, `@WsMessage` and `@WsBinaryMessage` annotations
    * [x] Support for data binding
    * [x] Support for `@Before` and `@After` handlers
    * [x] Support returning `CompletableFuture<T>`
    * [x] Support dependency injection
* [x] Custom conversion methods
    * [x] Support `@Converter` on static methods
    * [x] Support `@Converter` on instance methods
    * [x] Support `@UseConverter` annotations on types
    * [x] Support `@UseConverter` annotations on parameters
    * [x] Support `@UseConverter` annotations on fields/setters
    * [x] Support `@UseConverter` annotations on setter parameters.
    * [x] Support HTTP and WebSocket conversions

## Dagger (Optional)
There is direct support for [Dagger](https://dagger.dev). To use it, you must configure the Dagger annotation processor, like so:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.1</version>
    <configuration>
        <annotationProcessorPaths>
            <!-- Make sure Dagger comes first! -->
            <path>
                <groupId>com.google.dagger</groupId>
                <artifactId>dagger-compiler</artifactId>
                <version>2.44</version>
            </path>
            <path>
                <groupId>com.truncon</groupId>
                <artifactId>javalin-mvc-generator</artifactId>
                <version>5.0.1</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

This is the same plugin listed at the top of the README, but with a section added for Dagger. 

You must create a `@Component` interface like this:

```java
@Component
@MvcComponent
public interface WebContainer {
}
```

The `@MvcComponent` annotation tells Javalin MVC where to look when trying to resolve dependencies. *NOTE: Exactly one class in your project can be marked with `@MvcComponent`.* Next, you can use the `@Inject` annotation on the classes that need constructed:

```java
@Controller
public class MyController {
    private final Dependency dependency;

    @Inject
    public MyController(Dependency dependency) {
        this.dependency = dependency;
    }
}
```

Here, `Dependency` is just a make-believe interface for whatever dependency(s) your classes need. Now, reference those classes in the container:

```java
@Component
@MvcComponent
public interface WebContainer {
    MyController getMyController();
}
```

In order for `Dependency` to be resolved, you must tell Dagger how to create it. The easiest way is with a module:

```java
@Module
public interface AppModule {
    @Binds
    Dependency bindDependency(DependencyImpl impl);
}
```

Here, `DependencyImpl` is the concrete class that implements `Dependency`. Now we just need to register the module with the container:

```java
@Component(modules = AppModule.class) // <-- Add module class here
@MvcComponent
public interface WebContainer {
    MyController getMyController();
}
```

Finally, when you create the `JavalinControllerRegistry`, you need to pass an additional argument: a `Supplier<WebContainer>`:

```java
ControllerRegistry registry = new JavalinControllerRegistry(DaggerWebContainer::create);
registry.register(app);
```

The reason you pass a `Supplier<T>` is because each request spawns a new dependency injection scope. This means annotations like `@Singleton` will also be scoped to the request. If you want to share the same objects *across* requests, you can create them in the `main` and implement custom `@Provides` methods that return them. If you want to be really DI-friendly, you can have an application-wide container and a request-wide container and just inject the application-wide container into the request-wide container. I leave that to the reader.

## Runtime DI Frameworks (Guice, etc.)
Most DI frameworks are wired up at runtime, not at compile time. Javalin MVC also supports these DI frameworks. For this example, I am going to show how you could do this with [Guice](https://github.com/google/guice).

Some class in your project (any class, really) must be marked with the `@MvcModule`. We'll mark the Guice module, like so:

```java
@MvcModule
public final class GuiceAppModule extends AbstractModule {
    public GuiceAppModule() {
    }

    @Override
    protected void configure() {
        super.configure();
        bind(Dependency.class).to(DependencyImpl.class);
    }
}
```

In that example, we are using the bind DSL in the `configure()` method to bind the interface `Dependency` to the concrete class `DependencyImpl`.

Next, you can use the `@Inject` annotation on the classes that need constructed:

```java
@Controller
public class MyController {
    private final Dependency dependency;

    @Inject
    public MyController(Dependency dependency) {
        this.dependency = dependency;
    }
}
```

> Make sure you use either `javax.inject.Inject` or `jakarta.inject.Inject`. Some dependency injection frameworks will provide their own `@Inject` annotations, like Guice's `com.google.inject.Inject`, but all of them should also work with one of the standard annotations.

Finally, when you create the `JavalinControllerRegistry`, you need to pass an additional argument: a `Supplier<Injector>`. `Injector` is a very basic interface in the `javalin-mvc-api` library that Javalin MVC uses to construct your classes at runtime. Here is how you would implement the `Injector` interface for Guice:

```java
public final class GuiceInjector implements Injector {
    private final com.google.inject.Injector injector;
    
    public GuiceInjector(Module... modules) {
        this.injector = Guice.createInjector(modules);
    }

    @Override
    public <T> T getInstance(Class<T> clz) {
        return injector.getInstance(clz);
    }

    @Override
    public Object getHandle() {
        return injector;
    }
}
```

Now call the `JavalinControllerRegistry` constructor:

```java
Module module = new GuiceAppModule(); // We might as well reuse this across requests
ControllerRegistry registry = new JavalinControllerRegistry(() -> new GuiceInjector(module));
registry.register(app);
```

The reason you pass a `Supplier<Injector>` is because each request spawns a new dependency injection scope. This means annotations like `@Singleton` will also be scoped to the request. If you want to share the same objects *across* requests, you can create them in the `main` and bind the instances to the request-level implementations. If you want to be really DI-friendly, you can have an application-wide container and a request-wide container and just inject the application-wide container into the request-wide container. I leave that to the reader.

## Before and After Handlers
One or more `@Before` annotations can be put on an action method. You pass it a `Class<?>` to specify which class will be used. The class must implement the `BeforeActionHandler` interface, overriding a method with the following signature:

```java
void executeBefore(BeforeActionContext context);
```

The `BeforeHandlerContext` provides access to the `HttpContext`. There is also a `getArguments()` method to get any arguments passed to the `@Before` annotation. A `setCancelled(boolean)` method is provided to cancel the request. If you cancel a request, you should also set the response. Once a request is cancelled, no other handlers will fire.

One or more `@After` annotations can be put on an action method. You pass it a `Class<?>` to specify which class will be used. The class must implement the `AfterActionHandler` interface, overriding a method with the following signature:

```java
void executeAfter(AfterActionContext context);
```

If executing a controller action results in an `Exception` being thrown, it will be available in the `AfterActionContext` using the `getException()` method. The handler can mark an exception handled by passing `true` to the `setHandled(boolean)` method, or by passing `null` to the `setException(Exception)` method. If no handler marks the exception as handled, it will be rethrown. Throwing an exception within an `@After` handler immediately jumps out of the request processing logic. Again, there is also a `getArguments()` method to get any arguments passed to the `@After` annotation

Here's an example `Log` handler that logs before and after an action fires:

```java
public final class Log implements BeforeActionHandler, AfterActionHandler {
    @Override
    public void executeBefore(BeforeActionContext context) {
        System.out.println("Before: " + String.join(",", context.getArguments()));
    }

    @Override
    public void executeAfter(AfterActionContext context) {
        System.out.println("After: " + String.join(", ", context.getArguments()));
        if (exception != null) {
            System.err.println(exception.toString());
        }
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
You can directly use Javalin OpenAPI annotations on controller methods, and they will appear in openapi/swagger. You must first configure Javalin to use openapi (see the example main above). Below is an absurd example demonstrating the majority of the annotations you can use:

```java
@HttpGet(route="/api/pickles")
@OpenApi(
    path = "/api/pickles",
    methods = { HttpMethod.GET },
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
    },
    operationId = "getAllPickles"
)
public ActionResult index() {
    // ... crazy API implementation
}
```

One caveat is that you must ensure method names in your controllers are unique; otherwise, which documentation goes to which controller action becomes ambiguous. This is a good practice anyway.

## WebSockets
WebSockets are handled by marking classes with the `@WsController` annotation. Unlike HTTP controllers, a WebSocket controller only handles a single route. Each WebSocket controller can process client connections, disconnects, errors, and messages (text or binary). The route the controller will handle is passed as a parameter to the `@WsController` annotation. The methods within the controller can be marked with the `@WsConnect`, `@WsClose`, `@WsError`, `@WsMessage`, or `@WsBinaryMessage` annotations. Only one instance of each annotation can appear within a class; however, the same method can have multiple annotations.

Similar to HTTP controllers, method parameters can be bound from query strings, path parameters, 
headers, and cookies. If you want to explicitly bind a value from a particular source, you can use the 
same `@From*` annotations, just like for HTTP. In addition, you can use the `@FromBody` annotation 
to bind parameters directly from the message. You can also use `@FromBody` to bind binary messages 
to `byte[]`, `ByteBuffer`, or `InputStream` parameters.

> Note, there is no support for URL-encoded form data as this does not exist for WebSockets.

If a method accepts a `WsContext` object, it will have direct access to the context object. Similarly, you can bind to `WsRequest` and `WsResponse` objects. A method-specific sub-interface exists for each method, so there is a `WsConnectContext`, `WsCloseContext`, `WsErrorContext`, `WsMessageContext`, and `WsBinaryMessageContext` that can be used as parameters, as well; however, these will only be initialized if used on the appropriate method.

You can send responses to the client using the `WsResponse.send` methods; however, you can also return an instance of `WsActionResult` from the `WsMessage` handler. Currently, the only supported result types are `WsContentResult` for sending plain text, `WsJsonResult` for sending JSON results, and `WsByteArrayResult` and `WsByteBufferResult` for sending binary results. Similar to HTTP controllers, you can also just return from your method, and it will be serialized appropriately.

```java
@WsController(route="/ws/pickles")
public final class WsPickleController {
    @WsConnect
    public void onConnect(WsConnectContext context) {
        // Do something...
    }

    @WsClose
    public void onClose(WsCloseContext context) {
        // Do something...
    }

    @WsError
    public void onError(WsErrorContext context) {
        // Do something...
    }

    @WsMessage
    public WsActionResult onMessage(@FromBody Payload payload) {
        return new WsJsonResult(payload);
    }
}
```

## Model Binding
Data can come from a lot of places: the route (e.g., `/users/{userId}`), query strings (e.g., `/users?name=John%20Smith`), headers, cookies, form fields (URL-encoded form data), body JSON, etc. You are allowed to bind your data to objects, as well. Consider a page that listed all your users with server-side paging and searching. You might have a model that looks like this:

```java
public final class UserSearch {
    private Integer userId;
    private String searchValue;
    private int offset;
    private int pageSize;
    private String[] orderByFields;
    
    public Integer getUserId() {
        return userId;
    }

    @FromPath
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSearchValue() {
        return searchValue;
    }

    @FromQuery
    @Named("search-value")
    public void setSearchValue(String value) {
        this.searchValue = value;
    }

    public int getOffset() {
        return offset;
    }

    @FromQuery
    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    @FromQuery
    @Named("page-size")
    public void setPageSize(int size) {
        this.pageSize = size;
    }

    public String[] getOrderByFields() {
        return orderByFields;
    }

    @FromQuery
    @Named("order-by")
    public void setOrderByFields(String[] fields) {
        this.orderByFields = fields;
    }
}
```

And your controller might look like this:

```java
@Controller
public final class UserController {
    public static final String GET_USER_ROUTE  = "/api/users/{userId}";
    @HttpGet(route = GET_USER_ROUTE)
    public ActionResult getUser(UserSearch search) {
        // Write code to do search here
        return new JsonResult(result);
    }

    public static final String GET_USERS_ROUTE = "/api/users";
    @HttpGet(route = GET_USERS_ROUTE)
    public ActionResult getUsers(UserSearch search) {
        // Write code to do search here
        return new JsonResult(results);
    }
}
```

Because the `UserSearch` model has `@From*` annotations, Javalin MVC automatically determines that the `search` parameter should be constructed and populated with values.

### Valid Binding Targets
At this time, only public fields and setters can be bound. Private, protected and package-level access is not supported. You also cannot place the annotations on the getters, since the logic to for looking up the corresponding setter can be a bit ambiguous in some cases. Furthermore, setters must begin with "set" and have exactly one argument.

> In the future, I might add support for binding getters or package-level members. I may also generate compile errors if you place `@From*` annotations on non-public fields and setters, but right now Javalin MVC just ignores them.

### Naming
Notice that Javalin MVC respects the `@Named` annotations so your class fields/setters don't have to match your path/query string/etc. names exactly.

### Default @From Bindings
If you place a `@From*` annotation on the controller parameter, by default, Javalin MVC will try to bind from that source by default. You can use this to avoid annotating models with Javalin MVC if you want to keep those concerns separate. For example, placing `@FromQuery` before the parameter below would cause Javalin MVC to try to populate all the `UserSearch` setters from the query string, even if we removed the `@FromQuery` annotations from the `UserSearch` class members:

```java
    public static final String GET_USERS_ROUTE = "/api/users";
    @HttpGet(route = GET_USERS_ROUTE)
    public ActionResult getUsers(@FromQuery UserSearch search) {
        // Write code to do search here
        return new JsonResult(results);
    }
```

This says, by default, all public fields and setters should get their values from the query string parameters. Only if another `@From*` annotation is explicitly set will the value be searched for elsewhere.

### NoBinding
The challenge with using default bindings is that Javalin MVC will then try to bind any public field/setter. If there are fields/setters you don't want to be bound, you should mark that field/setter with the `@NoBinding` annotation. This is especially important for fields that might enforce security (although, these probably shouldn't be in your models anyway).

### Nesting and inheritance
If your application supports many paged searches, you might want to move the paging information into a base class or a nested model class. For example, you could have a `PaginationModel` class:

```java
public class PaginationModel {
    private String searchValue;
    private int offset;
    private int pageSize;
    private String[] orderByFields;

    public String getSearchValue() {
        return searchValue;
    }

    @FromQuery
    @Named("search-value")
    public void setSearchValue(String value) {
        this.searchValue = value;
    }

    public int getOffset() {
        return offset;
    }

    @FromQuery
    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    @FromQuery
    @Named("page-size")
    public void setPageSize(int size) {
        this.pageSize = size;
    }

    public String[] getOrderByFields() {
        return orderByFields;
    }

    @FromQuery
    @Named("order-by")
    public void setOrderByFields(String[] fields) {
        this.orderByFields = fields;
    }
}
```

And then you could implement `UserSearch` in a couple ways, via inheritance:

```java
public final class UserSearch extends PaginationModel {
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    @FromPath
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
```

Or via composition:

```java
public final class UserSearch {
    private Integer userId;
    private PaginationModel pagination;

    public Integer getUserId() {
        return userId;
    }

    @FromPath
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public PaginationModel getPagination() {
        return pagination;
    }

    public void setPagination(PaginationModel model) {
        this.pagination = model;
    }
}
```

Javalin MVC is smart enough to look at inherited members as well as search recursively within class model members for `@From*` annotations. This allows you to nest your models however you see fit.

> Note: in the second example above, you can mark the `setPagination` method with a `@From*` annotation to overwrite the default binding source, similar to the `@From*` annotation on the action method parameter.

### Dependency Injection
By default, Javalin MVC will try to initialize your models using the default constructor; however, if you registered your model with a DI container, Javalin MVC will use it to initialize your model.

### JSON and binary binding
Javalin MVC also allows you to use the `@FromBody` annotation on model members. Just be aware that binding the same binary data multiple times can result in unexpected behavior.

## Custom Conversion
One of the big enhancements with Javalin MVC 2.x is the introduction of custom converters. Custom conversions are performed using a pair of new annotations: `@Converter` and `@UseConverter`. Static and instance methods can be annotated with `@Converter`, so long as they use the correct signature. For example:

```java
@Converter("pair")
public static Pair parse(HttpRequest request, String name, ValueSource valueSource) {
    Map<String, List<String>> lookup = request.getSourceLookup(valueSource);
    List<String> values = lookup.get(name);
    return values.size() == 1 ? Pair.parse(values.iterator().next()) : null;
}
```

Minimally, a converter method must accept either an `HttpContext` or a `HttpRequest` for HTTP, or a `WsContext` or `WsRequest` for WebSockets. Additionally, a `String name` parameter can be included that captures the name of the parameter or model field/setter where the converter is being used, respecting the `@Named` annotation if present. Also, a `ValueSource` or `WsValueSource` parameter can be included to specify where the values should be sourced from, which is controlled by whatever `@From*` annotation is present on the parameter or model field/setter. These parameters can appear in whatever order you see fit.

Once a converter is defined, you can use it using the `@UseConverter` annotation. The `@UseConverter` annotation can be placed on action method parameters, on model fields/setters or on the type itself. For example:

```java
@UseConverter("pair")
public final class Pair {
    private final int first;
    private final int second;

    public Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }
    
    public static Pair parse(String value) {
        String[] parts = value == null ? null : value.split(":");
        return (parts == null || parts.length != 2) ? null : new Pair(parts[0], parts[1]);
    }

    // etc.
}
```

Javalin MVC always checks for `@UseConverter` first before trying to perform any other built-in conversion.

### Dependency Injection
For instance method converters (a.k.a, non-`static`), Javalin MVC will try to create the converter objects using the default constructor, by default. However, if you register your class with a DI container, Javalin MVC will use it to instantiate the converter.

### Incremental Compilation Support
As of Javalin MVC 4.x, there is very early, basic support for incremental builds. In IDE environments, like IntelliJ, compile times are improved by only compiling files that changed. However, the sources that are generated at compile-time by Javalin MVC require looking at every decorated controller class, etc. Javalin MVC solves this by keeping track of which files were previously used so the full output can be generated. Keep in mind that this is very experimental, and if you encounter any issues, please let me know. My expectation is that support for incremental builds will eliminate some of the more mystifying errors reported in the past.

Often, if you can't figure out why the build is failing, try cleaning the project (like deleting the `target` directory or whatever). In IntelliJ, you can also right-click on the module and select Rebuild to perform a full compilation.

## JAX-RS Support
If you are familiar with the JAX-RS API, specifically it's annotations, feel free to use them instead of the equivalent annotations provided by Javalin MVC. Simply add the following dependency:

```xml
<dependency>
    <groupId>jakarta.ws.rs</groupId>
    <artifactId>jakarta.ws.rs-api</artifactId>
    <version>3.1.0</version>
</dependency>
```

As of Javalin MVC 5.x, you must use the `jakarta.*` annotations instead 
of `javax.*`. Due to Javalin 5 targeting Java 11+, Javalin MVC also targets Java 11+ and the jakarta annotations are required in newer Java versions.

## Known Limitations
Also, note some IDEs incorrectly run the Dagger and Javalin MVC annotation processors out-of-order or at random. Dagger should always run first - otherwise Javalin MVC can't see that a dependency is provided. This often manifests itself as Javalin MVC trying to default-construct a class without a default constructor. 😬 

## Submitting Issues/Feature Requests
Please let me know if you encounter any issues using Javalin MVC by submitting an issue. Also feel free to create issues for new feature requests. Also feel free to submit PRs for bug fixes and feature requests. Any help will be appreciated!

## License
                                 Apache License
                           Version 2.0, January 2004
                        http://www.apache.org/licenses/

TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION

1. Definitions.

   "License" shall mean the terms and conditions for use, reproduction,
   and distribution as defined by Sections 1 through 9 of this document.

   "Licensor" shall mean the copyright owner or entity authorized by
   the copyright owner that is granting the License.

   "Legal Entity" shall mean the union of the acting entity and all
   other entities that control, are controlled by, or are under common
   control with that entity. For the purposes of this definition,
   "control" means (i) the power, direct or indirect, to cause the
   direction or management of such entity, whether by contract or
   otherwise, or (ii) ownership of fifty percent (50%) or more of the
   outstanding shares, or (iii) beneficial ownership of such entity.

   "You" (or "Your") shall mean an individual or Legal Entity
   exercising permissions granted by this License.

   "Source" form shall mean the preferred form for making modifications,
   including but not limited to software source code, documentation
   source, and configuration files.

   "Object" form shall mean any form resulting from mechanical
   transformation or translation of a Source form, including but
   not limited to compiled object code, generated documentation,
   and conversions to other media types.

   "Work" shall mean the work of authorship, whether in Source or
   Object form, made available under the License, as indicated by a
   copyright notice that is included in or attached to the work
   (an example is provided in the Appendix below).

   "Derivative Works" shall mean any work, whether in Source or Object
   form, that is based on (or derived from) the Work and for which the
   editorial revisions, annotations, elaborations, or other modifications
   represent, as a whole, an original work of authorship. For the purposes
   of this License, Derivative Works shall not include works that remain
   separable from, or merely link (or bind by name) to the interfaces of,
   the Work and Derivative Works thereof.

   "Contribution" shall mean any work of authorship, including
   the original version of the Work and any modifications or additions
   to that Work or Derivative Works thereof, that is intentionally
   submitted to Licensor for inclusion in the Work by the copyright owner
   or by an individual or Legal Entity authorized to submit on behalf of
   the copyright owner. For the purposes of this definition, "submitted"
   means any form of electronic, verbal, or written communication sent
   to the Licensor or its representatives, including but not limited to
   communication on electronic mailing lists, source code control systems,
   and issue tracking systems that are managed by, or on behalf of, the
   Licensor for the purpose of discussing and improving the Work, but
   excluding communication that is conspicuously marked or otherwise
   designated in writing by the copyright owner as "Not a Contribution."

   "Contributor" shall mean Licensor and any individual or Legal Entity
   on behalf of whom a Contribution has been received by Licensor and
   subsequently incorporated within the Work.

2. Grant of Copyright License. Subject to the terms and conditions of
   this License, each Contributor hereby grants to You a perpetual,
   worldwide, non-exclusive, no-charge, royalty-free, irrevocable
   copyright license to reproduce, prepare Derivative Works of,
   publicly display, publicly perform, sublicense, and distribute the
   Work and such Derivative Works in Source or Object form.

3. Grant of Patent License. Subject to the terms and conditions of
   this License, each Contributor hereby grants to You a perpetual,
   worldwide, non-exclusive, no-charge, royalty-free, irrevocable
   (except as stated in this section) patent license to make, have made,
   use, offer to sell, sell, import, and otherwise transfer the Work,
   where such license applies only to those patent claims licensable
   by such Contributor that are necessarily infringed by their
   Contribution(s) alone or by combination of their Contribution(s)
   with the Work to which such Contribution(s) was submitted. If You
   institute patent litigation against any entity (including a
   cross-claim or counterclaim in a lawsuit) alleging that the Work
   or a Contribution incorporated within the Work constitutes direct
   or contributory patent infringement, then any patent licenses
   granted to You under this License for that Work shall terminate
   as of the date such litigation is filed.

4. Redistribution. You may reproduce and distribute copies of the
   Work or Derivative Works thereof in any medium, with or without
   modifications, and in Source or Object form, provided that You
   meet the following conditions:

   (a) You must give any other recipients of the Work or
   Derivative Works a copy of this License; and

   (b) You must cause any modified files to carry prominent notices
   stating that You changed the files; and

   (c) You must retain, in the Source form of any Derivative Works
   that You distribute, all copyright, patent, trademark, and
   attribution notices from the Source form of the Work,
   excluding those notices that do not pertain to any part of
   the Derivative Works; and

   (d) If the Work includes a "NOTICE" text file as part of its
   distribution, then any Derivative Works that You distribute must
   include a readable copy of the attribution notices contained
   within such NOTICE file, excluding those notices that do not
   pertain to any part of the Derivative Works, in at least one
   of the following places: within a NOTICE text file distributed
   as part of the Derivative Works; within the Source form or
   documentation, if provided along with the Derivative Works; or,
   within a display generated by the Derivative Works, if and
   wherever such third-party notices normally appear. The contents
   of the NOTICE file are for informational purposes only and
   do not modify the License. You may add Your own attribution
   notices within Derivative Works that You distribute, alongside
   or as an addendum to the NOTICE text from the Work, provided
   that such additional attribution notices cannot be construed
   as modifying the License.

   You may add Your own copyright statement to Your modifications and
   may provide additional or different license terms and conditions
   for use, reproduction, or distribution of Your modifications, or
   for any such Derivative Works as a whole, provided Your use,
   reproduction, and distribution of the Work otherwise complies with
   the conditions stated in this License.

5. Submission of Contributions. Unless You explicitly state otherwise,
   any Contribution intentionally submitted for inclusion in the Work
   by You to the Licensor shall be under the terms and conditions of
   this License, without any additional terms or conditions.
   Notwithstanding the above, nothing herein shall supersede or modify
   the terms of any separate license agreement you may have executed
   with Licensor regarding such Contributions.

6. Trademarks. This License does not grant permission to use the trade
   names, trademarks, service marks, or product names of the Licensor,
   except as required for reasonable and customary use in describing the
   origin of the Work and reproducing the content of the NOTICE file.

7. Disclaimer of Warranty. Unless required by applicable law or
   agreed to in writing, Licensor provides the Work (and each
   Contributor provides its Contributions) on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
   implied, including, without limitation, any warranties or conditions
   of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A
   PARTICULAR PURPOSE. You are solely responsible for determining the
   appropriateness of using or redistributing the Work and assume any
   risks associated with Your exercise of permissions under this License.

8. Limitation of Liability. In no event and under no legal theory,
   whether in tort (including negligence), contract, or otherwise,
   unless required by applicable law (such as deliberate and grossly
   negligent acts) or agreed to in writing, shall any Contributor be
   liable to You for damages, including any direct, indirect, special,
   incidental, or consequential damages of any character arising as a
   result of this License or out of the use or inability to use the
   Work (including but not limited to damages for loss of goodwill,
   work stoppage, computer failure or malfunction, or any and all
   other commercial damages or losses), even if such Contributor
   has been advised of the possibility of such damages.

9. Accepting Warranty or Additional Liability. While redistributing
   the Work or Derivative Works thereof, You may choose to offer,
   and charge a fee for, acceptance of support, warranty, indemnity,
   or other liability obligations and/or rights consistent with this
   License. However, in accepting such obligations, You may act only
   on Your own behalf and on Your sole responsibility, not on behalf
   of any other Contributor, and only if You agree to indemnify,
   defend, and hold each Contributor harmless for any liability
   incurred by, or claims asserted against, such Contributor by reason
   of your accepting any such warranty or additional liability.

END OF TERMS AND CONDITIONS

APPENDIX: How to apply the Apache License to your work.

      To apply the Apache License to your work, attach the following
      boilerplate notice, with the fields enclosed by brackets "{}"
      replaced with your own identifying information. (Don't include
      the brackets!)  The text should be enclosed in the appropriate
      comment syntax for the file format. We also recommend that a
      file or class name and description of purpose be included on the
      same "printed page" as the copyright notice for easier
      identification within third-party archives.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
