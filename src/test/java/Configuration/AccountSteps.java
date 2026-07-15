package Configuration;

import API.UserAccount;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AccountSteps {

    @Step("Регистрация нового пользователя")
    public String registerUser(String email, String password, String name) {
        UserAccount user = new UserAccount(email, password, name);
        Response response = RestAssured.given()
                .baseUri(Config.BASE_URL)
                .contentType("application/json")
                .body(user)
                .post("/api/auth/register");
        return response.jsonPath().getString("accessToken");
    }
    @Step("Логин пользователя")
    public String loginUser(String email, String password) {
        UserAccount user = new UserAccount(email, "password", "Mr. Bin");
        Response loginResponse = given()
                .baseUri(Config.BASE_URL)
                .header("Content-type", "application/json")
                .body(user)
                .post("/api/auth/login");
        return loginResponse.jsonPath().getString("accessToken");
    }

    @Step("Удаление пользователя")
    public void deleteUser(String accessToken) {
        if (accessToken != null && !accessToken.isBlank()) {
            RestAssured.given()
                    .contentType("application/json")
                    .header("Authorization", accessToken)
                    .delete("/api/auth/user");
        }
    }
}