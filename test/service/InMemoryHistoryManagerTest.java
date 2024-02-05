package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HistoryManager")
class InMemoryHistoryManagerTest {


    @Test
    @DisplayName("Должна быть добавлена задача")
    void shouldAddTask() {
        InMemoryHistoryManager  historyManager = new InMemoryHistoryManager();
        Task task = new Task("First", Status.NEW, "Creation");
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }


}