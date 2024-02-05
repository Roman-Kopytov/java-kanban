package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    public void shouldGetDefaultTaskManager() {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        assertEquals("class service.InMemoryTaskManager", taskManager.getClass().toString());
    }
}