package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static service.HttpTaskServer.getGson;
import static service.Managers.getDefaultHistory;


class HttpTaskServerTest {

    Gson gson = getGson();

    InMemoryTaskManager taskManager;
    HttpTaskServer taskServer;
    HttpClient client;
    HttpRequest request;

    private Task task;
    private Epic epic;
    private SubTask subTask;

    @BeforeEach
    void init() throws IIOException {
        taskManager = new InMemoryTaskManager(getDefaultHistory());
        taskServer = new HttpTaskServer(taskManager);
        task = taskManager.createTask(new Task("Task", Status.NEW, "Description", LocalDateTime.of(2020, 10, 10, 22, 10), Duration.ofMinutes(50)));
        epic = taskManager.createEpic(new Epic("addNewEpic1", Status.NEW, "addNewEpic description"));
        subTask = taskManager.createSubTask(new SubTask("NewSubTask1", Status.NEW, "NewSubtask description",
                LocalDateTime.of(2024, 1, 1, 0, 0), Duration.ofMinutes(30), epic));
        System.out.println("Запуск сервера на порту" + 8080);
        taskServer.start();

    }

    @DisplayName("Возвращает лист Task")
    @Test
    void shouldReturnTaskList() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        final List<Task> tasks = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertNotNull(tasks);
        assertEquals(taskManager.tasks.size(), tasks.size(), "Task size");
        Task actual = tasks.get(0);
        assertEquals(task, actual, "Таски равны");
    }

    @DisplayName("Возвращает Task по id")
    @Test
    void shouldReturnTask() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task actual = gson.fromJson(response.body(), Task.class);
        assertNotNull(actual);
        assertEquals(taskManager.tasks.get(task.getId()), actual, "Task равны");
    }

    @DisplayName("Создает Task")
    @Test
    void shouldCreateTask() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        Task newTask = new Task("TaskNew", Status.NEW, "Description", LocalDateTime.of(2024, 10, 10, 22, 10), Duration.ofMinutes(50));
        String jsonString = gson.toJson(newTask);
        request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        Task actual = gson.fromJson(response.body(), Task.class);
        assertNotNull(actual);
        newTask.setId(4);
        assertEquals(newTask, actual, "Task равны");
    }

    @DisplayName("Обновляет Task")
    @Test
    void shouldUpdateTask() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        Task newTask = new Task("TaskNew", Status.NEW, "Description", LocalDateTime.of(2024, 10, 10, 22, 10), Duration.ofMinutes(50));
        newTask.setId(1);
        String jsonString = gson.toJson(newTask);
        request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task actual = gson.fromJson(response.body(), Task.class);
        assertNotNull(actual);
        assertEquals(newTask, actual, "Task равны");

    }

    @DisplayName("Удаляет Task")
    @Test
    void shouldDeleteTask() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        request = HttpRequest
                .newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        assertEquals(0, taskManager.tasks.size());
    }

    @DisplayName("Возвращает лист Epic")
    @Test
    void shouldReturnTEpicList() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        final List<Epic> epics = gson.fromJson(response.body(), new TypeToken<ArrayList<Epic>>() {
        }.getType());

        assertNotNull(epics);
        assertEquals(taskManager.epics.size(), epics.size(), "Epic size");
        Task actual = epics.get(0);
        assertEquals(epic, actual, "Таски равны");
    }

    @DisplayName("Возвращает лист SubTask")
    @Test
    void shouldReturnEpicSubTasks() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/2/subtasks");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        final List<SubTask> subTasks = gson.fromJson(response.body(), new TypeToken<ArrayList<SubTask>>() {
        }.getType());

        assertNotNull(subTasks);
        assertEquals(taskManager.getEpicSubTasks(2).size(), subTasks.size(), "Epic size");

        assertEquals(taskManager.getEpicSubTasks(2), subTasks, "Таски равны");
    }

    @DisplayName("Возвращает Epic по id")
    @Test
    void shouldReturnEpic() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/2");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Epic actual = gson.fromJson(response.body(), Epic.class);
        assertNotNull(actual);
        assertEquals(taskManager.epics.get(epic.getId()), actual, "Epic равны");
    }

    @DisplayName("Создает Epic")
    @Test
    void shouldCreateEpic() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        Epic newEpic = new Epic("EpicNew", Status.NEW, "Description", LocalDateTime.of(2024, 10, 10, 22, 10), Duration.ofMinutes(50));
        String jsonString = gson.toJson(newEpic);
        request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        Epic actual = gson.fromJson(response.body(), Epic.class);
        assertNotNull(actual);
        newEpic.setId(4);
        assertEquals(newEpic, actual, "Task равны");
    }

    @DisplayName("Обновляет Epic")
    @Test
    void shouldUpdateEpic() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        Epic newEpic = new Epic("EpicNew", Status.NEW, "newDescription");

        newEpic.setId(2);
        String jsonString = gson.toJson(newEpic);
        request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Epic actual = gson.fromJson(response.body(), Epic.class);
        assertNotNull(actual);
        assertEquals(newEpic.getName(), actual.getName(), "Name равны");
        assertEquals(newEpic.getDescription(), actual.getDescription(), "Name равны");
    }

    @DisplayName("Возвращает лист SubTask")
    @Test
    void shouldReturnSubTaskList() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        final List<SubTask> subTasks = gson.fromJson(response.body(), new TypeToken<ArrayList<SubTask>>() {
        }.getType());

        assertNotNull(subTasks);
        assertEquals(taskManager.tasks.size(), subTasks.size(), "Task size");
        Task actual = subTasks.get(0);
        assertEquals(subTask, actual, "Таски равны");
    }

    @DisplayName("Возвращает SubTask по id")
    @Test
    void shouldReturnSubTask() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/3");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        SubTask actual = gson.fromJson(response.body(), SubTask.class);
        assertNotNull(actual);
        assertEquals(taskManager.subTasks.get(subTask.getId()), actual, "Task равны");
    }

    @DisplayName("Создает SubTask")
    @Test
    void shouldCreateSubTask() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        Task newTask = new Task("TaskNew", Status.NEW, "Description", LocalDateTime.of(2024, 10, 10, 22, 10), Duration.ofMinutes(50));
        String jsonString = gson.toJson(newTask);
        request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        Task actual = gson.fromJson(response.body(), Task.class);
        assertNotNull(actual);
        newTask.setId(4);
        assertEquals(newTask, actual, "Task равны");
    }

    @DisplayName("Обновляет UpdateTask")
    @Test
    void shouldUpdateSubTask() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        SubTask newSubTask = new SubTask("SubTaskNew", Status.NEW, "Description", LocalDateTime.of(2024, 10, 10, 22, 10), Duration.ofMinutes(50), epic);
        newSubTask.setId(3);
        String jsonString = gson.toJson(newSubTask);
        request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        SubTask actual = gson.fromJson(response.body(), SubTask.class);
        assertNotNull(actual);
        assertEquals(newSubTask, actual, "Task равны");
    }

    @DisplayName("Удаляет SubTask")
    @Test
    void shouldDeleteSubTask() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/3");
        request = HttpRequest
                .newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        assertEquals(0, taskManager.subTasks.size());
    }

    @DisplayName("Возвращает history")
    @Test
    void shouldReturnHistory() throws IOException, InterruptedException {
        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getSubTask(3);
        client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        final List<Task> history = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());//Не смог понять как верно дессериализовать

        assertNotNull(history);
        assertEquals(taskManager.getHistory().size(), history.size(), "History size");

        assertEquals(gson.toJson(taskManager.getHistory()), response.body(), "history равны");//Сравнил поэтому Json
    }

    @DisplayName("Возвращает приоритетные задачи")
    @Test
    void shouldReturnPrioritizedList() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        final List<Task> prioritized = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());
        assertNotNull(prioritized);
        assertEquals(taskManager.getPrioritizedTasks().size(), prioritized.size(), "prioritized size");
        assertEquals(gson.toJson(taskManager.getPrioritizedTasks()), response.body(), "prioritized равны");
    }

    @AfterEach
    void stop() {
        taskServer.stop();
    }
}