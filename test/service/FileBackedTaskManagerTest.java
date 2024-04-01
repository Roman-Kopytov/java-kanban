package service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static service.FileBackedTaskManager.loadFromFile;

class FileBackedTaskManagerTest extends TaskManagerTest {

    FileBackedTaskManager manager2;
    File tmpFile;


    @Override
    InMemoryTaskManager createManager() throws IOException {
        tmpFile = File.createTempFile("data", ".csv");
        return new FileBackedTaskManager(new InMemoryHistoryManager(), tmpFile);
    }

    @Test
    @DisplayName("Должен сохранять файл и восстанавливать его")
    public void shouldSaveAndLoad() {
        manager2 = loadFromFile(tmpFile);
        assertEquals(manager.getAllTask(), (manager2.getAllTask()), "Задачи должны совпадать");
        assertEquals(manager.getAllSubTask(), (manager2.getAllSubTask()), "Подзадачи должны совпадать");
        assertEquals(manager.getAllEpic(), (manager2.getAllEpic()), "Эпики должны совпадать");
        assertEquals(manager.getHistory(), manager2.getHistory(), "История должна совпадать");

        manager.deleteEpic(1);
        manager.deleteSubTask(5);
        manager.deleteSubTask(7);

        manager2 = loadFromFile(tmpFile);

        assertEquals(manager.getAllTask(), (manager2.getAllTask()), "Задачи должны совпадать");
        assertEquals(manager.getAllSubTask(), (manager2.getAllSubTask()), "Подзадачи должны совпадать");
        assertEquals(manager.getAllEpic(), (manager2.getAllEpic()), "Эпики должны совпадать");
        assertEquals(manager.getHistory(), manager2.getHistory(), "История должна совпадать");

        tmpFile.deleteOnExit();
    }

    @Test
    @DisplayName("Должен сохранять список отсортированных задач")
    public void shouldSavePrioritizedList() {
        manager2 = loadFromFile(tmpFile);
        assertEquals(manager.getPrioritizedTasks(), (manager2.getPrioritizedTasks()), "Задачи должны совпадать");
    }
}