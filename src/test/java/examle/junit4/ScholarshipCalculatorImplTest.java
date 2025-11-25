package examle.junit4;

import ru.rsreu.sovynhik.calculation.IscholarshipCalculator;
import ru.rsreu.sovynhik.calculation.ScholarshipCalculatorImpl;

import org.junit.Assert;
import org.junit.Test;

// Листинг 4. Простейший тест
public class ScholarshipCalculatorImplTest {
    @Test
    public void testScholarshipCalculate() {
        IscholarshipCalculator scholarshipCalculator =
                new ScholarshipCalculatorImpl();
        double basicScholarship = ScholarshipCalculatorImpl.BASIC_SCHOLARSHIP; // 100
        double stepUpCoefficient = 0.1;
        double expected = basicScholarship * stepUpCoefficient;
        double actual = scholarshipCalculator.scholarshipCalculate(stepUpCoefficient);

        // Проверка на совпадение с погрешностью 0,01
        Assert.assertEquals("Тест прошел", expected, actual, 0.01);
    }
}