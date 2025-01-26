package other;

import tasks.Task;

public class Node {
    private Task task;
    private Task prevTask;
    private Task nextTask;

    public Node(Task task, Task prevTask) {
        this.task = task;
        this.prevTask = prevTask;
        this.nextTask = null;
    }

    public Task getPrevTask() {
        return prevTask;
    }

    public void setPrevTask(Task prevTask) {
        this.prevTask = prevTask;
    }

    public Task getNextTask() {
        return nextTask;
    }

    public void setNextTask(Task nextTask) {
        this.nextTask = nextTask;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
