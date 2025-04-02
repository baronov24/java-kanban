package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(int id, String name, String description, int epicId) {
        super(id, name, description);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, String startTime, long duration, int epicId) {
        super(id, name, description, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, LocalDateTime startTime, Duration duration, int epicId) {
        super(id, name, description, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "ID: " + getId() +
                ", Название: " + getName() +
                ", Описание: " + getDescription() +
                ", Статус: " + getStatus() +
                ", Время старта: " + getStartTime() +
                ", Продолжительность (мин.): " + (getDuration().getSeconds() / 60) +
                ", ID эпика: " + getEpicId();
    }
}
