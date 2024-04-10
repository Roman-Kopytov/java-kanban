package server;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import controller.*;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    static final int PORT = 8080;
    HttpServer httpServer;
    TaskManager manager;
    ErrorHandler errorHandler;

    Gson gson;

    public HttpTaskServer() {
        this(Managers.getDefaultTaskManager());
    }

    public HttpTaskServer(TaskManager taskManager) {
        this.manager = taskManager;
        this.gson = getGson();
        this.errorHandler = new ErrorHandler(gson);
        try {
            httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        httpServer.createContext("/tasks", new TaskHandler(manager, gson));
        httpServer.createContext("/epics", new EpicHandler(manager, gson));
        httpServer.createContext("/subtasks", new SubTaskHandler(manager, gson));
        httpServer.createContext("/history", new HistoryHandler(manager, gson));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager, gson));
    }


    private void sendText(HttpExchange httpExchange, String json) {

    }


    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановлен сервер на порту " + PORT);
    }

    public void start() {
        System.out.println("Запуск сервера на порту" + PORT);
        httpServer.start();
    }

    static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    public static void main(String[] args) {
        HttpTaskServer server = new HttpTaskServer();
        server.start();
    }

}
