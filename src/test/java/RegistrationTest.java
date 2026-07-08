import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationTest {
    private WebDriver driver;
    private RegistrationPage objRegistrationPage;
    private String accessToken;
    private static final String BASE_URL = "https://qa-stellarburgers.education-services.ru";


    @BeforeEach
    void SetUp(){
        driver = new ChromeDriver();
        driver.get(BASE_URL + "/register");
        objRegistrationPage = new RegistrationPage(driver);
        accessToken = null;
    }

    @Test
    @DisplayName("Проверка успешной регистрации юзера")
    public void successfulRegistrationTest(){
        String email = "peace_data_" + System.currentTimeMillis() + "@yandex.ru";
        objRegistrationPage.fillForm("Mr. Bean", email, "password");
        objRegistrationPage.ClickRegButton();
        assertTrue(objRegistrationPage.LoginFormDisplayed(), "Регистрация не завершена");

        UserAccount user = new UserAccount(email, "password", "Mr. Bin");
        Response loginResponse = given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(user)
                .post("/api/auth/login");
        accessToken = loginResponse.jsonPath().getString("accessToken");
        assertTrue(accessToken != null && !accessToken.isBlank(), "Токен авторизации не получен");
    }

    @Test
    @DisplayName("Проверка отображения ошибки регистрации при вводе короткого пароля")
    public void errorIsVisibleWhenShortPassTest(){
        String email = "peace_data_" + System.currentTimeMillis() + "@yandex.ru";
        objRegistrationPage.fillForm("Mr. Bean", email, "pass");
        objRegistrationPage.ClickRegButton();
        assertTrue(objRegistrationPage.isInputErrorDisplayed(), "Ошибка регистрации не отображается");
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
