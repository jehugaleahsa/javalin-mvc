# What's new in Javalin MVC 5?
Javalin was upgraded to version 5. There are a few changes that impacted Javalin MVC. This document will summarize them for you.

## Java 11+
Probably the biggest impact is that Javalin targets Java 11 now, to support using the latest servlet-api (5.0). Due to this, many `javax.*` dependencies are now `jakarta.*` dependencies.

For people using, be aware that Guice is still implementing the `javax.*` interfaces (see [here](https://github.com/google/guice/issues/1383)). Javalin MVC will respect either `@Inject` annotation for the time being.

For [JAX-RS](https://github.com/jax-rs), only the jakarta annotations are supported in Javalin MVC 5.x. I couldn't think of a reason why not to force an upgrade for this dependency. It's less code maintenance for me.

## Quick Starter
There's a [new project](https://github.com/jehugaleahsa/javalin-mvc-starter) on my GitHub showing how to wire up a realistic web application. It's not limited to Javalin MVC, but also shows ways to use Hibernate, log4j 2, pac4j, etc. It doesn't show how to host a realistic front-end, but feel free to ask if you need it.

## Before and After Action Contexts
Needless to say, the interfaces for `BeforeActionHandler`, `AfterActionHandler`, `WsBeforeActionHandler`, and `WsAfterActionHandler` were complicated. I have replaced the old interfaces, so that they take a single argument with no return values.

There's now a `BeforeActionContext`, `AfterActionContext`, `WsBeforeActionContext`, and `WsAfterActionContext` parameter. It provides access to the `HttpContext` / `WsContext` and arguments. The "before" context classes now provide a `setCancelled(boolean)` method to prevent controller actions from running. The "after" context classes now provide a `setHandled(boolean)` method to indicate that a thrown exception has been handled... Furthermore, the exception remains available for inspection by subsequent handlers - it can even be *replaced if needed*.

## WebSocket bug fixes
I discovered some bugs related to invalid code being generated when combining dependency injection with WebSockets. I created several new unit tests to cover these cases, so it won't crop up again.

## Renamed `WsDisconnect*` to `WsClose*`
I am not sure why, but Javalin renamed the WebSocket methods related to client disconnects from `WsDisconnect*` to `WsClose*`. I followed suit, to avoid any confusion. Sorry if this is an inconvenience. A quick search/replace should resolve it, hopefully.

## Removal of @Deprecated methods in `HttpRequest` and `WsRequest`
In 4.x, I wanted to make the names for getting values from query strings, the path, cookies, headers, etc. more consistent. To avoid introducing a breaking change in a minor release, I waited until 5.x to remove these. They have been marked `@Deprecated` for a while, so now is the time to update your code if you haven't already done so.

## Removal of `@FromJson` and `@FromBinary`.
The `@FromBody` annotation is a general-purpose annotation for saying that a value comes from the body of a request. There's no benefit to explicitly specifying it's JSON or binary data, since that's inferred from the context of the request. These have been marked `@Deprecated` for a while, so now is the time to update your code if you haven't done so already.

## OpenAPI changes.
In Javalin 5.x, the previous OpenAPI implementation was tossed out, replaced with [something new](https://javalin.io/migration-guide-javalin-4-to-5#new-openapi-project). The biggest thing to keep in mind is that this is a compile-time tool, which means a little more configuration in your Maven file with the benefit that's there's no runtime overhead involved. I suggest reading the OpenAPI tutorial provided or see how I set it up in my Javalin MVC starter project.

The good thing about this change is that I didn't need to do anything to make it work. The OpenAPI annotations work perfectly fine on the controller methods, where before I needed to "move" them onto the Javalin route definitions.

