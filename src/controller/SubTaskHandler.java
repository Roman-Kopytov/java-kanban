package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import model.SubTask;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SubTaskHandler extends BaseHandler {

    public SubTaskHandler(TaskManager manager, Gson gson) {
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
                manager.deleteSubTask(id.get());
                exchange.sendResponseHeaders(204, 0);
            }
        } else if (splitPath.length == 2) {
            manager.deleteAllSubTask();
            exchange.sendResponseHeaders(204, 0);
        } else {
            throw new NotFoundException("Invalid path");
        }
    }

    private void handlePostRequest(HttpExchange exchange, String[] splitPath) throws IOException {
        if (splitPath.length == 2) {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            SubTask subTask = gson.fromJson(body, SubTask.class);
            int id = subTask.getId();
            if (id != 0) {
                manager.updateTask(subTask);
                sendText(exchange, 200, gson.toJson(manager.getSubTask(id)));
            } else {
                sendText(exchange, 201, gson.toJson(manager.createSubTask(subTask)));
            }
        } else {
            throw new NotFoundException("Invalid path");
        }

    }

    private void handleGetRequest(HttpExchange exchange, String[] splitPath) throws IOException {
        if (splitPath.length == 2) {
            sendText(exchange, 200, gson.toJson(manager.getAllSubTask()));
        } else if (splitPath.length == 3) {
            Optional<Integer> id = getId(exchange);
            if (id.isPresent()) {
                sendText(exchange, 200, gson.toJson(manager.getSubTask(id.get())));
            }
        } else {
            throw new NotFoundException("Invalid path");
        }
    }
}
