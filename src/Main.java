import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();
        Task tripdNew = manager.createTask(new Task("Переезд", Status.NEW, "Нужно переехать в Сосновый бор"));
        Task sochiNew = manager.createTask(new Task("Поездка в сочи", Status.NEW, "Нужно собрать вещи"));

        Epic ticketNew = manager.createEpic(new Epic("Купить билеты", Status.NEW, "Нужно купить как можно быстрее билеты."));
        SubTask tinkoffNew = manager.createSubTask(new SubTask("Зайти в приложение тинькофф", Status.NEW, "Возьми телефон и зайди в приложение.", ticketNew));
        SubTask buyNew = manager.createSubTask(new SubTask("Выбрать билеты", Status.NEW, "Выбери самые выгодные билеты.", ticketNew));

        Epic kissNew = manager.createEpic(new Epic("Поцеловать жену", Status.NEW, "Встретиться с женой и чмокнуть ее."));
        SubTask flowersNew = manager.createSubTask(new SubTask("Купить цветы", Status.NEW, "Купи красивый букет цветов", kissNew));

        System.out.println(tripdNew);
        System.out.println(sochiNew);
        System.out.println(ticketNew);
        System.out.println(tinkoffNew);
        System.out.println(buyNew);
        System.out.println(kissNew);
        System.out.println(flowersNew);
        System.out.println();

        tripdNew.setStatus(Status.IN_PROGRESS);
        sochiNew.setStatus(Status.DONE);
        tinkoffNew.setStatus(Status.IN_PROGRESS);
        buyNew.setStatus(Status.DONE);

        manager.updateSubTask(tinkoffNew);
        manager.updateSubTask(buyNew);
        manager.updateTask(tripdNew);
        manager.updateTask(sochiNew);

        System.out.println(manager.getAllEpic());
        System.out.println();
        System.out.println(manager.getAllSubTask());
        System.out.println();
        System.out.println(manager.getAllTask());

        manager.deleteEpic(3);
        manager.deleteTask(1);
        System.out.println("Удалили epic 3 и task 1");
        System.out.println(manager.getAllEpic());
        System.out.println();
        System.out.println(manager.getAllSubTask());
        System.out.println();
        System.out.println(manager.getAllTask());
    }
}
