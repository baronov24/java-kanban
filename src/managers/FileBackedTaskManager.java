package managers;

import enums.Status;
import enums.TaskType;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    void save() {
        StringBuilder sb = new StringBuilder();
        sb.append("id,type,name,status,description,startTime,duration,epic\n");

        for (Task task : getTasks()) {
            sb.append(task.toStringForFile()).append("\n");
        }

        for (Epic epic : getEpics()) {
            sb.append(epic.toStringForFile()).append("\n");
        }

        for (Subtask subtask : getSubtasks()) {
            sb.append(subtask.toStringForFile()).append("\n");
        }

        try {
            Files.writeString(file.toPath(), sb.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения...", e);
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
        TaskType type = TaskType.valueOf(array[1]);
        String name = array[2];
        Status status = Status.valueOf(array[3]);
        String description = array[4];
        LocalDateTime startTime = null;
        Duration duration = null;
        int epicId;

        if (type == TaskType.SUBTASK) {
            epicId = Integer.parseInt(array[7]);
        } else {
            epicId = -1;
        }

        if (type != TaskType.EPIC && !array[5].equals("null")) {
            startTime = LocalDateTime.parse(array[5]);
            duration = Duration.ofMinutes(Integer.parseInt(array[6]));
        }

        Task task;

        switch (type) {
            case TASK:
                task = new Task(id, name, description, startTime, duration);
                break;
            case EPIC:
                task = new Epic(id, name, description);
                break;
            case SUBTASK:
                task = new Subtask(id, name, description, startTime, duration, epicId);
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
                String[] array = line.split(",");
                TaskType type = TaskType.valueOf(array[1]);

                switch (type) {
                    case TASK:
                        manager.newTask(task);
                        break;
                    case EPIC:
                        manager.newEpic((Epic) task);
                        break;
                    case SUBTASK:
                        manager.newSubtask((Subtask) task);
                        break;
                    default:
                        throw new IllegalArgumentException("Неизвестный тип задачи...");
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки...", e);
        }

        return manager;
    }
}
