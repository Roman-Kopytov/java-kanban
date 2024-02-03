package model;

import java.util.Objects;

public class SubTask extends Task {
    private final int epicId;

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

    public SubTask(String name, Status status, String description, Epic epic) {
        super(name, status, description);
        this.epicId = epic.getId();

    }

    public int getEpicId() {
        return epicId;
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
