package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, SubTask> subTasks;
    int seq = 0;

    private int generateId() {
        return ++seq;
    }

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }

    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteAllTask() {
        tasks.clear();
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public Task createEpic(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Task get(int id) {
        return tasks.get(id);
    }


    public void deleteEpic(int id) {
        for (SubTask subTask : epics.get(id).getSubTasks()){
            subTasks.remove(subTask.getId());
        }
        epics.remove(id);
    }

    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public void deleteAllEpic() {
        subTasks.clear();
        epics.clear();
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    public Epic getId(int id) {
        return epics.get(id);
    }

    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        if (saved == null) {
            return;
        }
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }

    public ArrayList<SubTask> getEpicSubTask(Epic epic) {
        Epic saved = epics.get(epic.getId());
        return saved.getSubTasks();
    }




    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    public void deleteAllSubTask() {
        subTasks.clear();
        for (Epic epic : epics.values()){
            calculateEpicStatus(epic);
        }

    }

    public void updateSubTask(SubTask subtask) {
        Epic epic = subtask.getEpic();
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        calculateEpicStatus(savedEpic);
    }

    public void deleteSubTask(int id) {
        SubTask savedSubTask = subTasks.get(id);
        Epic savedEpic = savedSubTask.getEpic();
        subTasks.remove(id);

        calculateEpicStatus(savedEpic);
    }

    public SubTask createSubTask(SubTask subtask,Epic epic) {
        subtask.setId(generateId());
        subTasks.put(subtask.getId(), subtask);
        return subtask;
    }

    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    private void calculateEpicStatus(Epic epic) {
        ArrayList<SubTask> savedSubTask = epic.getSubTasks();
        int counterDoneStatus = 0;

        int counterNewStatus = 0;
        int counterProgressStatus = 0;
        for (SubTask subTask : savedSubTask) {
            Status savedStatus = Status.valueOf(subTask.getStatus());
            if (Status.IN_PROGRESS.equals(savedStatus)) {
                counterProgressStatus++;

            } else if (Status.DONE.equals(savedStatus)) {
                counterDoneStatus++;
            } else if (Status.NEW.equals(savedStatus)) {
                counterNewStatus++;
            }
        }
        if (savedSubTask.size() == counterNewStatus) {
            epic.setStatus("NEW");
        } else if (counterProgressStatus > 0 || (counterNewStatus > 0 && counterDoneStatus > 0)) {
            epic.setStatus("IN_PROGRESS");
        } else if (savedSubTask.size() == counterDoneStatus) {
            epic.setStatus("DONE");
        }
    }
}