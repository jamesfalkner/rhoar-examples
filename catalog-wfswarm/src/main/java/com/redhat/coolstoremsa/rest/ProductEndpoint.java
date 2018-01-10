package com.redhat.coolstoremsa.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
//import javax.ws.rs.client.AsyncInvoker;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.core.GenericType;
//import com.netflix.hystrix.*;
//import com.netflix.hystrix.strategy.HystrixPlugins;
//import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
//import com.redhat.coolstoremsa.model.Inventory;
//import java.util.concurrent.ExecutionException;
import com.redhat.coolstoremsa.model.Product;
import com.redhat.coolstoremsa.service.ProductService;

import java.util.List;
import java.util.logging.Logger;

@Path("/products")
public class ProductEndpoint {

//    static {
//        HystrixPlugins.getInstance().registerEventNotifier(new HystrixEventNotifier() {
//            @Override
//            public void markEvent(HystrixEventType eventType, HystrixCommandKey key) {
//                LOG.info("CIRCUIT BREAKER: " + eventType.name());
//            }
//        });
//
//    }
    private static Logger LOG = Logger.getLogger(ProductEndpoint.class.getName());

    @Inject
    private ProductService productService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> listAll() {

        List<Product> products = productService.getProducts();

//        products.parallelStream()
//                .forEach(p -> p.setQuantity(new GetInventoryCommand(p.getItemId()).execute().getQuantity()));

        return products;
    }

    @GET
    @Path("/{itemId}")
    public Product getCart(@PathParam("itemId") String itemId) {
        return productService.getProductByItemId(itemId);
    }

//    class GetInventoryCommand extends HystrixCommand<Inventory> {
//
//        private String itemId;
//
//        public GetInventoryCommand(String itemId) {
//            super(HystrixCommandGroupKey.Factory.asKey("InventoryGroup"), 500);
//            this.itemId = itemId;
//        }
//
//        @Override
//        protected Inventory run() {
//            String url = "http://inventory:8080/api/inventory/" + itemId;
//            try {
//                AsyncInvoker request = ClientBuilder.newClient().target(url).request().async();
//                return request.get(new GenericType<Inventory>(){}).get();
//            } catch (InterruptedException | ExecutionException e) {
//                throw new RuntimeException(" Oops, Something went wrong ! ") ;
//            }
//        }
//
//        @Override
//        protected Inventory getFallback() {
//            return new Inventory(itemId, "Nowhere", -1, "http://redhat.com");
//        }
//
//    }

}