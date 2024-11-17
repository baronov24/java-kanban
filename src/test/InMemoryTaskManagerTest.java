package test;

import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class InMemoryTaskManagerTest {
    @Test
    public void addNewTask() {
        TaskManager manager = new InMemoryTaskManager();

        Task task = new Task(1, "Купить телевизор", "65 дюймов");
        manager.newTask(task);

        Epic epic = new Epic(2, "Сходить в магазин", "Купить продукты");
        manager.newEpic(epic);

        Subtask subtask = new Subtask(3, "Купить хлеб", "Можно без акции", epic);
        manager.newSubtask(subtask);

        Assertions.assertEquals(task, manager.getTask(1));
        Assertions.assertEquals(epic, manager.getEpic(2));
        Assertions.assertEquals(subtask, manager.getSubtask(3));

        Assertions.assertEquals(1, manager.getTask(1).getId());
        Assertions.assertEquals("Купить телевизор", manager.getTask(1).getName());
        Assertions.assertEquals("65 дюймов", manager.getTask(1).getDescription());
    }
}
