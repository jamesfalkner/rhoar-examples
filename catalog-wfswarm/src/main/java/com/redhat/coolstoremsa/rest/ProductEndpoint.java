package com.redhat.coolstoremsa.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.Invocation.Builder;
//import javax.ws.rs.core.GenericType;
//import javax.ws.rs.core.MediaType;
//
//import com.netflix.hystrix.HystrixCommand;
//import com.netflix.hystrix.HystrixCommandGroupKey;
//import com.netflix.hystrix.HystrixCommandProperties;
//import com.redhat.coolstoremsa.model.Inventory;
import com.redhat.coolstoremsa.model.Product;
import com.redhat.coolstoremsa.service.ProductService;

import java.util.List;
import java.util.logging.Logger;

@Path("/products")
public class ProductEndpoint {

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
//            Builder request = ClientBuilder.newClient().target(url).request();
//            try {
//                return request.get(new GenericType<Inventory>(){});
//            } catch (Exception e) {
//                LOG.severe("Failed to call Inventory service at " + url + ": " + e.getMessage());
//                throw e;
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