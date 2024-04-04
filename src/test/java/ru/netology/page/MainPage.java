package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {
    private final SelenideElement buyButton = $$(By.className("button__text")).find(Condition.exactText("Купить"));

    //  private final SelenideElement buyCreditButton = $$(By.className("button__text")).find(Condition.exactText("Купить в кредит"));

    public BuyPage simpleBuy() {
        buyButton.click();
        return new BuyPage();
    }


}