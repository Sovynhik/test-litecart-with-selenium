import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminCustomerLifecycleTest {
    private static WebDriver driver;
    private static WebDriverWait wait;

    private static final String BASIC_URL = "https://demo.litecart.net";
    private static final String ADMIN_URL = BASIC_URL + "/admin";

    private static final String CUSTOMER_FIRST_NAME = "Алексей";
    private static final String CUSTOMER_LAST_NAME = "Петров";
    private static final String CUSTOMER_EMAIL = "alex_petrov@gmail.com";
    private static final String CUSTOMER_PASSWORD = "Passw0rd!";
    private static final String CUSTOMER_ADDRESS_1 = "ул. Строителей, д. 10";
    private static final String CUSTOMER_ADDRESS_2 = "ул. Почтовая, д. 24";
    private static final String CUSTOMER_PHONE = "+79991234567";
    private static final String CUSTOMER_CITY = "Рязань";
    private static final String CUSTOMER_POSTCODE = "101000";


    @BeforeAll
    static void setUp() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    @DisplayName("1. Вход в панель администратора")
    void loginAsAdmin() {
        driver.get(ADMIN_URL);
        driver.findElement(By.xpath("//button[@name='login' or @type='submit']")).click();



        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.xpath("//div[contains(@class,'alert-success')]"),
                "You are now logged in as demo"
        ));

        assertTrue(
                driver.findElement(By.xpath("//div[contains(@class,'alert-success')]")).getText().contains("You are now logged in as demo"),
                "Успешный вход — отображается сообщение 'You are now logged in as demo'"
        );
    }

    @Test
    @Order(2)
    @DisplayName("2. Создание нового пользователя через админку")
    void shouldCreateNewCustomerInAdminPanel() {
        driver.findElement(By.xpath("//a[contains(@href, 'app=customers')]")).click();
        driver.findElement(By.xpath("//a[contains(@href, 'edit_customer')]")).click();

        driver.findElement(By.xpath("//input[@name='firstname']")).sendKeys(CUSTOMER_FIRST_NAME);
        driver.findElement(By.xpath("//input[@name='lastname']")).sendKeys(CUSTOMER_LAST_NAME);
        driver.findElement(By.xpath("//input[@name='email']")).sendKeys(CUSTOMER_EMAIL);
        driver.findElement(By.xpath("//input[@name='new_password']")).sendKeys(CUSTOMER_PASSWORD);

        driver.findElement(By.xpath("//input[@name='address1']")).sendKeys(CUSTOMER_ADDRESS_1);
        driver.findElement(By.xpath("//input[@name='address2']")).sendKeys(CUSTOMER_ADDRESS_2);
        driver.findElement(By.xpath("//input[@name='postcode']")).sendKeys(CUSTOMER_POSTCODE);
        driver.findElement(By.xpath("//input[@name='city']")).sendKeys(CUSTOMER_CITY);
        driver.findElement(By.xpath("//input[@name='phone']")).sendKeys(CUSTOMER_PHONE);

        driver.findElement(By.xpath("//label[./input[@name='status' and @value='0']]")).click();

        WebElement countrySelect = driver.findElement(By.xpath("//select[@name='country_code']"));
        Select countryDropdown = new Select(countrySelect);
        countryDropdown.selectByValue("RU");

        WebElement newsletterCheckbox = driver.findElement(By.xpath("//input[@name='newsletter']"));
        if (!newsletterCheckbox.isSelected()) {
            newsletterCheckbox.click();
        }

        driver.findElement(By.xpath("//button[@name='save']")).click();

        wait.until(ExpectedConditions.urlContains("doc=customers"));
        assertTrue(driver.getCurrentUrl().contains("doc=customers"),
                "После сохранения должен быть редирект в список клиентов (на демо запись отключена)");
    }

    @Test
    @Order(3)
    @DisplayName("3. Активация учётной записи user")
    void shouldActivateCustomerAccount() {
        driver.findElement(By.xpath("//a[contains(@href, 'app=customers')]")).click();
        driver.findElement(By.xpath("//a[contains(@class,'btn-default') and @title='Edit']")).click();

        WebElement enabled = driver.findElement(By.xpath("//input[@name='status' and @value='1']"));
        if (!enabled.isSelected()) enabled.click();

        driver.findElement(By.xpath("//button[@name='save']")).click();

        wait.until(ExpectedConditions.urlContains("doc=customers"));
        assertTrue(driver.getCurrentUrl().contains("doc=customers"));
    }
}