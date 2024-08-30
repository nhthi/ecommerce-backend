package com.nht.ecommerce.service;

import com.nht.ecommerce.exception.ProductException;
import com.nht.ecommerce.model.Product;
import com.nht.ecommerce.request.CreateProductReq;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public Product createProduct(CreateProductReq req);

    public String deleteProduct(Long productId) throws ProductException;

    public Product updateProduct (Long productId, Product product) throws ProductException;

    public Product findProductById(Long productId) throws ProductException;

    public List<Product> findProductByCategory(String category);

    public Page<Product> getAllProducts(String category,List<String> colors,List<String>sizes,int minPrice, int maxPrice,int minDiscount,String sort,String stock, int pageNumber,int pageSize );

    public List<Product> findAllProduct();

    public List<Product> searchProduct(String query);

    public List<Product> filterByBrandOrCategory(String brand, String category);
}
