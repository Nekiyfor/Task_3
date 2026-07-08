import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {
    private static WebDriver driver;
    private LoginPage objLoginPage;
    private BurgerConstructorPage objBurgerConstructorPage;
    private String accessToken;
    private static final String BASE_URL = "https://qa-stellarburgers.education-services.ru";


    @BeforeEach
    void SetUp(){
        driver = new ChromeDriver();
        objLoginPage = new LoginPage(driver);
        objBurgerConstructorPage = new BurgerConstructorPage(driver);
        accessToken = null;
    }

    @Test
    @DisplayName("Проверка на успешный вход в аккаунт")
    public void successfulLoginTest(){
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
                Arguments.of("Со страницы конструктора", BASE_URL, (Consumer<LoginPage>) LoginPage::constructorLoginButtonClick),
                Arguments.of("Со страницы ленты заказов", BASE_URL + "/feed", (Consumer<LoginPage>) LoginPage::accountButtonClick),
                Arguments.of("Из формы регистрации", BASE_URL + "/register", (Consumer<LoginPage>) LoginPage::otherPageLoginButtonClick),
                Arguments.of("Из формы восстановления пароля", BASE_URL + "/forgot-password", (Consumer<LoginPage>) LoginPage::otherPageLoginButtonClick)
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

