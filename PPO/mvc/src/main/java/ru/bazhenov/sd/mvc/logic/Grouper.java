package ru.bazhenov.sd.mvc.logic;

import ru.bazhenov.sd.mvc.model.Task;
import ru.bazhenov.sd.mvc.model.ToDoList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Grouper {

    public static ArrayList<ToDoList> prepare(List<Task> list) {
        HashMap<String, ToDoList> map = new HashMap<>();
        for (Task t : list) {
            String listName = t.getList();
            map.putIfAbsent(listName, new ToDoList(listName));
            map.get(listName).addTask(t);
        }
        return new ArrayList<>(map.values());
    }
}
