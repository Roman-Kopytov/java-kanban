package service;


import exception.NotFoundException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {


    T manager;
    Task taskFirst;
    Task taskSecond;
    Epic epicFirst;
    Epic epicSecond;
    SubTask subTaskFirst;
    SubTask subTaskSecond;
    SubTask subTaskThird;
    SubTask subTaskFourth;


    abstract T createManager() throws IOException;

    @BeforeEach
    public void beforeEach() throws IOException {
        manager = createManager();
        taskFirst = manager.createTask(new Task("addNewTask1", Status.NEW, "addNewTask description",
                null, Duration.ofMinutes(30)));
        taskSecond = manager.createTask(new Task("addNewTask2", Status.NEW, "addNewTask description",
                LocalDateTime.of(2024, 1, 2, 0, 0), Duration.ofMinutes(10)));

        epicFirst = manager.createEpic(new Epic("addNewEpic1", Status.NEW, "addNewEpic description"));
        epicSecond = manager.createEpic(new Epic("addNewEpic2", Status.NEW, "addNewEpic description"));

        subTaskFirst = manager.createSubTask(new SubTask("NewSubTask1", Status.NEW, "NewSubtask description",
                LocalDateTime.of(2024, 1, 1, 0, 0), Duration.ofMinutes(30), epicFirst));
        subTaskSecond = manager.createSubTask(new SubTask("NewSubTask2", Status.NEW, "NewSubtask description",
                LocalDateTime.of(2024, 2, 1, 0, 0), Duration.ofMinutes(60), epicFirst));

        subTaskThird = manager.createSubTask(new SubTask("NewSubTask3", Status.NEW, "NewSubtask description",
                LocalDateTime.of(2024, 2, 2, 0, 0), Duration.ofMinutes(60), epicSecond));
        subTaskFourth = manager.createSubTask(new SubTask("NewSubTask4", Status.NEW, "NewSubtask description",
                LocalDateTime.of(2024, 2, 3, 0, 0), Duration.ofMinutes(60), epicSecond));

        manager.getEpic(3);
        manager.getTask(2);
        manager.getSubTask(6);
        manager.getEpic(4);
        manager.getEpic(4);
        manager.getEpic(3);
    }

    @Test
    void shouldGetAllTask() {
        final List<Task> tasks = manager.getAllTask();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(taskFirst, tasks.getFirst(), "Задачи не совпадают.");
        assertEquals(taskSecond, tasks.getLast(), "Задачи не совпадают.");
        assertEquals(2, tasks.size(), "Неверное количество Задач.");

    }

    @Test
    @DisplayName("Должен удалять все задачи")
    void shouldDeleteAllTask() {
        final int taskIdFirst = taskFirst.getId();
        final int taskIdSecond = taskSecond.getId();
        manager.deleteAllTask();
        assertThrows(NotFoundException.class, () -> manager.getTask(taskIdFirst), "Должен быть пустой");
        assertThrows(NotFoundException.class, () -> manager.getTask(taskIdSecond), "Должен быть пустой");
        assertEquals(0, manager.getAllTask().size(), "Список должен быть пуст");
    }

    @Test
    @DisplayName("Должен обновлять задачу")
    void shouldUpdateTask() {
        final int taskId = taskSecond.getId();
        Task testTask = new Task("Update name", Status.IN_PROGRESS, "Update description",
                LocalDateTime.of(2020, 2, 2, 16, 35), Duration.ofMinutes(25));
        testTask.setId(taskId);
        manager.updateTask(testTask);
        assertEquals(manager.getTask(taskId), taskSecond, "Должны быть равны");
    }

    @Test
    @DisplayName("Должен удалять задачу")
    void shouldDeleteTask() {
        final int taskId = taskFirst.getId();
        manager.deleteTask(taskId);
        assertThrows(NotFoundException.class, () -> manager.getTask(taskId), "Должен быть пустой");
    }

    @Test
    @DisplayName("Должен добавлять задачу")
    void shouldCreateTask() {
        final Task savedTask = manager.getTask(1);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(taskFirst, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = manager.getAllTask();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(taskFirst, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    @DisplayName("Должен удалять эпик")
    void deleteEpic() {
        final int epicId = epicFirst.getId();
        manager.deleteEpic(epicId);
        assertThrows(NotFoundException.class, () -> manager.getEpic(epicId), "Должен быть пустой");
    }

    @Test
    @DisplayName("Должен создавать эпик")
    void createEpic() {
        final int epicId = epicFirst.getId();

        final Epic savedEpic = manager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epicFirst, savedEpic, "Эпики не совпадают.");
    }

    @Test
    @DisplayName("Должен удалять все эпики")
    void shouldDeleteAllEpic() {
        final int epicIdFirst = epicFirst.getId();
        final int epicIdSecond = epicSecond.getId();
        manager.deleteAllEpic();
        assertThrows(NotFoundException.class, () -> manager.getEpic(epicIdFirst), "Должен быть пустой");
        assertThrows(NotFoundException.class, () -> manager.getEpic(epicIdSecond), "Должен быть пустой");
    }

    @Test
    @DisplayName("Должен возвращать все эпики")
    void shouldGetAllEPics() {
        final List<Epic> epics = manager.getAllEpic();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(epicFirst, epics.getFirst(), "Эпики не совпадают.");
        assertEquals(epicSecond, epics.getLast(), "Эпики не совпадают.");
        assertEquals(2, epics.size(), "Неверное количество Эпиков.");
    }

    @Test
    @DisplayName("Должен обновлять эпик")
    void shouldUpdateEpic() {
        final int epicId = epicFirst.getId();
        Epic testEpic = new Epic("Update name", Status.IN_PROGRESS, "Update description");
        testEpic.setId(epicId);
        manager.updateEpic(testEpic);

        assertEquals(manager.getEpic(epicId).getName(), testEpic.getName(), "Должны быть равны");
        assertEquals(manager.getEpic(epicId).getDescription(), testEpic.getDescription(), "Должны быть равны");
        assertEquals(manager.getEpic(epicId).getStatus(), Status.NEW, "Должны быть равны");
    }

    @Test
    void shouldGetEpicSubTasks() {
        assertEquals(List.of(subTaskFirst, subTaskSecond), manager.getEpicSubTasks(3));
        assertEquals(List.of(subTaskThird, subTaskFourth), manager.getEpicSubTasks(4));
    }

    @Test
    @DisplayName("Должен возвращать все подзадачи")
    void shouldGetAllSubTask() {
        List<SubTask> expectedSubTasks = List.of(subTaskFirst, subTaskSecond, subTaskThird, subTaskFourth);
        List<SubTask> actualSubTasks = manager.getAllSubTask();
        assertEquals(expectedSubTasks, actualSubTasks, "Должны совпадать");
    }

    @Test
    @DisplayName("Должен удалять все подзадачи")
    void shouldDeleteAllSubTask() {
        final int subTaskIdFirst = subTaskFirst.getId();
        final int subTaskIdSecond = subTaskSecond.getId();
        final int subTaskIdThird = subTaskThird.getId();
        final int subTaskIdFourth = subTaskFourth.getId();
        manager.deleteAllSubTask();
        assertEquals(0, manager.getAllSubTask().size(), "Должен быть пустой");
        assertThrows(NotFoundException.class, () -> manager.getSubTask(subTaskIdFirst), "Должен быть пустой");
        assertThrows(NotFoundException.class, () -> manager.getSubTask(subTaskIdSecond), "Должен быть пустой");
        assertThrows(NotFoundException.class, () -> manager.getSubTask(subTaskIdThird), "Должен быть пустой");
        assertThrows(NotFoundException.class, () -> manager.getSubTask(subTaskIdFourth), "Должен быть пустой");

    }

    @Test
    void shouldUpdateSubTask() {
        final int subTaskId = subTaskSecond.getId();
        SubTask testSubTask = new SubTask("UpdateName", Status.IN_PROGRESS, "UpdateDescription",
                LocalDateTime.of(2019, 1, 2, 20, 38), Duration.ofMinutes(26), epicFirst);
        testSubTask.setId(subTaskId);
        manager.updateSubTask(testSubTask);
        assertEquals(testSubTask, manager.getSubTask(subTaskId), "Должны совпадать");
    }

    @Test
    void shouldDeleteSubTask() {
        final int subTaskIdFirst = subTaskFirst.getId();
        manager.deleteSubTask(subTaskIdFirst);
        assertThrows(NotFoundException.class, () -> manager.getSubTask(subTaskIdFirst), "Должен быть пустой");
    }

    @Test
    @DisplayName("Должен создавать подзадачу")
    void shouldCreateSubTask() {
        final int epicId = epicFirst.getId();
        List<SubTask> savedSubTaskList = manager.getEpicSubTasks(epicId);
        assertEquals(2, savedSubTaskList.size(), "Неверное количество subtask.");
        assertEquals(subTaskFirst, savedSubTaskList.getFirst(), "Должны совпадать");
        assertEquals(subTaskSecond, savedSubTaskList.getLast(), "Должны совпадать");
    }


    @Test
    @DisplayName("Должен возвращать список обращений к задачам")
    void shouldGetHistory() {
        List<Task> historyList = manager.getHistory();
        List<Task> expectedList = List.of(taskSecond, subTaskSecond, epicSecond, epicFirst);

        assertEquals(expectedList, historyList, "Должны совпадать");
    }

    @Test
    @DisplayName("Эпик не должен хранить неактуальные подзадачи")
    void shouldDeleteSubtaskFromEpic() {
        final int epicId = epicFirst.getId();
        final int subTaskFirstId = subTaskFirst.getId();
        manager.deleteSubTask(subTaskFirstId);
        List<Task> expectedList = new ArrayList<>();
        expectedList.add(subTaskSecond);
        assertEquals(expectedList, manager.getEpicSubTasks(epicId), "Списки должны совпадать");
    }
}