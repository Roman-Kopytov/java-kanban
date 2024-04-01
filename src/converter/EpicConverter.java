package converter;

import model.Epic;

import java.time.format.DateTimeFormatter;

public class EpicConverter implements Converter<Epic> {

    private final DateTimeFormatter formatter;

    public EpicConverter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public String toString(Epic task) {
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
