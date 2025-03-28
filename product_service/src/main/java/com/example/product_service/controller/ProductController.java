package com.example.product_service.controller;

import com.example.product_service.dto.ProductDTO;
import com.example.product_service.dto.SearchCriteria;
import com.example.product_service.entity.Product;
import com.example.product_service.service.ProductService;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

//    @GetMapping
//    public ResponseEntity<List<ProductDTO>> getAllProducts() {
//        return ResponseEntity.ok(productService.getAllProducts());
//    }



    @GetMapping
    public ResponseEntity<?> getProducts(@RequestHeader HttpHeaders headers) {
        System.out.println("Received Headers: " + headers);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<Product> products = productService.searchProducts(name, category, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }


}
