import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class TestSeleniumLitecart {

    private static final String SITE = "https://demo.litecart.net/";
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    static void setupMirrors() {
        // Зеркала Taobao — обход блокировки Microsoft и GitHub
        System.setProperty("wdm.edgeDriverMirrorUrl", "https://npm.taobao.org/mirrors/edgedriver/");
        System.setProperty("wdm.geckoDriverMirrorUrl", "https://npm.taobao.org/mirrors/geckodriver/");
        System.setProperty("wdm.chromeDriverMirrorUrl", "https://npm.taobao.org/mirrors/chromedriver/");
    }

    @BeforeEach
    void setUp() {}

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testFirefox() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        runTest();
    }

    @Test
    void testEdge() {
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        runTest();
    }

    private void runTest() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get(SITE);

        wait.until(d -> "complete".equals(
                ((JavascriptExecutor) d).executeScript("return document.readyState")
        ));

        WebElement purpleDuckLink = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.partialLinkText("Purple Duck"))
        );

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", purpleDuckLink);

        wait.until(ExpectedConditions.titleIs("Purple Duck | Rubber Ducks | My Store"));

        System.out.println("Тест прошёл успешно в: " + driver.getClass().getSimpleName());
    }
}