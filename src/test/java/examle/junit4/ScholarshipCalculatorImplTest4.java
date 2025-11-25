package examle.junit4;

import ru.rsreu.sovynhik.calculation.NotSuchMarkException;
import ru.rsreu.sovynhik.calculation.ScholarshipCalculatorImpl;

import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

// Листинг 7. Параметризованные тесты
@RunWith(Parameterized.class) // Обязательная аннотация для параметризованных тестов
public class ScholarshipCalculatorImplTest4 {

    // 1. Объявление полей для хранения параметров
    private int averageMark;
    private double stepUpCoefficient;

    // 2. Public-конструктор для связывания параметров с полями
    public ScholarshipCalculatorImplTest4(int averageMark, double stepUpCoefficient) {
        this.averageMark = averageMark;
        this.stepUpCoefficient = stepUpCoefficient;
    }

    // 3. Статический метод с аннотацией @Parameters, предоставляющий данные
    @Parameters
    public static Collection<Object[]> stepUpCoefficientTableValues() {
        return Arrays.asList(new Object[][] {
                // {средний балл, ожидаемый коэффициент}
                { 3, 1.0 },
                { 4, 1.3 },
                { 5, 1.5 }
        });
    }

    // 4. Тестовый метод, который будет запущен для каждого набора параметров
    @Test
    public void stepUpCoefficientTest() throws NotSuchMarkException {
        ScholarshipCalculatorImpl scholarshipCalculator =
                new ScholarshipCalculatorImpl();
        double expected = this.stepUpCoefficient;
        double actual =
                scholarshipCalculator.stepUpCoefficientCalculate(this.averageMark);
        assertEquals(expected, actual, 0.01);
    }
}