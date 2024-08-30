package com.nht.ecommerce.request;

import com.nht.ecommerce.model.Category;
import com.nht.ecommerce.model.Rating;
import com.nht.ecommerce.model.Review;
import com.nht.ecommerce.model.Size;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductReq {
    private String title;

    private String description;

    private int price;

    private int discountPrice;

    private int discountPercent;

    private int quantity;

    private String brand;

    private String color;

    private Set<Size> sizes = new HashSet<>();

    private String imageUrl;

    private  String topLevelCategory;

    private  String secondLevelCategory;

    private  String thirdLevelCategory;

}
