package lab_01;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import java.util.Optional;

public class TestResultLogger implements TestWatcher {

    @Override
    public void testSuccessful(ExtensionContext context) {
        System.out.println("-> ФИНИШ: " + context.getDisplayName() + " -> УСПЕШНО");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String errorMsg = Optional.ofNullable(cause.getMessage()).orElse("Неизвестная ошибка");
        System.out.println("-> ФИНИШ: " + context.getDisplayName() + " -> ПРОВАЛ: " + errorMsg);
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        System.out.println("-> ФИНИШ: " + context.getDisplayName() + " -> ПРОПУЩЕН. Причина: " + reason.orElse("Не указана"));
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        System.out.println("-> ФИНИШ: " + context.getDisplayName() + " -> ПРЕРВАН. Причина: " + cause.getMessage());
    }
}