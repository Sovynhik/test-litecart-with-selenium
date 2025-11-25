package lab_01;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.DisplayName;
import ru.rsreu.sovynhik.calculation.NotSuchMarkException;
import ru.rsreu.sovynhik.calculation.ScholarshipCalculatorImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.stream.Stream;

@ExtendWith(TestResultLogger.class)
public class ScholarshipParameterizedTest {

    private final ScholarshipCalculatorImpl calculator = new ScholarshipCalculatorImpl();

    // Метод, предоставляющий данные для параметризованного теста
    static Stream<Object[]> stepUpCoefficientTableValues() {
        return Stream.of(new Object[][]{
                {3, 1.0},
                {4, 1.3},
                {5, 1.5}
        });
    }

    @ParameterizedTest
    @MethodSource("stepUpCoefficientTableValues") // Указывает на метод-источник данных
    @DisplayName("Параметризованный тест коэффициента для баллов {0}")
    void stepUpCoefficientCalculationTest(int averageMark, double expectedCoefficient) throws NotSuchMarkException {

        double actual = calculator.stepUpCoefficientCalculate(averageMark);

        assertEquals(
                expectedCoefficient,
                actual,
                0.01,
                "Коэффициент для балла " + averageMark + " неверен"
        );
    }
}