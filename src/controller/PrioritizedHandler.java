package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import service.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHandler {


    public PrioritizedHandler(TaskManager manager, Gson gson) {
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
            default:
                sendText(exchange, 405, "Unsupported method - " + method);
        }
    }

    private void handleGetRequest(HttpExchange exchange, String[] splitPath) throws IOException {
        if (splitPath.length == 2) {
            sendText(exchange, 200, gson.toJson(manager.getHistory()));
        } else {
            throw new NotFoundException("Invalid path");
        }
    }
}
