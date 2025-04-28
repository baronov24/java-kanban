package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enums.HttpMethod;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.Set;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;
    Gson gson;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpMethod httpMethod = HttpMethod.valueOf(exchange.getRequestMethod());
        String[] splitPath = exchange.getRequestURI().getPath().split("/");

        if (httpMethod == HttpMethod.GET && splitPath.length == 2) {
            try {
                Set<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

                if (prioritizedTasks == null || prioritizedTasks.isEmpty()) {
                    sendResponse(exchange, "Список приоритетных задач не найден или пустой...", 404);
                }

                String response = gson.toJson(prioritizedTasks);
                sendResponse(exchange, response, 200);
            } catch (IOException e) {
                sendResponse(exchange, "Неверный синтаксис запроса...", 400);
            }
        }
    }
}
