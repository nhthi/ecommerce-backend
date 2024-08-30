package com.nht.ecommerce.controller;

import com.nht.ecommerce.exception.ProductException;
import com.nht.ecommerce.model.Product;
import com.nht.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<Product>> findProductByCategoryHandler(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) List<String> color, @RequestParam(required = false) List<String> size, @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice, @RequestParam(required = false) Integer minDiscount, @RequestParam(required = false) String sort,
            @RequestParam(required = false) String stock, @RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize
    ) {
        Page<Product> res = productService.getAllProducts(
                category, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> findProductById(@PathVariable("productId") Long productId) throws ProductException {
        Product product = productService.findProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String query) throws ProductException {
        List<Product>  products = new ArrayList<>();
        if(query.length() >0 ){
            products = productService.searchProduct(query);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterProducts(@RequestParam String brand,@RequestParam String category) throws ProductException {
        List<Product>  products = new ArrayList<>();
        if(brand.length() >0 || category.length()>0){
            products = productService.filterByBrandOrCategory(brand,category);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
