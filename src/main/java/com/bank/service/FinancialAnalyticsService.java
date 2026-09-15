package com.bank.service;

import com.bank.application.Application;
import com.bank.client.Client;
import com.bank.product.CreditProduct;
import com.bank.product.DepositProduct;
import com.bank.product.Product;
import com.bank.repository.CrudRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FinancialAnalyticsService {

    private final CrudRepository<Product, String> productRepository;
    private final CrudRepository<Application, String>  applicationRepository;
    private final CrudRepository<Client, String> clientRepository;

    public FinancialAnalyticsService(CrudRepository<Product, String> productRepository,
                                     CrudRepository<Application, String> applicationRepository,
                                     CrudRepository<Client, String> clientRepository) {
        this.productRepository = productRepository;
        this.applicationRepository = applicationRepository;
        this.clientRepository = clientRepository;
    }

    public record ProductStats(double averageInterestRate, double averageMinSum) {}

// пункты 2 и 1 из раздела Аналитические функции
    public List<Product> findActiveProductsByCriteria(double minRate, double maxMinSum) {
        return productRepository.findAll().stream()
                .filter(Product::isActive) // 1. Продукт активен
                .filter(p -> p.getInterestRate() > minRate) // 2. Ставка > minRate
                .filter(p -> p.getMinSum() <= maxMinSum) // 3. Мин. сумма <= maxMinSum
                .collect(Collectors.toList());
    }

    public Map<String, ProductStats> getAverageStatsByProductType() {
        return productRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        product -> (product instanceof CreditProduct) ? "CREDIT" : "DEPOSIT",
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    double avgRate = list.stream().mapToDouble(Product::getInterestRate).average().orElse(0.0);
                                    double avgMinSum = list.stream().mapToDouble(Product::getMinSum).average().orElse(0.0);
                                    return new ProductStats(avgRate, avgMinSum);
                                }
                        )
                ));
    }
    // пункт 3
    public double getAverageApplicationAmount() {
        return applicationRepository.findAll().stream()
                .mapToDouble(Application::getAmount)
                .average()
                .orElse(0.0);
    }

    public int getMaxApplicationTerm() {
        return applicationRepository.findAll().stream()
                .mapToInt(Application::getTerm)
                .max().orElse(0);
    }

    public Map<String, Double> getApplicationStatusDistribution() {
        List<Application> allApps = applicationRepository.findAll();
        if (allApps.isEmpty()) {
            return Collections.emptyMap(); // Защита от деления на 0
        }

        return allApps.stream()
                .collect(Collectors.groupingBy(
                        Application::getStatus, // 1. Группируем по названию статуса
                        Collectors.collectingAndThen(
                                Collectors.counting(), // 2. Считаем количество заявок в статусе
                                count -> (count * 100.0) / allApps.size() // 3. Переводим количество в %
                        )
                ));
    }
// пункт 4
    public List<Product> findUnusedProductsInPeriod(LocalDate startDate, LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("даты начала и конца периода не могут быть null");
        }

        Set<String> productIdsWithApplicationsInPeriod = applicationRepository.findAll().stream()
                .filter(app -> app.getCreationDate() != null)
                .filter(app -> !app.getCreationDate().isBefore(startDate) && !app.getCreationDate().isAfter(endDate))
                .map(Application::getProductId)
                .collect(Collectors.toSet());

        return productRepository.findAll().stream()
                .filter(product -> !productIdsWithApplicationsInPeriod.contains(product.getId()))
                .collect(Collectors.toList());
    }
//пункт 5
    public Map<String, Double> getAverageAmountByClientAgeGroup() {
        return applicationRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        app -> {
                            Client client = clientRepository.findById(app.getClientId()).orElse(null);
                            if (client == null) {
                                return "Неизвестен";
                            }

                            int age = client.getAge();
                            if (age < 30) {
                                return "<30";
                            } else if (age <= 50) {
                                return "30-50";
                            } else {
                                return ">50";
                            }
                        },
                        Collectors.averagingDouble(Application::getAmount)
                ));
    }
    // пункт 6
    public record ProductPopularityStats(
            Product product,
            long totalApplications,
            double approvalRate
    ) {}

    public List<ProductPopularityStats> getProductPopularityRanking() {
        return productRepository.findAll().stream()
                .map(product -> {
                    List<Application> productApps = applicationRepository.findAll().stream()
                            .filter(app -> app.getProductId().equals(product.getId()))
                            .toList();

                    long totalApps = productApps.size();

                    long approvedApps = productApps.stream()
                            .filter(app -> "APPROVED".equalsIgnoreCase(app.getStatus()))
                            .count();

                    double approvalRate = (totalApps == 0) ? 0.0 : (approvedApps * 100.0) / totalApps;

                    return new ProductPopularityStats(product, totalApps, approvalRate);
                })
                // сортировка сначала по количеству заявок, затем по % одобрения
                .sorted(Comparator.comparing(ProductPopularityStats::totalApplications).reversed()
                        .thenComparing(Comparator.comparing(ProductPopularityStats::approvalRate).reversed()))
                .collect(Collectors.toList());
    }
    // пункт 7

    public List<Application> getApplicationsByProductType(String productType) {
        if (productType == null) {
            return Collections.emptyList();
        }

        Set<String> targetProductIds = productRepository.findAll().stream()
                .filter(p -> ("CREDIT".equalsIgnoreCase(productType) && p instanceof CreditProduct)
                        || ("DEPOSIT".equalsIgnoreCase(productType) && p instanceof DepositProduct))
                .map(Product::getId)
                .collect(Collectors.toSet());

        return applicationRepository.findAll().stream()
                .filter(app -> targetProductIds.contains(app.getProductId()))
                .collect(Collectors.toList());
    }

    // или группировка по конкретным объектам продуктов:

    public Map<Product, List<Application>> getApplicationsGroupedByProduct() {
        return productRepository.findAll().stream()
                .collect(Collectors.toMap(
                        product -> product,
                        product -> applicationRepository.findAll().stream()
                                .filter(app -> app.getProductId().equals(product.getId()))
                                .collect(Collectors.toList())
                ));
    }
}

