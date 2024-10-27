import java.util.ArrayList;
import java.util.Objects;

public class Task {
    private final int id;
    private String name;
    private String description;
    private Status status;

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        status = Status.NEW;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Задача N " + getId() +
                ", Название: " + getName() +
                ", Описание: " + getDescription() +
                ", Статус: " + getStatus();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

class Epic extends Task {
    ArrayList<Integer> idSubtask = new ArrayList<>();

    public Epic(int id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public String toString() {
        return "Задача N " + getId() +
                ", Название: " + getName() +
                ", Описание: " + getDescription() +
                ", Статус: " + getStatus() +
                ", ID подзадач: " + idSubtask.toString();
    }
}

class Subtask extends Task {
    private final int idEpic;

    public Subtask(int id, String name, String description, int idEpic) {
        super(id, name, description);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public String toString() {
        return "Задача N " + getId() +
                ", Название: " + getName() +
                ", Описание: " + getDescription() +
                ", Статус: " + getStatus() +
                ", ID эпика: " + getIdEpic();
    }
}
