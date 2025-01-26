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

            Task task = new Task(1, "Купить телевизор", "65 дюймов");
            manager.newTask(task);

            Epic epic = new Epic(2, "Сходить в магазин", "Купить продукты");
            manager.newEpic(epic);

            Subtask subtask = new Subtask(3, "Купить хлеб", "Можно без акции", 2);
            manager.newSubtask(subtask);

            manager = FileBackedTaskManager.loadFromFile(file);

            Assertions.assertEquals(1, manager.getTask(1).getId());
            Assertions.assertEquals(2, manager.getEpic(2).getId());
            Assertions.assertEquals(3, manager.getSubtask(3).getId());
            Assertions.assertEquals(2, manager.getSubtask(3).getEpicId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
