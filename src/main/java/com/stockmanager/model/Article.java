
package com.stockmanager.model;

public class Article {

    private int id;
    private String reference;
    private String name;
    private String description;
    private double purchasePrice;
    private double sellingPrice;
    private int quantity;
    private int minimumStock;
    private Integer categoryId;
    private String createdAt;
    private String categoryName;

    // Constructeur pour créer un nouvel article
    public Article(String reference, String name, String description,
                   double purchasePrice, double sellingPrice,
                   int minimumStock, Integer categoryId) {

        this.reference = reference;
        this.name = name;
        this.description = description;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.quantity = 0;
        this.minimumStock = minimumStock;
        this.categoryId = categoryId;
    }
    public Article(
        String reference,
        String name,
        String description,
        double purchasePrice,
        double sellingPrice,
        int quantity,
        int minimumStock,
        Integer categoryId) {

    this.reference = reference;
    this.name = name;
    this.description = description;
    this.purchasePrice = purchasePrice;
    this.sellingPrice = sellingPrice;
    this.quantity = quantity;
    this.minimumStock = minimumStock;
    this.categoryId = categoryId;
}

    // Constructeur pour récupérer un article depuis la base
    public Article(int id, String reference, String name,
                   String description, double purchasePrice,
                   double sellingPrice, int quantity,
                   int minimumStock, Integer categoryId,
                   String createdAt) {

        this.id = id;
        this.reference = reference;
        this.name = name;
        this.description = description;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
        this.minimumStock = minimumStock;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getMinimumStock() {
        return minimumStock;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String getCategoryName() {
    return categoryName;
}

public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
}
@Override
public String toString() {
    return reference + " - " + name;
}
}
