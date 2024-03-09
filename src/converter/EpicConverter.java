package converter;

import model.Epic;
import model.TaskType;

public class EpicConverter implements Converter<Epic> {

    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString(Epic task) {
        return task.getId() + "," + getType() + "," + task.getName() +
                "," + task.getStatus() + "," + task.getDescription();
    }
}
