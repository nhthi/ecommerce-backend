package com.nht.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private int price;

    @Column(name="discount_price")
    private int discountPrice;

    @Column(name="discount_percent")
    private int discountPercent;
    private int quantity;

    private String brand;

    private String color;

//    @Embedded
    @ElementCollection
    @CollectionTable(name = "size", joinColumns = @JoinColumn(name = "product_id"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Size> sizes = new HashSet<>();

    private String imageUrl;

    @OneToMany(mappedBy = "product",cascade =CascadeType.ALL,orphanRemoval = true )
    private List<Rating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "product",cascade =CascadeType.ALL,orphanRemoval = true )
    private List<Review> reviews = new ArrayList<>();

    @Column(name="num_ratings")
    private int numRatings ;

    private LocalDateTime createAt;

//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<OrderItem> orderItems = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="caterogy_id")
    private Category category;
}
