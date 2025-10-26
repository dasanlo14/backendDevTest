package com.backendDemo.similarProducts.web.rest;

import com.backendDemo.similarProducts.domain.ProductDetail;
import com.backendDemo.similarProducts.exception.InternalServerErrorException;
import com.backendDemo.similarProducts.exception.ProductNotFoundException;
import com.backendDemo.similarProducts.service.ProductDetailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductDetailResourceIT {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ProductDetailService productDetailService;

    @Test
    @DisplayName("GET /product/1/similar devolviendo lista con productos")
    void findSimilarProducts_ok_returnsList() throws Exception {
        var productDetail2 = new ProductDetail("2", "A",10.5, true);
        var productDetail3 = new ProductDetail("3", "B", 20.0, false);

        when(productDetailService.findSimilarProducts("1"))
                .thenReturn(List.of(productDetail2, productDetail3));

        mvc.perform(get("/product/1/similar"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("2"))
                .andExpect(jsonPath("$[0].name").value("A"))
                .andExpect(jsonPath("$[0].price").value(10.5))
                .andExpect(jsonPath("$[0].availability").value(true))
                .andExpect(jsonPath("$[1].id").value("3"))
                .andExpect(jsonPath("$[1].name").value("B"))
                .andExpect(jsonPath("$[1].price").value(20.0))
                .andExpect(jsonPath("$[1].availability").value(false));
    }

    @Test
    @DisplayName("GET /product/1/similar devolviendo lista vacía")
    void findSimilarProducts_ok_emptyList() throws Exception {
        when(productDetailService.findSimilarProducts("1"))
                .thenReturn(List.of());

        mvc.perform(get("/product/1/similar"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /product/999/similar lanza ProductNotFoundException")
    void findSimilarProducts_notFound() throws Exception {
        when(productDetailService.findSimilarProducts("999"))
                .thenThrow(new ProductNotFoundException("Producto con ID 999 no encontrado"));

        mvc.perform(get("/product/999/similar"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.type").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Producto con ID 999 no encontrado"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("GET /product/7/similar lanza InternalServerErrorException")
    void findSimilarProducts_internalServerError() throws Exception {
        when(productDetailService.findSimilarProducts("7"))
                .thenThrow(new InternalServerErrorException("Fallo del servicio"));

        mvc.perform(get("/product/7/similar"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.type").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Fallo del servicio"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
