package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.TaskTimeOverlapException;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;
    Gson gson;

    public SubtasksHandler(TaskManager taskManager) {
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
                if (len == 2) getSubtasks(exchange);
                else if (len == 3) getSubtask(exchange, splitPath);
                else sendResponse(exchange, "Неверный синтаксис запроса...", 400);
                break;
            case "POST":
                if (len == 2) updateSubtask(exchange);
                else sendResponse(exchange, "Неверный синтаксис запроса...", 400);
                break;
            case "DELETE":
                if (len == 2) deleteSubtasks(exchange);
                else if (len == 3) deleteSubtask(exchange, splitPath);
                else sendResponse(exchange, "Неверный синтаксис запроса...", 400);
                break;
            default:
                sendResponse(exchange, "Неверный синтаксис запроса...", 400);
        }
    }

    private void getSubtasks(HttpExchange exchange) throws IOException {
        try {
            List<Subtask> subtasks = taskManager.getSubtasks();
            String response = gson.toJson(subtasks);
            sendResponse(exchange, response, 200);
        } catch (IOException e) {
            sendResponse(exchange, "Ошибка... Не найден список подзадач...", 404);
        }
    }

    private void getSubtask(HttpExchange exchange, String[] splitPath) throws IOException {
        int id = Integer.parseInt(splitPath[2]);

        try {
            Subtask subtask = taskManager.getSubtask(id);
            String response = gson.toJson(subtask);
            sendResponse(exchange, response, 200);
        } catch (IOException e) {
            sendResponse(exchange, "Ошибка... Подзадача с id " + id + " не найдена...", 404);
        }
    }

    private void updateSubtask(HttpExchange exchange) throws IOException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtask = gson.fromJson(body, Subtask.class);

            if (subtask.getId() == 0) {
                subtask.setId(taskManager.newId());
                taskManager.newSubtask(subtask);
                sendResponse(exchange, "Добавлена новая подзадача", 201);
            } else {
                if (taskManager.getSubtask(subtask.getId()) == null) {
                    sendResponse(exchange, "Ошибка... Подзадача с id " + subtask.getId() + " не найдена...",
                            404);
                } else {
                    taskManager.updateSubtask(subtask.getId(), subtask);
                    sendResponse(exchange, "Подзадача с id " + subtask.getId() + " успешно обновлена!",
                            201);
                }
            }
        } catch (TaskTimeOverlapException e) {
            sendResponse(exchange, "Обнаружено пересечение времени задач, операция прервана...", 406);
        }
    }

    private void deleteSubtasks(HttpExchange exchange) throws IOException {
        try {
            taskManager.clearSubtaskList();
            sendResponse(exchange, "Список подзадач очищен!", 200);
        } catch (IOException e) {
            sendResponse(exchange, "Ошибка... Не найден список подзадач...", 404);
        }
    }

    private void deleteSubtask(HttpExchange exchange, String[] splitPath) throws IOException {
        int id = Integer.parseInt(splitPath[2]);

        try {
            taskManager.deleteSubtask(id);
            sendResponse(exchange, "Подзадача с id " + id + " успешно удалена!", 200);
        } catch (IOException e) {
            sendResponse(exchange, "Ошибка... Подзадача с id " + id + " не найдена...", 404);
        }
    }
}
