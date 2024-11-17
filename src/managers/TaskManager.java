package managers;

import enums.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    void newTask(Task task);

    void newEpic(Epic epic);

    void newSubtask(Subtask subtask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    void clearTaskList();

    void clearEpicList();

    void clearSubtaskList();

    void changeTaskStatus(int id, Status status);

    void changeEpicStatus(int id, Status status);

    void changeSubtaskStatus(int id, Status status);

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    List<Subtask> getEpicSubtasks(int id);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    void updateTask(int id, Task task);

    void updateEpic(int id, Epic epic);

    void updateSubtask(int id, Subtask subtask);

    void updateEpicStatus(int id);
}
