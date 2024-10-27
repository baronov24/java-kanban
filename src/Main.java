public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task;
        Epic epic;
        Subtask subtask;

        task = new Task(++TaskManager.id, "Купить телевизор", "65 дюймов");
        taskManager.newTask(task);

        task = new Task(++TaskManager.id, "Прочитать книгу", "Кровь, пот и пиксели");
        taskManager.newTask(task);

        epic = new Epic(++TaskManager.id, "Сходить в магазин", "Купить продукты");
        int idEpic = taskManager.newEpic(epic);

        subtask = new Subtask(++TaskManager.id, "Купить хлеб", "Можно без акции", idEpic);
        taskManager.newSubtask(subtask, idEpic);

        subtask = new Subtask(++TaskManager.id, "Купить сыр", "Желательно по акции", idEpic);
        taskManager.newSubtask(subtask, idEpic);

        epic = new Epic(++TaskManager.id, "Убрать квартиру", "Грязные полы");
        idEpic = taskManager.newEpic(epic);

        subtask = new Subtask(++TaskManager.id, "Вымыть полы", "Влажная уборка", idEpic);
        taskManager.newSubtask(subtask, idEpic);

        taskManager.printList("Task");
        taskManager.printList("Epic");
        taskManager.printList("Subtask");

        int id = 1;
        taskManager.changeStatus(id, Status.IN_PROGRESS);

        id = 2;
        taskManager.changeStatus(id, Status.DONE);

        id = 4;
        taskManager.changeStatus(id, Status.DONE);

        id = 7;
        taskManager.changeStatus(id, Status.DONE);

        taskManager.printList("Task");
        taskManager.printList("Epic");
        taskManager.printList("Subtask");

        id = 1;
        taskManager.deleteTask(id);

        id = 3;
        taskManager.deleteTask(id);

        taskManager.printList("Task");
        taskManager.printList("Epic");
        taskManager.printList("Subtask");
    }
}
