import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.time.Duration.ofSeconds;

public class RegistrationPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By registrationForm = By.xpath("//form");
    private By fieldName = By.xpath("//label[text()='Имя']/following-sibling::input");
    private By fieldEmail = By.xpath("//label[text()='Email']/following-sibling::input");
    private By fieldPassword = By.xpath("//label[text()='Пароль']/following-sibling::input");
    private By inputError = By.xpath("//p[text()='Некорректный пароль']");
    private By registrationButton = By.xpath("//button[text()='Зарегистрироваться']");
    private By loginHeader = By.xpath("//h2[text() = 'Вход']");

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, ofSeconds(3));
    }

    public void ClickRegButton() {
        driver.findElement(registrationButton).click();
    }

    public void fillForm(String name, String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(registrationForm));
        driver.findElement(fieldName).sendKeys(name);
        driver.findElement(fieldEmail).sendKeys(email);
        driver.findElement(fieldPassword).sendKeys(password);
    }

    public boolean isInputErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(inputError));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean LoginFormDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(loginHeader));
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
