package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    List<Subtask> listOfSubtasks;
    LocalDateTime endTime;

    public Epic(int id, String name, String description) {
        super(id, name, description);
        listOfSubtasks = new ArrayList<>();
    }

    public Epic(String name, String description) {
        super(name, description);
        listOfSubtasks = new ArrayList<>();
    }

    public List<Subtask> getListOfSubtasks() {
        return listOfSubtasks;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
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
                ", Время старта: " + getStartTime() +
                ", Продолжительность (мин.): " + (getDuration().getSeconds() / 60) +
                ", ID подзадач: " + keysOfSubtasks;
    }

    @Override
    public String toStringForFile() {
        return String.format("%d,EPIC,%s,%s,%s",
                getId(), getName(), getStatus(), getDescription());
    }
}
