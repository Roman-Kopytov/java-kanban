package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class EpicHandler extends BaseHandler implements PathHandler {
    public EpicHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public String getPath() {
        return "epic";
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
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
                try (exchange) {
                    manager.deleteEpic(id.get());
                    exchange.sendResponseHeaders(204, 0);
                } catch (Exception e) {
                    errorHandler.handle(exchange, e);
                }
            }
        } else if (splitPath.length == 2) {
            try (exchange) {
                manager.deleteAllEpic();
                exchange.sendResponseHeaders(204, 0);
            } catch (Exception e) {
                errorHandler.handle(exchange, e);
            }
        }
    }

    private void handlePostRequest(HttpExchange exchange, String[] splitPath) throws IOException {
        if (splitPath.length == 2) {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(body, Epic.class);
            int id = epic.getId();
            if (id != 0) {
                try (exchange) {
                    manager.updateTask(epic);
                    sendText(exchange, 200, gson.toJson(manager.getTask(id)));
                } catch (Exception e) {
                    errorHandler.handle(exchange, e);
                }
            } else {
                try (exchange) {
                    sendText(exchange, 201, gson.toJson(manager.createTask(epic)));
                } catch (Exception e) {
                    errorHandler.handle(exchange, e);
                }
            }
        }
    }

    private void handleGetRequest(HttpExchange exchange, String[] splitPath) throws IOException {
        Optional<Integer> id = getId(exchange);
        if (splitPath.length == 4 && splitPath[3].equals("subtasks")) {
            if (id.isPresent()) {
                try (exchange) {
                    sendText(exchange, 200, gson.toJson(manager.getEpicSubTasks(id.get())));
                } catch (Exception e) {
                    errorHandler.handle(exchange, e);
                }
            }
        } else if (splitPath.length == 3) {
            if (id.isPresent()) {
                try (exchange) {
                    sendText(exchange, 200, gson.toJson(manager.getEpic(id.get())));
                } catch (Exception e) {
                    errorHandler.handle(exchange, e);
                }
            }
        } else if ((splitPath.length == 2)) {
            try (exchange) {
                sendText(exchange, 200, gson.toJson(manager.getAllEpic()));
            } catch (Exception e) {
                errorHandler.handle(exchange, e);
            }
        }
    }
}
