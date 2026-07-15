import Configuration.AccountSteps;
import Configuration.Browser;
import Configuration.Config;
import POM.RegistrationPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationTest {
    private WebDriver driver;
    private RegistrationPage objRegistrationPage;
    private String accessToken;
    private final AccountSteps accountStep = new AccountSteps();
    private final Browser browserFactory = new Browser();



    @BeforeEach
    void setUp(){
        String browser = System.getProperty("browser", "chrome");
        driver = browserFactory.getWebDriver(browser);
        driver.get(Config.BASE_URL + "/register");
        objRegistrationPage = new RegistrationPage(driver);
        accessToken = null;
    }

    @Test
    @DisplayName("Проверка успешной регистрации юзера")
    public void successfulRegistrationTest(){
        String email = Config.uniqueEmail();
        objRegistrationPage.fillForm("Mr. Bean", email, "password");
        objRegistrationPage.clickRegButton();
        assertTrue(objRegistrationPage.loginFormDisplayed(), "Регистрация не завершена");
        accessToken = accountStep.loginUser(email, "password");
    }

    @Test
    @DisplayName("Проверка отображения ошибки регистрации при вводе короткого пароля")
    public void errorIsVisibleWhenShortPassTest(){
        objRegistrationPage.fillForm("Mr. Bean", Config.uniqueEmail(), "pass");
        objRegistrationPage.clickRegButton();
        assertTrue(objRegistrationPage.isInputErrorDisplayed(), "Ошибка регистрации не отображается");
    }

    @AfterEach
    void cleanup() {
        try {
            accountStep.deleteUser(accessToken);
        } catch (Exception e) {
            System.err.println("Не удалось удалить пользователя через API: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
