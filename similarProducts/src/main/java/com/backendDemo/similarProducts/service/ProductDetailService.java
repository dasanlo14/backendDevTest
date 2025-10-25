package com.backendDemo.similarProducts.service;

import com.backendDemo.similarProducts.domain.ProductDetail;

import java.util.List;

public interface ProductDetailService {

    public List<ProductDetail> findSimilarProducts(String productId);
}
