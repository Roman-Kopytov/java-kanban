package converter;

import model.Task;

public class TaskConverter implements Converter<Task> {

    @Override
    public String toString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getName() +
                "," + task.getStatus() + "," + task.getDescription();
    }
}
