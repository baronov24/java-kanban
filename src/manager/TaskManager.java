package manager;

import enums.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id;
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

    public void newEpic(Epic epic) {
        id++;
        epics.put(id, epic);
    }

    public void newSubtask(Subtask subtask) {
        id++;
        subtasks.put(id, subtask);

        epics.get(subtask.getIdEpic()).getIdSubtask().add(id);
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
        int idEpic = subtasks.get(idSubtask).getIdEpic();

        subtasks.remove(idSubtask);

        boolean statusNew = true;
        boolean statusDone = true;
        ArrayList<Integer> tempArray = epics.get(idEpic).getIdSubtask();

        tempArray.remove((Integer) idSubtask);

        for (Integer key : tempArray) {
            if (subtasks.get(key).getStatus() == Status.IN_PROGRESS) {
                statusNew = false;
                statusDone = false;
                break;
            } else if (subtasks.get(key).getStatus() == Status.NEW) {
                statusDone = false;
            } else if (subtasks.get(key).getStatus() == Status.DONE) {
                statusNew = false;
            }
        }

        if (statusNew) {
            epics.get(idEpic).setStatus(Status.NEW);
        } else if (statusDone) {
            epics.get(idEpic).setStatus(Status.DONE);
        } else {
            epics.get(idEpic).setStatus(Status.IN_PROGRESS);
        }
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

        for (HashMap.Entry<Integer, Epic> entry : epics.entrySet()) {
            Integer key = entry.getKey();

            epics.get(key).getIdSubtask().clear();
            epics.get(key).setStatus(Status.NEW);
        }
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

    public HashMap<Integer, Task> getListTask() {
        return tasks;
    }

    public HashMap<Integer, Epic> getListEpic() {
        return epics;
    }

    public HashMap<Integer, Subtask> getListSubtask() {
        return subtasks;
    }

    public void updateTask(int idTask, Task task) {
        tasks.put(idTask, task);
    }

    public void updateEpic(int idEpic, Epic epic) {
        epics.put(idEpic, epic);
    }

    public void updateSubtask(int idSubtask, Subtask subtask) {
        subtasks.put(idSubtask, subtask);
    }
}
