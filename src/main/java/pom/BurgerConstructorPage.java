package pom;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.time.Duration.ofSeconds;

public class BurgerConstructorPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By tabBread = By.xpath("//span[text() = 'Булки']");
    private By tabSauces = By.xpath("//span[text() = 'Соусы']");
    private By tabToppings = By.xpath("//span[text() = 'Начинки']");
    private By currentTab = By.xpath("//div[contains(@class, 'tab_tab_type_current')]");
    private By constructorOrderButton = By.xpath("//button[text() = 'Оформить заказ']");

    public BurgerConstructorPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, ofSeconds(3));
    }

    @Step("Выбор и клик вдкладки ингридиентов в конструкторе бургеров")
    public void clickTab(String selectTab) {
        switch (selectTab) {
            case "tabBread":
                driver.findElement(tabBread).click();
                break;
            case "tabSauces":
                driver.findElement(tabSauces).click();
                break;
            case "tabToppings":
                driver.findElement(tabToppings).click();
                break;
        }
    }

    @Step("Получение названия текущей вкладки")
    public String currentTabText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(currentTab)).getText();
    }

    @Step("Проверка того что вкладка неактивна перед кликом")
    public boolean isTabActive(String expectedTabText) {
        String activeTabText = wait.until(ExpectedConditions.visibilityOfElementLocated(currentTab)).getText();
        return activeTabText.contains(expectedTabText);
    }

    @Step("Проверка отображения текущей вкладки")
    public boolean currentTabIsDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(currentTab));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверка отображения конструктора бургеров")
    public boolean constructorOrderButtonDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(constructorOrderButton));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

