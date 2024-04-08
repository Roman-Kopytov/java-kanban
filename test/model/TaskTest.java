package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    private static void assertEqualsTask(Task expected, Task actual, String massage) {
        assertEquals(expected.getId(), actual.getId(), massage + "id");
        assertEquals(expected.getDescription(), actual.getDescription(), massage + "description");
        assertEquals(expected.getName(), actual.getName(), massage + "name");
        assertEquals(expected.getStatus(), actual.getStatus(), massage + "status");

    }

    @Test
    void shouldBeEquals() {
        Task taskactual = new Task("First", Status.NEW, "Creation");
        Task taskExpected = new Task("First", Status.NEW, "Creation");
        assertEqualsTask(taskExpected, taskactual, "Task должны совпадать");
    }

}