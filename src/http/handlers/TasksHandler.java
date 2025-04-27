package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.TaskTimeOverlapException;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;
    Gson gson;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        int len = splitPath.length;

        switch (method) {
            case "GET":
                if (len == 2) getTasks(exchange);
                else if (len == 3) getTask(exchange, splitPath);
                else sendResponse(exchange, "Неверный синтаксис запроса...", 400);
                break;
            case "POST":
                if (len == 2) updateTask(exchange);
                else sendResponse(exchange, "Неверный синтаксис запроса...", 400);
                break;
            case "DELETE":
                if (len == 2) deleteTasks(exchange);
                else if (len == 3) deleteTask(exchange, splitPath);
                else sendResponse(exchange, "Неверный синтаксис запроса...", 400);
                break;
            default:
                sendResponse(exchange, "Неверный синтаксис запроса...", 400);
        }
    }

    private void getTasks(HttpExchange exchange) throws IOException {
        try {
            List<Task> tasks = taskManager.getTasks();
            String response = gson.toJson(tasks);
            sendResponse(exchange, response, 200);
        } catch (IOException e) {
            sendResponse(exchange, "Ошибка... Не найден список задач...", 404);
        }
    }

    private void getTask(HttpExchange exchange, String[] splitPath) throws IOException {
        int id = Integer.parseInt(splitPath[2]);

        try {
            Task task = taskManager.getTask(id);
            String response = gson.toJson(task);
            sendResponse(exchange, response, 200);
        } catch (IOException e) {
            sendResponse(exchange, "Ошибка... Задача с id " + id + " не найдена...", 404);
        }
    }

    private void updateTask(HttpExchange exchange) throws IOException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(body, Task.class);

            if (task.getId() == 0) {
                task.setId(taskManager.newId());
                taskManager.newTask(task);
                sendResponse(exchange, "Добавлена новая задача", 201);
            } else {
                if (taskManager.getTask(task.getId()) == null) {
                    sendResponse(exchange, "Ошибка... Задача с id " + task.getId() + " не найдена...",
                            404);
                } else {
                    taskManager.updateTask(task.getId(), task);
                    sendResponse(exchange, "Задача с id " + task.getId() + " успешно обновлена!", 201);
                }
            }
        } catch (TaskTimeOverlapException e) {
            sendResponse(exchange, "Обнаружено пересечение времени задач, операция прервана...", 406);
        }
    }

    private void deleteTasks(HttpExchange exchange) throws IOException {
        try {
            taskManager.clearTaskList();
            sendResponse(exchange, "Список задач очищен!", 200);
        } catch (IOException e) {
            sendResponse(exchange, "Ошибка... Не найден список задач...", 404);
        }
    }

    private void deleteTask(HttpExchange exchange, String[] splitPath) throws IOException {
        int id = Integer.parseInt(splitPath[2]);

        try {
            taskManager.deleteTask(id);
            sendResponse(exchange, "Задача с id " + id + " успешно удалена!", 200);
        } catch (IOException e) {
            sendResponse(exchange, "Ошибка... Задача с id " + id + " не найдена...", 404);
        }
    }
}
