package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
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
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formFillingCard(cardInfo);
        buyPage.findMessageContent("Успешно", "Операция одобрена Банком.");
        Assertions.assertEquals("APPROVED", SQLHelper.getStatus());
    }

    @Test
    void successPathDeclinedCard() throws InterruptedException {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getDeclinedCardAllForm();
        buyPage.formFillingCard(cardInfo);
        buyPage.findMessageContent("Ошибка", "Ошибка! Банк отказал в проведении операции.");
        Assertions.assertEquals("DECLINED", SQLHelper.getStatus());
    }

    @Test
    void cardNumberEmpty() throws InterruptedException {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryNumber(cardInfo, "");
        buyPage.findMessageError("number", "Поле обязательно для заполнения");

    }

    @Test
    void cardNumberOneSymbol() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryNumber(cardInfo, DataHelper.generateInvalidNumericData(1));
        buyPage.findMessageError("number", "Неверный формат");
    }

    @Test
    void cardNumberFifteenSymbol() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryNumber(cardInfo, DataHelper.generateInvalidNumericData(15));
        buyPage.findMessageError("number", "Неверный формат");
    }

    @Test
    void cardNumberSeventeenSymbol() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("number", DataHelper.generateInvalidNumericData(17));
        int expected = 19; //17 символов обрезается до 16 и добавляется 3 промежуточных пробела
        int actual = buyPage.getCardNumberFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardNumberCyrillic() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("number", DataHelper.generateInvalidCyrillicData(16));
        int expected = 0;
        int actual = buyPage.getCardNumberFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardNumberLatin() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("number", DataHelper.generateInvalidLatinData(16));
        int expected = 0;
        int actual = buyPage.getCardNumberFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardNumberSymbol() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("number", DataHelper.generateInvalidSymbolData(16));
        int expected = 0;
        int actual = buyPage.getCardNumberFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardMonthEmpty() throws InterruptedException {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryMonth(cardInfo, "");
        buyPage.findMessageError("month", "Поле обязательно для заполнения");

    }

    @Test
    void cardMonthOneSymbol() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryMonth(cardInfo, DataHelper.generateInvalidNumericData(1));
        buyPage.findMessageError("month", "Неверный формат");
    }

    @Test
    void cardMonthZero() throws InterruptedException {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryMonth(cardInfo, "00");

        buyPage.findMessageError("month", "Неверный формат");

    }

    @Test
    void cardMonthThirteen() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryMonth(cardInfo, "13");
        buyPage.findMessageError("month", "Неверно указан срок действия карты");
    }

    @Test
    void cardMonthMoreFourteen() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryMonth(cardInfo, DataHelper.generateInvalidMonth());
        buyPage.findMessageError("month", "Неверно указан срок действия карты");
    }

    @Test
    void cardMonthCurrentYearLastMonth() {
        String currentMonth = DataHelper.getCurrentMonth();
        String month = "";
        String year = "";
        if (currentMonth.equals("01")) {
            month = "12";
            year = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
        } else {
            month = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("MM"));
            year = DataHelper.gerCurrentYear();
        }
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryMonthAndYear(cardInfo, month, year);
        buyPage.findMessageError("month", "Неверно указан срок действия карты");
    }

    @Test
    void cardMonthThreeSymbol() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("month", DataHelper.generateInvalidNumericData(3));
        int expected = 2;
        int actual = buyPage.getCardMonthFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardMonthCyrillic() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("month", DataHelper.generateInvalidCyrillicData(2));
        int expected = 0;
        int actual = buyPage.getCardMonthFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardMonthLatin() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("month", DataHelper.generateInvalidLatinData(2));
        int expected = 0;
        int actual = buyPage.getCardMonthFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardMonthSymbol() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("month", DataHelper.generateInvalidSymbolData(2));
        int expected = 0;
        int actual = buyPage.getCardMonthFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardYearEmpty() throws InterruptedException {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryYear(cardInfo, "");
        buyPage.findMessageError("year", "Поле обязательно для заполнения");
    }

    @Test
    void cardYearOneSymbol() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryYear(cardInfo, DataHelper.generateInvalidNumericData(1));
        buyPage.findMessageError("year", "Неверный формат");
    }

    @Test
    void cardYearZero() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryYear(cardInfo, "00");
        buyPage.findMessageError("year", "Истёк срок действия карты");
    }

    @Test
    void cardYearThreeSymbol() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("year", DataHelper.generateInvalidNumericData(3));
        int expected = 2;
        int actual = buyPage.getCardYearFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardYearCyrillic() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("year", DataHelper.generateInvalidCyrillicData(2));
        int expected = 0;
        int actual = buyPage.getCardYearFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardYearLatin() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("year", DataHelper.generateInvalidLatinData(2));
        int expected = 0;
        int actual = buyPage.getCardYearFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardYearSymbol() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("year", DataHelper.generateInvalidSymbolData(2));
        int expected = 0;
        int actual = buyPage.getCardYearFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardYearLastYear() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryYear(cardInfo, LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy")));
        buyPage.findMessageError("year", "Истёк срок действия карты");
    }

    @Test
    void cardYearPlusSix() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryYear(cardInfo, LocalDate.now().plusYears(6).format(DateTimeFormatter.ofPattern("yy")));
        buyPage.findMessageError("year", "Неверно указан срок действия карты");
    }

    @Test
    void cardYearMoreYearPlusSeven() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryYear(cardInfo, DataHelper.generateInvalidYear());
        buyPage.findMessageError("year", "Неверно указан срок действия карты");
    }

    @Test
    void cardOwnerEmpty() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryOwner(cardInfo, "");
        buyPage.findMessageError("owner", "Поле обязательно для заполнения");
    }

    @Test
    void cardOwnerCyrillic() throws InterruptedException {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("owner", DataHelper.generateInvalidOwnerCyrillic(5, 8));
        int expected = 0;
        int actual = buyPage.getCardOwnerFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardOwnerNumeric() throws InterruptedException {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("owner", DataHelper.generateInvalidOwnerNumeric(5, 8));
        int expected = 0;
        int actual = buyPage.getCardOwnerFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardOwnerSymbol() throws InterruptedException {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("owner", DataHelper.generateInvalidOwnerSymbol(5, 8));
        int expected = 0;
        int actual = buyPage.getCardOwnerFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardOwnerTwentyTwoSymbol() throws InterruptedException {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("owner", DataHelper.generateInvalidLatinData(10) + " " + DataHelper.generateInvalidLatinData(11));
        int expected = 21;
        int actual = buyPage.getCardOwnerFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardOwnerNotWhitespace() throws InterruptedException {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryOwner(cardInfo, DataHelper.generateInvalidLatinData(14));
        buyPage.findMessageError("owner", "Неверный формат");
    }

    @Test
    void cardCodeEmpty() throws InterruptedException {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryCode(cardInfo, "");
        buyPage.findMessageError("code", "Поле обязательно для заполнения");
    }

    @Test
    void cardCodeOneSymbol() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryCode(cardInfo, DataHelper.generateInvalidNumericData(1));
        buyPage.findMessageError("code", "Неверный формат");
    }

    @Test
    void cardCodeTwoSymbol() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryCode(cardInfo, DataHelper.generateInvalidNumericData(2));
        buyPage.findMessageError("code", "Неверный формат");
    }

    @Test
    void cardCodeFourSymbol() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("code", DataHelper.generateInvalidNumericData(4));
        int expected = 3;
        int actual = buyPage.getCardCodeFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardCodeCyrillic() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("code", DataHelper.generateInvalidCyrillicData(3));
        int expected = 0;
        int actual = buyPage.getCardCodeFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardCodeLatin() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("code", DataHelper.generateInvalidLatinData(3));
        int expected = 0;
        int actual = buyPage.getCardCodeFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardCodeSymbol() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("code", DataHelper.generateInvalidSymbolData(3));
        int expected = 0;
        int actual = buyPage.getCardCodeFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    // дополнительные позитивные тесты
    @Test
    void cardMonthCurrentCurrentMonth() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryMonthAndYear(cardInfo, DataHelper.getCurrentMonth(), DataHelper.gerCurrentYear());
        buyPage.findMessageContent("Успешно", "Операция одобрена Банком.");
    }

    @Test
    void cardMonthCurrentYearNextMonth() {
        String currentMonth = DataHelper.getCurrentMonth();
        String month = "";
        String year = "";
        if (currentMonth.equals("12")) {
            month = "01";
            year = LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy"));
        } else {
            month = LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern("MM"));
            year = DataHelper.gerCurrentYear();
        }
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryMonthAndYear(cardInfo, month, year);
        buyPage.findMessageContent("Успешно", "Операция одобрена Банком.");
    }

    @Test
    void cardOwnerDoubleName() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryOwner(cardInfo, DataHelper.generateInvalidLatinData(5)
                + "-" + DataHelper.generateInvalidLatinData(5)
                + " " + DataHelper.generateInvalidLatinData(8));
        buyPage.findMessageContent("Успешно", "Операция одобрена Банком.");
    }

    @Test
    void cardMonthMin() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryMonth(cardInfo, "01");
        buyPage.findMessageContent("Успешно", "Операция одобрена Банком.");
    }

    @Test
    void cardMonthMax() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryMonth(cardInfo, "12");
        buyPage.findMessageContent("Успешно", "Операция одобрена Банком.");
    }

    @Test
    void cardYearPlusFive() {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryYear(cardInfo, LocalDate.now().plusYears(5).format(DateTimeFormatter.ofPattern("yy")));
        buyPage.findMessageContent("Успешно", "Операция одобрена Банком.");
    }

    // дополнительные негативные тесты
    @Test
    void cardOwnerFirstWhitespace() throws InterruptedException {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryOwner(cardInfo, " " + DataHelper.generateInvalidLatinData(5)
                + " " + DataHelper.generateInvalidLatinData(5));
        buyPage.findMessageError("owner", "Неверный формат");
    }

    @Test
    void cardOwnerFirstDash() throws InterruptedException {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryOwner(cardInfo, "-" + DataHelper.generateInvalidLatinData(5)
                + " " + DataHelper.generateInvalidLatinData(5));
        buyPage.findMessageError("owner", "Неверный формат");
    }

    @Test
    void cardOwnerManyWhitespace() throws InterruptedException {
        open("http://localhost:8080");
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryOwner(cardInfo, DataHelper.generateInvalidLatinData(5)
                + "     " + DataHelper.generateInvalidLatinData(5));
        buyPage.findMessageError("owner", "Неверный формат");
    }

}
