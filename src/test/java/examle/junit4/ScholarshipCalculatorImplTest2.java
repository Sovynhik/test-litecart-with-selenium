package examle.junit4;

import ru.rsreu.sovynhik.calculation.NotSuchMarkException;
import ru.rsreu.sovynhik.calculation.ScholarshipCalculatorImpl;

import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

// Листинг 5. Тестирование с использованием фикстур
public class ScholarshipCalculatorImplTest2 {

    private ScholarshipCalculatorImpl scholarshipCalculator;

    @Before // Действия, выполняемые до выполнения каждого теста (Фикстура)
    public void initScholarshipCalculator() {
        scholarshipCalculator = new ScholarshipCalculatorImpl();// создаем объект
    }

    @After // Действия, выполняемые после выполнения каждого теста (Фикстура)
    public void clearScholarshipCalculator() {
        scholarshipCalculator = null;// удаляем ссылку на объект
    }

    @Test
    public void stepUpCoefficientForFiveTest() throws NotSuchMarkException {
        double expected = 1.5;
        double actual = scholarshipCalculator.stepUpCoefficientCalculate(5);
        assertEquals("Coefficient for mark 5 is wrong:", expected, actual, 0.01);
    }

    @Test
    public void stepUpCoefficientForThreeTest() throws NotSuchMarkException {
        double expected = 1;
        double actual = scholarshipCalculator.stepUpCoefficientCalculate(3);
        assertEquals("Coefficient for mark 3 is wrong:", expected, actual, 0.01);
    }

    // Использование параметра expected для тестирования исключения
    @Test(expected = NotSuchMarkException.class)
    public void stepUpCoefficientForInvalidMarkTest() throws NotSuchMarkException {
        // Ожидаем, что этот вызов сгенерирует NotSuchMarkException
        scholarshipCalculator.stepUpCoefficientCalculate(2);
    }

    // Использование аннотации @Ignore для временного отключения теста
    @Test
    @org.junit.Ignore("Тест временно отключен из-за изменения требований")
    public void stepUpCoefficientForFourTest() throws NotSuchMarkException {
        double expected = 1.3;
        double actual = scholarshipCalculator.stepUpCoefficientCalculate(4);
        assertEquals(expected, actual, 0.01);
    }
}