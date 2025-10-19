package no.gjensidige.product.controller;

import no.gjensidige.product.entity.Product;
import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;


    @Mock
    private ProductService productService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getProducts() {
        Set<String> uniqueNames = new HashSet<>(Arrays.asList("Larry", "Steve", "James"));
        List<Product> productList = new ArrayList<>();
        uniqueNames.forEach(name ->
        {
            Product p = new Product();
            p.setProductName(name);
            productList.add(p);
        });


        when(productService.getAllProducts()).thenReturn(productList);

        List<Product> productList1 = productController.getProducts();

        verify(productService).getAllProducts();

        assertEquals(3, productList1.size());

    }

    @Test
    public void getProduct() {
        Product p = new Product();
        p.setId(1l);

        when(productService.getProduct(1l)).thenReturn(p);

        Product product = productController.getProduct(1l);

        verify(productService).getProduct(1l);
        assertEquals(1l, product.getId().longValue());
    }

    @Test
    public void createProduct() {
        ProductDTO dto = new ProductDTO();
        dto.setProductName("Created Product");

        Product created = new Product();
        created.setId(10L);
        created.setProductName(dto.getProductName());

        when(productService.createProduct(dto)).thenReturn(created);

        Product result = productController.createProduct(dto);

        verify(productService).createProduct(dto);
        assertEquals(10L, result.getId());
        assertEquals("Created Product", result.getProductName());
    }

    @Test
    public void updateProduct() {
        Long id = 5L;
        ProductDTO dto = new ProductDTO();
        dto.setProductName("Updated Product");

        Product updated = new Product();
        updated.setId(id);
        updated.setProductName(dto.getProductName());

        when(productService.updateProduct(id, dto)).thenReturn(updated);

        Product result = productController.updateProduct(id, dto);

        verify(productService).updateProduct(id, dto);
        assertEquals(id, result.getId());
        assertEquals("Updated Product", result.getProductName());
    }

    @Test
    public void deleteProduct() {

        Product p = new Product();
        p.setId(1l);

        when(productService.deleteProduct(1l)).thenReturn(p);

        Product product = productController.deleteProduct(1l);

        verify(productService).deleteProduct(1l);

        assertEquals(1l, product.getId().longValue());

    }
}