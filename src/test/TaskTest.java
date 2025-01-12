package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

class TaskTest {
    @Test
    public void taskIsEqualTaskIfSameId() {
        Task task1 = new Task(1, "Задача 1", "Описание задачи 1");
        Task task2 = new Task(1, "Задача 2", "Описание задачи 2");

        Assertions.assertEquals(task1, task2);
    }

    @Test
    public void subtaskIsEqualEpicIfSameId() {
        Epic epic = new Epic(1, "Эпик", "Описание эпика");
        Subtask subtask = new Subtask(1, "Задача", "Описание задачи", epic);

        Assertions.assertEquals(subtask, epic);
    }
}