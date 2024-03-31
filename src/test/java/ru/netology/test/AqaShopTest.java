package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.MainPage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.open;

public class AqaShopTest {

    @BeforeAll
    static void setupAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void successPathApprovedCard() {
        var mainPage = open("http://localhost:8080", MainPage.class);
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formFillingCard(cardInfo);
        buyPage.findMessageContent("Успешно", "Операция одобрена Банком.");
        Assertions.assertEquals("APPROVED", SQLHelper.getStatus());
    }



}