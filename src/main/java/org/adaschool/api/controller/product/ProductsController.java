package org.adaschool.api.controller.product;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(value = "Controlador de productos", description = "Controla todos los endpoints para manejar diferentes productos")
public class ProductsController {

    private final ProductsService productsService;

    public ProductsController(@Autowired ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping
    @ApiOperation(value = "agrega un producto a la base de datos", response = Product.class)
    public ResponseEntity<Product> createProduct(
            @ApiParam(value = "cuerpo con los datos del producto a agregar", required = true) @RequestBody ProductDto productDto) {
        Product product = this.productsService.save(new Product(productDto));
        URI createdProductUri = URI.create(product.getId());
        return ResponseEntity.created(createdProductUri).body(product);
    }

    @GetMapping
    @ApiOperation(value = "devuelve todos los productos registrados en la base de datos")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = this.productsService.all();
        return ResponseEntity.ok(products);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "devuelve (si existe) el producto con el id indicado", response = Product.class)
    public ResponseEntity<Product> findById(
            @ApiParam(value = "id del producto a buscar", required = true) @PathVariable("id") String id) {
        Optional<Product> optionalProduct = this.productsService.findById(id);
        if (optionalProduct.isEmpty()) throw new ProductNotFoundException(id);
        return ResponseEntity.ok(optionalProduct.get());
    }

    @PutMapping("{id}")
    @ApiOperation(value = "edita (si existe) el producto con id indicado con los datos enviados", response = Product.class)
    public ResponseEntity<Product> updateProduct(
            @ApiParam(value = "id del producto a actualizar", required = true) @PathVariable String id,
            @ApiParam(value = "datos a modificar del producto", required = true) @RequestBody ProductDto productDto) {
        Optional<Product> optionalProduct = this.productsService.findById(id);
        if (optionalProduct.isEmpty()) throw new ProductNotFoundException(id);
        Product product = optionalProduct.get();
        product.update(productDto);
        this.productsService.save(product);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "borra (si existe) el producto con el id indicado", response = Product.class)
    public ResponseEntity<Void> deleteProduct(
            @ApiParam(value = "id del producto a borrar", required = true) @PathVariable String id) {
        if (this.productsService.findById(id).isEmpty()) throw new ProductNotFoundException(id);
        this.productsService.deleteById(id);
        return ResponseEntity.ok(null);
    }
}
