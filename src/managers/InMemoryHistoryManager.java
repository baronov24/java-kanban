package managers;

import other.Node;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    static class HistoryList {
        Map<Integer, Node> map = new HashMap<>();
        private Task head;
        private Task tail;

        private void linkLast(Task task) {
            Node node;

            if (map.containsKey(task.getId())) {
                removeNode(map.get(task.getId()));
            }

            if (map.isEmpty()) {
                head = task;
                tail = task;
                node = new Node(task, null);
            } else {
                map.get(tail.getId()).setNextTask(task);
                node = new Node(task, tail);
                tail = task;
            }

            map.put(task.getId(), node);
        }

        private void removeNode(Node node) {
            map.remove(node.getTask().getId());

            Task prevTask = node.getPrevTask();
            Task nextTask = node.getNextTask();

            if (prevTask == null) {
                head = nextTask;
            } else {
                map.get(prevTask.getId()).setNextTask(nextTask);
            }

            if (nextTask == null) {
                tail = prevTask;
            } else {
                map.get(nextTask.getId()).setPrevTask(prevTask);
            }
        }

        public List<Task> getTasks() {
            List<Task> answer = new ArrayList<>();
            Task task = head;

            while (task != null) {
                answer.add(task);
                task = map.get(task.getId()).getNextTask();
            }

            return answer;
        }
    }

    HistoryList history = new HistoryList();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        history.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    public void remove(int id) {
        history.removeNode(history.map.get(id));
    }
}
