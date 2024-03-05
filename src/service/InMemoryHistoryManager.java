package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static class Node {
        Task item;
        Node prev;
        Node next;

        Node(Node prev, Task element, Node next) {
            this.prev = prev;
            this.item = element;
            this.next = next;
        }
    }

    private final HashMap<Integer, Node> history = new HashMap<>();

    Node first;

    Node last;

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(getTasks());
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        Node savedNode = history.get(task.getId());
        if (savedNode != null) {
            removeNode(savedNode);
        }
        linkLast(task);
        history.put(task.getId(), last);
    }

    @Override
    public void remove(int id) {
        Node savedNode = history.get(id);
        removeNode(savedNode);
    }

    private List<Task> getTasks() {
        List<Task> historyTask = new ArrayList<>();
        Node current = first;
        while (current != null) {
            historyTask.add(current.item);
            current = current.next;
        }
        return historyTask;
    }

    private void linkLast(Task task) {
        final Node newNode = new Node(last, task, null);
        if (first == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        last = newNode;
    }

    private void removeNode(Node task) {
        Node currentNode = task;
        if (currentNode == null) {
            return;
        }
        if (currentNode.prev == null) {
            first = currentNode.next;
        } else {
            currentNode.prev.next = currentNode.next;
        }
        if (currentNode.next == null) {
            last = currentNode.prev;
        } else {
            currentNode.next.prev = currentNode.prev;
        }
        history.remove(task.item.getId());
    }
}
