package lab_03;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class LitecartXPathHighlighterTest {

    // =======================================================================
    // КОНСТАНТЫ НАСТРОЙКИ (Configuration Constants)
    // =======================================================================
    private static final String SITE_URL = "https://demo.litecart.net";
    private static final int TIMEOUT_SECONDS = 15;

    // Задержки для визуального эффекта
    private static final int SCROLL_DELAY_MS = 600;
    private static final int HIGHLIGHT_DELAY_MS = 800;
    private static final int UNHIGHLIGHT_DELAY_MS = 400;

    // JavaScript константы
    private static final String BORDER_STYLE = "4px solid red";
    private static final String SCRIPT_SCROLL = "arguments[0].scrollIntoView({block: 'center'});";
    private static final String SCRIPT_HIGHLIGHT = "arguments[0].style.border = '" + BORDER_STYLE + "';";
    private static final String SCRIPT_UNHIGHLIGHT = "arguments[0].style.border = '';";

    // Константы браузеров и логирования
    private static final String BROWSER_FIREFOX = "Firefox";
    private static final String BROWSER_CHROME = "Chrome";
    private static final String LOG_FOUND = " найден";
    private static final String LOG_NOT_FOUND = " НЕ НАЙДЕН";
    private static final String LOG_COMPLETE_PREFIX = "Готово! Тест успешно завершен в браузере ";
    private static final String SCREENSHOT_LOG_PREFIX = "Скриншот: ";
    private static final String FILENAME_ERROR_PREFIX = "ERROR_image";
    private static final String FILENAME_PREFIX = "image";
    private static final String SCREENSHOTS_DIR_PREFIX = "test"; // Используется в takeScreenshot

    // =======================================================================
    // КОНСТАНТЫ ЭЛЕМЕНТОВ (Element Locators and Names)
    // =======================================================================

    // XPath константы
    private static final String XPATH_LOGO = "//*[@id='site-menu']//img";
    private static final String XPATH_REGIONAL_SETTINGS = "//a[.//i[@class='fa fa-globe']]";
    private static final String XPATH_CART = "//a[@id='cart']";
    private static final String XPATH_SEARCH_FORM = "//form[@name='search_form']";
    private static final String XPATH_MY_ACCOUNT = "//a[contains(@href, 'edit_account')]";

    // Имена элементов для логирования
    private static final String NAME_LOGO = "Логотип";
    private static final String NAME_REGIONAL_SETTINGS = "Regional Settings";
    private static final String NAME_CART = "Корзина";
    private static final String NAME_SEARCH_FORM = "Форма поиска";
    private static final String NAME_MY_ACCOUNT = "My Account";

    // =======================================================================
    // ПОЛЯ И МЕТОДЫ
    // =======================================================================
    private WebDriver driver;
    private JavascriptExecutor js;
    private int highlightCounter = 1;
    private final Path screenshotsDir = Path.of("screenshots");

    @BeforeEach
    void setup(TestInfo testInfo) throws IOException {
        highlightCounter = 1;
        // Создаем подпапку, используя имя теста
        Files.createDirectories(screenshotsDir.resolve(testInfo.getDisplayName().replace("()", "")));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testFirefox() throws IOException, InterruptedException {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        js = (JavascriptExecutor) driver;

        driver.get(SITE_URL);
        runTestLogic(BROWSER_FIREFOX);
    }

    @Test
    void testChrome() throws IOException, InterruptedException {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        js = (JavascriptExecutor) driver;

        driver.get(SITE_URL);
        runTestLogic(BROWSER_CHROME);
    }

    private void runTestLogic(String browserName) throws IOException, InterruptedException {
        String[] xpaths = {
                XPATH_LOGO,
                XPATH_REGIONAL_SETTINGS,
                XPATH_CART,
                XPATH_SEARCH_FORM,
                XPATH_MY_ACCOUNT
        };

        String[] names = {
                NAME_LOGO,
                NAME_REGIONAL_SETTINGS,
                NAME_CART,
                NAME_SEARCH_FORM,
                NAME_MY_ACCOUNT
        };

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));

        for (int i = 0; i < xpaths.length; i++) {
            try {
                WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpaths[i])));

                // Прокручиваем к элементу
                js.executeScript(SCRIPT_SCROLL, element);
                Thread.sleep(SCROLL_DELAY_MS);

                System.out.println(browserName + ": " + (i + 1) + ". " + names[i] + LOG_FOUND);
                highlight(element, browserName);

            } catch (Exception e) {
                System.out.println(browserName + ": " + (i + 1) + ". " + names[i] + LOG_NOT_FOUND);
                takeScreenshot(FILENAME_ERROR_PREFIX + (i + 1) + ".png", browserName);
            }
        }

        System.out.println(LOG_COMPLETE_PREFIX + browserName);
    }

    private void highlight(WebElement element, String browserName) throws InterruptedException, IOException {
        js.executeScript(SCRIPT_HIGHLIGHT, element);
        Thread.sleep(HIGHLIGHT_DELAY_MS);
        takeScreenshot(FILENAME_PREFIX + highlightCounter++ + ".png", browserName);
        js.executeScript(SCRIPT_UNHIGHLIGHT, element);
        Thread.sleep(UNHIGHLIGHT_DELAY_MS);
    }

    private void takeScreenshot(String filename, String browserName) throws IOException {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Папка для скриншотов теперь включает имя теста
        Path destinationDir = screenshotsDir.resolve(SCREENSHOTS_DIR_PREFIX + browserName);
        Files.createDirectories(destinationDir);

        File dest = destinationDir.resolve(filename).toFile();
        FileUtils.copyFile(screenshot, dest);
        System.out.println(SCREENSHOT_LOG_PREFIX + dest.getAbsolutePath());
    }
}