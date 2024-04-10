package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHandler implements PathHandler {

    public HistoryHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public String getPath() {
        return "history";
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                handleGetRequest(exchange);
                break;
            default:
                sendText(exchange, 405, "Unsupported method - " + method);
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        try (exchange) {
            sendText(exchange, 200, gson.toJson(manager.getHistory()));
        } catch (Exception e) {
            errorHandler.handle(exchange, e);
        }
    }
}
