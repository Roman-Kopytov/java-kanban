package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubTaskTest {

    private static void assertEqualsSubTask(SubTask expected, SubTask actual, String massage) {
        assertEquals(expected.getId(), actual.getId(), massage + "id");
        assertEquals(expected.getDescription(), actual.getDescription(), massage + "description");
        assertEquals(expected.getName(), actual.getName(), massage + "name");
        assertEquals(expected.getStatus(), actual.getStatus(), massage + "status");

    }

    @Test
    void shouldBeEquals() {
        Epic epic = new Epic("First", Status.NEW, "Creation");
        SubTask subTaskactual = new SubTask("First", Status.NEW, "Creation", epic);
        SubTask subTaskExpected = new SubTask("First", Status.NEW, "Creation", epic);
        assertEqualsSubTask(subTaskExpected, subTaskactual, "Task должны совпадать");
    }
}