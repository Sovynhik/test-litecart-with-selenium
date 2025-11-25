package lab_02;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LitecartBrowserCompatibilityTest {

    private static final String SITE = "https://demo.litecart.net/";
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {  }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testFirefox() {
        // скачает с официального GitHub
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        runTest();
    }

    @Test
    void testChrome() {
        // скачает с официального Google
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        runTest();
    }

    private void runTest() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get(SITE);

        // Ждём полной загрузки страницы
        wait.until(d -> "complete".equals(
                ((JavascriptExecutor) d).executeScript("return document.readyState")
        ));

        // Находим и кликаем по ссылке Purple Duck
        WebElement purpleDuckLink = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.partialLinkText("Purple Duck"))
        );

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", purpleDuckLink);

        // Проверяем, что перешли на нужную страницу
        wait.until(ExpectedConditions.titleIs("Purple Duck | Rubber Ducks | My Store"));

        System.out.println("Тест прошёл успешно в браузере: " + driver.getClass().getSimpleName());
    }
}