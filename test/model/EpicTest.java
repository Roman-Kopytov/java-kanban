package model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private static void assertEqualsEpic(Epic expected, Epic actual, String massage){
        assertEquals(expected.getId(),actual.getId(),massage + "id");
        assertEquals(expected.getDescription(),actual.getDescription(), massage+ "description");
        assertEquals(expected.getName(), actual.getName(),massage + "name");
        assertEquals(expected.getStatus(), actual.getStatus(), massage + "status");

    }
    @Test
    void shouldBeEquals() {
        Epic taskactual = new Epic("First", Status.NEW, "Creation");
        Epic taskExpected = new Epic("First", Status.NEW, "Creation");
        assertEqualsEpic(taskExpected, taskactual, "Epic должны совпадать");
    }


}