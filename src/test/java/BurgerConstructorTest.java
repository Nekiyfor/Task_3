import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;

import java.util.stream.Stream;

public class BurgerConstructorTest {
    private WebDriver driver;
    private BurgerConstructorPage objBurgerConstructorPage;
    private final Browser browserFactory = new Browser();
    private static final String BASE_URL = "https://qa-stellarburgers.education-services.ru";

    @BeforeEach
    public void SetUp(){
        String browser = System.getProperty("browser", "chrome");
        driver = browserFactory.getWebDriver(browser);
        objBurgerConstructorPage = new BurgerConstructorPage(driver);
        driver.get(BASE_URL);
    }

    @ParameterizedTest (name = "{0}")
    @MethodSource("provideBurgerConstructorData")
    @DisplayName("Проверка переключения между разными вкладками в конструкторе бургеров")
    public void BurgerConstTabTest(String testName, String selectTab, String expectedTab) {
        if (!objBurgerConstructorPage.isTabActive(expectedTab)) {
            objBurgerConstructorPage.clickTab(selectTab);
        }
        String actualTab = objBurgerConstructorPage.CurrentTabText();
        Assertions.assertTrue(actualTab.contains(expectedTab), "Не удалось переключиться на вкладку: " + selectTab);
    }

    private static Stream<Arguments> provideBurgerConstructorData(){
        return Stream.of(
                Arguments.of("Переключение на вкладку 'Булки'", "tabBread", "Булки"),
                Arguments.of("Переключение на вкладку 'Соусы'", "tabSauces", "Соусы"),
                Arguments.of("Переключение на вкладку 'Начинки'", "tabToppings", "Начинки")
        );
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

}
