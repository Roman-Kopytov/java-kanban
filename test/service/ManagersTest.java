package service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Managers")
class ManagersTest {
    @Test
    @DisplayName("Должен возвращать taskManager")
    public void shouldGetDefaultTaskManager() {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        assertNotNull(taskManager);
    }

    @Test
    @DisplayName("Должен возвращать historyManager")
    public void shouldGetDefaultHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager);
    }
}