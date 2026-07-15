
import configuration.AccountSteps;
import configuration.Browser;
import configuration.Config;
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

public class LoginTest {
    private WebDriver driver;
    private LoginPage objLoginPage;
    private BurgerConstructorPage objBurgerConstructorPage;
    private String accessToken;
    private final AccountSteps accountStep = new AccountSteps();
    private final Browser browserFactory = new Browser();


    @BeforeEach
    void setUp() {
        String browser = System.getProperty("browser", "chrome");
        driver = browserFactory.getWebDriver(browser);
        objLoginPage = new LoginPage(driver);
        objBurgerConstructorPage = new BurgerConstructorPage(driver);
        accessToken = null;
    }

    @Test
    @DisplayName("Проверка на успешный вход в аккаунт")
    public void successfulLoginTest() {
        String email = Config.uniqueEmail();
        accessToken = accountStep.registerUser(email, "password", "Mr. Bean");
        driver.get(Config.BASE_URL + "/login");
        objLoginPage.login(email, "password");
        assertTrue(objBurgerConstructorPage.constructorOrderButtonDisplayed(), "Не удалось авторизоваться в системе");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideAuthFormTransitionData")
    @DisplayName("Переход в форму авторизации с разных страниц")
    public void transitionToAuthFormTest(String testName, String Url, Consumer<LoginPage> clickAction) {
        driver.get(Url);
        clickAction.accept(objLoginPage);
        assertTrue(objLoginPage.loginFormIsDisplayed(), "Форма авторизации не отображается");
    }

    private static Stream<Arguments> provideAuthFormTransitionData() {
        return Stream.of(
                Arguments.of("Со страницы конструктора", Config.BASE_URL, (Consumer<LoginPage>) LoginPage::constructorLoginButtonClick),
                Arguments.of("Со страницы ленты заказов", Config.BASE_URL + "/feed", (Consumer<LoginPage>) LoginPage::accountButtonClick),
                Arguments.of("Из формы регистрации", Config.BASE_URL + "/register", (Consumer<LoginPage>) LoginPage::otherPageLoginButtonClick),
                Arguments.of("Из формы восстановления пароля", Config.BASE_URL + "/forgot-password", (Consumer<LoginPage>) LoginPage::otherPageLoginButtonClick)
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

