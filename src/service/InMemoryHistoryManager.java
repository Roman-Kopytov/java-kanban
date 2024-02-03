package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> lastTenTasks = new ArrayList<>(10);


    @Override
    public List<Task> getHistory(){
        return lastTenTasks;
    }

    @Override
    public void add(Task task){
        lastTenTasks.add(task);
    }
}
