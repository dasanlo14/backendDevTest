package com.backendDemo.similarProducts.client.impl;

import com.backendDemo.similarProducts.client.ProductClient;
import com.backendDemo.similarProducts.domain.ProductDetail;
import com.backendDemo.similarProducts.exception.InternalServerErrorException;
import com.backendDemo.similarProducts.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductClientImpl implements ProductClient {
    @Value("${product.api.url}")
    private String productApiUrl;

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public String[] getSimilarProductsIds(String productId) {
        try{
        String similarProductsUrl = productApiUrl + "/product/" + productId + "/similarids";
        return restTemplate.getForObject(similarProductsUrl, String[].class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ProductNotFoundException("Producto con ID " + productId + " no encontrado");
        }

    }

    @Override
    public ProductDetail findProductById(String productId) {
        String productByIdUrl = productApiUrl + "/product/" + productId;
        try {
            return restTemplate.getForObject(productByIdUrl, ProductDetail.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ProductNotFoundException("Producto con ID " + productId + " no encontrado");
        } catch (HttpServerErrorException.InternalServerError e) {
            String body = e.getResponseBodyAsString();
            String msg = "Fallo del servicio (" + e.getRawStatusCode() + " "
                    + e.getStatusText() + ") al llamar a " + productByIdUrl
                    + (body.isBlank() ? " - no body" : " - body: " + body);
            throw new InternalServerErrorException(msg);
        }
    }
}
