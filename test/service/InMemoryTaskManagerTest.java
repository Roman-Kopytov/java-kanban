package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Менеджер задач")
class InMemoryTaskManagerTest {

    private static HistoryManager historyManager;
    private static InMemoryTaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);
    }

    @Test
    @DisplayName("Должен добавлять задачу")
    void shouldAddNewTask() {

        Task task = new Task("Test addNewTask", Status.NEW, "Test addNewTask description");
        taskManager.createTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTask();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    @DisplayName("Должен удалять все задачи")
    public void shouldDeleteAllTask() {
        Task taskFirst = new Task("Test addNewTask", Status.NEW, "Test addNewTask description");
        Task taskSecond = new Task("Test addNewTask", Status.NEW, "Test addNewTask description");
        taskManager.createTask(taskFirst);
        taskManager.createTask(taskSecond);
        final int taskIdFirst = taskFirst.getId();
        final int taskIdSecond = taskFirst.getId();
        taskManager.deleteAllTask();
        assertNull(taskManager.getTask(taskIdFirst));
        assertNull(taskManager.getTask(taskIdSecond));
    }

    @Test
    @DisplayName("Должен обновлять задачу")
    public void shouldUpdateTask() {
        Task task = new Task("Test addNewTask", Status.NEW, "Test addNewTask description");
        taskManager.createTask(task);
        final int taskId = task.getId();
        task.setName("Update name");
        task.setDescription("Update description");
        task.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);
        assertEquals(taskManager.getTask(taskId), task, "Должны быть равны");
    }

    @Test
    @DisplayName("Должен добавлять задачу")
    public void shouldDeleteTask() {
        Task task = new Task("Test addNewTask", Status.NEW, "Test addNewTask description");
        taskManager.createTask(task);
        final int taskId = task.getId();
        taskManager.deleteTask(taskId);
        assertEquals(null, taskManager.getTask(taskId), "Должны быть равны");
    }

    @Test
    @DisplayName("Должен создавать эпик")
    public void shouldCreateEpic() {
        Epic epic = new Epic("Test addNewEpic", Status.NEW, "Test addNewEpic description");
        taskManager.createEpic(epic);
        final int epicId = epic.getId();
        Epic savedEPic = taskManager.getEpic(epicId);
        assertEquals(epic, savedEPic, "Должны быть равны");
    }

    @Test
    @DisplayName("Должен удалять эпик")
    public void shouldDeleteEpic() {
        Epic epic = new Epic("Test addNewEpic", Status.NEW, "Test addNewEpic description");
        taskManager.createEpic(epic);
        final int epicId = epic.getId();
        taskManager.deleteEpic(epicId);
        assertEquals(null, taskManager.getTask(epicId), "Должны быть равны");
    }

    @Test
    @DisplayName("Должен удалять все эпики")
    public void shouldDeleteAllEpic() {
        Epic epicFirst = new Epic("Test addNewEpic", Status.NEW, "Test addNewEpic description");
        Epic epicSecond = new Epic("Test addNewEpic", Status.NEW, "Test addNewEpic description");
        taskManager.createTask(epicFirst);
        taskManager.createTask(epicSecond);
        final int epicIdFirst = epicFirst.getId();
        final int epicIdSecond = epicSecond.getId();
        taskManager.deleteAllEpic();
        assertNull(taskManager.getEpic(epicIdFirst));
        assertNull(taskManager.getEpic(epicIdSecond));
    }

    @Test
    @DisplayName("Должен возвращать все эпики")
    public void shouldGetAllEPics() {
        Epic epic = new Epic("Test addNewEpic", Status.NEW, "Test addNewEpic description");
        taskManager.createEpic(epic);
        final int epicId = epic.getId();

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> tasks = taskManager.getAllEpic();

        assertNotNull(tasks, "Эпики не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество эпиков.");
        assertEquals(epic, tasks.get(0), "Эпики не совпадают.");
    }

    @Test
    @DisplayName("Должен обновлять эпик")
    public void shouldUpdateEpic() {
        Epic epic = new Epic("Test addNewEpic", Status.NEW, "Test addNewEpic description");
        taskManager.createEpic(epic);
        final int epicId = epic.getId();
        epic.setName("Update name");
        epic.setDescription("Update description");
        epic.setStatus(Status.IN_PROGRESS);
        taskManager.updateEpic(epic);
        assertEquals(taskManager.getEpic(epicId), epic, "Должны быть равны");
    }

    @Test
    @DisplayName("Должен создавать подзадачу")
    public void shouldCreateSubTask() {
        Epic epic = new Epic("Test addNewEpic", Status.NEW, "Test addNewEpic description");
        taskManager.createEpic(epic);
        final int epicId = epic.getId();
        SubTask subTask = new SubTask("Test NewSubTask", Status.NEW, "Test NewSubtask description", epic);
        taskManager.createSubTask(subTask);
        final int subTaskId = subTask.getId();
        List<SubTask> savedSubTaskList = taskManager.getEpicSubTasks(epicId);
        assertEquals(subTask, savedSubTaskList.get(0), "Должны совпадать");

        SubTask savedSubTask = taskManager.getSubTask(subTaskId);
        assertEquals(subTask, savedSubTask, "Должны совпадать");


        subTask.setStatus(Status.IN_PROGRESS);
        subTask.setDescription("UpdateDescription");
        subTask.setName("UpdateName");
        taskManager.updateSubTask(subTask);
        assertEquals(subTask, taskManager.getSubTask(subTaskId), "Должны совпадать");

        taskManager.deleteSubTask(subTaskId);
        assertEquals(null, taskManager.getEpic(subTaskId), "Должен быть пустой");
    }

    @Test
    @DisplayName("Должен возвращать все подзадачи")
    public void shouldGetAllSubTask() {
        Epic epic = new Epic("Test addNewEpic", Status.NEW, "Test addNewEpic description");
        taskManager.createEpic(epic);
        final int epicId = epic.getId();
        SubTask subTaskFirst = new SubTask("Test NewSubTask1", Status.NEW, "Test NewSubtask description", epic);
        SubTask subTaskSecond = new SubTask("Test NewSubTask2", Status.NEW, "Test NewSubtask description", epic);
        taskManager.createSubTask(subTaskFirst);
        taskManager.createSubTask(subTaskSecond);
        final int subTaskFirstId = subTaskFirst.getId();
        final int subTaskSecondId = subTaskSecond.getId();
        List<SubTask> expectedSubTasks = new ArrayList<>();
        expectedSubTasks.add(subTaskFirst);
        expectedSubTasks.add(subTaskSecond);
        List<SubTask> actualSubTasks = taskManager.getAllSubTask();
        assertEquals(expectedSubTasks, actualSubTasks, "Должны совпадать");


    }

    @Test
    @DisplayName("Должен удалять все подзадачи")
    public void shouldDeleteAllSubTask() {
        Epic epic = new Epic("Test addNewEpic", Status.NEW, "Test addNewEpic description");
        taskManager.createEpic(epic);
        final int epicId = epic.getId();
        SubTask subTaskFirst = new SubTask("Test NewSubTask1", Status.NEW, "Test NewSubtask description", epic);
        SubTask subTaskSecond = new SubTask("Test NewSubTask2", Status.NEW, "Test NewSubtask description", epic);
        taskManager.createSubTask(subTaskFirst);
        taskManager.createSubTask(subTaskSecond);
        final int subTaskFirstId = subTaskFirst.getId();
        final int subTaskSecondId = subTaskSecond.getId();
        taskManager.deleteAllSubTask();
        assertEquals(null,taskManager.getSubTask(subTaskFirstId));
        assertEquals(null,taskManager.getSubTask(subTaskSecondId));

    }

    @Test
    @DisplayName("Должен возвращать список обращений к задачам")
    public void shouldGetHistory() {
        Epic epicFirst = new Epic("Test addNewEpic", Status.NEW, "Test addNewEpic description");
        Epic epicSecond = new Epic("Test addNewEpic", Status.NEW, "Test addNewEpic description");
        taskManager.createEpic(epicFirst);
        taskManager.createEpic(epicSecond);
        final int epicIdFirst = epicFirst.getId();
        final int epicIdSecond = epicSecond.getId();
        taskManager.getEpic(epicIdFirst);
        taskManager.getEpic(epicIdSecond);
        taskManager.getEpic(epicIdSecond);
        taskManager.getEpic(epicIdFirst);
        List<Task> historyList = taskManager.getHistory();
        List<Task> expectedList = new ArrayList<>();
        expectedList.add(epicFirst);
        expectedList.add(epicSecond);
        expectedList.add(epicSecond);
        expectedList.add(epicFirst);
        assertEquals(expectedList, historyList, "Должны совпадать");
    }

}