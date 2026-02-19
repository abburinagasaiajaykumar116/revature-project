package org.example.revshop.model;



import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String name;

    @Column(length = 2000)
    private String description;

    private Double price;
    private Double mrp;
    private Double discount;
    private Integer stockThreshold;

    public Product(Long productId, String name, String description, Double price, Double mrp, Double discount, Integer stockThreshold, Boolean isActive, Integer stockQuantity, Long sellerId, Integer categoryId) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.mrp = mrp;
        this.discount = discount;
        this.stockThreshold = stockThreshold;
        this.isActive = isActive;
        this.stockQuantity = stockQuantity;
        this.sellerId = sellerId;
        this.categoryId = categoryId;
    }

    private Boolean isActive = true;
    private Integer stockQuantity;
    private Long sellerId;
    private Integer categoryId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getStockThreshold() {
        return stockThreshold;
    }

    public void setStockThreshold(Integer stockThreshold) {
        this.stockThreshold = stockThreshold;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }


   public  Product(){}








    // getters setters
}
