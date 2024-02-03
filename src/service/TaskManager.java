package service;

import model.Epic;
import model.SubTask;

import java.util.List;

public interface TaskManager {

    List<model.Task> getAllTask();

    void deleteAllTask();

    void updateTask(model.Task task);

    void deleteTask(int id);

    model.Task createTask(model.Task task);

    model.Task getTask(int id);

    void deleteEpic(int id);

    Epic createEpic(Epic epic);

    void deleteAllEpic();

    List<Epic> getAllEpic();

    Epic getId(int id);

    void updateEpic(Epic epic);

    List<SubTask> getEpicSubTasks(int epicId);

    List<SubTask> getAllSubTask();

    void deleteAllSubTask();

    void updateSubTask(SubTask subtask);

    void deleteSubTask(int id);

    SubTask createSubTask(SubTask subTask);

    SubTask getSubTask(int id);

    void calculateEpicStatus(Epic epic);
}
