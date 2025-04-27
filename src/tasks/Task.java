package tasks;

import enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private int id;
    private String name;
    private String description;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        status = Status.NEW;
    }

    public Task(int id, String name, String description, String startTime, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        status = Status.NEW;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"));
    }

    public Task(int id, String name, String description, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        status = Status.NEW;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        if (getStartTime() == null) {
            return "ID: " + getId() +
                    ", Название: " + getName() +
                    ", Описание: " + getDescription() +
                    ", Статус: " + getStatus();
        } else {
            return "ID: " + getId() +
                    ", Название: " + getName() +
                    ", Описание: " + getDescription() +
                    ", Статус: " + getStatus() +
                    ", Время старта: " + getStartTime() +
                    ", Продолжительность (мин.): " + (getDuration().getSeconds() / 60);
        }
    }

    public String toStringForFile() {
        if (getStartTime() == null) {
            return String.format("%d,TASK,%s,%s,%s,null,null",
                    getId(), getName(), getStatus(), getDescription());
        } else {
            return String.format("%d,TASK,%s,%s,%s,%s,%d",
                    getId(), getName(), getStatus(), getDescription(),
                    getStartTime(), getDuration().getSeconds() / 60);
        }
    }
}
