package Configuration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Browser {
    public WebDriver getWebDriver(String browserName) {
        switch (browserName.toLowerCase()) {
            case "chrome":
                return new ChromeDriver();
            case "yandex":
                String yandexPath = System.getProperty("yandex.browser.path");
                if (yandexPath == null || yandexPath.trim().isEmpty()) {
                    throw new IllegalArgumentException("Не указан путь к Яндекс.Браузеру. Используйте команду: -Dyandex.browser.path=/путь/к/браузеру");
                }
                ChromeOptions opts = new ChromeOptions();
                opts.setBinary(yandexPath);
                return new ChromeDriver(opts);
            default:
                throw new IllegalArgumentException("Неизвестный браузер: " + browserName);
        }
    }
}
