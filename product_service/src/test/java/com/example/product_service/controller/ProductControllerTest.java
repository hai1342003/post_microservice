package com.example.product_service.controller;

import com.example.product_service.dto.ProductDTO;
import com.example.product_service.entity.Product;
import com.example.product_service.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.any;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetAllProducts() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("Laptop Test");
        product.setPrice(1000.0);

        Mockito.when(productService.getAllProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.products[0].name").value("Laptop Test"));
    }

    @Test
    void testGetProductById() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("Laptop A");

        Mockito.when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop A"));
    }

    @Test
    void testCreateProduct() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image1", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "image".getBytes());

        ProductDTO createdProduct = new ProductDTO();
        createdProduct.setId(1L);
        createdProduct.setName("New Laptop");

        Mockito.when(productService.createProduct(any(ProductDTO.class))).thenReturn(createdProduct);

        mockMvc.perform(multipart("/api/products")
                        .file(image)
                        .param("name", "New Laptop")
                        .param("description", "A great product")
                        .param("price", "999.99")
                        .param("category", "Electronics")
                        .param("ram", "16GB")
                        .param("bestseller", "true"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.product.name").value("New Laptop"));
    }

    @Test
    void testDeleteProductByPost() throws Exception {
        Mockito.when(productService.deleteProductById(1L)).thenReturn(true);

        mockMvc.perform(post("/api/products/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product deleted successfully!"));
    }

    @Test
    void testUpdateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Laptop");

        Mockito.when(productService.updateProduct(eq(1L), any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Laptop"));
    }

    @Test
    void testDeleteProductByDelete() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testSearchProducts() throws Exception {
        Product product = new Product();
        product.setName("Search Laptop");
        product.setPrice(500.0);

        Mockito.when(productService.searchProducts(any(), any(), any(), any()))
                .thenReturn(List.of(product));

        mockMvc.perform(get("/api/products/search")
                        .param("name", "Search")
                        .param("minPrice", "100")
                        .param("maxPrice", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Search Laptop"));
    }
}
