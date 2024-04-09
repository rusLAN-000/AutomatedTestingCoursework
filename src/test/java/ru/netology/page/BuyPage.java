
package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class BuyPage {
    private final SelenideElement heading = $$(By.className("heading")).find(Condition.exactText("Оплата по карте"));
    private final SelenideElement cardNumber = $$(By.className("input__top")).find(Condition.text("Номер карты")).parent().find(By.className("input__control"));
    private final SelenideElement cardMonth = $$(By.className("input__top")).find(Condition.text("Месяц")).parent().find(By.className("input__control"));
    private final SelenideElement cardYear = $$(By.className("input__top")).find(Condition.text("Год")).parent().find(By.className("input__control"));
    private final SelenideElement cardOwner = $$(By.className("input__top")).find(Condition.text("Владелец")).parent().find(By.className("input__control"));
    private final SelenideElement cardCode = $$(By.className("input__top")).find(Condition.text("CVC/CVV")).parent().find(By.className("input__control"));
    private final SelenideElement continueButton = $$(By.className("button__text")).find(Condition.exactText("Продолжить"));
    private final SelenideElement MessageTitle = $(By.className("notification__title"));
    private final SelenideElement MessageContent = $(By.className("notification__content"));
    private final SelenideElement errorForNumber = $$(By.className("input__top")).find(Condition.text("Номер карты")).parent().find(By.className("input__sub"));
    private final SelenideElement errorForMonth = $$(By.className("input__top")).find(Condition.text("Месяц")).parent().find(By.className("input__sub"));
    private final SelenideElement errorForYear = $$(By.className("input__top")).find(Condition.text("Год")).parent().find(By.className("input__sub"));
    private final SelenideElement errorForOwner = $$(By.className("input__top")).find(Condition.text("Владелец")).parent().find(By.className("input__sub"));
    private final SelenideElement errorForCode = $$(By.className("input__top")).find(Condition.text("CVC/CVV")).parent().find(By.className("input__sub"));

    public BuyPage() {
        heading.shouldBe(Condition.visible);
    }

    public void formFillingCard(DataHelper.CardInfo cardInfo) {

        cardNumber.setValue(cardInfo.getNumber());
        cardMonth.setValue(cardInfo.getMonth());
        cardYear.setValue(cardInfo.getYear());
        cardOwner.setValue(cardInfo.getOwner());
        cardCode.setValue(cardInfo.getCode());
        continueButton.click();

    }

    public void findMessageContent(String title, String expectedText) {
        MessageTitle.shouldHave(Condition.exactText(title), Duration.ofSeconds(10)).shouldBe(Condition.visible);
        MessageContent.shouldHave(Condition.exactText(expectedText), Duration.ofSeconds(10)).shouldBe(Condition.visible);
    }

    public void formCardArbitraryNumber(DataHelper.CardInfo cardInfo, String arbitraryNumber) {

        cardNumber.setValue(arbitraryNumber);
        cardMonth.setValue(cardInfo.getMonth());
        cardYear.setValue(cardInfo.getYear());
        cardOwner.setValue(cardInfo.getOwner());
        cardCode.setValue(cardInfo.getCode());
        continueButton.click();
    }

    public void formCardArbitraryMonth(DataHelper.CardInfo cardInfo, String arbitraryMonth) {

        cardNumber.setValue(cardInfo.getNumber());
        cardMonth.setValue(arbitraryMonth);
        cardYear.setValue(cardInfo.getYear());
        cardOwner.setValue(cardInfo.getOwner());
        cardCode.setValue(cardInfo.getCode());
        continueButton.click();
    }

    public void formCardArbitraryYear(DataHelper.CardInfo cardInfo, String arbitraryYear) {

        cardNumber.setValue(cardInfo.getNumber());
        cardMonth.setValue(cardInfo.getMonth());
        cardYear.setValue(arbitraryYear);
        cardOwner.setValue(cardInfo.getOwner());
        cardCode.setValue(cardInfo.getCode());
        continueButton.click();
    }

    public void formCardArbitraryOwner(DataHelper.CardInfo cardInfo, String arbitraryOwner) {

        cardNumber.setValue(cardInfo.getNumber());
        cardMonth.setValue(cardInfo.getMonth());
        cardYear.setValue(cardInfo.getYear());
        cardOwner.setValue(arbitraryOwner);
        cardCode.setValue(cardInfo.getCode());
        continueButton.click();
    }

    public void formCardArbitraryCode(DataHelper.CardInfo cardInfo, String arbitraryCode) {

        cardNumber.setValue(cardInfo.getNumber());
        cardMonth.setValue(cardInfo.getMonth());
        cardYear.setValue(cardInfo.getYear());
        cardOwner.setValue(cardInfo.getOwner());
        cardCode.setValue(arbitraryCode);
        continueButton.click();
    }

    public void formCardArbitraryMonthAndYear(DataHelper.CardInfo cardInfo, String arbitraryMonth, String arbitraryYear) {

        cardNumber.setValue(cardInfo.getNumber());
        cardMonth.setValue(arbitraryMonth);
        cardYear.setValue(arbitraryYear);
        cardOwner.setValue(cardInfo.getOwner());
        cardCode.setValue(cardInfo.getCode());
        continueButton.click();
    }

    public void fillingOneField(String name, String value) {

        if (name.equals("number")) {
            cardNumber.setValue(value);
        }
        if (name.equals("month")) {
            cardMonth.setValue(value);
        }
        if (name.equals("year")) {
            cardYear.setValue(value);
        }
        if (name.equals("owner")) {
            cardOwner.setValue(value);
        }
        if (name.equals("code")) {
            cardCode.setValue(value);
        }

    }

    public void findMessageError(String name, String error) {
        if (name.equals("number")) {
            errorForNumber.shouldHave(Condition.exactText(error));
        }
        if (name.equals("month")) {
            errorForMonth.shouldHave(Condition.exactText(error));
        }
        if (name.equals("year")) {
            errorForYear.shouldHave(Condition.exactText(error));
        }
        if (name.equals("owner")) {
            errorForOwner.shouldHave(Condition.exactText(error));
        }
        if (name.equals("code")) {
            errorForCode.shouldHave(Condition.exactText(error));
        }
    }

    public String getCardNumberFromForm() {
        return cardNumber.getValue();
    }

    public String getCardMonthFromForm() {
        return cardMonth.getValue();
    }

    public String getCardYearFromForm() {
        return cardYear.getValue();
    }

    public String getCardOwnerFromForm() {
        return cardOwner.getValue();
    }

    public String getCardCodeFromForm() {
        return cardCode.getValue();
    }

}
