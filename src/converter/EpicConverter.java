package converter;

import model.Epic;

public class EpicConverter implements Converter<Epic> {

    @Override
    public String toString(Epic task) {
        return task.getId() + "," + task.getType() + "," + task.getName() +
                "," + task.getStatus() + "," + task.getDescription();
    }
}
