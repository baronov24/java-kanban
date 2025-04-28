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

    public Subtask(String name, String description, LocalDateTime startTime, Duration duration, int epicId) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        if (getStartTime() == null) {
            return "ID: " + getId() +
                    ", Название: " + getName() +
                    ", Описание: " + getDescription() +
                    ", Статус: " + getStatus() +
                    ", ID эпика: " + getEpicId();
        } else {
            return "ID: " + getId() +
                    ", Название: " + getName() +
                    ", Описание: " + getDescription() +
                    ", Статус: " + getStatus() +
                    ", Время старта: " + getStartTime() +
                    ", Продолжительность (мин.): " + (getDuration().getSeconds() / 60) +
                    ", ID эпика: " + getEpicId();
        }
    }

    @Override
    public String toStringForFile() {
        if (getStartTime() == null) {
            return String.format("%d,SUBTASK,%s,%s,%s,null,null,%d",
                    getId(), getName(), getStatus(), getDescription(), getEpicId());
        } else {
            return String.format("%d,SUBTASK,%s,%s,%s,%s,%d,%d",
                    getId(), getName(), getStatus(), getDescription(),
                    getStartTime(), getDuration().getSeconds() / 60, getEpicId());
        }
    }
}
