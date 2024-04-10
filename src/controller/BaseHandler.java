package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class BaseHandler {

    TaskManager manager;
    ErrorHandler errorHandler;
    Gson gson;

    public BaseHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
        this.errorHandler = new ErrorHandler(gson);
    }


    void sendText(HttpExchange httpExchange, int statusCode, String json) throws IOException {
        byte[] response = json.getBytes(StandardCharsets.UTF_8);
        httpExchange.sendResponseHeaders(statusCode, response.length);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response);
        }
    }


    Optional<Integer> getId(HttpExchange httpExchange) {
        String[] splitPath = httpExchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(splitPath[2]));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

}
