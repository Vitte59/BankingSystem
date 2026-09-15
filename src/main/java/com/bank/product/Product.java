package com.bank.product;

import com.bank.repository.HasId;

public abstract class Product implements FinancialProduct, HasId<String> {
    private String id;
    private String name;
    private String description;
    private boolean isActive;
    private final double minSum;
    private final double maxSum;
    private String currency;
    private final double interestRate;

    public Product (String id, String name, String description, boolean isActive,
                    double minSum, double maxSum, String currency,  double interestRate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.minSum = minSum;
        this.maxSum = maxSum;
        this.currency = currency;
        this.interestRate = interestRate;
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
    public double getMinSum() {
        return minSum;
    }
    public double getMaxSum() {
        return maxSum;
    }
    public String getCurrency() { return currency;}
    public double getInterestRate() { return interestRate; }
}

