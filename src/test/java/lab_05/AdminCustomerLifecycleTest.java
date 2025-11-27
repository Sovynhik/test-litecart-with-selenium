package lab_05;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
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
@ExtendWith(TestLifecycleLogger.class)
public class AdminCustomerLifecycleTest {
    private static WebDriver driver;
    private static WebDriverWait wait;

    private static final String BASIC_URL = "https://demo.litecart.net";
    private static final String ADMIN_URL = BASIC_URL + "/admin";

    private static final String CUSTOMER_FIRST_NAME = "Алексей";
    private static final String CUSTOMER_LAST_NAME = "Петров";
    private static final String CUSTOMER_EMAIL = "alex_petrov_test_" + System.currentTimeMillis() + "@gmail.com";
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

    /**
     * Выполняет авторизацию в административной панели litecart по адресу ADMIN_URL,
     * используя данные, заданные по умолчанию в демо-версии.
     */
    private void loginAsAdmin() {
        driver.get(ADMIN_URL);
        driver.findElement(By.xpath("//button[@name='login' or @type='submit']")).click();
    }

    /**
     * Выполняет выход из административной панели, кликая по ссылке 'Sign Out',
     * и перенаправляет браузер на базовый URL магазина (BASIC_URL).
     */
    private void logoutAdmin() {
        driver.findElement(By.xpath("//a[@title='Sign Out']")).click();
        driver.get(BASIC_URL);
    }

    /**
     * Заполняет поля Email и Password на форме авторизации клиента.
     */
    private void fillLoginFields(String email, String password) {
        driver.findElement(By.xpath("//input[@name='email']")).clear();
        driver.findElement(By.xpath("//input[@name='email']")).sendKeys(email);
        driver.findElement(By.xpath("//input[@name='password']")).clear();
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys(password);
    }

    /**
     * Ожидает и проверяет появление всплывающего уведомления.
     * @param partialText Часть текста, которую ожидаем увидеть в уведомлении.
     * @param alertClass Класс уведомления ('alert-success' или 'alert-danger').
     */
    private void verifyAlertMessage(String partialText, String alertClass) {
        By alertLocator = By.xpath(String.format("//div[@id='notices']//div[contains(@class, '%s') and contains(., '%s')]", alertClass, partialText));

        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(
                alertLocator
        ));

        assertTrue(
                alert.getText().contains(partialText),
                String.format("Ожидался текст: '%s', Фактический текст: %s", partialText, alert.getText())
        );
    }

    /**
     * Переход на страницу редактирования клиента по Email в админ-панели.
     */
    private void goToCustomerEditPage(String email) {
        driver.findElement(By.xpath("//a[contains(@href, 'app=customers')]")).click();

        By customerLinkLocator = By.xpath(
                String.format("//a[@class='link' and contains(text(),'%s')]", email)
        );

        wait.until(ExpectedConditions.elementToBeClickable(customerLinkLocator)).click();
    }

    @Test
    @Order(1)
    @DisplayName("1. Создание нового пользователя через админку")
    void shouldCreateNewCustomerInAdminPanel() {
        loginAsAdmin();

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
                "После сохранения должен быть редирект в список клиентов.");

        logoutAdmin();
    }

    @Test
    @Order(2)
    @DisplayName("2. Активация учётной записи user")
    void shouldActivateCustomerAccount() {
        loginAsAdmin();

        goToCustomerEditPage(CUSTOMER_EMAIL);

        By enabledLabelLocator = By.xpath("//label[./input[@name='status' and @value='1']]");
        WebElement enabledInput = driver.findElement(By.xpath("//input[@name='status' and @value='1']"));

        if (!enabledInput.isSelected()) {
            driver.findElement(enabledLabelLocator).click();
        }

        driver.findElement(By.xpath("//button[@name='save']")).click();

        wait.until(ExpectedConditions.urlContains("doc=customers"));
        assertTrue(driver.getCurrentUrl().contains("doc=customers"));

        logoutAdmin();
    }

    @Test
    @Order(3)
    @DisplayName("3. Авторизация под новой учётной записью")
    void shouldLoginAsNewCustomer() {
        driver.get(BASIC_URL);

        driver.findElement(By.xpath("//a[i[contains(@class,'fa-user')]]")).click();

        fillLoginFields(CUSTOMER_EMAIL, CUSTOMER_PASSWORD);

        driver.findElement(By.xpath("//button[@name='login']")).click();

        verifyAlertMessage("logged in", "alert-success");
    }

    @Test
    @Order(4)
    @DisplayName("4. Выход из новой учётной записи")
    void shouldLogoutCustomer() {
        driver.findElement(By.xpath("//a[contains(@class,'nav-link') and i[contains(@class,'fa-user')]]")).click();

        By logoutLinkLocator = By.xpath("//a[contains(@href,'logout') and text()='Logout']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(logoutLinkLocator)).click();

        verifyAlertMessage("logged out", "alert-success");
    }

    @Test
    @Order(5)
    @DisplayName("5. Отключение учётной записи user")
    void shouldDisableCustomerAccount() {
        loginAsAdmin();

        goToCustomerEditPage(CUSTOMER_EMAIL);

        By disabledLabelLocator = By.xpath("//label[./input[@name='status' and @value='0']]");
        WebElement disabledInput = driver.findElement(By.xpath("//input[@name='status' and @value='0']"));

        if (!disabledInput.isSelected()) {
            driver.findElement(disabledLabelLocator).click();
        }

        driver.findElement(By.xpath("//button[@name='save']")).click();

        wait.until(ExpectedConditions.urlContains("doc=customers"));
        assertTrue(driver.getCurrentUrl().contains("doc=customers"), "Должен быть редирект в список клиентов.");

        logoutAdmin();
    }

    @Test
    @Order(6)
    @DisplayName("6. Проверка невозможности авторизации отключенного user")
    void shouldFailToLoginWithDisabledAccount() {
        driver.get(BASIC_URL);

        driver.findElement(By.xpath("//a[i[contains(@class,'fa-user')]]")).click();

        fillLoginFields(CUSTOMER_EMAIL, CUSTOMER_PASSWORD);

        driver.findElement(By.xpath("//button[@name='login']")).click();

        verifyAlertMessage("disabled or not activated", "alert-danger");
    }

    @Test
    @Order(7)
    @DisplayName("7. Удаление учётной записи user")
    void shouldDeleteCustomerAccount() {
        loginAsAdmin();

        goToCustomerEditPage(CUSTOMER_EMAIL);

        driver.findElement(By.xpath("//button[@name='delete']")).click();

        wait.until(ExpectedConditions.alertIsPresent()).accept();

        wait.until(ExpectedConditions.urlContains("doc=customers"));
        assertTrue(driver.getCurrentUrl().contains("doc=customers"), "После удаления должен быть редирект в список клиентов.");

        By customerLinkLocator = By.xpath(String.format("//a[@class='link' and contains(text(), '%s')]", CUSTOMER_EMAIL));

        boolean isCustomerPresent = driver.findElements(customerLinkLocator).isEmpty();

        assertTrue(isCustomerPresent, "Удаленный пользователь не должен присутствовать в списке.");

        logoutAdmin();
    }
}