package com.backendDemo.similarProducts.web.rest;

import com.backendDemo.similarProducts.domain.ProductDetail;
import com.backendDemo.similarProducts.service.ProductDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductDetailResource {

    private final ProductDetailService productDetailService;

    public ProductDetailResource(ProductDetailService productDetailService){
        this.productDetailService = productDetailService;
    }

    @GetMapping("/{productId}/similar")
    public ResponseEntity<List<ProductDetail>> findSimilarProducts(@PathVariable String productId){
        List<ProductDetail> similarProducts = this.productDetailService.findSimilarProducts(productId);
        return ResponseEntity.ok(similarProducts);
    }
}
