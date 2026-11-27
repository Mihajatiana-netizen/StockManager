
package com.stockmanager.model;

public class Movement {

    private int id;
    private int articleId;
    private String type;
    private int quantity;
    private String movementDate;
    private String description;
    private String articleName;

    // Constructeur pour un nouveau mouvement
    public Movement(
            int articleId,
            String type,
            int quantity,
            String description) {

        this.articleId = articleId;
        this.type = type;
        this.quantity = quantity;
        this.description = description;
    }

    // Constructeur pour un mouvement existant
    public Movement(
            int id,
            int articleId,
            String type,
            int quantity,
            String movementDate,
            String description) {

        this.id = id;
        this.articleId = articleId;
        this.type = type;
        this.quantity = quantity;
        this.movementDate = movementDate;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getArticleId() {
        return articleId;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMovementDate() {
        return movementDate;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setMovementDate(String movementDate) {
        this.movementDate = movementDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }
        public String getArticleName() {
    return articleName;
}

public void setArticleName(String articleName) {
    this.articleName = articleName;
}

public String getTypeLabel() {
    return "ENTRY".equals(type) ? "Entrée" : "Sortie";
}
}
