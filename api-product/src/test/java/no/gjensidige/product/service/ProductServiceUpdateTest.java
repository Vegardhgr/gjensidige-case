package no.gjensidige.product.service;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.exception.ProductNotFoundException;
import no.gjensidige.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Service level update tests (Exercise 1)
 */
public class ProductServiceUpdateTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper; // not used in update partial path but kept for consistency

    @BeforeEach
    public void setup() { MockitoAnnotations.openMocks(this); }

    @Test
    public void partialUpdate_onlyProvidedFieldsChange() {
        Product existing = new Product();
        existing.setId(5L);
        existing.setProductName("Original");
        existing.setCategory("CategoryA");
        existing.setUnitPrice(10.0);
        existing.setUnitCost(2.0);
        existing.setNumberSold(BigInteger.valueOf(50));

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(existing));
        when(productRepository.save(existing)).thenReturn(existing); // same instance

        ProductDTO patch = new ProductDTO();
        patch.setProductName("Updated Name"); // only changing name
        // All other fields left null

        Product updated = productService.updateProduct(5L, patch);

        verify(productRepository).findById(5L);
        verify(productRepository).save(existing);

        assertEquals("Updated Name", updated.getProductName());
        assertEquals("CategoryA", updated.getCategory()); // unchanged
        assertEquals(Double.valueOf(10.0), updated.getUnitPrice());
        assertEquals(Double.valueOf(2.0), updated.getUnitCost());
        assertEquals(BigInteger.valueOf(50), updated.getNumberSold());
    }

    @Test
    public void updateProduct_notFoundThrows() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        ProductDTO dto = new ProductDTO();
        dto.setProductName("X");
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(99L, dto));
    }
}
