import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Browser {
    public WebDriver getWebDriver(String browserName) {
        switch (browserName) {
            case "chrome":
                return new ChromeDriver();
            case "yandex":
                ChromeOptions opts = new ChromeOptions();
                opts.setBinary("/Applications/Yandex.app/Contents/MacOS/Yandex"); // Укажите путь к вашему браузеру
                return new ChromeDriver(opts);
            default:
                throw new IllegalArgumentException("Неизвестный браузер: " + browserName);
        }
    }
}
