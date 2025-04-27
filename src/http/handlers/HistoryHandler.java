package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;
    Gson gson;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] splitPath = exchange.getRequestURI().getPath().split("/");

        if (method.equals("GET") && splitPath.length == 2) {
            try {
                List<Task> history = taskManager.getHistory();

                if (history == null || history.isEmpty()) {
                    sendResponse(exchange, "История не найдена или пустая...", 404);
                }

                String response = gson.toJson(history);
                sendResponse(exchange, response, 200);
            } catch (IOException e) {
                sendResponse(exchange, "Неверный синтаксис запроса...", 400);
            }
        }
    }
}
