package com.example.product_service.service;

import com.example.product_service.dto.ProductDTO;
import com.example.product_service.dto.SearchCriteria;
import com.example.product_service.entity.Product;
import com.example.product_service.repository.ProductRepository;
import com.example.product_service.specification.ProductSpecification;

import jakarta.persistence.Cacheable;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    private final ProductSpecification productSpecification;

    public ProductService(ProductRepository productRepository, ProductSpecification productSpecification) {
        this.productRepository = productRepository;
        this.productSpecification = productSpecification;
    }




//    @Cacheable(value = "allProducts")
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> new ProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStock(),
                        product.getCategory()
                )).collect(Collectors.toList());
    }




    public List<Product> searchProducts(String name, String category, Double minPrice, Double maxPrice) {
        return productRepository.searchProducts(name, category, minPrice, maxPrice);
    }



//    @Cacheable(value = "products", key = "#id")
    public ProductDTO getProductById(Long id) {

        simulateSlowService();
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStock(), product.getCategory());
    }

    public ProductDTO createProduct(ProductDTO productDTO) {

        Product product = new Product(null, productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(), productDTO.getStock(), productDTO.getCategory());
        product = productRepository.save(product);
        return new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStock(), product.getCategory());
    }

    @CacheEvict(value = "products", key = "#product.id")
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product = productRepository.save(product);
        return new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStock(), product.getCategory());
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    private void simulateSlowService() {
        try {
            Thread.sleep(3000); // Giả lập độ trễ
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}