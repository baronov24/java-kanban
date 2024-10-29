import enums.Status;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task;
        Epic epic;
        Subtask subtask;

        task = new Task("Купить телевизор", "65 дюймов");
        taskManager.newTask(task);

        task = new Task("Прочитать книгу", "Кровь, пот и пиксели");
        taskManager.newTask(task);

        epic = new Epic("Сходить в магазин", "Купить продукты");
        int idEpic = taskManager.newEpic(epic);

        subtask = new Subtask("Купить хлеб", "Можно без акции", idEpic);
        taskManager.newSubtask(subtask);

        subtask = new Subtask("Купить сыр", "Желательно по акции", idEpic);
        taskManager.newSubtask(subtask);

        epic = new Epic("Убрать квартиру", "Грязные полы");
        idEpic = taskManager.newEpic(epic);

        subtask = new Subtask("Вымыть полы", "Влажная уборка", idEpic);
        taskManager.newSubtask(subtask);

        int id = 1;
        taskManager.changeTaskStatus(id, Status.IN_PROGRESS);

        id = 2;
        taskManager.changeTaskStatus(id, Status.DONE);

        id = 3;
        taskManager.changeEpicStatus(id, Status.DONE);

        id = 7;
        taskManager.changeSubtaskStatus(id, Status.DONE);

        id = 1;
        taskManager.deleteTask(id);

        id = 3;
        taskManager.deleteEpic(id);
    }
}
