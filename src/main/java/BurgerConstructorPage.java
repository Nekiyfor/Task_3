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

    public void clickTab(String selectTab){
        switch (selectTab){
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

    public String CurrentTabText() {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(currentTab)).getText();
    }
    //Метод нужен для проверки того что владка неактивна, прежде чем на неё кликнуть
    public boolean isTabActive(String expectedTabText) {
        String activeTabText = wait.until(ExpectedConditions.visibilityOfElementLocated(currentTab)).getText();
        return activeTabText.contains(expectedTabText);
    }

    public boolean CurrentTabIsDisplayed(){
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(currentTab));
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean constructorOrderButtonDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(constructorOrderButton));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

