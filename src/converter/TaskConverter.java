package converter;

import model.Task;

import java.time.format.DateTimeFormatter;

public class TaskConverter implements Converter<Task> {

    private final DateTimeFormatter formatter;

    public TaskConverter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public String toString(Task task) {
        if (task.getStartTime() == null) {
            return task.getId() + "," + task.getType() + "," + task.getName() +
                    "," + task.getStatus() + "," + task.getDescription() + "," + null
                    + "," + task.getDuration().toMinutes();
        }
        return task.getId() + "," + task.getType() + "," + task.getName() +
                "," + task.getStatus() + "," + task.getDescription() + "," + task.getStartTime().format(formatter)
                + "," + task.getDuration().toMinutes();
    }
}
