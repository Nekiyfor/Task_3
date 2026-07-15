package pom;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.time.Duration.ofSeconds;

public class AccountPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By profileData = By.xpath("//div[contains(@class, 'Profile_profile')]");
    private By logout = By.xpath("//button[text() = 'Выход']");
    private By burgerConst = By.xpath("//p[text() = 'Конструктор']");
    private By logotype = By.xpath("//div[contains(@class, 'logo')]");

    public AccountPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, ofSeconds(3));
    }

    @Step("Клик по кнопке Выход")
    public void logoutClick() {
        wait.until(ExpectedConditions.elementToBeClickable(logout));
        driver.findElement(logout).click();
    }

    @Step("Клик по кнопке КОнструктора бургеров")
    public void burgerConstClick() {
        wait.until(ExpectedConditions.elementToBeClickable(burgerConst));
        driver.findElement(burgerConst).click();
    }

    @Step("Клик по логотипу")
    public void logotypeClick() {
        wait.until(ExpectedConditions.elementToBeClickable(logotype));
        driver.findElement(logotype).click();
    }

    @Step("Проверка отображения информации о пользователе в личном кабинете")
    public boolean profileDataIsDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(profileData));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
