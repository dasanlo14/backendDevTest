package com.backendDemo.similarProducts.service.impl;

import com.backendDemo.similarProducts.client.ProductClient;
import com.backendDemo.similarProducts.domain.ProductDetail;
import com.backendDemo.similarProducts.service.ProductDetailService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {

    private final ProductClient productClient;

    public ProductDetailServiceImpl(ProductClient productClient) {
        this.productClient = productClient;
    }

    @Override
    public List<ProductDetail> findSimilarProducts(String productId){
        String[] similarProductsIds = this.productClient.getSimilarProductsIds(productId);
        List<ProductDetail> productDetails = new ArrayList<>();
        for(String id : similarProductsIds){
            ProductDetail productDetail = this.productClient.findProductById(id);
            productDetails.add(productDetail);
        }
        return productDetails;
    }
}
