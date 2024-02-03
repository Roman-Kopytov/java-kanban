package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private HashMap<Integer, model.Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();

    List<Task> lastTenTask;
    int seq = 0;

    public InMemoryTaskManager(){

    }
    
    public InMemoryTaskManager(HistoryManager defaultHistory) {
        this.lastTenTask = defaultHistory.getHistory();
    }


    private int generateId() {
        return ++seq;
    }


    @Override
    public List<model.Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTask() {
        tasks.clear();
    }

    @Override
    public void updateTask(model.Task task) {
        if (!tasks.containsKey(task.getId())) {
            return;
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    @Override
    public model.Task createTask(model.Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task getTask(int id) {
        lastTenTask.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public void deleteEpic(int id) {
        Epic savedEpic = epics.get(id);
        if (savedEpic != null) {
            for (Integer subTaskId : savedEpic.getSubTasksId()) {
                subTasks.remove(subTaskId);
            }
            epics.remove(id);
        }
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public void deleteAllEpic() {
        subTasks.clear();
        epics.clear();
    }

    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic getId(int id) {
        return epics.get(id);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        if (saved == null) {
            return;
        }
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }

    @Override
    public List<SubTask> getEpicSubTasks(int epicId) {
        List<SubTask> epicSubTasks = new ArrayList<>();
        Epic savedEpic = epics.get(epicId);  // реализовал не через containsKey, так как мы потом используем полученное значение
        if (savedEpic != null) {
            for (int id : savedEpic.getSubTasksId()) {
                epicSubTasks.add(subTasks.get(id));
            }
        }
        lastTenTask.add(subTasks.get(epicId));
        return epicSubTasks;
    }

    @Override
    public List<SubTask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteAllSubTask() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.deleteSubTasksId();
            calculateEpicStatus(epic);
        }
    }

    @Override
    public void updateSubTask(SubTask subtask) {
        Epic savedEpic = epics.get(subtask.getEpicId());
        if (savedEpic == null) {
            return;
        }
        subTasks.put(subtask.getId(), subtask);
        calculateEpicStatus(savedEpic);
    }

    @Override
    public void deleteSubTask(int id) {
        SubTask savedSubTask = subTasks.get(id);
        if (savedSubTask == null) {
            return;
        }
        Epic savedEpic = epics.get(savedSubTask.getEpicId());
        savedEpic.getSubTasksId().remove(Integer.valueOf(id));
        subTasks.remove(id);

        calculateEpicStatus(savedEpic);
    }

    @Override
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

    @Override
    public SubTask getSubTask(int id) {
        lastTenTask.add(subTasks.get(id));
        return subTasks.get(id);

    }

    @Override
    public void calculateEpicStatus(Epic epic) {
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
