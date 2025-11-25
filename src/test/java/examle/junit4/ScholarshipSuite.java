package examle.junit4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// Листинг 6. Запуск группы тестов
@RunWith(Suite.class) // Обязательная аннотация для запуска набора тестов
@Suite.SuiteClasses( {
        ScholarshipCalculatorImplTest.class,
        ScholarshipCalculatorImplTest2.class,
        ScholarshipCalculatorImplTest4.class
})
public class ScholarshipSuite {
    // Этот класс не требует тела, его роль — только группировка
}