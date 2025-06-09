package com.example.product_service.service;

import com.example.product_service.dto.ProductDTO;
import com.example.product_service.dto.SearchCriteria;
import com.example.product_service.entity.Product;
import com.example.product_service.repository.ProductRepository;
import com.example.product_service.specification.ProductSpecification;

import java.util.Optional;
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
                        product.getCategory(),
                        product.getRam(),
                        product.getBestseller(),
                        product.getImage1()
                )).collect(Collectors.toList());
    }




    public List<Product> searchProducts(String name, String category, Double minPrice, Double maxPrice) {
        return productRepository.searchProducts(name, category, minPrice, maxPrice);
    }



//    @Cacheable(value = "products", key = "#id")
    public ProductDTO getProductById(Long id) {

        simulateSlowService();
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getCategory(), product.getRam(), product.getBestseller(), product.getImage1());
    }

    public ProductDTO createProduct(ProductDTO productDTO) {

        Product product = new Product(null, productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(), productDTO.getCategory(), productDTO.getRam(), productDTO.getBestseller(), productDTO.getImage1());
        product = productRepository.save(product);
        return new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getCategory(), product.getRam(), product.getBestseller(), product.getImage1());
    }

    @CacheEvict(value = "products", key = "#product.id")
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCategory(productDTO.getCategory());
        product.setRam(productDTO.getRam());
        product.setBestseller(productDTO.getBestseller());
        product.setImage1(product.getImage1());
        product = productRepository.save(product);
        return new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getCategory(), product.getRam(), product.getBestseller(), product.getImage1());
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    private void simulateSlowService() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean deleteProductById(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}