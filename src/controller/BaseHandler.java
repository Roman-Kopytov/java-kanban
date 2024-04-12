package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.ManagerSaveException;
import exception.NotFoundException;
import exception.ValidationException;
import service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public abstract class BaseHandler implements HttpHandler {

    TaskManager manager;
    ErrorHandler errorHandler;
    Gson gson;

    public BaseHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
        this.errorHandler = new ErrorHandler(gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            prepareResponse(exchange);
        } catch (ManagerSaveException e) {
            errorHandler.handle(exchange, e);
        } catch (NotFoundException e) {
            errorHandler.handle(exchange, e);
        } catch (ValidationException e) {
            errorHandler.handle(exchange, e);
        } catch (Exception e) {
            errorHandler.handle(exchange, e);
        } finally {
            exchange.close();
        }
    }

    abstract void prepareResponse(HttpExchange exchange) throws IOException;


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
