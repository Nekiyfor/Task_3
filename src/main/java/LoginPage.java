import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.time.Duration.ofSeconds;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By loginForm = By.xpath("//form");
    private By fieldEmail = By.xpath("//label[text()='Email']/following-sibling::input");
    private By fieldPassword = By.xpath("//label[text()='Пароль']/following-sibling::input");
    private By loginButton = By.xpath("//button[text() = 'Войти']");
    private By accountButton = By.xpath("//p[text() = 'Личный Кабинет']");
    private By otherPageLoginButton = By.xpath("//a[text() = 'Войти']");
    private By constructorPageLoginButton = By.xpath("//button[text() = 'Войти в аккаунт']");


    public LoginPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, ofSeconds(3));
    }

    public void Login(String email, String password){
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginForm));
        driver.findElement(fieldEmail).sendKeys(email);
        driver.findElement(fieldPassword).sendKeys(password);
        driver.findElement(loginButton).click();
    }

    public void accountButtonClick() {
        wait.until(ExpectedConditions.elementToBeClickable(accountButton));
        driver.findElement(accountButton).click();
    }

    public void constructorLoginButtonClick(){
        wait.until(ExpectedConditions.elementToBeClickable(constructorPageLoginButton));
        driver.findElement(constructorPageLoginButton).click();
    }

    public void otherPageLoginButtonClick(){
        wait.until(ExpectedConditions.elementToBeClickable(otherPageLoginButton));
        driver.findElement(otherPageLoginButton).click();
    }

    public boolean loginFormIsDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(loginForm));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
