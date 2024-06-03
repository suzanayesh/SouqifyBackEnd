package com.code.auth.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class ProductPic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private String url;

    public ProductPic() {
    }

    public ProductPic( Long productId, String url) {
        this.productId = productId;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductPic that = (ProductPic) o;
        return Objects.equals(id, that.id) && Objects.equals(productId, that.productId) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, url);
    }

    @Override
    public String toString() {
        return "ProductPic{" +
                "id=" + id +
                ", productId=" + productId +
                ", url='" + url + '\'' +
                '}';
    }

    // getters and setters
}