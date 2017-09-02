package com.redhat.cloudnative.catalog;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api/catalog")
public class CatalogController {

	@Autowired
    private ProductRepository repository;

	private StoreProperties storeProperties;

	@Autowired
	public CatalogController(StoreProperties props) {
	    this.storeProperties = props;
    }

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getAll() {
        Spliterator<Product> products = repository.findAll().spliterator();
        List<String> recalledProducts = storeProperties.getRecalledProducts();

        return StreamSupport.stream(products, false)
                .filter(product -> !recalledProducts.contains(product.getItemId()))
                .collect(Collectors.toList());
    }
}