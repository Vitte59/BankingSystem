package com.bank.product;

public abstract class Product implements FinancialProduct {
    private String id;
    private String name;
    private String description;
    private boolean isActive;
    private double maxSum;
    private double minSum;
    private String currency;

    public Product (String id, String name, String description, boolean isActive,
                    double maxSum, double minSum, String currency) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.maxSum = maxSum;
        this.minSum = minSum;
        this.currency = currency;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public boolean isActive() {
        return isActive;
    }
    public double getMaxSum() {
        return maxSum;
    }
    public double getMinSum() {
        return minSum;
    }
    public String getCurrency() {
        return currency;
    }
}

