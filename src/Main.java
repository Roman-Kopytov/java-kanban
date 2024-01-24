import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();
        Task trip = new Task("Переезд", Status.NEW, "Нужно переехать в Сосновый бор");
        Task sochi = new Task("Поездка в сочи", Status.NEW, "Нужно собрать вещи");
        Epic ticket = new Epic("Купить билеты", Status.NEW, "Нужно купить как можно быстрее билеты.");

        Epic kiss = new Epic("Поцеловать жену", Status.NEW, "Встретиться с женой и чмокнуть ее.");







        Task tripdNew = manager.createEpic(trip);

        Task sochiNew = manager.createEpic(sochi);

        Epic ticketNew = manager.createEpic(ticket);
        SubTask tinkoff = new SubTask("Зайти в приложение тинькофф", Status.NEW, "Возьми телефон и зайди в приложение.",ticketNew);
        SubTask buy = new SubTask("Выбрать билеты", Status.NEW, "Выбери самые выгодные билеты.",ticketNew);
        SubTask tinkoffNew = manager.createSubTask(tinkoff);
        ticketNew.setSubTaskId(tinkoff);
        SubTask buyNew = manager.createSubTask(buy);
        ticketNew.setSubTaskId(buyNew);


        Epic kissNew = manager.createEpic(kiss);
        SubTask flowers = new SubTask("Купить цветы", Status.NEW, "Купи красивый букет цветов",kissNew);
        SubTask flowersNew = manager.createSubTask(flowers);
        kissNew.setSubTaskId(flowersNew);




        System.out.println(tripdNew.toString());
        System.out.println(sochiNew.toString());
        System.out.println(ticketNew.toString());
        System.out.println(tinkoffNew.toString());
        System.out.println(buyNew.toString());
        System.out.println(kissNew.toString());
        System.out.println(flowersNew.toString());
        System.out.println();

        tripdNew.setStatus("IN_PROGRESS");
        sochiNew.setStatus("DONE");
        tinkoffNew.setStatus("IN_PROGRESS");
        buyNew.setStatus("DONE");

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
