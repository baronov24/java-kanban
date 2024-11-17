package tasks;

public class Subtask extends Task {
    private final Epic parent;

    public Subtask(int id, String name, String description, Epic parent) {
        super(id, name, description);
        this.parent = parent;
    }

    public Epic getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return "ID: " + getId() +
                ", Название: " + getName() +
                ", Описание: " + getDescription() +
                ", Статус: " + getStatus() +
                ", ID эпика: " + getParent().getId();
    }
}
