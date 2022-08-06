package com.tdd.products.products.service;

import com.tdd.products.products.model.Product;
import com.tdd.products.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        Product existingProduct = productRepository.findProductById(product.getId());
        if (existingProduct != null) {
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setQuantity(product.getQuantity());
            existingProduct = productRepository.save(existingProduct);
        } else {
            logger.error("Product with id {} could not be updated!", product.getId());
        }
        return existingProduct;
    }

    public Product findById(Integer id) {
        logger.info("Finding product by id:{}", id);
        return productRepository.findProductById(id);
    }

    public void delete(Integer id) {
        logger.info("Deleting product with id:{}", id);
        Product existingProduct = productRepository.findProductById(id);
        if (existingProduct != null) {
            productRepository.delete(existingProduct);
        } else {
            logger.error("Product with id {} could not be found!", id);
        }
    }

    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }
}
