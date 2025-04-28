package managers;

import enums.Status;
import exceptions.TaskTimeOverlapException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, Subtask> subtasks;
    HistoryManager history = Managers.getDefaultHistory();
    Set<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime).thenComparingInt(Task::getId));
    }

    @Override
    public void newTask(Task task) {
        if (!checkTime(task)) {
            throw new TaskTimeOverlapException("Ошибка! Обнаружено пересечение времени задач, операция прервана...");
        }

        tasks.put(task.getId(), task);

        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void newEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void newSubtask(Subtask subtask) {
        if (subtask.getEpicId() == subtask.getId()) {
            return;
        }

        if (!checkTime(subtask)) {
            throw new TaskTimeOverlapException("Ошибка! Обнаружено пересечение времени задач, операция прервана...");
        }

        subtasks.put(subtask.getId(), subtask);

        epics.get(subtask.getEpicId()).getListOfSubtasks().add(subtask);

        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
            updateEpicTime(subtask.getEpicId());
        }
    }

    @Override
    public void deleteTask(int id) {
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            for (int i = 0; i < epics.get(id).getListOfSubtasks().size(); i++) {
                subtasks.remove(epics.get(id).getListOfSubtasks().get(i).getId());
            }

            epics.remove(id);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        epics.get(subtasks.get(id).getEpicId()).getListOfSubtasks().remove(subtasks.get(id));
        prioritizedTasks.remove(subtasks.get(id));
        subtasks.remove(id);

        updateEpicStatus(id);
    }

    @Override
    public void clearTaskList() {
        for (Task task : tasks.values()) {
            prioritizedTasks.remove(task);
        }

        tasks.clear();
    }

    @Override
    public void clearEpicList() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubtaskList() {
        for (Subtask subtask : subtasks.values()) {
            prioritizedTasks.remove(subtask);
        }

        subtasks.clear();

        for (HashMap.Entry<Integer, Epic> entry : epics.entrySet()) {
            Integer key = entry.getKey();

            epics.get(key).getListOfSubtasks().clear();
            epics.get(key).setStatus(Status.NEW);
        }
    }

    @Override
    public void changeTaskStatus(int id, Status status) {
        if (tasks.containsKey(id)) {
            tasks.get(id).setStatus(status);
        }
    }

    //не учтен вариант если все подзадачи со статусом DONE,
    //но мы принудительно меняем статус эпика на NEW или IN_PROGRESS
    @Override
    public void changeEpicStatus(int id, Status status) {
        if (epics.containsKey(id)) {
            epics.get(id).setStatus(status);

            if (status == Status.DONE) {
                for (Subtask subtask : epics.get(id).getListOfSubtasks()) {
                    subtask.setStatus(Status.DONE);
                }
            }
        }
    }

    @Override
    public void changeSubtaskStatus(int id, Status status) {
        if (subtasks.containsKey(id)) {
            subtasks.get(id).setStatus(status);

            updateEpicStatus(subtasks.get(id).getEpicId());
        }
    }

    @Override
    public Task getTask(int id) {
        history.add(tasks.get(id));

        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        history.add(epics.get(id));

        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        history.add(subtasks.get(id));

        return subtasks.get(id);
    }

    @Override
    public List<Subtask> getEpicSubtasks(int id) {
        return epics.get(id).getListOfSubtasks();
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void updateTask(int id, Task task) {
        tasks.put(id, task);
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        epics.put(id, epic);
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        subtasks.put(id, subtask);

        updateEpicStatus(subtask.getEpicId());
    }

    @Override
    public void updateEpicStatus(int id) {
        boolean statusNew = true;
        boolean statusDone = true;
        List<Subtask> tempArray = epics.get(id).getListOfSubtasks();

        for (int i = 0; i < tempArray.size(); i++) {
            if (tempArray.get(i).getStatus() == Status.IN_PROGRESS) {
                statusNew = false;
                statusDone = false;
                break;
            } else if (tempArray.get(i).getStatus() == Status.NEW) {
                statusDone = false;

                if (!statusNew) {
                    break;
                }
            } else if (tempArray.get(i).getStatus() == Status.DONE) {
                statusNew = false;

                if (!statusDone) {
                    break;
                }
            }
        }

        if (statusNew) {
            epics.get(id).setStatus(Status.NEW);
        } else if (statusDone) {
            epics.get(id).setStatus(Status.DONE);
        } else {
            epics.get(id).setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    public void updateEpicTime(int id) {
        List<Subtask> listOfSubtasks = epics.get(id).getListOfSubtasks();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        Duration duration = Duration.ofMinutes(0);

        for (int i = 0; i < listOfSubtasks.size(); i++) {
            if (startTime == null || startTime.isAfter(listOfSubtasks.get(i).getStartTime())) {
                startTime = listOfSubtasks.get(i).getStartTime();
            }

            if (endTime == null || endTime.isBefore(listOfSubtasks.get(i).getEndTime())) {
                endTime = listOfSubtasks.get(i).getEndTime();
            }

            duration = duration.plus(listOfSubtasks.get(i).getDuration());
        }

        epics.get(id).setDuration(duration);
        epics.get(id).setStartTime(startTime);
        epics.get(id).setEndTime(endTime);
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public boolean checkTime(Task task) {
        if (task.getStartTime() == null) {
            return true;
        }

        for (Task taskPriority : prioritizedTasks) {
            if (!(task.getStartTime().isAfter(taskPriority.getEndTime()) ||
                    task.getEndTime().isBefore(taskPriority.getStartTime()))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int newId() {
        int max = 0;

        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            if (max < entry.getKey()) {
                max = entry.getKey();
            }
        }

        for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
            if (max < entry.getKey()) {
                max = entry.getKey();
            }
        }

        for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
            if (max < entry.getKey()) {
                max = entry.getKey();
            }
        }

        return max + 1;
    }
}
