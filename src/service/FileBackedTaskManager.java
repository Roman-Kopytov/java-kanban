package service;

import converter.Converter;
import converter.EpicConverter;
import converter.SubTaskConverter;
import converter.TaskConverter;
import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public static final String TASK_CSV = "resources/task.CSV";
    private final File file;
    private DateTimeFormatter formatter;

    private Map<TaskType, Converter> converters = new HashMap<>();

    public FileBackedTaskManager() {
        this(Managers.getDefaultHistory());
        converters = fillConvertersMap();
    }

    public FileBackedTaskManager(HistoryManager historyManager) {
        this(historyManager, new File(TASK_CSV));
        converters = fillConvertersMap();
    }

    public FileBackedTaskManager(File file) {
        this(Managers.getDefaultHistory(), file);
        converters = fillConvertersMap();
    }

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
        converters = fillConvertersMap();
    }

    private void historyFromString(String value) {
        if (value != null) {
            String[] historyArray = value.split(",");
            for (String idStr : historyArray) {
                int id = Integer.parseInt(idStr);
                Task savedTask = tasks.get(id);
                Epic savedEpic = epics.get(id);
                SubTask savedSubTask = subTasks.get(id);
                if (savedEpic != null) {
                    defaultHistory.add(savedEpic);
                }
                if (savedTask != null) {
                    defaultHistory.add(savedTask);
                }
                if (savedSubTask != null) {
                    defaultHistory.add(savedSubTask);
                }
            }
        }
    }

    private String historyToString(HistoryManager manager) {
        StringBuffer stringHistory = new StringBuffer();
        List<Task> savedHistory = manager.getHistory();
        for (Task task : savedHistory) {
            stringHistory.append(task.getId() + ",");
        }
        return stringHistory.toString();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.loadFromFile();
        return manager;
    }

    public static void main(String[] args) {

    }

    Map<TaskType, Converter> fillConvertersMap() {
        Map<TaskType, Converter> converters = new HashMap<>();
        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy/ HH:mm:ss");
        converters.put(TaskType.TASK, new TaskConverter(formatter));
        converters.put(TaskType.EPIC, new EpicConverter(formatter));
        converters.put(TaskType.SUB_TASK, new SubTaskConverter(formatter));
        return converters;
    }


    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public Task createTask(Task task) {
        Task taskNew = super.createTask(task);
        save();
        return taskNew;
    }

    @Override
    public Task getTask(int id) {
        Task taskNew = super.getTask(id);
        save();
        return taskNew;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic epicNew = super.createEpic(epic);
        save();
        return epicNew;
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epicNew = super.getEpic(id);
        save();
        return epicNew;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask subTaskNew = super.createSubTask(subTask);
        save();
        return subTaskNew;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTaskNew = super.getSubTask(id);
        save();
        return subTaskNew;
    }

    private String toString(Task task) {
        return converters.get(task.getType()).toString(task);
    }

    private Task fromString(String value) {
        String[] attributes = value.split(",");
        int id = Integer.parseInt(attributes[0]);
        TaskType type = TaskType.valueOf(attributes[1]);
        String name = attributes[2];
        Status status = Status.valueOf(attributes[3]);
        String description = attributes[4];
        LocalDateTime startTime;
        if (!attributes[5].equals("null")) {
            startTime = LocalDateTime.parse(attributes[5], formatter);
        } else {
            startTime = null;
        }
        Duration duration = Duration.ofMinutes(Long.parseLong(attributes[6]));
        Task task = null;
        switch (type) {
            case TASK:
                task = new Task(name, status, description, startTime, duration);
                break;

            case SUB_TASK:
                task = new SubTask(name, status, description, startTime, duration, Integer.parseInt(attributes[7]));
                break;

            case EPIC:
                task = new Epic(name, status, description, startTime, duration);
                break;

        }
        task.setId(id);
        return task;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append("id,type,name,status,description,startTime,duration,epicId");
            writer.newLine();
            for (Map.Entry<Integer, Task> entry : super.tasks.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, SubTask> entry : super.subTasks.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : super.epics.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }

            writer.newLine();
            writer.append(historyToString(super.defaultHistory));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле: " + file.getAbsolutePath(), e);
        }

    }

    private void addSubTaskToEpic(SubTask subTask) {
        Epic savedEpic = epics.get(subTask.getEpicId());
        if (savedEpic == null) {
            System.out.println("Такого эпика нет в хранилище.");
            return;
        }
        savedEpic.getSubTasksId().add(subTask.getId());
    }

    public void loadFromFile() {
        try {
            final String s = Files.readString(file.toPath());
            s.split("\n");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле: " + file.getAbsolutePath(), e);
        }

        int maxId = 0;
        try (final BufferedReader reader = new BufferedReader(new FileReader(file, UTF_8))) {
            reader.readLine();
            while (true) {
                String line = reader.readLine();
                if (line.isEmpty()) {
                    break;
                }
                final Task task = fromString(line);

                final int id = task.getId();
                if (task.getType() == TaskType.TASK) {
                    tasks.put(id, task);
                    if (task.getStartTime() != null) {
                        prioritizedTasks.add(task);
                    }
                } else if (task.getType() == TaskType.SUB_TASK) {
                    subTasks.put(id, (SubTask) task);
                    if (task.getStartTime() != null) {
                        prioritizedTasks.add(task);
                    }
                } else if (task.getType() == TaskType.EPIC) {
                    epics.put(id, (Epic) task);
                }

                if (maxId < id) {
                    maxId = id;
                }
            }

            String line = reader.readLine();

            historyFromString(line);
            for (SubTask subTask : subTasks.values()) {
                addSubTaskToEpic(subTask);
            }
            for (Epic epic : epics.values()) {
                calculateEpicDateTime(epic);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле: " + file.getAbsolutePath(), e);
        }
        seq = maxId;
    }
}
