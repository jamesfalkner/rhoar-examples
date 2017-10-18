package com.redhat.coolstore.catalog.api;

import com.redhat.coolstore.catalog.model.Product;
import com.redhat.coolstore.catalog.verticle.service.CatalogService;
import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.List;

public class ApiVerticle extends AbstractVerticle {

    private CatalogService catalogService;

    private CircuitBreaker circuitBreaker;

    public ApiVerticle(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        Router router = Router.router(vertx);

        router.get("/products").handler(this::getProducts);
        router.get("/product/:itemId").handler(this::getProduct);
        router.route("/product").handler(BodyHandler.create());
        router.post("/product").handler(this::addProduct);
        router.route("/*").handler(StaticHandler.create());

        //Health Check
        HealthCheckHandler healthCheckHandler = HealthCheckHandler.create(vertx)
                .register("health", this::health);
        router.get("/health").handler(healthCheckHandler);

        circuitBreaker = CircuitBreaker.create("product-circuit-breaker", vertx,
                new CircuitBreakerOptions()
                        .setMaxFailures(3) // number of failure before opening the circuit
                        .setTimeout(1000) // consider a failure if the operation does not succeed in time
                        .setFallbackOnFailure(true) // do we call the fallback on failure
                        .setResetTimeout(5000) // time spent in open state before attempting to re-try
        );

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(config().getInteger("catalog.http.port", 8080), result -> {
                    if (result.succeeded()) {
                        startFuture.complete();
                    } else {
                        startFuture.fail(result.cause());
                    }
                });
    }

    private void getProducts(RoutingContext rc) {



        circuitBreaker.executeWithFallback(event -> {
            catalogService.getProducts(ar -> {
                    List<Product> products = ar.result();
                    JsonArray json = new JsonArray();
                    products.stream()
                            .map(Product::toJson)
                            .forEach(json::add);
                    event.complete(json);
            });

        }, throwable -> new JsonArray().add(new Product("1", "fallback product", "fallback desc", 1000000).toJson())).setHandler(event -> {
            rc.response()
                    .putHeader("Content-type", "application/json")
                    .end(event.result().encodePrettily());


        });
    }


    private void getProduct(RoutingContext rc) {
        String itemId = rc.request().getParam("itemid");
        catalogService.getProduct(itemId, ar -> {
            if (ar.succeeded()) {
                Product product = ar.result();
                if (product != null) {
                    rc.response()
                            .putHeader("Content-type", "application/json")
                            .end(product.toJson().encodePrettily());
                }
            } else {
                rc.fail(ar.cause());
            }
        });
    }


    private void addProduct(RoutingContext rc) {
        JsonObject json = rc.getBodyAsJson();
        catalogService.addProduct(new Product(json), ar -> {
            if (ar.succeeded()) {
                rc.response().setStatusCode(201).end();
            } else {
                rc.fail(ar.cause());
            }
        });
    }

    private void health(Future<Status> future) {
        catalogService.ping(ar -> {
            if (ar.succeeded()) {
                // HealthCheckHandler has a timeout of 1000s. If timeout is exceeded, the future will be failed
                if (!future.isComplete()) {
                    future.complete(Status.OK());
                }
            } else {
                if (!future.isComplete()) {
                    future.complete(Status.KO());
                }
            }
        });
    }

}
