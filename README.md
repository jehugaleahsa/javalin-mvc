# Javalin MVC
Build Javalin route handlers at compile time using controllers and action methods.

## Javalin
When I started working in Java, I took a look at the Java ecosystem for popular web libraries and frameworks. To be honest, I was disappointed to see the most popular options are still monolithic frameworks. Coming from a .NET background, I got to watch as ASP.NET MVC evolved from a massive, opinionated framework to the fast and flexible ASP.NET MVC Core it has become. Coming to Java, using a monolithic framework felt like a step backwards, so I started looking at other alternatives.

That's when I found [Javalin](https://javalin.io): it's a light-weight web library similar to Node.js' Express, where you simply provide routes and route handlers (lambdas) to process requests. You literally have running code within 5 minutes of creating your project. A quick scroll through their documentation and you already know almost everything you will ever need to know. It's simple: the way it should be.

## Why Javalin MVC?
While Javalin is simple and amazing, you might find yourself wishing it had a couple niceties. For one, you'll probably find yourself doing a lot of conversions from `string` to `int` or writing the same code to deserialize your JSON. If you build anything substantial, you might also find having all your route handlers in one file is a bit much to sift through. How can we simplify role-based authentication, logging, etc.?

Coincidentally, I was learning about Java's [annotation processing](https://medium.com/@jintin/annotation-processing-in-java-3621cb05343a) around the same time I started playing with Javalin. It occurred to me, I could build routes automatically by inspecting annotations and generating the code at compile time. Javalin MVC just translates controller classes and "action methods" into Javalin routes at compile time.

One of the major benefits to Javalin MVC being a compile-time tool is that there's absolutely no runtime overhead for using this library. It's as fast as if you wrote all the Javalin route handlers by hand. Not only that, but it's a compile-time error if you try to register two HTTP methods/routes multiple times -- with raw Javalin you'd only discover that error at runtime!

## Installation
The following dependencies are needed in your web project:

```xml
<dependencies>
  <!-- Javalin MVC -->
  <dependency>
    <groupId>com.truncon</groupId>
    <artifactId>javalin-mvc-api</artifactId>
    <version>2.0.0</version>
  </dependency>
  <dependency>
    <groupId>com.truncon</groupId>
    <artifactId>javalin-mvc-core</artifactId>
    <version>2.0.0</version>
  </dependency>
  <!-- Dependency Injection -->
  <dependency>
    <groupId>com.google.dagger</groupId>
    <artifactId>dagger</artifactId>
    <version>2.42</version>
  </dependency>
</dependencies>
```

By having `javalin-mvc-core` as a dependency, you get Javalin as a transitive dependency, along with access to its OpenAPI plugin. 

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
                        <version>2.42</version>
                    </path>
                    <path>
                        <groupId>com.truncon</groupId>
                        <artifactId>javalin-mvc-core</artifactId>
                        <version>4.0.0</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

I have no idea how to configure Javalin MVC to run with Gradle, although it should mirror closely how Dagger is configured. Feel free to submit a PR with the steps listed here if you get this working, and you're feeling generous. 😁

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

```java
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
Action method parameters can be bound to the values coming from your request headers, route parameters, query strings, form fields (url encoded) or the request body (JSON, etc.). By default, the method parameter and the request parameter are matched by name, but the `@Named` annotation can be used to override this behavior. Furthermore, you can say explicitly where to bind a parameter by using the `@FromPath`, `@FromQuery`, `@FromHeader` or `@FromCookie`, or `@FromForm` annotations (recommended).

Consider this example:

```java
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
import com.truncon.javalin.mvc.ControllerRegistry;
import com.truncon.javalin.mvc.JavalinControllerRegistry;
// etc...

