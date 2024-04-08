package service;

import exception.NotFoundException;
import exception.ValidationException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final HistoryManager defaultHistory;
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected int seq = 0;

    public InMemoryTaskManager(HistoryManager defaultHistory) {
        this.defaultHistory = defaultHistory;
    }


    private int generateId() {
        return ++seq;
    }


    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTask() {
        for (Integer id : tasks.keySet()) {
            defaultHistory.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void updateTask(Task task) {
        Task savedTask = tasks.get(task.getId());
        if (savedTask == null) {
            return;
        }
        if (!isTaskIntersect(task)) {
            if (task.getStartTime() != null) {
                prioritizedTasks.remove(savedTask);
                prioritizedTasks.add(task);
            }
            tasks.put(task.getId(), task);
        } else {
            throw new ValidationException("There is an intersection in execution time with task number="
                    + task.getId());
        }
    }

    @Override
    public void deleteTask(int id) {
        if (tasks.get(id).getStartTime() != null) {
            prioritizedTasks.remove(tasks.get(id));
        }
        tasks.remove(id);
        defaultHistory.remove(id);
    }

    @Override
    public Task createTask(Task task) {
        if (!isTaskIntersect(task)) {
            task.setId(generateId());
            tasks.put(task.getId(), task);
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
            return task;
        } else {
            throw new ValidationException("There is an intersection in execution time with task number="
                    + task.getId());
        }
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new NotFoundException("Задача с id = " + id);
        }
        defaultHistory.add(task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public void deleteEpic(int id) {
        Epic savedEpic = epics.get(id);
        if (savedEpic != null) {
            for (Integer subTaskId : savedEpic.getSubTasksId()) {
                prioritizedTasks.remove(subTasks.get(subTaskId));
                subTasks.remove(subTaskId);
                defaultHistory.remove(subTaskId);
            }
            epics.remove(id);
            defaultHistory.remove(id);
        }
    }

    @Override
    public void deleteAllEpic() {
        for (Epic epic : epics.values()) {
            defaultHistory.remove(epic.getId());
            for (Integer subTaskId : epic.getSubTasksId()) {
                prioritizedTasks.remove(subTasks.get(subTaskId));
                defaultHistory.remove(subTaskId);
            }
        }
        subTasks.clear();
        epics.clear();
    }

    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new NotFoundException("Задача с id = " + id);
        }
        defaultHistory.add(epic);
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        if (saved == null) {
            throw new NotFoundException("Задача с id = " + epic.getId());
        }
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }

    @Override
    public List<SubTask> getEpicSubTasks(int epicId) {
        List<SubTask> epicSubTasks = new ArrayList<>();
        Epic savedEpic = epics.get(epicId);
        if (savedEpic != null) {
            for (int id : savedEpic.getSubTasksId()) {
                epicSubTasks.add(subTasks.get(id));
            }
        }
        return epicSubTasks;
    }

    @Override
    public List<SubTask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteAllSubTask() {
        for (SubTask subTask : subTasks.values()) {
            prioritizedTasks.remove(subTask);
        }
        subTasks.clear();
        for (Epic epic : epics.values()) {
            for (Integer subTaskId : epic.getSubTasksId()) {
                defaultHistory.remove(subTaskId);
            }
            epic.deleteSubTasksId();
            calculateEpicDateTime(epic);
            calculateEpicStatus(epic);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        Epic savedEpic = epics.get(subTask.getEpicId());
        if (savedEpic == null) {
            throw new NotFoundException("Задача с id = " + subTask.getEpicId());
        }
        if (!isTaskIntersect(subTask)) {
            if (subTask.getStartTime() != null) {
                prioritizedTasks.remove(subTasks.get(subTask.getId()));
                prioritizedTasks.add(subTask);
            }
            subTasks.put(subTask.getId(), subTask);
            calculateEpicDateTime(savedEpic);
            calculateEpicStatus(savedEpic);
        } else {
            throw new ValidationException("There is an intersection in execution time with task number="
                    + subTask.getId());
        }
    }

    @Override
    public void deleteSubTask(int id) {
        SubTask savedSubTask = subTasks.get(id);
        if (savedSubTask == null) {
            throw new NotFoundException("Задача с id = " + id);
        }
        Epic savedEpic = epics.get(savedSubTask.getEpicId());
        savedEpic.getSubTasksId().remove(Integer.valueOf(id));
        prioritizedTasks.remove(subTasks.get(id));
        subTasks.remove(id);
        defaultHistory.remove(id);
        calculateEpicDateTime(savedEpic);
        calculateEpicStatus(savedEpic);
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        Epic savedEpic = epics.get(subTask.getEpicId());
        if (savedEpic == null) {
            throw new NotFoundException("Задача с id = " + subTask.getEpicId());
        } else {
            subTask.setId(generateId());
            if (!isTaskIntersect(subTask)) {
                subTasks.put(subTask.getId(), subTask);

                savedEpic.getSubTasksId().add(subTask.getId());
                if (subTask.getStartTime() != null) {
                    prioritizedTasks.add(subTask);
                }
                calculateEpicDateTime(savedEpic);
                calculateEpicStatus(savedEpic);
                return subTask;
            } else {
                throw new ValidationException("There is an intersection in execution time with task number="
                        + subTask.getId());
            }
        }
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask == null) {
            throw new NotFoundException("Задача с id = " + id);
        }
        defaultHistory.add(subTask);
        return subTask;
    }

    @Override
    public List<Task> getHistory() {
        return defaultHistory.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }


    protected void calculateEpicDateTime(Epic epic) {
        ArrayList<Integer> savedSubTaskId = epic.getSubTasksId();
        if (!savedSubTaskId.isEmpty()) {
            Duration epicDuration = Duration.ofMinutes(0);
            long durationMinutes = 0;
            LocalDateTime minStartTime = LocalDateTime.MAX;
            LocalDateTime maxEndTime = LocalDateTime.MIN;
            for (Integer id : savedSubTaskId) {
                SubTask savedSubTask = subTasks.get(id);
                LocalDateTime savedStartTime = savedSubTask.getStartTime();
                LocalDateTime savedEndTime;
                if (savedStartTime == null) {
                    continue;
                } else {
                    savedEndTime = savedSubTask.getEndTime();
                    if (savedStartTime.isBefore(minStartTime)) {
                        minStartTime = savedStartTime;
                    }
                    if (savedEndTime.isAfter(maxEndTime)) {
                        maxEndTime = savedEndTime;
                    }
                }
                durationMinutes = durationMinutes + savedSubTask.getDuration().toMinutes();
            }
            epic.setStartTime(minStartTime);
            epic.setDuration(epicDuration.plusMinutes(durationMinutes));
            epic.setEndTime(maxEndTime);

        } else {
            epic.setStartTime(null);
            epic.setDuration(Duration.ZERO);
            epic.setEndTime(null);
        }
    }


    protected <T extends Task> boolean isTaskIntersect(T task) {
        if (task.getStartTime() != null) {
            return getPrioritizedTasks().stream()
                    .anyMatch(savedTask ->
                            savedTask.getEndTime().isAfter(task.getStartTime()) && task.getEndTime().isAfter(savedTask.getStartTime())
                    );
        } else {
            return false;
        }
    }

    protected void calculateEpicStatus(Epic epic) {
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
