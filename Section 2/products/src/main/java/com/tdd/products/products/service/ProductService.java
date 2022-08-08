package com.tdd.products.products.service;

import com.tdd.products.products.model.Product;
import com.tdd.products.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product save(Product product) {
        logger.info("Saving new product with name:{}", product.getName());
        product.setVersion(1);
        return productRepository.save(product);
    }

    public Product update(Product product) {
        logger.info("Updating product with id:{}", product.getId());
        Optional<Product> existingProductOptional = productRepository.findById(product.getId());
        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setQuantity(product.getQuantity());
            return productRepository.save(existingProduct);
        } else {
            logger.error("Product with id {} could not be updated!", product.getId());
            return null;
        }
    }

    public Optional<Product> findById(Integer id) {
        logger.info("Finding product by id:{}", id);
        return productRepository.findById(id);
    }

    public void delete(Integer id) {
        logger.info("Deleting product with id:{}", id);
        Optional<Product> existingProductOptional = findById(id);
        if (existingProductOptional.isPresent()) {
            productRepository.delete(existingProductOptional.get());
        } else {
            logger.error("Product with id {} could not be found!", id);
        }
    }

    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }
}
