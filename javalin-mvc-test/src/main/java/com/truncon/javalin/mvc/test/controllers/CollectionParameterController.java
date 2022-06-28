package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.JsonResult;
import com.truncon.javalin.mvc.api.Named;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Controller
public class CollectionParameterController {
    public static final String ITERABLE_ROUTE = "/api/parameters/collections/iterable";
    @HttpGet(route = ITERABLE_ROUTE)
    public ActionResult getIterable(@FromQuery @Named("value") Iterable<Integer> values) {
        return new JsonResult(values);
    }


    public static final String COLLECTION_ROUTE = "/api/parameters/collections/collection";
    @HttpGet(route = COLLECTION_ROUTE)
    public ActionResult getCollection(@FromQuery @Named("value") Collection<Double> values) {
        return new JsonResult(values);
    }

    public static final String LIST_ROUTE = "/api/parameters/collections/list";
    @HttpGet(route = LIST_ROUTE)
    public ActionResult getList(@FromQuery @Named("value") List<String> values) {
        return new JsonResult(values);
    }

    public static final String SET_ROUTE = "/api/parameters/collections/set";
    @HttpGet(route = SET_ROUTE)
    public ActionResult getSet(@FromQuery @Named("value") Set<String> values) {
        return new JsonResult(values);
    }

    public static final String ARRAY_LIST_ROUTE = "/api/parameters/collections/array_list";
    @HttpGet(route = ARRAY_LIST_ROUTE)
    public ActionResult getArrayList(@FromQuery @Named("value") ArrayList<UUID> values) {
        return new JsonResult(values);
    }

    public static final String LINKED_LIST_ROUTE = "/api/parameters/collections/linked_list";
    @HttpGet(route = LINKED_LIST_ROUTE)
    public ActionResult getLinkedList(@FromQuery @Named("value") LinkedList<LocalDateTime> values) {
        return new JsonResult(values);
    }

    public static final String HASH_SET_ROUTE = "/api/parameters/collections/hash_set";
    @HttpGet(route = HASH_SET_ROUTE)
    public ActionResult getHashSet(@FromQuery @Named("value") HashSet<String> values) {
        return new JsonResult(values);
    }

    public static final String LINKED_HASH_SET_ROUTE = "/api/parameters/collections/linked_hash_set";
    @HttpGet(route = LINKED_HASH_SET_ROUTE)
    public ActionResult getLinkedHashSet(@FromQuery @Named("value") LinkedHashSet<BigInteger> values) {
        return new JsonResult(values);
    }
}
