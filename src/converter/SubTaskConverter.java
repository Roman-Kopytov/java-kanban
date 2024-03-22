package converter;

import model.SubTask;

public class SubTaskConverter implements Converter<SubTask> {

    @Override
    public String toString(SubTask task) {
        return task.getId() + "," + task.getType() + "," + task.getName() +
                "," + task.getStatus() + "," + task.getDescription() + "," + task.getEpicId();
    }
}
