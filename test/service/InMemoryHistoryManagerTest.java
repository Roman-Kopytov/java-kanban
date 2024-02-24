package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("HistoryManager")
class InMemoryHistoryManagerTest {


    @Test
    @DisplayName("Должна быть добавлена задача")
    void shouldAddTask() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        InMemoryTaskManager taskManager = new InMemoryTaskManager(historyManager);
        Task task = new Task("First", Status.NEW, "Creation");
        taskManager.createTask(task);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    @DisplayName("Должен выводить задачи в правильной последовательности")
    void shouldReturnInCorrectOrder() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        InMemoryTaskManager taskManager = new InMemoryTaskManager(historyManager);
        Task task = new Task("First", Status.NEW, "Creation");
        Task taskTwo = new Task("Two", Status.NEW, "Creation");
        Task taskThree = new Task("Three", Status.NEW, "Creation");
        taskManager.createTask(task);
        taskManager.createTask(taskTwo);
        taskManager.createTask(taskThree);
        taskManager.getTask(3);
        taskManager.getTask(2);
        taskManager.getTask(1);
        taskManager.getTask(3);
        List<Task> actualHistory = historyManager.getHistory();
        List<Task> expectedHistory = List.of(taskTwo, task, taskThree);
        assertEquals(expectedHistory, actualHistory, "Должны быть равны");
    }

    @Test
    @DisplayName("Должен удалять задачи")
    void shouldDeleteTask() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        InMemoryTaskManager taskManager = new InMemoryTaskManager(historyManager);
        Task task = new Task("First", Status.NEW, "Creation");
        Task taskTwo = new Task("Two", Status.NEW, "Creation");
        Task taskThree = new Task("Three", Status.NEW, "Creation");
        taskManager.createTask(task);
        taskManager.createTask(taskTwo);
        taskManager.createTask(taskThree);
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(3);
        historyManager.remove(1);
        assertEquals(taskTwo, historyManager.getHistory().getFirst(), "Удалена не первая задача");

        taskManager.getTask(1);
        historyManager.remove(1);
        assertEquals(taskThree, historyManager.getHistory().getLast(), "Удалена не последняя задача");

        taskManager.getTask(1);
        historyManager.remove(3);
        assertEquals(task, historyManager.getHistory().get(1), "Удалена задача не из центра");
    }
}