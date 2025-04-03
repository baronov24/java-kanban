import managers.FileBackedTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;

public class FileBackedTaskManagerTest {
    @Test
    public void testFile() {
        try {
            File file = File.createTempFile("test", ".csv");
            FileBackedTaskManager manager = new FileBackedTaskManager(file);

            Task task = new Task(1, "Купить телевизор", "65 дюймов", "01.01.2025, 12:00", 60);
            manager.newTask(task);

            Epic epic = new Epic(2, "Сходить в магазин", "Купить продукты");
            manager.newEpic(epic);

            Subtask subtask = new Subtask(3, "Купить хлеб", "Можно без акции", "02.01.2025, 12:00", 60, 2);
            manager.newSubtask(subtask);

            task = new Task(24, "Прочитать книгу", "Кровь пот и пиксели");
            manager.newTask(task);

            manager = FileBackedTaskManager.loadFromFile(file);

            System.out.println(manager.getTask(1).toString());
            System.out.println(manager.getTask(24).toString());
            System.out.println(manager.getEpic(2).toString());
            System.out.println(manager.getSubtask(3).toString());

            Assertions.assertEquals(1, manager.getTask(1).getId());
            Assertions.assertEquals(24, manager.getTask(24).getId());
            Assertions.assertEquals(2, manager.getEpic(2).getId());
            Assertions.assertEquals(3, manager.getSubtask(3).getId());
            Assertions.assertEquals(2, manager.getSubtask(3).getEpicId());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
