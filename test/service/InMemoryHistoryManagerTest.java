package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("HistoryManager")
class InMemoryHistoryManagerTest {

    private static HistoryManager historyManager;
    private static InMemoryTaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);
    }

    @Test
    @DisplayName("Должна быть добавлена задача")
    void shouldAddTask() {
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
        Task task = new Task("First", Status.NEW, "Creation");
        Task taskTwo = new Task("Two", Status.NEW, "Creation");
        Task taskThree = new Task("Three", Status.NEW, "Creation");
        Task taskFour = new Task("Four", Status.NEW, "Creation");
        Task taskFive = new Task("Five", Status.NEW, "Creation");
        taskManager.createTask(task);
        taskManager.createTask(taskTwo);
        taskManager.createTask(taskThree);
        taskManager.createTask(taskFour);
        taskManager.createTask(taskFive);
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.deleteTask(1);
        assertEquals(taskTwo, historyManager.getHistory().getFirst(), "Удалена не первая задача");

        taskManager.getTask(3);
        taskManager.deleteTask(3);
        assertEquals(taskTwo, historyManager.getHistory().getLast(), "Удалена не последняя задача");


        taskManager.getTask(4);
        taskManager.getTask(5);
        taskManager.deleteTask(4);
        assertEquals(taskFive, historyManager.getHistory().get(1), "Удалена задача не из центра");
    }
}