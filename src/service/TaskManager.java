package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getAllTask();

    void deleteAllTask();

    void updateTask(model.Task task);

    void deleteTask(int id);

    Task createTask(model.Task task);

    Task getTask(int id);

    void deleteEpic(int id);

    Epic createEpic(Epic epic);

    void deleteAllEpic();

    List<Epic> getAllEpic();

    Epic getEpic(int id);

    void updateEpic(Epic epic);

    List<SubTask> getEpicSubTasks(int epicId);

    List<SubTask> getAllSubTask();

    void deleteAllSubTask();

    void updateSubTask(SubTask subtask);

    void deleteSubTask(int id);

    SubTask createSubTask(SubTask subTask);

    SubTask getSubTask(int id);

    List<Task> getHistory();

}
