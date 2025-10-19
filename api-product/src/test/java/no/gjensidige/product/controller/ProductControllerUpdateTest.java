package no.gjensidige.product.controller;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Controller level test for update (Exercise 1)
 */
public class ProductControllerUpdateTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Before
    public void init() { MockitoAnnotations.initMocks(this); }

    @Test
    public void updateProduct_updatesFields() {
        Long id = 10L;
        ProductDTO dto = new ProductDTO();
        dto.setProductName("New Name");
        dto.setPrice(199.99);

        Product updated = new Product();
        updated.setId(id);
        updated.setProductName(dto.getProductName());
        updated.setUnitPrice(dto.getUnitPrice());

        when(productService.updateProduct(id, dto)).thenReturn(updated);

        Product result = productController.updateProduct(id, dto);

        verify(productService).updateProduct(id, dto);
        assertEquals(id, result.getId());
        assertEquals("New Name", result.getProductName());
        assertEquals(dto.getUnitPrice(), result.getUnitPrice());
    }
}
