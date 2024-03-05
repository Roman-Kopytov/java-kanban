package model;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Epic epic = (Epic) o;

        return Objects.equals(subTasksId, epic.subTasksId);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (subTasksId != null ? subTasksId.hashCode() : 0);
        return result;
    }

    private final ArrayList<Integer> subTasksId = new ArrayList<>();

    public Epic(String name, Status status, String description) {
        super(name, status, description);
    }

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void deleteSubTasksId() {
        subTasksId.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", subTasksId='" +  subTasksId + '\'' +
                '}';
    }

}
