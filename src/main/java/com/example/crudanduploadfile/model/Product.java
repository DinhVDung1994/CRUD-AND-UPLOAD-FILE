package com.example.crudanduploadfile.model;

import javax.persistence.*;

// POJO = Plain Object Java Object
@Entity
// đặt tên cho bảng nếu không đặt sẽ mặc định lấy tên của class làm tên table
@Table(name = "tblProduct")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // Validate = constraint
    // nullable = false => yêu cầu không được để trống
    // unique = true    => yêu cầu không được trùng nhau
    // length = 300     => độ dài tối đa được phép là 300 kí tự
    @Column(nullable = false,unique = true,length = 300)
    private String productName;
    private int year;
    private Double price;
    private String url;

    public Product() {
    }

    public Product(String productName, int year, Double price, String url) {
        this.productName = productName;
        this.year = year;
        this.price = price;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", url='" + url + '\'' +
                '}';
    }
}