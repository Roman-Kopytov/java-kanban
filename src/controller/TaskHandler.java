package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TaskHandler extends BaseHandler {


    public TaskHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }


    @Override
    public void prepareResponse(HttpExchange exchange) throws IOException {
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
                manager.deleteTask(id.get());
                exchange.sendResponseHeaders(204, 0);
            }
        } else if (splitPath.length == 2) {
            manager.deleteAllTask();
            exchange.sendResponseHeaders(204, 0);
        } else {
            throw new NotFoundException("Invalid path");
        }
    }


    private void handlePostRequest(HttpExchange exchange, String[] splitPath) throws IOException {
        if (splitPath.length == 2) {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(body, Task.class);
            int id = task.getId();
            if (id != 0) {
                manager.updateTask(task);
                sendText(exchange, 200, gson.toJson(manager.getTask(id)));
            } else {
                sendText(exchange, 201, gson.toJson(manager.createTask(task)));
            }
        } else {
            throw new NotFoundException("Invalid path");
        }
    }

    private void handleGetRequest(HttpExchange exchange, String[] splitPath) throws IOException {
        if (splitPath.length == 3) {
            Optional<Integer> id = getId(exchange);
            if (id.isPresent()) {
                Task task = manager.getTask(id.get());
                sendText(exchange, 200, gson.toJson(task));
            }
        } else if (splitPath.length == 2) {
            sendText(exchange, 200, gson.toJson(manager.getAllTask()));
        } else {
            throw new NotFoundException("Invalid path");
        }
    }
}
