package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enums.HttpMethod;
import exceptions.TaskTimeOverlapException;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;
    Gson gson;

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // HttpMethod вроде как есть в Spring, а я еще не имел с ним дела... сделал через enum
        HttpMethod httpMethod = HttpMethod.valueOf(exchange.getRequestMethod());
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        int len = splitPath.length;

        switch (httpMethod) {
            case GET:
                if (len == 2) {
                    getEpics(exchange);
                } else if (len == 3) {
                    getEpic(exchange, splitPath);
                } else if (len == 4) {
                    getEpicSubtasks(exchange, splitPath);
                } else {
                    sendResponse(exchange, "Неверный синтаксис запроса...", 400);
                }

                break;
            case POST:
                if (len == 2) {
                    updateEpic(exchange);
                } else {
                    sendResponse(exchange, "Неверный синтаксис запроса...", 400);
                }

                break;
            case DELETE:
                if (len == 2) {
                    deleteEpics(exchange);
                } else if (len == 3) {
                    deleteEpic(exchange, splitPath);
                } else {
                    sendResponse(exchange, "Неверный синтаксис запроса...", 400);
                }

                break;
            default:
                sendResponse(exchange, "Неверный синтаксис запроса...", 400);
        }
    }

    private void getEpics(HttpExchange exchange) throws IOException {
        try {
            List<Epic> epics = taskManager.getEpics();
            String response = gson.toJson(epics);
            sendResponse(exchange, response, 200);
        } catch (IOException e) {
            sendResponse(exchange, "Ошибка... Не найден список эпиков...", 404);
        }
    }

    private void getEpic(HttpExchange exchange, String[] splitPath) throws IOException {
        int id = Integer.parseInt(splitPath[2]);

        try {
            Epic epic = taskManager.getEpic(id);
            String response = gson.toJson(epic);
            sendResponse(exchange, response, 200);
        } catch (IOException e) {
            sendResponse(exchange, "Ошибка... Эпик с id " + id + " не найден...", 404);
        }
    }

    private void getEpicSubtasks(HttpExchange exchange, String[] splitPath) throws IOException {
        int id = Integer.parseInt(splitPath[2]);

        if (taskManager.getEpic(id) == null) {
            sendResponse(exchange, "Ошибка... Эпик с id " + id + " не найден...", 404);
        }

        try {
            List<Subtask> subtasks = taskManager.getEpicSubtasks(id);
            String response = gson.toJson(subtasks);
            sendResponse(exchange, response, 200);
        } catch (IOException e) {
            sendResponse(exchange, "Не найден список подзадач у эпика с id " + id, 404);
        }
    }

    private void updateEpic(HttpExchange exchange) throws IOException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(body, Epic.class);

            if (epic.getId() == 0) {
                epic.setId(taskManager.newId());
                taskManager.newEpic(epic);
                sendResponse(exchange, "Добавлен новый эпик", 201);
            } else {
                if (taskManager.getEpic(epic.getId()) == null) {
                    sendResponse(exchange, "Ошибка... Эпик с id " + epic.getId() + " не найден...",
                            404);
                } else {
                    taskManager.updateEpic(epic.getId(), epic);
                    sendResponse(exchange, "Эпик с id " + epic.getId() + " успешно обновлен!", 201);
                }
            }
        } catch (TaskTimeOverlapException e) {
            sendResponse(exchange, "Обнаружено пересечение времени задач, операция прервана...", 406);
        }
    }

    private void deleteEpics(HttpExchange exchange) throws IOException {
        try {
            taskManager.clearEpicList();
            sendResponse(exchange, "Список эпиков очищен!", 200);
        } catch (IOException e) {
            sendResponse(exchange, "Ошибка... Не найден список эпиков...", 404);
        }
    }

    private void deleteEpic(HttpExchange exchange, String[] splitPath) throws IOException {
        int id = Integer.parseInt(splitPath[2]);

        try {
            taskManager.deleteEpic(id);
            sendResponse(exchange, "Эпик с id " + id + " успешно удален!", 200);
        } catch (IOException e) {
            sendResponse(exchange, "Ошибка... Эпик с id " + id + " не найден...", 404);
        }
    }
}
