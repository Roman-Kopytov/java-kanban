package converter;

import model.SubTask;

import java.time.format.DateTimeFormatter;

public class SubTaskConverter implements Converter<SubTask> {

    private final DateTimeFormatter formatter;

    public SubTaskConverter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public String toString(SubTask task) {
        if (task.getStartTime() == null) {
            return task.getId() + "," + task.getType() + "," + task.getName() +
                    "," + task.getStatus() + "," + task.getDescription() + "," + null
                    + "," + task.getDuration().toMinutes() + "," + task.getEpicId();
        }
        return task.getId() + "," + task.getType() + "," + task.getName() +
                "," + task.getStatus() + "," + task.getDescription() + "," + task.getStartTime().format(formatter)
                + "," + task.getDuration().toMinutes() + "," + task.getEpicId();
    }
}
