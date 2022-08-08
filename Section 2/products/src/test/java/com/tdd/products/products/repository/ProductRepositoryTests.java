package com.tdd.products.products.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdd.products.products.model.Product;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    private static final File DATA_JSON = Paths.get("src", "test", "resources", "products.json").toFile();

    @BeforeEach
    void setup() throws IOException {
        // Deserialize products from JSON file to Product array
        Product[] products = new ObjectMapper().readValue(DATA_JSON, Product[].class);

        // Save each product to database
        productRepository.saveAll(Arrays.asList(products));
    }

    @AfterEach
    void cleanup() {
        // Cleanup the database after each test
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("Test product not found with non-existing id")
    void testProductNotFoundForNonExistingId() {
        // Given two products in the database

        // When
        Optional<Product> retrievedProduct = productRepository.findById(100);

        // Then
        assertThat(retrievedProduct).withFailMessage("Product with id 100 should not exist").isNotPresent();
    }

    @Test
    @DisplayName("Test product saved successfully")
    void testProductSavedSuccessfully() {
        // Prepare mock product
        Product newProduct = new Product("New Product", "New Product Description", 8);

        // When
        Product savedProduct = productRepository.save(newProduct);

        // Then
        Assertions.assertNotNull(savedProduct, "Product should be saved");
        Assertions.assertNotNull(savedProduct.getId(), "Product should have an id when saved");
        Assertions.assertEquals(newProduct.getName(), savedProduct.getName());
    }

    @Test
    @DisplayName("Test product updated successfully")
    void testProductUpdatedSuccessfully() {
        // Prepare the product
        Product productToUpdate = new Product(1, "Updated Product", "New Product Description", 20, 2);

        // When
        Product updatedProduct = productRepository.save(productToUpdate);

        // Then
        Assertions.assertEquals(productToUpdate.getName(), updatedProduct.getName());
        Assertions.assertEquals(2, updatedProduct.getVersion());
        Assertions.assertEquals(20, updatedProduct.getQuantity());
    }

    @Test
    @DisplayName("Test product deleted successfully")
    void testProductDeletedSuccessfully() {
        // Given two products in the database


        // When
        productRepository.deleteById(1);

        // Then
        Assertions.assertEquals(1L, productRepository.count());
    }
}
