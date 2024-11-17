import managers.InMemoryTaskManager;
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new InMemoryTaskManager();
        Task task;
        Epic epic;
        Subtask subtask;

        task = new Task(1, "Купить телевизор", "65 дюймов");
        manager.newTask(task);

        task = new Task(2, "Прочитать книгу", "Кровь, пот и пиксели");
        manager.newTask(task);

        epic = new Epic(3, "Сходить в магазин", "Купить продукты");
        manager.newEpic(epic);

        subtask = new Subtask(4, "Купить хлеб", "Можно без акции", epic);
        manager.newSubtask(subtask);

        subtask = new Subtask(5, "Купить сыр", "Желательно по акции", epic);
        manager.newSubtask(subtask);

        epic = new Epic(6, "Убрать квартиру", "Грязные полы");
        manager.newEpic(epic);

        subtask = new Subtask(7, "Вымыть полы", "Влажная уборка", epic);
        manager.newSubtask(subtask);

        manager.getTask(1);

        printAllTasks(manager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : Managers.getDefaultHistory().getHistory()) {
            System.out.println(task);
        }
    }
}
