package service;

import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> historyTasks = new ArrayList<>(10);


    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyTasks);
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            if (historyTasks.size() == 10) {
                historyTasks.removeFirst();
            }
            historyTasks.add(task);
        }
    }
}