public static void main(String[] args) throws IOException {
    JsonMapper jsonMapper = new JavalinJackson(new ObjectMapper());
    Javalin app = Javalin.create(config -> {
        // Remove the following line to disable Open API annotation processing
        config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
        config.jsonMapper(jsonMapper);
        // This example is using the SPA feature
        config.addStaticFiles("./public", Location.EXTERNAL);
        config.addSinglePageRoot("/", "./public/index.html", Location.EXTERNAL);
    });

    // Provide method of constructing a new DI container
    // Dagger prepends "Dagger" automatically at compile time
    Supplier<WebContainer> scopeFactory = () -> DaggerWebContainer.builder().build();
    // Javalin MVC generates "com.truncon.javalin.mvc.JavalinControllerRegistry" automatically at compile time
    ControllerRegistry registry = new JavalinControllerRegistry(jsonMapper, scopeFactory);
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

If you have access to the generated sources, you can inspect the generated `JavalinControllerRegistry.java` file. If you do, you will see most of the file consists of calls to `app.get(...)`, `app.post(...)`, etc.

## Supported Features
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
    * [ ] `@HttpConnect` (No supported by Javalin)
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
* [x] Support pre-execution interceptor via `@Before`
* [x] Support post-execution interceptor via `@After`
* [x] Support async operations (by returning `CompletableFuture<T>`)
* [x] Support Dagger dependency injection
    * [x] Inject controller dependencies
    * [x] Inject injector (self-injection)
    * [x] Inject context/request/response objects
* [x] Open API/Swagger Annotations
    * [x] Now uses built-in Javalin OpenAPI annotations
* [x] WebSockets
    * [x] Specify routes via `@WsController`
    * [x] Support `@WsConnect`, `@WsDisconnect`, `@WsError`, `@WsMessage` and WsBinaryMessage annotations
    * [x] Support for data binding
    * [x] Support for @Before and @After handlers
* [x] Custom conversion methods
    * [x] Support `@Converter` on static methods
    * [x] Support `@Converter` on instance methods
    * [x] Support `@UseConverter` annotations on types
    * [x] Support `@UseConverter` annotations on parameters
    * [x] Support `@UseConverter` annotations on fields/setters
    * [x] Support `@UseConverter` annotations on setter parameters.
    * [x] Support HTTP and WebSocket conversions

## Dagger (Optional)
Dependency injection is at the core of modern software projects. It supports switching between implementations at runtime and promotes testability. Historically, dependency injection has utilized runtime reflection to instantiate objects and inject them. However, waiting to perform injection until runtime comes with the risk of missing bindings that will lead to system failure. There's also the overhead of constructing objects using reflection. However, the [Dagger](https://google.github.io/dagger/) project uses annotation processing to provide compile-time dependency injection. This provides all the benefits of using an inversion of control (IoC) container without the risk of missing bindings leading to runtime failures. There's also minimal overhead because there's no reflection involved.

Dagger is integrated into `javalin-mvc-core`, making Dagger the default DI; however, your code will still compile if you choose not to use it. Dagger, being a compile-time DI library, has a somewhat different API than other DI libraries. Instead of having a single `injector.get(Class<?> clz)` method that can be used to retrieve every type of object, there are specific methods for each dependency. Ideally, a generic DI interface could be provided so `javalin-mvc-core` could work against *any* DI library, but this dramatic difference in API makes that infeasible. It was a tough decision, but I ended up choosing Dagger. Technically, you can wire in your own choice of DI library on top of Dagger, so don't feel confined by that decision.

The `javalin-mvc-core` project needs to know how to instantiate objects with Dagger; to do this, you must mark your Dagger container with the `@MvcComponent` annotation. Your Dagger container will, minimally, look like this:

```java
@Component
@MvcComponent
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
WebSockets are handled by marking classes with the `@WsController` annotation. Unlike HTTP controllers, a WebSocket controller only handles a single route. Each WebSocket controller can process client connections, disconnections, errors, and messages (text or binary). The route the controller will handle is passed as a parameter to the `@WsController` annotation. The methods within the controller can be marked with the `@WsConnect`, `@WsDisconnect`, `@WsError`, `@WsMessage`, or `@WsBinaryMessage` annotations. Only one instance of each annotation can appear within a class; however, the same method can have multiple annotations.

Similar to HTTP controllers, method parameters can be bound from query strings, path parameters, headers, and cookies. Note, there's no support for URL-encoded form data. If you want to explicitly bind a value from a particular source, you can use the same `@From*` annotations, just like for HTTP. In addition, you can use the `@FromJson` annotation to bind parameters directly from the message. You can also use `@FromBinary` to bind binary messages to `byte[]` or `ByteBuffer` parameters.

If a method accepts a `WsContext` object, it will have direct access to the context object. Similarly, you can bind to `WsRequest` and `WsResponse` objects. A method-specific sub-interface exists for each method, so there is a `WsConnectContext`, `WsDisconnectContext`, `WsErrorContext`, `WsMessageContext`, and `WsBinaryMessageContext` that can be used as parameters, as well; however, these will only be initialized if used on the appropriate method.

You can send responses to the client using the `WsResponse.send` methods; however, you can also return an instance of `WsActionResult` from the `WsMessage` handler. Currently, the only supported result types are `WsContentResult` for sending plain text, `WsJsonResult` for sending JSON results, and `WsByteArrayResult` and `WsByteBufferResult` for sending binary results. Similar to HTTP controllers, you can also just return from your method, and it will be serialized appropriately.

```java
@WsController(route="/ws/pickles")
public final class WsPickleController {
    @Inject
    public WsPickleController() {
    }

    @WsConnect
    public void onConnect(WsConnectContext context) {
        // Do something...
    }

    @WsDisconnect
    public void onDisconnect(WsDisconnectContext context) {
        // Do something...
    }

    @WsError
    public void onError(WsErrorContext context) {
        // Do something...
    }

    @WsMessage
    public WsActionResult onMessage(@FromJson Payload payload) {
        return new WsJsonResult(payload);
    }
}
```

## Model Binding
Data can come from a lot of places: the route (e.g., `/users/{userId}`), query strings (e.g., `/users?name=John%20Smith`), headers, cookies, form fields (URL encoded form data), body JSON, etc. Since Javalin MVC 2.x, you are allowed to bind your data to objects, as well. Consider a page that listed all your users with a server-side paging and searching. You might have a model that looks like this:

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

In the future, I might add support for binding getters or package-level members. I may also generate compile errors if you place `@From*` annotations on non-public fields and setters, but right now Javalin MVC just ignores them.

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

Javalin MVC is smart enough to look at inherited members as well as search recursively within class model members for `@From*` annotations. This allows you to nest your models however you see fit. Note: in the second example above, you can mark the `setPagination` method with a `@From*` annotation to overwrite the default binding source, similar to the `@From*` annotation on the action method parameter.

### Dependency Injection
By default, Javalin MVC will try to initialize your models using the default constructor; however, if you registered your model with Dagger, Javalin MVC will use it to initialize your model.

### JSON and binary binding
Javalin MVC also allows you to use the `@FromJson` and `@FromBinary` annotations on model members. Just be aware that binding the same binary data multiple times can result in unexpected behavior.

## Custom Conversion
One of the big enhancements with Javalin MVC 2.x is the introduction of custom converters. Custom conversions are performed using a pair of new annotations: `@Converter` and `@UseConverter`. Static and instance methods can be annotated with `@Converter`, so long as they use the correct signature. For example:

```java
@Converter("pair")
public static Pair parse(HttpRequest request, String name, ValueSource valueSource) {
    Map<String, Collection<String>> lookup = request.getSourceLookup(valueSource);
    Collection<String> values = lookup.get(name);
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
For instance method converters (a.k.a, non-`static`), Javalin MVC will try to create the converter objects using the default constructor, by default. However, if you register your class with Dagger, Javalin MVC will use Dagger to instantiate the converter.

## Known Limitations
At the time of this writing, Javalin MVC does not support incremental compilation. This means that all of your classes must be compiled in the same `javac` command in order for the full `JavalinControllerRegistry.java` file to be generated. This is the default behavior using Maven (e.g., `mvn compile`) but if you are working in an IDE, such as IntelliJ, you might need to use the "Rebuild" command or run Maven as a separate step.

A common alternative workflow is to temporarily enable annotation processing and run `mvn compile` from the command line. Then simply copy the generated `/target/generated-sources/annotations/com/truncon/javalin/mvc/JavalinControllerRegistry.java` file into your codebase and disable the annotation processor again until you make another change. This is a bit of a pain but is useful for people who only intend to use this project to save themselves typing route handlers by hand.

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

Copyright 2017 David Åse

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
