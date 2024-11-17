package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    List<Subtask> listOfSubtasks;

    public Epic(int id, String name, String description) {
        super(id, name, description);
        listOfSubtasks = new ArrayList<>();
    }

    public List<Subtask> getListOfSubtasks() {
        return listOfSubtasks;
    }

    @Override
    public String toString() {
        StringBuilder keysOfSubtasks = new StringBuilder();

        for (int i = 0; i < listOfSubtasks.size(); i++) {
            if (i == 0) {
                keysOfSubtasks.append(listOfSubtasks.get(i).getId());
            } else {
                keysOfSubtasks.append(", ").append(listOfSubtasks.get(i).getId());
            }
        }

        return "ID: " + getId() +
                ", Название: " + getName() +
                ", Описание: " + getDescription() +
                ", Статус: " + getStatus() +
                ", ID подзадач: " + keysOfSubtasks;
    }
}
