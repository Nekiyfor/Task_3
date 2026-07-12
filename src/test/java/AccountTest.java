import io.restassured.response.Response;
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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountTest {
    private WebDriver driver;
    private AccountPage objAccountPage;
    private LoginPage objLoginPage;
    private BurgerConstructorPage objConstructorPage;
    private String accessToken;
    private final Browser browserFactory = new Browser();
    private static final String BASE_URL = "https://qa-stellarburgers.education-services.ru";

    @BeforeEach
    public void SetUp(){
        String browser = System.getProperty("browser", "chrome");
        driver = browserFactory.getWebDriver(browser);
        objAccountPage = new AccountPage(driver);
        objLoginPage = new LoginPage(driver);
        objConstructorPage = new BurgerConstructorPage(driver);
        accessToken = null;
    }

    @Test
    @DisplayName("Переход в личный кабинет")
    public void TransferToPersonalAccount(){
        String email = "peace_data_" + System.currentTimeMillis() + "@yandex.ru";
        UserAccount user = new UserAccount(email, "password", "Mr. Bin");
        Response loginResponse = given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(user)
                .post("/api/auth/register");
        accessToken = loginResponse.jsonPath().getString("accessToken");
        assertTrue(accessToken != null && !accessToken.isBlank(), "Токен авторизации не получен");

        driver.get(BASE_URL + "/login");
        objLoginPage.Login(email,"password");
        objLoginPage.accountButtonClick();
        assertTrue(objAccountPage.ProfileDataIsDisplayed(), "Не удалось перейти в личный кабинет");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideHeaderButton")
    @DisplayName("Переход из Личного кабинета в Конструктор бургеров")
    public void TransferToBurgerConstructor(String testName, Consumer<AccountPage> clickAction) {
        String email = "peace_data_" + System.currentTimeMillis() + "@yandex.ru";
        UserAccount user = new UserAccount(email, "password", "Mr. Bin");
        Response loginResponse = given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(user)
                .post("/api/auth/register");
        accessToken = loginResponse.jsonPath().getString("accessToken");
        assertTrue(accessToken != null && !accessToken.isBlank(), "Токен авторизации не получен");

        driver.get(BASE_URL + "/login");
        objLoginPage.Login(email,"password");
        objLoginPage.accountButtonClick();
        clickAction.accept(objAccountPage);
        assertTrue(objConstructorPage.CurrentTabIsDisplayed(), "Конструктор бургеров не отображается");
    }



    @Test
    @DisplayName("Выход из аккаунта через личный кабинет")
    public void LogoutFromAccount(){
        String email = "peace_data_" + System.currentTimeMillis() + "@yandex.ru";
        UserAccount user = new UserAccount(email, "password", "Mr. Bin");
        Response loginResponse = given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(user)
                .post("/api/auth/register");
        accessToken = loginResponse.jsonPath().getString("accessToken");
        assertTrue(accessToken != null && !accessToken.isBlank(), "Токен авторизации не получен");

        driver.get(BASE_URL + "/login");
        objLoginPage.Login(email,"password");
        objLoginPage.accountButtonClick();
        objAccountPage.LoguotClick();
        assertTrue(objLoginPage.loginFormIsDisplayed(), "Не удалось выйти из аккаунта");
    }

    private static Stream<Arguments> provideHeaderButton(){
        return Stream.of(Arguments.of("Переход через кнопку 'Конструктор'", (Consumer<AccountPage>) AccountPage::BurgerConstClick),
                Arguments.of("Переход через логотип", (Consumer<AccountPage>) AccountPage::LogotypeClick)
        );
    }


    @AfterEach
    void cleanup() {
        try {
            if (accessToken != null && !accessToken.isBlank()) {
                given()
                        .baseUri(BASE_URL)
                        .contentType("application/json")
                        .header("Authorization", accessToken)
                        .delete("/api/auth/user");
            }
        } catch (Exception e) {
            System.err.println("Не удалось удалить пользователя через API: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

}
