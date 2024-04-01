package model;


import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private final int epicId;

    public SubTask(String name, Status status, String description, Epic epic) {
        super(name, status, description);
        this.epicId = epic.getId();
    }

    public SubTask(String name, Status status, String description, Integer id) {
        super(name, status, description);
        this.epicId = id;
    }

    public SubTask(String name, Status status, String description, LocalDateTime startTime, Duration duration, Epic epic) {
        super(name, status, description, startTime, duration);
        this.epicId = epic.getId();
    }

    public SubTask(String name, Status status, String description, LocalDateTime startTime, Duration duration, Epic epic, Integer id) {
        super(name, status, description, startTime, duration);
        this.epicId = epic.getId();
        this.id = id;
    }

    public SubTask(String name, Status status, String description, LocalDateTime startTime, Duration duration, Integer id) {
        super(name, status, description, startTime, duration);
        this.epicId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SubTask subTask = (SubTask) o;

        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + epicId;
        return result;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUB_TASK;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", epicId=" + epicId + '\'' +
                '}';
    }


}
