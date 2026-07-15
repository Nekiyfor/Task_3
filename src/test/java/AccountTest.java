import configuration.AccountSteps;
import configuration.Browser;
import configuration.Config;
import pom.AccountPage;
import pom.BurgerConstructorPage;
import pom.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountTest {
    private WebDriver driver;
    private AccountPage objAccountPage;
    private LoginPage objLoginPage;
    private BurgerConstructorPage objConstructorPage;
    private String accessToken;
    private final AccountSteps accountStep = new AccountSteps();
    private final Browser browserFactory = new Browser();

    @BeforeEach
    public void setUp(){
        String browser = System.getProperty("browser", "chrome");
        driver = browserFactory.getWebDriver(browser);
        objAccountPage = new AccountPage(driver);
        objLoginPage = new LoginPage(driver);
        objConstructorPage = new BurgerConstructorPage(driver);
        accessToken = null;
    }

    @Test
    @DisplayName("Переход в личный кабинет")
    public void transferToPersonalAccountTest(){
        String email = Config.uniqueEmail();
        accessToken = accountStep.registerUser(email, "password", "Mr. Bean");
        driver.get(Config.BASE_URL + "/login");
        objLoginPage.login(email,"password");
        objLoginPage.accountButtonClick();
        assertTrue(objAccountPage.profileDataIsDisplayed(), "Не удалось перейти в личный кабинет");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideHeaderButton")
    @DisplayName("Переход из Личного кабинета в Конструктор бургеров")
    public void transferToBurgerConstructorTest(String testName, Consumer<AccountPage> clickAction) {
        String email = Config.uniqueEmail();
        accessToken = accountStep.registerUser(email, "password", "Mr. Bean");

        driver.get(Config.BASE_URL + "/login");
        objLoginPage.login(email,"password");
        objLoginPage.accountButtonClick();
        clickAction.accept(objAccountPage);
        assertTrue(objConstructorPage.currentTabIsDisplayed(), "Конструктор бургеров не отображается");
    }



    @Test
    @DisplayName("Выход из аккаунта через личный кабинет")
    public void logoutFromAccountTest(){
        String email = Config.uniqueEmail();
        accessToken = accountStep.registerUser(email, "password", "Mr. Bean");

        driver.get(Config.BASE_URL + "/login");
        objLoginPage.login(email,"password");
        objLoginPage.accountButtonClick();
        objAccountPage.logoutClick();
        assertTrue(objLoginPage.loginFormIsDisplayed(), "Не удалось выйти из аккаунта");
    }

    private static Stream<Arguments> provideHeaderButton(){
        return Stream.of(Arguments.of("Переход через кнопку 'Конструктор'", (Consumer<AccountPage>) AccountPage::burgerConstClick),
                Arguments.of("Переход через логотип", (Consumer<AccountPage>) AccountPage::logotypeClick)
        );
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
