
package com.stockmanager.model;

public class DashboardStats {

    private final int totalArticles;
    private final int totalCategories;
    private final int lowStockCount;
    private final int outOfStockCount;
    private final int totalMovements;
    private final double totalStockValue;

    public DashboardStats(
            int totalArticles,
            int totalCategories,
            int lowStockCount,
            int outOfStockCount,
            int totalMovements,
            double totalStockValue) {

        this.totalArticles = totalArticles;
        this.totalCategories = totalCategories;
        this.lowStockCount = lowStockCount;
        this.outOfStockCount = outOfStockCount;
        this.totalMovements = totalMovements;
        this.totalStockValue = totalStockValue;
    }

    public int getTotalArticles() {
        return totalArticles;
    }

    public int getTotalCategories() {
        return totalCategories;
    }

    public int getLowStockCount() {
        return lowStockCount;
    }

    public int getOutOfStockCount() {
        return outOfStockCount;
    }

    public int getTotalMovements() {
        return totalMovements;
    }

    public double getTotalStockValue() {
        return totalStockValue;
    }
}
