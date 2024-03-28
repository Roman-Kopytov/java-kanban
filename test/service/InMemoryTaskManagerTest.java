package service;

import org.junit.jupiter.api.DisplayName;

@DisplayName("Менеджер задач")
class InMemoryTaskManagerTest extends TaskManagerTest {


    @Override
    InMemoryTaskManager createManager() {
        return new InMemoryTaskManager(new InMemoryHistoryManager());
    }


//    @Test
//    @DisplayName("Эпик не должен хранить неактуальные подзадачи")
//    public void shouldDeleteSubtaskFromEpic() {
//        Epic epic = new Epic("Test addNewEpic", Status.NEW, "Test addNewEpic description");
//        manager.createEpic(epic);
//        final int epicId = epic.getId();
//        SubTask subTaskFirst = new SubTask("Test NewSubTask1", Status.NEW, "Test NewSubtask description", epic);
//        SubTask subTaskSecond = new SubTask("Test NewSubTask2", Status.NEW, "Test NewSubtask description", epic);
//        manager.createSubTask(subTaskFirst);
//        manager.createSubTask(subTaskSecond);
//        final int subTaskFirstId = subTaskFirst.getId();
//        manager.deleteSubTask(subTaskFirstId);
//        List<Task> expectedList = new ArrayList<>();
//        expectedList.add(subTaskSecond);
//        assertEquals(expectedList, manager.getEpicSubTasks(epicId), "Списки должны совпадать");
//    }
}