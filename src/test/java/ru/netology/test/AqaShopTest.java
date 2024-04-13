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

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
    }


    @Test
    void successPathApprovedCard() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formFillingCard(cardInfo);
        buyPage.findMessageContent("Успешно", "Операция одобрена Банком.");
        Assertions.assertEquals("APPROVED", SQLHelper.getStatus());
    }

    @Test
    void successPathDeclinedCard() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getDeclinedCardAllForm();
        buyPage.formFillingCard(cardInfo);
        buyPage.findMessageContent("Ошибка", "Ошибка! Банк отказал в проведении операции.");
        Assertions.assertEquals("DECLINED", SQLHelper.getStatus());
    }

    @Test
    void cardNumberEmpty() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryNumber(cardInfo, "");
        buyPage.findMessageError("number", "Поле обязательно для заполнения");

    }

    @Test
    void cardNumberOneSymbol() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryNumber(cardInfo, DataHelper.generateInvalidNumericData(1));
        buyPage.findMessageError("number", "Неверный формат");
    }

    @Test
    void cardNumberFifteenSymbol() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryNumber(cardInfo, DataHelper.generateInvalidNumericData(15));
        buyPage.findMessageError("number", "Неверный формат");
    }

    @Test
    void cardNumberSeventeenSymbol() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("number", DataHelper.generateInvalidNumericData(17));
        int expected = 19; //17 символов обрезается до 16 и добавляется 3 промежуточных пробела
        int actual = buyPage.getCardNumberFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardNumberCyrillic() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("number", DataHelper.generateInvalidCyrillicData(16));
        int expected = 0;
        int actual = buyPage.getCardNumberFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardNumberLatin() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("number", DataHelper.generateInvalidLatinData(16));
        int expected = 0;
        int actual = buyPage.getCardNumberFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardNumberSymbol() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("number", DataHelper.generateInvalidSymbolData(16));
        int expected = 0;
        int actual = buyPage.getCardNumberFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardMonthEmpty() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryMonth(cardInfo, "");
        buyPage.findMessageError("month", "Поле обязательно для заполнения");

    }

    @Test
    void cardMonthOneSymbol() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryMonth(cardInfo, DataHelper.generateInvalidNumericData(1));
        buyPage.findMessageError("month", "Неверный формат");
    }

    @Test
    void cardMonthZero() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryMonth(cardInfo, "00");
        buyPage.findMessageError("month", "Неверный формат");


    }

    @Test
    void cardCodeCyrillicNorm() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getInvalidCardWithRussianOwner();
        buyPage.formFillingCard(cardInfo);
        buyPage.findMessageError("owner", "Неверный формат");
    }

    @Test
    void cardMonthThirteen() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryMonth(cardInfo, "13");
        buyPage.findMessageError("month", "Неверно указан срок действия карты");
    }

    @Test
    void cardMonthMoreFourteen() {
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
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryMonthAndYear(cardInfo, month, year);
        buyPage.findMessageError("month", "Неверно указан срок действия карты");
    }

    @Test
    void cardMonthThreeSymbol() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("month", DataHelper.generateInvalidNumericData(3));
        int expected = 2;
        int actual = buyPage.getCardMonthFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardMonthCyrillic() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("month", DataHelper.generateInvalidCyrillicData(2));
        int expected = 0;
        int actual = buyPage.getCardMonthFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardMonthLatin() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("month", DataHelper.generateInvalidLatinData(2));
        int expected = 0;
        int actual = buyPage.getCardMonthFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardMonthSymbol() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("month", DataHelper.generateInvalidSymbolData(2));
        int expected = 0;
        int actual = buyPage.getCardMonthFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardYearEmpty() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryYear(cardInfo, "");
        buyPage.findMessageError("year", "Поле обязательно для заполнения");
    }

    @Test
    void cardYearOneSymbol() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryYear(cardInfo, DataHelper.generateInvalidNumericData(1));
        buyPage.findMessageError("year", "Неверный формат");
    }

    @Test
    void cardYearZero() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryYear(cardInfo, "00");
        buyPage.findMessageError("year", "Истёк срок действия карты");
    }

    @Test
    void cardYearThreeSymbol() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("year", DataHelper.generateInvalidNumericData(3));
        int expected = 2;
        int actual = buyPage.getCardYearFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardYearCyrillic() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("year", DataHelper.generateInvalidCyrillicData(2));
        int expected = 0;
        int actual = buyPage.getCardYearFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardYearLatin() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("year", DataHelper.generateInvalidLatinData(2));
        int expected = 0;
        int actual = buyPage.getCardYearFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardYearSymbol() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("year", DataHelper.generateInvalidSymbolData(2));
        int expected = 0;
        int actual = buyPage.getCardYearFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardYearLastYear() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryYear(cardInfo, LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy")));
        buyPage.findMessageError("year", "Истёк срок действия карты");
    }

    @Test
    void cardYearPlusSix() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryYear(cardInfo, LocalDate.now().plusYears(6).format(DateTimeFormatter.ofPattern("yy")));
        buyPage.findMessageError("year", "Неверно указан срок действия карты");
    }

    @Test
    void cardYearMoreYearPlusSeven() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryYear(cardInfo, DataHelper.generateInvalidYear());
        buyPage.findMessageError("year", "Неверно указан срок действия карты");
    }

    @Test
    void cardOwnerEmpty() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryOwner(cardInfo, "");
        buyPage.findMessageError("owner", "Поле обязательно для заполнения");
    }



    @Test
    void cardOwnerNumeric() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("owner", DataHelper.generateInvalidOwnerNumeric(5, 8));
        int expected = 0;
        int actual = buyPage.getCardOwnerFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardOwnerSymbol() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("owner", DataHelper.generateInvalidOwnerSymbol(5, 8));
        int expected = 0;
        int actual = buyPage.getCardOwnerFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardOwnerTwentyTwoSymbol() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("owner", DataHelper.generateInvalidLatinData(10) + " " + DataHelper.generateInvalidLatinData(11));
        int expected = 21;
        int actual = buyPage.getCardOwnerFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardOwnerNotWhitespace() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryOwner(cardInfo, DataHelper.generateInvalidLatinData(14));
        buyPage.findMessageError("owner", "Неверный формат");
    }

    @Test
    void cardCodeEmpty() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryCode(cardInfo, "");
        buyPage.findMessageError("code", "Поле обязательно для заполнения");
    }

    @Test
    void cardCodeOneSymbol() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryCode(cardInfo, DataHelper.generateInvalidNumericData(1));
        buyPage.findMessageError("code", "Неверный формат");
    }

    @Test
    void cardCodeTwoSymbol() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryCode(cardInfo, DataHelper.generateInvalidNumericData(2));
        buyPage.findMessageError("code", "Неверный формат");
    }

    @Test
    void cardCodeFourSymbol() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("code", DataHelper.generateInvalidNumericData(4));
        int expected = 3;
        int actual = buyPage.getCardCodeFromForm().length();
        Assertions.assertEquals(expected, actual);
    }



    @Test
    void cardCodeLatin() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        buyPage.fillingOneField("code", DataHelper.generateInvalidLatinData(3));
        int expected = 0;
        int actual = buyPage.getCardCodeFromForm().length();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void cardCodeSymbol() {
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
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryMonthAndYear(cardInfo, month, year);
        buyPage.findMessageContent("Успешно", "Операция одобрена Банком.");
    }

    @Test
    void cardOwnerDoubleName() {
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
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryMonth(cardInfo, "01");
        buyPage.findMessageContent("Успешно", "Операция одобрена Банком.");
    }

    @Test
    void cardMonthMax() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryMonth(cardInfo, "12");
        buyPage.findMessageContent("Успешно", "Операция одобрена Банком.");
    }

    @Test
    void cardYearPlusFive() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryYear(cardInfo, LocalDate.now().plusYears(5).format(DateTimeFormatter.ofPattern("yy")));
        buyPage.findMessageContent("Успешно", "Операция одобрена Банком.");
    }

    // дополнительные негативные тесты
    @Test
    void cardOwnerFirstWhitespace() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryOwner(cardInfo, " " + DataHelper.generateInvalidLatinData(5)
                + " " + DataHelper.generateInvalidLatinData(5));
        buyPage.findMessageError("owner", "Неверный формат");
    }

    @Test
    void cardOwnerFirstDash() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryOwner(cardInfo, "-" + DataHelper.generateInvalidLatinData(5)
                + " " + DataHelper.generateInvalidLatinData(5));
        buyPage.findMessageError("owner", "Неверный формат");
    }

    @Test
    void cardOwnerManyWhitespace() {
        var mainPage = new MainPage();
        var buyPage = mainPage.simpleBuy();
        var cardInfo = DataHelper.getApprovedCardAllForm();
        buyPage.formCardArbitraryOwner(cardInfo, DataHelper.generateInvalidLatinData(5)
                + "     " + DataHelper.generateInvalidLatinData(5));
        buyPage.findMessageError("owner", "Неверный формат");
    }

}
