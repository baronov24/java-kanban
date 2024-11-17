package managers;

public class Managers {
    TaskManager manager = new InMemoryTaskManager();
    public static HistoryManager history = new InMemoryHistoryManager();

    public TaskManager getDefault() {
        return manager;
    }

    public static HistoryManager getDefaultHistory() {
        return history;
    }
}
