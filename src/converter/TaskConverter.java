package converter;

import model.Task;
import model.TaskType;

public class TaskConverter implements Converter<Task> {
    public TaskType getType() {
        return TaskType.TASK;
    }

    @Override
    public String toString(Task task) {
        return task.getId() + "," + getType() + "," + task.getName() +
                "," + task.getStatus() + "," + task.getDescription();
    }
}
