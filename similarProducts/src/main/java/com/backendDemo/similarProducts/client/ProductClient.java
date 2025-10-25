package com.backendDemo.similarProducts.client;

import com.backendDemo.similarProducts.domain.ProductDetail;

import java.util.List;

public interface ProductClient {
    public String[] getSimilarProductsIds(String productId);
    public ProductDetail findProductById(String productId);
}
