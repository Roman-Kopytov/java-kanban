package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {

    HistoryManager historyManager;
    FileBackedTaskManager manager;
    FileBackedTaskManager manager2;
    File tmpFile;

    @BeforeEach
    public void beforeEach() throws IOException {
        historyManager = new InMemoryHistoryManager();
        tmpFile = File.createTempFile("data", ".csv");
        manager = new FileBackedTaskManager(historyManager, tmpFile);

        manager.createTask(new Task("First", Status.NEW, "Description1",
                LocalDateTime.of(2024, 2, 1, 0, 0), Duration.ofMinutes(30)));
        Epic epicOne = manager.createEpic(new Epic("Купить билеты", Status.NEW, "Description"));
        manager.createSubTask(new SubTask("subTaskOne", Status.NEW, "Description",
                null, Duration.ofMinutes(50), epicOne));
//        manager.createSubTask(new SubTask("subTaskOne", Status.NEW, "Description",
//                LocalDateTime.of(2024, 1, 1, 0, 0), Duration.ofMinutes(50), epicOne));
        manager.createSubTask(new SubTask("subTaskTwo", Status.NEW, "Description",
                LocalDateTime.of(2024, 1, 2, 0, 0), Duration.ofMinutes(80), epicOne));

        manager.getTask(1);
        manager.getEpic(2);
        manager.getTask(1);
        manager.getSubTask(3);
        manager.getSubTask(4);
    }

    @Test
    @DisplayName("Должен сохранять файл и восстанавливать его")
    public void shouldSaveAndLoad() {
        manager2 = FileBackedTaskManager.loadFromFile(tmpFile);
        assertEquals(manager.tasks, (manager2.tasks), "Задачи должны совпадать");
        assertEquals(manager.subTasks, (manager2.subTasks), "Подзадачи должны совпадать");
        assertEquals(manager.epics, (manager2.epics), "Эпики должны совпадать");
        assertEquals(manager.getHistory(), manager2.getHistory(), "История должна совпадать");

        manager.deleteTask(1);
        manager.deleteSubTask(3);
        manager.deleteSubTask(4);

        manager2 = FileBackedTaskManager.loadFromFile(tmpFile);

        assertEquals(manager.tasks, (manager2.tasks), "Задачи должны совпадать");
        assertEquals(manager.subTasks, (manager2.subTasks), "Подзадачи должны совпадать");
        assertEquals(manager.epics, (manager2.epics), "Эпики должны совпадать");
        assertEquals(manager.getHistory(), manager2.getHistory(), "История должна совпадать");

        tmpFile.deleteOnExit();
    }
}