package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    int seq = 0;

    private int generateId() {
        return ++seq;
    }


    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteAllTask() {
        tasks.clear();
    }

    public void updateTask(Task task) {
        if (tasks.get(task.getId()) == null) {
            return;
        }
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
        for (Integer subTaskId : epics.get(id).getSubTasksId()) {
            subTasks.remove(subTaskId);
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
        ArrayList<SubTask> epicSubTask = new ArrayList<>();
        for (int id : saved.getSubTasksId()) {
            epicSubTask.add(subTasks.get(id));
        }
        return epicSubTask;
    }


    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    public void deleteAllSubTask() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.deleteSubTasksId();
            calculateEpicStatus(epic);
        }
    }

    public void updateSubTask(SubTask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        calculateEpicStatus(savedEpic);
    }

    public void deleteSubTask(int id) {
        SubTask savedSubTask = subTasks.get(id);
        Epic savedEpic = epics.get(savedSubTask.getEpicId());
        savedEpic.getSubTasksId().remove(id);
        subTasks.remove(id);

        calculateEpicStatus(savedEpic);
    }

    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        Epic savedEpic = epics.get(subTask.getEpicId());
        if (savedEpic == null) {
            System.out.println("Такого эпика нет в хранилище.");
            return subTask;
        } else {
            subTasks.put(subTask.getId(), subTask);
        }
        calculateEpicStatus(savedEpic);
        return subTask;
    }

    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    private void calculateEpicStatus(Epic epic) {
        ArrayList<Integer> savedSubTaskId = epic.getSubTasksId();
        int counterDoneStatus = 0;
        int counterNewStatus = 0;
        int counterProgressStatus = 0;

        for (int taskId : savedSubTaskId) {
            SubTask savedSubTask = subTasks.get(taskId);
            Status savedStatus = Status.valueOf(savedSubTask.getStatus());
            if (Status.IN_PROGRESS.equals(savedStatus)) {
                counterProgressStatus++;

            } else if (Status.DONE.equals(savedStatus)) {
                counterDoneStatus++;
            } else if (Status.NEW.equals(savedStatus)) {
                counterNewStatus++;
            }
        }
        if (savedSubTaskId.size() == counterNewStatus) {
            epic.setStatus("NEW");
        } else if (counterProgressStatus > 0 || (counterNewStatus > 0 && counterDoneStatus > 0)) {
            epic.setStatus("IN_PROGRESS");
        } else if (savedSubTaskId.size() == counterDoneStatus) {
            epic.setStatus("DONE");
        }
    }
}