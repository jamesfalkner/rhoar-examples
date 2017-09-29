package com.redhat.cloudnative.catalog;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "store")
public class StoreConfiguration {

    private List<String> recalledProducts;

    public List<String> getRecalledProducts() {
        return recalledProducts;
    }

    public void setRecalledProducts(List<String> recalledProducts) {
        this.recalledProducts = recalledProducts;
    }
}
