package com.bank.application;

import java.time.LocalDate;

public class Application {
    private final String id;
    private final String clientId;
    private final String productId;
    private final double amount;
    private final int term;
    private final String status;
    private final LocalDate creationDate;

    private Application(Builder builder) {
        this.id = builder.id;
        this.clientId = builder.clientId;
        this.productId = builder.productId;
        this.amount = builder.amount;
        this.term = builder.term;
        this.status = builder.status;
        this.creationDate = builder.creationDate;
    }

        public String getId() {
            return id;
        }
        public String getClientId() { return clientId; }
        public String getProductId() { return productId; }
        public double getAmount() { return amount; }
        public int getTerm() { return term; }
        public String getStatus() { return status; }
        public LocalDate getCreationDate() { return creationDate; }


        public static class Builder {
            private String id;
            private String clientId;
            private String productId;
            private double amount;
            private int term;
            private String status;
            private LocalDate creationDate;

            public Builder id (String id) {
                this.id = id;
                return this;
            }

            public Builder clientId(String clientId) {
                this.clientId = clientId;
                return this;
            }

            public Builder productId(String productId) {
                this.productId = productId;
                return this;
            }

            public Builder amount (double amount) {
                this.amount = amount;
                return this;
            }

            public Builder term (int term) {
                this.term = term;
                return this;
            }

            public Builder status(String status) {
                this.status = status;
                return this;
            }

            public Builder creationDate(LocalDate creationDate) {
                this.creationDate = creationDate;
                return this;
            }

            public Application build() {
                if (id == null || id.trim().isEmpty()) {
                    throw new IllegalArgumentException("id не может быть пустым");
                }
                if (clientId == null || clientId.trim().isEmpty()) {
                    throw new IllegalArgumentException("client ID не может быть пустым");
                }
                if (productId == null || productId.trim().isEmpty()) {
                    throw new IllegalArgumentException("Product ID не может быть пустым");
                }
                if (amount <= 0) {
                    throw new IllegalArgumentException("amount должен быть больше 0");
                }

                return new Application(this);

            }





        }


}
