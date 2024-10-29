package manager;

import enums.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public int id;
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, Subtask> subtasks;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    public void newTask(Task task) {
        id++;
        tasks.put(id, task);
    }

    public int newEpic(Epic epic) {
        id++;
        epics.put(id, epic);

        return id;
    }

    public void newSubtask(Subtask subtask, int idEpic) {
        id++;
        subtasks.put(id, subtask);

        epics.get(idEpic).getIdSubtask().add(id);
    }

    public void deleteTask(int idTask) {
        tasks.remove(idTask);
    }

    public void deleteEpic(int idEpic) {
        if (epics.containsKey(idEpic)) {
            for (Integer key : epics.get(idEpic).getIdSubtask()) {
                subtasks.remove(key);
            }

            epics.remove(idEpic);
        }
    }

    public void deleteSubtask(int idSubtask) {
        subtasks.remove(idSubtask);
    }

    public void clearTaskList() {
        tasks.clear();
    }

    public void clearEpicList() {
        epics.clear();
        subtasks.clear();
    }

    public void clearSubtaskList() {
        subtasks.clear();
    }

    public void changeTaskStatus(int idTask, Status status) {
        if (tasks.containsKey(idTask)) {
            tasks.get(idTask).setStatus(status);
        }
    }

    public void changeEpicStatus(int idEpic, Status status) {
        if (epics.containsKey(idEpic)) {
            epics.get(idEpic).setStatus(status);

            if (status == Status.DONE) {
                for (Integer idSubtask : epics.get(idEpic).getIdSubtask()) {
                    subtasks.get(idSubtask).setStatus(status);
                }
            }
        }
    }

    public void changeSubtaskStatus(int idSubtask, Status status) {
        if (subtasks.containsKey(idSubtask)) {
            subtasks.get(idSubtask).setStatus(status);

            int idEpic = subtasks.get(idSubtask).getIdEpic();

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

    public Task getTask(int idTask) {
        return tasks.get(idTask);
    }

    public Epic getEpic(int idEpic) {
        return epics.get(idEpic);
    }

    public Subtask getSubtask(int idSubtask) {
        return subtasks.get(idSubtask);
    }

    public ArrayList<Subtask> getListOfSubtasksByEpic(int idEpic) {
        ArrayList<Subtask> answer = new ArrayList<>();

        for (HashMap.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
            Integer key = entry.getKey();

            if (subtasks.get(key).getIdEpic() == idEpic) {
                answer.add(subtasks.get(key));
            }
        }

        return answer;
    }

    public void printList(String type) {
        switch (type) {
            case "Tasks.Task":
                System.out.println("Список задач:");

                for (HashMap.Entry<Integer, Task> entry : tasks.entrySet()) {
                    Integer key = entry.getKey();

                    System.out.println(tasks.get(key).toString());
                }

                System.out.println();
                break;
            case "Tasks.Epic":
                System.out.println("Список эпиков:");

                for (HashMap.Entry<Integer, Epic> entry : epics.entrySet()) {
                    Integer key = entry.getKey();

                    System.out.println(epics.get(key).toString());
                }

                System.out.println();
                break;
            case "Tasks.Subtask":
                System.out.println("Список подзадач:");

                for (HashMap.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
                    Integer key = entry.getKey();

                    System.out.println(subtasks.get(key).toString());
                }

                System.out.println();
        }
    }
}
