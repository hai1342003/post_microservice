package com.example.product_service.controller;

import com.example.product_service.dto.ProductDTO;
import com.example.product_service.dto.SearchCriteria;
import com.example.product_service.entity.Product;
import com.example.product_service.service.ProductService;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> getProducts() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("products", productService.getAllProducts());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") String price,
            @RequestParam("category") String category,
            @RequestParam("ram") String ram,
            @RequestParam("bestseller") String bestsellerStr,
            @RequestParam(value = "image1", required = false) MultipartFile image1
    ) throws IOException {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(name);
        productDTO.setDescription(description);
        productDTO.setPrice(Double.parseDouble(price));
        productDTO.setCategory(category);
        productDTO.setRam(ram);
        productDTO.setBestseller(Boolean.parseBoolean(bestsellerStr));

        if (image1 != null && !image1.isEmpty()) {
            productDTO.setImage1(image1.getOriginalFilename());

            String uploadDir = "C:/Users/Dell/OneDrive - vnu.edu.vn/Desktop/admin/public/images/";
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            File destination = new File(uploadDir + image1.getOriginalFilename());
            image1.transferTo(destination);
        }


        ProductDTO savedProduct = productService.createProduct(productDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Product created successfully!");
        response.put("product", savedProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeProduct(@RequestBody Map<String, Object> payload) {

        try {
            Long id = Long.valueOf(payload.get("id").toString());

            boolean deleted = productService.deleteProductById(id);

            if (deleted) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Product deleted successfully!");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Product not found!"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error deleting product: " + e.getMessage()
            ));
        }
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
