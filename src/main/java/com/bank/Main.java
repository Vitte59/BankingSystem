package com.bank;

import com.bank.application.Application;
import com.bank.exception.BusinessException;
import com.bank.product.LoanType;
import com.bank.product.Product;
import com.bank.product.ProductFactory;
import com.bank.service.ApplicationProcessor;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws BusinessException {

        System.out.println("Запуск системы\n");

        // 1. Создаём кредитный продукт через фабрику
        Product credit = ProductFactory.createCreditProduct(
                "P001",
                "Ипотека",
                "Долгосрочный кредит на жилье",
                true,          // активен
                100_000,       // мин сумма
                10_000_000,    // макс сумма
                "RUB",
                LoanType.MORTGAGE,
                "Аннуитет",
                true           // есть залог
        );

        // 2. Создаём заявку через Builder
        Application application = new Application.Builder()
                .id("APP001")
                .clientId("CLIENT_123")
                .productId("P001")
                .amount(5_000_000) // в пределах диапазона (100k - 10M)
                .term(120)         // срок 10 лет
                .status("PENDING")
                .creationDate(LocalDate.now())
                .build();

        System.out.println("Заявка создана. Передаём в обработку...\n");

        // 3. Получаем синглтон-процессор и обрабатываем заявку
        ApplicationProcessor processor = ApplicationProcessor.getInstance();
        processor.process(application, credit);

        System.out.println("\nЗаявка успешно обработана!");
    }
}