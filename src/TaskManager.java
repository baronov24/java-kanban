import java.util.HashMap;

public class TaskManager {
    public static int id;
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, Subtask> subtasks;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    public void newTask(Task task) {
        tasks.put(id, task);
    }

    public int newEpic(Epic epic) {
        epics.put(id, epic);

        return id;
    }

    public void newSubtask(Subtask subtask, int idEpic) {
        subtasks.put(id, subtask);

        epics.get(idEpic).idSubtask.add(id);
    }

    public void printList(String type) {
        switch (type) {
            case "Task":
                System.out.println("Список задач:");

                for (HashMap.Entry<Integer, Task> entry : tasks.entrySet()) {
                    Integer key = entry.getKey();

                    System.out.println(tasks.get(key).toString());
                }

                System.out.println();
                break;
            case "Epic":
                System.out.println("Список эпиков:");

                for (HashMap.Entry<Integer, Epic> entry : epics.entrySet()) {
                    Integer key = entry.getKey();

                    System.out.println(epics.get(key).toString());
                }

                System.out.println();
                break;
            case "Subtask":
                System.out.println("Список подзадач:");

                for (HashMap.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
                    Integer key = entry.getKey();

                    System.out.println(subtasks.get(key).toString());
                }

                System.out.println();
        }
    }

    public void changeStatus(int idTask, Status status) {
        if (tasks.containsKey(idTask)) {
            tasks.get(idTask).setStatus(status);
        } else if (epics.containsKey(idTask)) {
            System.out.println("Нельзя изменить статус эпика...");
        } else if (subtasks.containsKey(idTask)) {
            subtasks.get(idTask).setStatus(status);

            int idEpic = subtasks.get(idTask).getIdEpic();

            switch (status) {
                case IN_PROGRESS:
                    epics.get(idEpic).setStatus(status);
                    break;
                case DONE:
                    boolean done = true;

                    for (HashMap.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
                        Subtask value = entry.getValue();

                        if (value.getIdEpic() == idEpic && value.getStatus() != Status.DONE) {
                            done = false;
                            break;
                        }
                    }

                    if (done) {
                        epics.get(idEpic).setStatus(status);
                    } else {
                        epics.get(idEpic).setStatus(Status.IN_PROGRESS);
                    }
            }
        }
    }

    public void deleteTask(int idTask) {
        if (tasks.containsKey(idTask)) {
            tasks.remove(idTask);
        } else if (epics.containsKey(idTask)) {
            for (Integer key : epics.get(idTask).idSubtask) {
                subtasks.remove(key);
            }

            epics.remove(idTask);
        } else if (subtasks.containsKey(idTask)) {
            subtasks.remove(idTask);
        }
    }
}
