package test;

import managers.InMemoryTaskManager;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Task;

public class HistoryManagerTest {
    @Test
    public void add() {
        TaskManager manager = new InMemoryTaskManager();

        Task task = new Task(1, "Сходить в школу", "Получить 5-ку");
        manager.newTask(task);

        Assertions.assertEquals(0, Managers.getDefaultHistory().getHistory().size());

        manager.getSubtask(1);

        Assertions.assertEquals(1, Managers.getDefaultHistory().getHistory().size());
    }
}
