package com.bank.product;

public class CreditProduct extends Product {

    private LoanType loanType;
    private String repaymentSchedule;
    private boolean hasCollateral; //залог


    public CreditProduct(String id, String name, String description, boolean isActive,
                         double maxSum, double minSum, String currency, LoanType loanType,
                         String repaymentSchedule, boolean hasCollateral) {
        super(id, name, description, isActive, maxSum, minSum, currency);
        this.loanType = loanType;
        this.repaymentSchedule = repaymentSchedule;
        this.hasCollateral = hasCollateral;
    }
    public LoanType getLoanType() {
        return loanType;
    }

    public String getRepaymentSchedule() {
        return repaymentSchedule;
    }

    public boolean hasCollateral() {
        return hasCollateral;
    }
}


