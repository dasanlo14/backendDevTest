package com.backendDemo.similarProducts.service.impl;

import com.backendDemo.similarProducts.client.ProductClient;
import com.backendDemo.similarProducts.domain.ProductDetail;
import com.backendDemo.similarProducts.service.ProductDetailService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {

    private final ProductClient productClient;

    public ProductDetailServiceImpl(ProductClient productClient) {
        this.productClient = productClient;
    }

    @Override
    public List<ProductDetail> findSimilarProducts(String productId){
        String[] similarProductsIds = this.productClient.getSimilarProductsIds(productId);
        try (var executor  = newVirtualThreadPerTaskExecutor()) {
            var futures = Arrays.stream(similarProductsIds)
                    .map(id -> supplyAsync(() -> this.productClient.findProductById(id), executor))
                    .toList();
            return futures.stream()
                        .map(CompletableFuture::join)
                        .toList();
        }
    }
}
