package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.BadRequest;
import exception.ManagerSaveException;
import exception.NotFoundException;
import exception.ValidationException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ErrorHandler {
    final Gson gson;

    public ErrorHandler(Gson gson) {
        this.gson = gson;
    }

    public void handle(HttpExchange httpExchange, ManagerSaveException e) throws IOException {
        e.printStackTrace();
        writeResponse(httpExchange, 400, "");
    }

    public void handle(HttpExchange httpExchange, NotFoundException e) throws IOException {
        e.printStackTrace();
        writeResponse(httpExchange, 404, "");
    }

    public void handle(HttpExchange httpExchange, ValidationException e) throws IOException {
        e.printStackTrace();
        writeResponse(httpExchange, 406, "");
    }

    public void handle(HttpExchange httpExchange, Exception e) throws IOException {
        e.printStackTrace();
        writeResponse(httpExchange, 500, "");
    }

    public void handle(HttpExchange httpExchange, BadRequest e) throws IOException {
        e.printStackTrace();
        writeResponse(httpExchange, 400, "");
    }

    private void writeResponse(HttpExchange httpExchange, int statusCode, String json) throws IOException {
        byte[] response = json.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(statusCode, response.length);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response);
        }
    }
}
