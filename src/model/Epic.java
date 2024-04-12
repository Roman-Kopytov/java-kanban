package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Integer> subTasksId = new ArrayList<>();

    private LocalDateTime endTime;

    public Epic() {
        super();
    }

    public Epic(String name, Status status, String description) {
        super(name, status, description);
        super.startTime = null;
        super.duration = Duration.ZERO;
    }

    public Epic(String name, Status status, String description, LocalDateTime startTime, Duration duration) {
        super(name, status, description, startTime, duration);
        super.startTime = null;
        super.duration = Duration.ZERO;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void deleteSubTasksId() {
        subTasksId.clear();
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Epic epic = (Epic) o;

        if (!subTasksId.equals(epic.subTasksId)) return false;
        return Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + subTasksId.hashCode();
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasksId=" + subTasksId +
                ", endTime=" + endTime +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}
