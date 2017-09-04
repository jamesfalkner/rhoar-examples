package com.redhat.coolstoremsa.service;


import com.redhat.coolstoremsa.model.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Produces;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class ProductService {


    @PersistenceContext
    private EntityManager em;

    public ProductService() {

    }

    public List<Product> getProducts() {

            Query query = em.createQuery("SELECT p FROM " + Product.class.getName() + " p");
            return (List<Product>) query.getResultList();

    }

    public Product getProductByItemId(String itemId) {
        Product p = em.find(Product.class, itemId);
        return p;
    }
}
