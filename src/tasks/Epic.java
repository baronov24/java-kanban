package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> idSubtask = new ArrayList<>();

    public Epic(int id, String name, String description) {
        super(id, name, description);
    }

    public ArrayList<Integer> getIdSubtask() {
        return idSubtask;
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
