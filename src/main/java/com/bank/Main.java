package com.bank;

import com.bank.application.Application;
import com.bank.client.Client;
import com.bank.exception.BusinessException;
import com.bank.product.LoanType;
import com.bank.product.Product;
import com.bank.product.ProductFactory;
import com.bank.repository.InMemoryRepository;
import com.bank.service.ApplicationProcessor;
import com.bank.service.FinancialAnalyticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws BusinessException {
        log.info("ПРОВЕРКА ЧАСТИ 1 (Создание объектов и Синглтон-процессор)");

        // 1.1 Создаём кредитный продукт через фабрику ("RUB" идет до 12.5)
        Product credit = ProductFactory.createProduct(
                "CREDIT",
                "P001",
                "Ипотека",
                "Долгосрочный кредит на жилье",
                true,          // активен
                100_000,       // мин сумма
                10_000_000,    // макс сумма
                "RUB",         // валюта
                12.5,          // процентная ставка
                LoanType.MORTGAGE,
                "Аннуитет",
                true,          // есть залог
                null,          // earlyClosureTerms
                false          // isRefillable
        );

        Application application = new Application.Builder()
                .id("APP001")
                .clientId("CLIENT_123")
                .productId("P001")
                .amount(5_000_000)
                .term(120)
                .status("APPROVED")
                .creationDate(LocalDate.now().minusDays(5))
                .build();

        log.info("Заявка создана. Передаём в сервис валидации и обработки.");

        ApplicationProcessor processor = ApplicationProcessor.getInstance();
        processor.process(application, credit);
        log.info("Заявка успешно прошла базовую обработку.");


        log.info("\nПРОВЕРКА ЧАСТИ 2 (In-Memory Репозитории, Сортировка и Аналитика)");


        InMemoryRepository<Product, String> productRepo = new InMemoryRepository<>();
        InMemoryRepository<Application, String> applicationRepo = new InMemoryRepository<>();
        InMemoryRepository<Client, String> clientRepo = new InMemoryRepository<>();


        Client client1 = new Client("CLIENT_123", "Иван Иванов", 25);
        Client client2 = new Client("CLIENT_456", "Петр Петров", 42);
        Client client3 = new Client("CLIENT_789", "Егор Воронов", 61);

        clientRepo.save(client1);
        clientRepo.save(client2);
        clientRepo.save(client3);


        productRepo.save(credit);

        Product credit2 = ProductFactory.createProduct("CREDIT", "P002", "Автокредит", "Кредит на авто", true, 50_000, 3_000_000, "RUB", 16.0, LoanType.CONSUMER, "Дифференцированный", true, null, false);
        Product deposit1 = ProductFactory.createProduct("DEPOSIT", "P003", "Вклад Накопительный", "Высокий процент", true, 10_000, 5_000_000, "RUB", 18.5, null, null, false, "30 дней", true);
        Product deposit2 = ProductFactory.createProduct("DEPOSIT", "P004", "Вклад Пенсионный", "Социальный вклад", false, 1_000, 1_000_000, "RUB", 19.0, null, null, false, "без штрафа", false);

        productRepo.save(credit2);
        productRepo.save(deposit1);
        productRepo.save(deposit2);

        applicationRepo.save(application);

        Application app2 = new Application.Builder().id("APP002").clientId("CLIENT_456").productId("P001").amount(2_000_000).term(60).status("PENDING").creationDate(LocalDate.now().minusDays(2)).build();
        Application app3 = new Application.Builder().id("APP003").clientId("CLIENT_789").productId("P003").amount(500_000).term(12).status("REJECTED").creationDate(LocalDate.now().minusDays(10)).build();

        applicationRepo.save(app2);
        applicationRepo.save(app3);

        log.info("Данные загружены в репозитории. Продуктов: {}, Заявок: {}, Клиентов: {}",
                productRepo.findAll().size(), applicationRepo.findAll().size(), clientRepo.findAll().size());


        // Демонстрация системы сортировки (Цепочка Comparator)
        log.info("Сортировка продуктов по критериям (Сначала активные, затем по ставке)");
        Comparator<Product> productComparator = Comparator.comparing(Product::isActive).reversed()
                .thenComparing(Comparator.comparing(Product::getInterestRate).reversed());

        List<Product> sortedProducts = productRepo.findAll(productComparator);
        sortedProducts.forEach(p -> log.info("Продукт: {}, Активен: {}, Ставка: {}%", p.getName(), p.isActive(), p.getInterestRate()));


        // Stream API - тест 7 пунктов
        log.info("Бизнес-аналитика (FinancialAnalyticsService)");
        FinancialAnalyticsService analytics = new FinancialAnalyticsService(productRepo, applicationRepo, clientRepo);

        log.info("Пункт 1 (Средняя ставка и мин.сумма по типам): {}", analytics.getAverageStatsByProductType());
        log.info("Пункт 2 (Фильтр: ставка > 15%, мин.сумма <= 100k): Найдено = {}", analytics.findActiveProductsByCriteria(15.0, 100_000).size());
        log.info("Пункт 3a (Средняя сумма заявки): {}", analytics.getAverageApplicationAmount());
        log.info("Пункт 3b (Максимальный срок в мес): {}", analytics.getMaxApplicationTerm());
        log.info("Пункт 3c (Распределение статусов %): {}", analytics.getApplicationStatusDistribution());
        log.info("Пункт 4 (Невостребованные продукты за 3 дня): {}", analytics.findUnusedProductsInPeriod(LocalDate.now().minusDays(3), LocalDate.now()).stream().map(Product::getName).toList());
        log.info("Пункт 5 (Средняя сумма по возрастным группам клиентов): {}", analytics.getAverageAmountByClientAgeGroup());
        log.info("Пункт 6 (Рейтинг популярности продуктов): {}", analytics.getProductPopularityRanking());
        log.info("Пункт 7a (Заявки по кредитным продуктам): Найдено = {}", analytics.getApplicationsByProductType("CREDIT").size());
        log.info("Пункт 7b (Группировка всех заявок по продуктам): Map содержит продуктов = {}", analytics.getApplicationsGroupedByProduct().size());

        log.info("Запуск банковской системы полностью завершен");
    }
}