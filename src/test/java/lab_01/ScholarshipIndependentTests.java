package lab_01;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.rsreu.sovynhik.calculation.NotSuchMarkException;
import ru.rsreu.sovynhik.calculation.ScholarshipCalculatorImpl;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(TestResultLogger.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ScholarshipIndependentTests {

    private ScholarshipCalculatorImpl calculator;

    @BeforeAll
    static void initGlobalSetup() {
        System.out.println("--- @BeforeAll: Инициализация тестового набора ---");
    }

    @BeforeEach
    void setup() {
        calculator = new ScholarshipCalculatorImpl();
    }

    @AfterEach
    void tearDown() {
        calculator = null;
    }

    @AfterAll
    static void destroyGlobalSetup() {
        System.out.println("--- @AfterAll: Очистка тестового набора ---");
    }
    @Test
    @DisplayName("1. Проверка расчета стипендии с коэфф. 1.3 (Успешный тест)")
    @Order(1)
    void testScholarshipCalculate() {
        double coefficient = 1.3;
        double expected = 130.0;
        double actual = calculator.scholarshipCalculate(coefficient);
        assertEquals(expected, actual, 0.01, "Базовый расчет стипендии неверен.");
    }

    @Test
    @DisplayName("2. Проверка расчета коэффициента для 5 (Таймаут 100мс)")
    @Timeout(value = 100, unit = java.util.concurrent.TimeUnit.MILLISECONDS)
    @Order(2)
    void testCoefficientForMark5() throws NotSuchMarkException {
        double expected = 1.5;
        double actual = calculator.stepUpCoefficientCalculate(5);
        assertEquals(expected, actual, 0.01, "Коэффициент для балла 5 неверен.");
    }

    @Test
    @DisplayName("3. Проверка исключения при недопустимом балле (assertThrows)")
    @Order(3)
    void testCoefficientForInvalidMark_ThrowsException() {
        // Проверяем, что вызов метода генерирует NotSuchMarkException
        assertThrows(NotSuchMarkException.class, () -> {
            calculator.stepUpCoefficientCalculate(2);
        }, "Ожидалось исключение NotSuchMarkException для балла 2.");
    }

    @Test
    @DisplayName("4. Игнорируемый тест (Disabled)")
    @Disabled("Тестирование коэффициента для 4 временно отключено по требованию аналитика.")
    @Order(4)
    void testCoefficientForMark4() throws NotSuchMarkException {
        double expected = 1.3;
        double actual = calculator.stepUpCoefficientCalculate(4);
        assertEquals(expected, actual, 0.01);
    }
}