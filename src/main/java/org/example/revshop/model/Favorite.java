package org.example.revshop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "favorites")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Favorite(Long id, Integer userId, Product product) {
        this.id = id;
        this.userId = userId;
        this.product = product;
    }

    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Favorite() {}

    public Long getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
