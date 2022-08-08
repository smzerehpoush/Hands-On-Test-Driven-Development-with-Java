package com.tdd.products.products.service;

import com.tdd.products.products.model.Product;
import com.tdd.products.products.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    @DisplayName("Find product with id successfully")
    void testFindProductById() {
        Product mockProduct = new Product(1, "Product", "Description", 10, 1);

        doReturn(Optional.of(mockProduct)).when(productRepository).findById(1);

        Optional<Product> foundProduct = productService.findById(1);

        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isSameAs("Product");
    }

    @Test
    @DisplayName("Fail to find product with id")
    void testFailToFindProductById() {
        doReturn(Optional.empty()).when(productRepository).findById(1);

        Optional<Product> foundProduct = productService.findById(1);

        assertThat(foundProduct).isNotPresent();
    }

    @Test
    @DisplayName("Find all products")
    void testFindAllProducts() {
        Product firstProduct = new Product(1, "1st Product", "Description", 10, 1);
        Product secondProduct = new Product(2, "2nd Product", "Description", 10, 1);

        doReturn(Arrays.asList(firstProduct, secondProduct)).when(productRepository).findAll();

        Iterable<Product> allProducts = productService.findAll();

        assertThat(allProducts).hasSize(2);
    }

    @Test
    @DisplayName("Save new product successfully")
    void testSuccessfulProductSave() {
        Product mockProduct = new Product(1, "Product", "Description", 10, 1);

        doReturn(mockProduct).when(productRepository).save(any());

        Product savedProduct = productService.save(mockProduct);

        assertThat(savedProduct).withFailMessage("Product should not be null").isNotNull();
        assertThat(mockProduct.getName()).isSameAs("Product");
        assertThat(mockProduct.getVersion()).isSameAs(1);
    }

    @Test
    @DisplayName("Update an existing product successfully")
    void testUpdatingProductSuccessfully() {
        Product existingProduct = new Product(1, "Product", "Description", 10, 1);
        Product updatedProduct = new Product(1, "New Name", "Description", 20, 2);

        doReturn(Optional.of(existingProduct)).when(productRepository).findById(1);
        doReturn(updatedProduct).when(productRepository).save(existingProduct);

        Product updateProduct = productService.update(existingProduct);

        assertThat(updateProduct.getName()).isEqualTo("New Name");
    }

    @Test
    @DisplayName("Fail to update an existing product")
    void testFailToUpdateExistingProduct() {
        Product mockProduct = new Product(1, "Product", "Description", 10, 1);

        doReturn(Optional.empty()).when(productRepository).findById(1);

        Product updatedProduct = productService.update(mockProduct);

        assertThat(updatedProduct).withFailMessage("Product should be null").isNull();
    }

}
