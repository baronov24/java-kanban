package tasks;

public class Subtask extends Task {
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
