package com.tdd.products.products.controller;

import com.tdd.products.products.model.Product;
import com.tdd.products.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductsController {
    private final ProductService productService;

    @GetMapping("/products")
    public Iterable<Product> getAllProducts() {
        return productService.findAll();
    }

    /**
     * Get the Product with specified ID
     *
     * @param id ID of the Product to get
     * @return ResponseEntity with the found Product
     * or NOT_FOUND if no Product found
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer id) {
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            try {
                return ResponseEntity
                        .ok()
                        .eTag(Integer.toString(product.getId()))
                        .location(new URI("/products/" + product.getId()))
                        .body(product);
            } catch (URISyntaxException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Saves a new Product
     *
     * @param product Product to save
     * @return ResponseEntity with the saved Product
     */
    @PostMapping("/products")
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        logger.info("Adding new product with name:{}", product.getName());
        Product newProduct = productService.save(product);
        try {
            return ResponseEntity
                    .created(new URI("/products/" + newProduct.getId()))
                    .eTag(Integer.toString(newProduct.getVersion()))
                    .body(newProduct);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update an existing Product
     *
     * @param product Product to update
     * @param ifMatch eTag version of the Product to update
     * @return ResponseEntity with the updated Product
     * or CONFLICT if eTag versions do not match
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id,
                                                 @RequestBody Product product,
                                                 @RequestHeader("If-Match") Integer ifMatch) {
        Optional<Product> existingProductOptional = productService.findById(id);
        if (!existingProductOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            Product existingProduct = existingProductOptional.get();
            if (!existingProduct.getVersion().equals(ifMatch)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } else {
                // safe to update the product
                logger.info("Updating product with name:{}", existingProduct.getName());

                existingProduct.setName(product.getName());
                existingProduct.setDescription(product.getDescription());
                existingProduct.setQuantity(product.getQuantity());
                existingProduct.setVersion(existingProduct.getVersion() + 1);

                try {
                    existingProduct = productService.update(existingProduct);
                    return ResponseEntity
                            .ok()
                            .eTag(Integer.toString(existingProduct.getVersion()))
                            .location(new URI("/products/" + existingProduct.getId()))
                            .body(existingProduct);
                } catch (URISyntaxException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
        }
    }

    /**
     * Delete an existing Product with given id
     *
     * @param id Product id to delete
     * @return ResponseEntity with HTTP status
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        logger.info("Deleting product with id:{}", id);

        Optional<Product> existingProductOptional = productService.findById(id);
        if (existingProductOptional.isPresent()) {
            productService.delete(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
