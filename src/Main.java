import model.Epic;
import model.SubTask;
import model.Task;
import service.TaskManager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();
        Task trip = new Task("Переезд", "NEW", "Нужно переехать в Сосновый бор");
        Task sochi = new Task("Поездка в сочи", "NEW", "Нужно собрать вещи");
        Epic ticket = new Epic("Купить билеты", "NEW", "Нужно купить как можно быстрее билеты.");
        SubTask tinkoff = new SubTask("Зайти в приложение тинькофф", "NEW", "ВОзьми телефон и зайди в приложение.");
        SubTask buy = new SubTask("Выбрать билеты", "NEW", "Выбери самые выгодные билеты.");
        Epic kiss = new Epic("Поцеловать жену", "NEW", "Встретиться с женой и чмокнуть ее.");
        SubTask flowers = new SubTask("Купить цветы", "NEW", "Купи красивый букет цветов");


        ArrayList<SubTask> subTasksTickets = new ArrayList<>();


        ArrayList<SubTask> subTasksflowers = new ArrayList<>();
        subTasksflowers.add(flowers);


        Task tripdNew = manager.createEpic(trip);

        Task sochiNew = manager.createEpic(sochi);

        Epic ticketNew = manager.createEpic(ticket);
        SubTask tinkoffNew = manager.createSubTask(tinkoff,ticket);
        tinkoffNew.setEpic(ticketNew);
        SubTask buyNew = manager.createSubTask(buy,ticket);
        buyNew.setEpic(ticketNew);
        subTasksTickets.add(tinkoffNew);
        subTasksTickets.add(buyNew);

        Epic kissNew = manager.createEpic(kiss);
        SubTask flowersNew = manager.createSubTask(flowers,kissNew);
        subTasksflowers.add(flowersNew);

        ticketNew.setSubTasks(subTasksTickets);
        kissNew.setSubTasks(subTasksflowers);


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
