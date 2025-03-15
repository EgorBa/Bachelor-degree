package ru.bazhenov.sd.mvc.model;

import java.util.ArrayList;
import java.util.List;

public class ToDoList {

    private ArrayList<Task> tasks;
    private String name;

    public ToDoList() {
        tasks = new ArrayList<>();
    }

    public ToDoList(String name, List<Task> taskList) {
        this.name = name;
        tasks = new ArrayList<>(taskList);
    }

    public ToDoList(String name) {
        this.name = name;
        tasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }
}
