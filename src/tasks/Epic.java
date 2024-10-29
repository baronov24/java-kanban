package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> idSubtask;

    public Epic(String name, String description) {
        super(name, description);
        idSubtask = new ArrayList<>();
    }

    public ArrayList<Integer> getIdSubtask() {
        return idSubtask;
    }

    @Override
    public String toString() {
        return "Название: " + getName() +
                ", Описание: " + getDescription() +
                ", Статус: " + getStatus() +
                ", ID подзадач: " + idSubtask.toString();
    }
}
