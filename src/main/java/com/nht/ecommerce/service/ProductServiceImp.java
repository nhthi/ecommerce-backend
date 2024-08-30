package com.nht.ecommerce.service;

import com.nht.ecommerce.exception.ProductException;
import com.nht.ecommerce.model.Category;
import com.nht.ecommerce.model.Product;
import com.nht.ecommerce.repository.CategoryRepository;
import com.nht.ecommerce.repository.OrderItemRepository;
import com.nht.ecommerce.repository.ProductRepository;
import com.nht.ecommerce.request.CreateProductReq;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public Product createProduct(CreateProductReq req) {
        Category topLevel = categoryRepository.findByName(req.getTopLevelCategory());
        if (topLevel == null) {
            topLevel = new Category();
            topLevel.setName(req.getTopLevelCategory());
            topLevel.setLevel(1);
            categoryRepository.save(topLevel);
        }

        Category secondLevel = categoryRepository.findByNameAndParent(req.getSecondLevelCategory(), topLevel.getName());
        if (secondLevel == null) {
            secondLevel = new Category();
            secondLevel.setName(req.getSecondLevelCategory());
            secondLevel.setParentCategory(topLevel);
            secondLevel.setLevel(2);
            categoryRepository.save(secondLevel);
        }

        Category thirdLevel = categoryRepository.findByNameAndParent(req.getThirdLevelCategory(), secondLevel.getName());
        if (thirdLevel == null) {
            thirdLevel = new Category();
            thirdLevel.setName(req.getThirdLevelCategory());
            thirdLevel.setParentCategory(secondLevel);
            thirdLevel.setLevel(3);
            categoryRepository.save(thirdLevel);
        }


        Product product = new Product();
        product.setTitle(req.getTitle());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setBrand(req.getBrand());
        product.setColor(req.getColor());
        product.setSizes(req.getSizes());
        product.setImageUrl(req.getImageUrl());
        product.setCreateAt(LocalDateTime.now());
        product.setQuantity(req.getQuantity());
        product.setCategory(thirdLevel);
        product.setDiscountPrice(req.getDiscountPrice());
        product.setDiscountPercent( req.getDiscountPercent());


        return productRepository.save(product);

    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {

        Product product = findProductById(productId);
        product.getSizes().clear();
//        orderItemRepository.deleteByProductId(productId);
        productRepository.deleteById(productId);

        return "Product deleted Successfully with id: " + productId;
    }

    @Override
    public Product updateProduct(Long productId, Product req) throws ProductException {
        Product updateProduct = findProductById(productId);
        if (req.getQuantity() != 0) {
            updateProduct.setQuantity(req.getQuantity());
        }
        return productRepository.save(updateProduct);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {

        Optional<Product> opt = productRepository.findById(productId);
        if (opt.isEmpty()) {
            throw new ProductException("Product not found with id: " + productId);
        }
        return opt.get();
    }

    @Override
    public List<Product> findProductByCategory(String category) {
        return List.of();
    }

    @Override
    public Page<Product> getAllProducts(String category, List<String> colors, List<String> sizes, int minPrice, int maxPrice, int minDiscount, String sort, String stock, int pageNumber, int pageSize) {

        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);

        if (colors!=null && !colors.isEmpty()) {
            products = products.stream().
                    filter(
                            product -> colors.stream().anyMatch((color -> color.equalsIgnoreCase(product.getColor())))
                    ).collect(Collectors.toList());
        }

        if (stock != null) {
            if (stock.equals("in_stock")) {
                products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
            } else if(stock.equals("out_of_stock")){
                products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());
            }
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());

        List<Product> pageContent = products.subList(startIndex, endIndex);
        Page<Product> filteredProduct = new PageImpl<>(pageContent, pageable, products.size());

        return filteredProduct;
    }

    @Override
    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> searchProduct(String query) {
        List<Product> products = productRepository.searchProduct(query);
        return products;
    }

    @Override
    public List<Product> filterByBrandOrCategory(String brand, String category) {
        return productRepository.filterByBrandOrCategory(brand,category);
    }


}
