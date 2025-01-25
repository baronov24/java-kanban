package managers;

import enums.Status;
import enums.TypesOfTasks;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    void save() {
        StringBuilder sb = new StringBuilder();
        sb.append("id,type,name,status,description,epic\n");

        for (Task task : getTasks()) {
            sb.append(toString(task)).append("\n");
        }

        for (Epic epic : getEpics()) {
            sb.append(toString(epic)).append("\n");
        }

        for (Subtask subtask : getSubtasks()) {
            sb.append(toString(subtask)).append("\n");
        }

        try {
            Files.writeString(file.toPath(), sb.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения...", e);
        }
    }

    String toString(Task task) {
        if (task instanceof Subtask subtask) {
            return String.format("%d,SUBTASK,%s,%s,%s,%d", task.getId(), task.getName(), task.getStatus(), task.getDescription(), subtask.getEpicId());
        } else if (task instanceof Epic) {
            return String.format("%d,EPIC,%s,%s,%s,", task.getId(), task.getName(), task.getStatus(), task.getDescription());
        } else {
            return String.format("%d,TASK,%s,%s,%s,", task.getId(), task.getName(), task.getStatus(), task.getDescription());
        }
    }

    @Override
    public void newTask(Task task) {
        super.newTask(task);
        save();
    }

    @Override
    public void newEpic(Epic epic) {
        super.newEpic(epic);
        save();
    }

    @Override
    public void newSubtask(Subtask subtask) {
        super.newSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void clearTaskList() {
        super.clearTaskList();
        save();
    }

    @Override
    public void clearEpicList() {
        super.clearEpicList();
        save();
    }

    @Override
    public void clearSubtaskList() {
        super.clearSubtaskList();
        save();
    }

    @Override
    public void changeTaskStatus(int id, Status status) {
        super.changeTaskStatus(id, status);
        save();
    }

    @Override
    public void changeEpicStatus(int id, Status status) {
        super.changeEpicStatus(id, status);
        save();
    }

    @Override
    public void changeSubtaskStatus(int id, Status status) {
        super.changeSubtaskStatus(id, status);
        save();
    }

    @Override
    public void updateTask(int id, Task task) {
        super.updateTask(id, task);
        save();
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        super.updateEpic(id, epic);
        save();
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        super.updateSubtask(id, subtask);
        save();
    }

    static Task fromString(String value) {
        String[] array = value.split(",");
        int id = Integer.parseInt(array[0]);
        TypesOfTasks type = TypesOfTasks.valueOf(array[1]);
        String name = array[2];
        Status status = Status.valueOf(array[3]);
        String description = array[4];
        int epicId;

        if (type == TypesOfTasks.SUBTASK) {
            epicId = Integer.parseInt(array[5]);
        } else {
            epicId = -1;
        }

        Task task;

        switch (type) {
            case TASK:
                task = new Task(id, name, description);
                break;
            case EPIC:
                task = new Epic(id, name, description);
                break;
            case SUBTASK:
                task = new Subtask(id, name, description, epicId);
                break;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи...");
        }

        task.setStatus(status);

        return task;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try {
            List<String> lines = Files.readAllLines(Paths.get(file.getPath()));

            for (String line : lines.subList(1, lines.size())) {
                Task task = fromString(line);

                if (task instanceof Epic) {
                    manager.newEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.newSubtask((Subtask) task);
                } else {
                    manager.newTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки...", e);
        }

        return manager;
    }
}
