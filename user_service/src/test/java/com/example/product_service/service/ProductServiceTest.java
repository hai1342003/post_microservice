package com.example.product_service.service;

import com.example.product_service.dto.ProductDTO;
import com.example.product_service.entity.Product;
import com.example.product_service.repository.ProductRepository;
import com.example.product_service.specification.ProductSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {


    @InjectMocks
    private ProductService productService;



    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductSpecification productSpecification;



    private Product product;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        product = Product.builder()
                .id(1L)
                .name("Laptop XYZ")
                .description("Gaming Laptop")
                .price(1200.0)
                .category("Gaming")
                .ram("16GB")
                .bestseller(true)
                .image1("laptop.jpg")
                .build();
    }

    @Test
    public void testGetAllProducts() {
        List<Product>  products = List.of(product);
        when(productRepository.findAll()).thenReturn(products);

        List<ProductDTO> result = productService.getAllProducts();
        assertEquals(1, result.size());
        assertEquals("Laptop XYZ", result.get(0).getName());
        verify(productRepository, times(1)).findAll();

    }

    @Test
    public void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO result = productService.getProductById(1L);

        assertEquals("Laptop XYZ", result.getName());
        assertEquals("Gaming", result.getCategory());
    }

    @Test
    public void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProductById(1L));
    }

    @Test
    public void testCreateProduct() {
        ProductDTO productDTO = new ProductDTO(null, "Laptop ABC", "Ultrabook", 900.0, "Office", "8GB", false, "abc.jpg");
        Product savedProduct = Product.builder()
                .id(2L)
                .name("Laptop ABC")
                .description("Ultrabook")
                .price(900.0)
                .category("Office")
                .ram("8GB")
                .bestseller(false)
                .image1("abc.jpg")
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductDTO result = productService.createProduct(productDTO);

        assertEquals("Laptop ABC", result.getName());
        assertEquals("Office", result.getCategory());
        verify(productRepository).save(any(Product.class));
    }


    @Test
    public void testUpdateProduct_Success() {
        ProductDTO updateDTO = new ProductDTO(null, "Laptop Updated", "Updated Desc", 1500.0, "Work", "32GB", true, "updated.jpg");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = productService.updateProduct(1L, updateDTO);

        assertEquals("Laptop Updated", result.getName());
        assertEquals("32GB", result.getRam());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    public void testUpdateProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductDTO dto = new ProductDTO();
        assertThrows(RuntimeException.class, () -> productService.updateProduct(1L, dto));
    }

    @Test
    public void testDeleteProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    public void testDeleteProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.deleteProduct(1L));
    }

    @Test
    public void testDeleteProductById_True() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        boolean result = productService.deleteProductById(1L);

        assertTrue(result);
        verify(productRepository).deleteById(1L);
    }

    @Test
    public void testDeleteProductById_False() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = productService.deleteProductById(1L);

        assertFalse(result);
    }

    @Test
    public void testSearchProducts() {
        List<Product> products = List.of(product);
        when(productRepository.searchProducts("Laptop", "Gaming", 1000.0, 1300.0)).thenReturn(products);

        List<Product> result = productService.searchProducts("Laptop", "Gaming", 1000.0, 1300.0);

        assertEquals(1, result.size());
        assertEquals("Laptop XYZ", result.get(0).getName());
    }
}
