package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import model.Epic;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class EpicHandler extends BaseHandler {
    public EpicHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void prepareResponse(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        String method = exchange.getRequestMethod();
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
                manager.deleteEpic(id.get());
                exchange.sendResponseHeaders(204, 0);
            }
        } else if (splitPath.length == 2) {
            manager.deleteAllEpic();
            exchange.sendResponseHeaders(204, 0);
        } else {
            throw new NotFoundException("Invalid path");
        }
    }

    private void handlePostRequest(HttpExchange exchange, String[] splitPath) throws IOException {
        if (splitPath.length == 2) {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(body, Epic.class);
            int id = epic.getId();
            if (id != 0) {
                manager.updateTask(epic);
                sendText(exchange, 200, gson.toJson(manager.getEpic(id)));
            } else {
                sendText(exchange, 201, gson.toJson(manager.createEpic(epic)));
            }
        } else {
            throw new NotFoundException("Invalid path");
        }
    }

    private void handleGetRequest(HttpExchange exchange, String[] splitPath) throws IOException {
        Optional<Integer> id;
        if (splitPath.length == 4 && splitPath[3].equals("subtasks")) {
            id = getId(exchange);
            if (id.isPresent()) {
                sendText(exchange, 200, gson.toJson(manager.getEpicSubTasks(id.get())));
            }
        } else if (splitPath.length == 3) {
            id = getId(exchange);
            if (id.isPresent()) {
                sendText(exchange, 200, gson.toJson(manager.getEpic(id.get())));
            }
        } else if ((splitPath.length == 2)) {
            sendText(exchange, 200, gson.toJson(manager.getAllEpic()));
        } else {
            throw new NotFoundException("Invalid path");
        }
    }
}
