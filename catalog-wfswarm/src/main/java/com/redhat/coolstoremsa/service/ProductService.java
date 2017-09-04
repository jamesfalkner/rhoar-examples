package com.redhat.coolstoremsa.service;


import com.redhat.coolstoremsa.model.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@Stateless
public class ProductService {


    @PersistenceContext
    private EntityManager em;

    public ProductService() {

    }

    public List<Product> getProducts() {

            Query query = em.createQuery("SELECT p FROM PRODUCT_CATALOG p");
            return (List<Product>) query.getResultList();

    }

    public Product getProductByItemId(String itemId) {
        Product p = em.find(Product.class, itemId);
        return p;
    }
}
