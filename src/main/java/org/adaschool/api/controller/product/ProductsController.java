package org.adaschool.api.controller.product;

import org.adaschool.api.exception.ProductNotFoundException;
import org.adaschool.api.repository.product.Product;
import org.adaschool.api.repository.product.ProductDto;
import org.adaschool.api.service.product.ProductsService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/products/")
public class ProductsController {

    private final ProductsService productsService;

    public ProductsController(@Autowired ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto) {
        Product product = this.productsService.save(new Product(productDto));
        URI createdProductUri = URI.create(product.getId());
        return ResponseEntity.created(createdProductUri).body(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = this.productsService.all();
        return ResponseEntity.ok(products);
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") String id) {
        Optional<Product> optionalProduct = this.productsService.findById(id);
        if (optionalProduct.isEmpty()) throw new ProductNotFoundException(id);
        return ResponseEntity.ok(optionalProduct.get());
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id,@RequestBody ProductDto productDto) {
        Optional<Product> optionalProduct = this.productsService.findById(id);
        if (optionalProduct.isEmpty()) throw new ProductNotFoundException(id);
        Product product = optionalProduct.get();
        product.update(productDto);
        this.productsService.save(product);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        if (this.productsService.findById(id).isEmpty()) throw new ProductNotFoundException(id);
        this.productsService.deleteById(id);
        return ResponseEntity.ok(null);
    }
}
