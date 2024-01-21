package model;

import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String status, String description) {
        super(name, status, description);
    }


    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", description='" + getDescription() + '\'' +
                '}';
    }
}
