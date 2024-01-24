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
        if (!tasks.containsKey(task.getId())) {
            return;
        }
        tasks.put(task.getId(), task);
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Task get(int id) {
        return tasks.get(id);
    }


    public void deleteEpic(int id) {
        Epic savedEpic = epics.get(id);
        if (savedEpic != null) {
            for (Integer subTaskId : savedEpic.getSubTasksId()) {
                subTasks.remove(subTaskId);
            }
            epics.remove(id);
        }
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

    public ArrayList<SubTask> getEpicSubTasks(int epicId) {
        ArrayList<SubTask> epicSubTasks = new ArrayList<>();
        Epic savedEpic = epics.get(epicId);  // реализовал не через containsKey, так как мы потом используем полученное значение
        if (savedEpic == null) {
            return epicSubTasks;
        } else {
            for (int id : savedEpic.getSubTasksId()) {
                epicSubTasks.add(subTasks.get(id));
            }
        }
        return epicSubTasks;
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
        Epic savedEpic = epics.get(subtask.getEpicId());
        if (savedEpic == null) {
            return;
        }
        subTasks.put(subtask.getId(), subtask);
        calculateEpicStatus(savedEpic);
    }

    public void deleteSubTask(int id) {
        SubTask savedSubTask = subTasks.get(id);
        if (savedSubTask == null) {
            return;
        }
        Epic savedEpic = epics.get(savedSubTask.getEpicId());
        savedEpic.getSubTasksId().remove(id);
        subTasks.remove(id);

        calculateEpicStatus(savedEpic);
    }

    public SubTask createSubTask(SubTask subTask) {
        Epic savedEpic = epics.get(subTask.getEpicId());
        if (savedEpic == null) {
            System.out.println("Такого эпика нет в хранилище.");
            return subTask;
        } else {
            subTask.setId(generateId());
            subTasks.put(subTask.getId(), subTask);
        }
        savedEpic.getSubTasksId().add(subTask.getId());
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
            Status savedStatus = savedSubTask.getStatus();
            if (Status.IN_PROGRESS.equals(savedStatus)) {
                counterProgressStatus++;

            } else if (Status.DONE.equals(savedStatus)) {
                counterDoneStatus++;
            } else if (Status.NEW.equals(savedStatus)) {
                counterNewStatus++;
            }
        }
        if (savedSubTaskId.size() == counterNewStatus) {
            epic.setStatus(Status.NEW);
        } else if (counterProgressStatus > 0 || (counterNewStatus > 0 && counterDoneStatus > 0)) {
            epic.setStatus(Status.IN_PROGRESS);
        } else if (savedSubTaskId.size() == counterDoneStatus) {
            epic.setStatus(Status.DONE);
        }
    }
}