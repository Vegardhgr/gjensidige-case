package no.gjensidige.product.service;

import no.gjensidige.product.dto.ProductDTO;
import no.gjensidige.product.exception.ProductNotFoundException;
import no.gjensidige.product.entity.Product;
import no.gjensidige.product.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ProductService
 *
 * Class responsible of data manipulation between dto and entity
 *
 *
 */

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    public Product getProduct(Long id) {

        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

    }

    public Product deleteProduct(Long id) {

        Product p  = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.deleteById(id);

        return p;
    }


    public Product createProduct(ProductDTO inputProduct) {

        Product product = convertToEntity(inputProduct);

        return productRepository.save(product);
    }


    private <T> void setIfNotNull(java.util.function.Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }


    public Product updateProduct(Long id, ProductDTO inputProduct) {
        Product existingProd = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        setIfNotNull(existingProd::setProductName, inputProduct.getProductName());
        setIfNotNull(existingProd::setCategory, inputProduct.getCategory());
        setIfNotNull(existingProd::setImageLink, inputProduct.getImageLink());
        setIfNotNull(existingProd::setUnitCost, inputProduct.getUnitCost());
        setIfNotNull(existingProd::setUnitPrice, inputProduct.getUnitPrice());
        setIfNotNull(existingProd::setNumberSold, inputProduct.getNumberSold());

        return productRepository.save(existingProd);

    }


    public ProductDTO convertToDTO(Product product) {

        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        return productDTO;
    }

    public Product convertToEntity(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);

        return product;

    }


}
