package service;

import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Менеджер задач")
class InMemoryTaskManagerTest extends TaskManagerTest {


    @Override
    InMemoryTaskManager createManager() {
        return new InMemoryTaskManager(new InMemoryHistoryManager());
    }

    @Test
    @DisplayName("Должен сортировать задачи по startTime")
    public void shouldReturnPrioritizedTasks() {
        ArrayList<Task> expectedList = new ArrayList<>();
        expectedList.add(manager.getSubTask(5));
        expectedList.add(manager.getTask(2));
        expectedList.add(manager.getSubTask(6));
        expectedList.add(manager.getSubTask(7));
        expectedList.add(manager.getSubTask(8));

        List<Task> actualList = manager.getPrioritizedTasks();

        assertEquals(expectedList, new ArrayList<>(actualList));
    }

    @Test
    @DisplayName("Должен считать статус эпика")
    public void shouldCalculateEpicStatus() {
        assertEquals(Status.NEW, manager.getEpic(3).getStatus(), "Должны совпадать");

        manager.updateSubTask(new SubTask("1", Status.IN_PROGRESS, "1",
                LocalDateTime.of(2018, 1, 2, 20, 38), Duration.ofMinutes(26), epicFirst, 5));
        manager.updateSubTask(new SubTask("2", Status.IN_PROGRESS, "2",
                LocalDateTime.of(2019, 1, 2, 20, 38), Duration.ofMinutes(26), epicFirst, 6));

        assertEquals(Status.IN_PROGRESS, manager.getEpic(3).getStatus(), "Должны совпадать");

        manager.updateSubTask(new SubTask("1", Status.DONE, "1",
                LocalDateTime.of(2020, 1, 2, 20, 38), Duration.ofMinutes(26), epicFirst, 5));
        manager.updateSubTask(new SubTask("2", Status.IN_PROGRESS, "2",
                LocalDateTime.of(2021, 1, 2, 20, 38), Duration.ofMinutes(26), epicFirst, 6));

        assertEquals(Status.IN_PROGRESS, manager.getEpic(3).getStatus(), "Должны совпадать");

        manager.updateSubTask(new SubTask("1", Status.DONE, "1",
                LocalDateTime.of(2022, 1, 2, 20, 38), Duration.ofMinutes(26), epicFirst, 5));
        manager.updateSubTask(new SubTask("2", Status.DONE, "2",
                LocalDateTime.of(2023, 1, 2, 20, 38), Duration.ofMinutes(26), epicFirst, 6));

        assertEquals(Status.DONE, manager.getEpic(3).getStatus(), "Должны совпадать");

        manager.updateSubTask(new SubTask("1", Status.NEW, "1",
                LocalDateTime.of(2024, 1, 2, 20, 38), Duration.ofMinutes(26), epicFirst, 5));
        manager.updateSubTask(new SubTask("2", Status.DONE, "2",
                LocalDateTime.of(2025, 1, 2, 20, 38), Duration.ofMinutes(26), epicFirst, 6));

        assertEquals(Status.IN_PROGRESS, manager.getEpic(3).getStatus(), "Должны совпадать");

        manager.updateSubTask(new SubTask("1", Status.NEW, "1",
                LocalDateTime.of(2026, 1, 2, 20, 38), Duration.ofMinutes(26), epicFirst, 5));
        manager.updateSubTask(new SubTask("2", Status.IN_PROGRESS, "2",
                LocalDateTime.of(2027, 1, 2, 20, 38), Duration.ofMinutes(26), epicFirst, 6));

        assertEquals(Status.IN_PROGRESS, manager.getEpic(3).getStatus(), "Должны совпадать");

        manager.deleteAllSubTask();
        assertEquals(Status.NEW, manager.getEpic(3).getStatus(), "Должны совпадать");
    }

}