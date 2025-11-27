package lab_05;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import java.util.Optional;

/**
 * TestWatcher для логирования результатов каждого тестового метода.
 * Выводит в консоль информацию о начале, успехе, пропуске или сбое теста.
 */
public class TestLifecycleLogger implements TestWatcher {

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        String testName = context.getDisplayName();
        System.out.printf(">>> ТЕСТ ПРОПУЩЕН => [%s]\n", testName);
        reason.ifPresent(r -> System.out.printf("    Причина: %s\n", r));
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        String testName = context.getDisplayName();
        System.out.printf(">>> ТЕСТ УСПЕШНО ЗАВЕРШЕН => [%s]\n", testName);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName();
        System.out.printf(">>> ТЕСТ ПРЕРВАН => [%s]\n", testName);
        if (cause != null) {
            System.out.printf("    Причина прерывания: %s\n", cause.getMessage());
        }
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName();
        System.err.printf("!!! ТЕСТ ПРОВАЛЕН !!!> [%s]\n", testName);
        if (cause != null) {
            System.err.printf("    Ошибка: %s\n", cause.getMessage());
        }
    }
}