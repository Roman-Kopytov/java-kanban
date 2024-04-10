package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TaskHandler extends BaseHandler implements PathHandler {


    public TaskHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public String getPath() {
        return "task";
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        switch (method) {
            case "GET":
                handleGetRequest(exchange, splitPath);
                break;
            case "POST":
                handlePostRequest(exchange, splitPath);
                break;
            case "DELETE":
                handleDeleteRequest(exchange, splitPath);
                break;
            default:
                sendText(exchange, 405, "Unsupported method - " + method);
        }
    }

    private void handleDeleteRequest(HttpExchange exchange, String[] splitPath) throws IOException {
        if (splitPath.length == 3) {
            Optional<Integer> id = getId(exchange);
            if (id.isPresent()) {
                try (exchange) {
                    try {
                        manager.deleteTask(id.get());
                        exchange.sendResponseHeaders(204, 0);
                    } catch (Exception e) {
                        errorHandler.handle(exchange, e);
                    }
                }
            }
        } else if (splitPath.length == 2) {
            try (exchange) {
                try {
                    manager.deleteAllTask();
                    exchange.sendResponseHeaders(204, 0);
                } catch (Exception e) {
                    errorHandler.handle(exchange, e);
                }
            }
        }
    }


    private void handlePostRequest(HttpExchange exchange, String[] splitPath) throws IOException {
        if (splitPath.length == 2) {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(body, Task.class);
            int id = task.getId();
            if (id != 0) {
                try (exchange) {
                    manager.updateTask(task);
                    sendText(exchange, 200, gson.toJson(manager.getTask(id)));
                } catch (Exception e) {
                    errorHandler.handle(exchange, e);
                }
            } else {
                try (exchange) {
                    try {
                        sendText(exchange, 201, gson.toJson(manager.createTask(task)));

                    } catch (Exception e) {
                        errorHandler.handle(exchange, e);
                    }
                }
            }
        }
    }

    private void handleGetRequest(HttpExchange exchange, String[] splitPath) throws IOException {
        if (splitPath.length == 3) {
            Optional<Integer> id = getId(exchange);
            if (id.isPresent()) {
                try (exchange) {
                    try {
                        Task task = manager.getTask(id.get());
                        sendText(exchange, 200, gson.toJson(task));
                    } catch (Exception e) {
                        errorHandler.handle(exchange, e);
                    }
                }
            }
        } else if (splitPath.length == 2) {
            try (exchange) {
                try {
                    sendText(exchange, 200, gson.toJson(manager.getAllTask()));
                } catch (Exception e) {
                    errorHandler.handle(exchange, e);
                }
            }
        }
    }
}
