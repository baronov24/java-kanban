import enums.Status;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {
    //
    //
    //
    //
    //
    // дико извиняюсь, не заметил Ваших комментариев в коде,
    // отправил работу без данных исправлений, не проверяйте, пришлю новую версию
    //
    //
    //
    //
    //
    //
    //
    //

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task;
        Epic epic;
        Subtask subtask;

        task = new Task(++taskManager.id, "Купить телевизор", "65 дюймов");
        taskManager.newTask(task);

        task = new Task(++taskManager.id, "Прочитать книгу", "Кровь, пот и пиксели");
        taskManager.newTask(task);

        epic = new Epic(++taskManager.id, "Сходить в магазин", "Купить продукты");
        int idEpic = taskManager.newEpic(epic);

        subtask = new Subtask(++taskManager.id, "Купить хлеб", "Можно без акции", idEpic);
        taskManager.newSubtask(subtask, idEpic);

        subtask = new Subtask(++taskManager.id, "Купить сыр", "Желательно по акции", idEpic);
        taskManager.newSubtask(subtask, idEpic);

        epic = new Epic(++taskManager.id, "Убрать квартиру", "Грязные полы");
        idEpic = taskManager.newEpic(epic);

        subtask = new Subtask(++taskManager.id, "Вымыть полы", "Влажная уборка", idEpic);
        taskManager.newSubtask(subtask, idEpic);

        taskManager.printList("Tasks.Task");
        taskManager.printList("Tasks.Epic");
        taskManager.printList("Tasks.Subtask");

        int id = 1;
        taskManager.changeTaskStatus(id, Status.IN_PROGRESS);

        id = 2;
        taskManager.changeTaskStatus(id, Status.DONE);

        id = 3;
        taskManager.changeEpicStatus(id, Status.DONE);

        id = 7;
        taskManager.changeSubtaskStatus(id, Status.DONE);

        taskManager.printList("Tasks.Task");
        taskManager.printList("Tasks.Epic");
        taskManager.printList("Tasks.Subtask");

        id = 1;
        taskManager.deleteTask(id);

        id = 3;
        taskManager.deleteEpic(id);

        taskManager.printList("Tasks.Task");
        taskManager.printList("Tasks.Epic");
        taskManager.printList("Tasks.Subtask");
    }
}
