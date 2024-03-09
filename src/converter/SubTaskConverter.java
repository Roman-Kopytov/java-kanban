package converter;

import model.SubTask;
import model.TaskType;

public class SubTaskConverter implements Converter<SubTask> {
    public TaskType getType() {
        return TaskType.SUB_TASK;
    }

    @Override
    public String toString(SubTask task) {
        return task.getId() + "," + getType() + "," + task.getName() +
                "," + task.getStatus() + "," + task.getDescription() + "," + task.getEpicId();
    }
}
