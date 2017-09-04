package com.redhat.coolstoremsa.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.coolstoremsa.model.Product;
import com.redhat.coolstoremsa.service.ProductService;

import java.util.List;

@Path("/products")
public class ProductEndpoint {

    @Inject
    private ProductService productService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> listAll() {
        return productService.getProducts();
    }

    @GET
    @Path("/{itemId}")
    public Product getCart(@PathParam("itemId") String itemId) {
        return productService.getProductByItemId(itemId);
    }


}