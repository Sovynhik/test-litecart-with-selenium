//import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class XPathLab3Test {

    private static WebDriver driver;
    private static JavascriptExecutor js;
    private int highlightCounter = 1;
    private final Path screenshotsDir = Path.of("screenshots");

    @BeforeAll
    static void setupDriver() {
        //WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        js = (JavascriptExecutor) driver;
    }

    @BeforeEach
    void setup() {
        driver.get("https://demo.litecart.net");
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    void testFiveElements() throws IOException, InterruptedException {
        String[] xpaths = {
                "//*[@id='site-menu']//img",                                // 1. Логотип
                "//a[.//i[@class='fa fa-globe']]",                          // 2. Regional Settings
                "//a[@id='cart']",                                          // 3. Корзина
                "//form[@name='search_form']",                              // 4. Форма поиска
                "//a[contains(@href, 'edit_account')]"                      // 5. My Account
        };

        String[] names = {
                "Логотип", "Regional Settings", "Корзина", "Форма поиска", "My Account"
        };

        highlightCounter = 1;
        Files.createDirectories(screenshotsDir);

        for (int i = 0; i < xpaths.length; i++) {
            try {
                WebElement element = new WebDriverWait(driver, Duration.ofSeconds(15))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpaths[i])));

                // Прокручиваем к элементу
                js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
                Thread.sleep(600);

                System.out.println((i + 1) + ". " + names[i] + " найден");
                highlight(element);  // Простая рамка + скриншот

            } catch (Exception e) {
                System.out.println((i + 1) + ". " + names[i] + " НЕ НАЙДЕН");
                takeScreenshot("ERROR_image" + (i + 1) + ".png");
            }
        }

        System.out.println("Готово! 5 скриншотов с рамкой в 'screenshots/'");
    }

    // простая красная рамка
    private void highlight(WebElement element) throws InterruptedException, IOException {
        js.executeScript("arguments[0].style.border = '4px solid red';", element);
        Thread.sleep(800);  // Ждём отрисовки
        takeScreenshot("image" + highlightCounter++ + ".png");
        js.executeScript("arguments[0].style.border = '';", element);
        Thread.sleep(400);
    }

    private void takeScreenshot(String filename) throws IOException {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File dest = screenshotsDir.resolve(filename).toFile();
        org.apache.commons.io.FileUtils.copyFile(screenshot, dest);
        System.out.println("Скриншот: " + dest.getAbsolutePath());
    }
}