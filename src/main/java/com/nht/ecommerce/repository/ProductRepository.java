package com.nht.ecommerce.repository;

import com.nht.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {


    @Query("select p from Product p where (p.category.name=:category or :category='' )" +
            "and ((:minPrice is null and :maxPrice is null) or (p.discountPrice between :minPrice and :maxPrice))" +
            "and (:minDiscount is null or p.discountPercent>=:minDiscount)" +
            "order by " +
            "case when :sort='price_low' then p.discountPrice end asc, " +
            "case when :sort='price_hight' then p.discountPrice end desc ")
    public List<Product> filterProducts(@Param("category") String category,
                                        @Param("minPrice") int minPrice,
                                        @Param("maxPrice")int maxPrice,
                                        @Param("minDiscount")int minDiscount,
                                        @Param("sort")String sort);


    @Query("select p from Product p where p.title like %:query% or p.description like %:query% or p.brand like %:query% " +
            "or p.category.name like %:query% or p.category.parentCategory.name like %:query% or p.category.parentCategory.parentCategory.name like %:query%")
    public List<Product> searchProduct(@Param("query") String query);

    @Query("select p from Product p where p.brand like %:brand% or p.category.name like %:category%")
    public List<Product> filterByBrandOrCategory(@Param("brand") String brand,@Param("category")String category);
}
