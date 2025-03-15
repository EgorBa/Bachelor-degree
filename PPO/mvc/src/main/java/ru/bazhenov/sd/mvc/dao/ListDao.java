package ru.bazhenov.sd.mvc.dao;

import ru.bazhenov.sd.mvc.model.Task;
import ru.bazhenov.sd.mvc.model.ToDoList;

import java.util.List;

public interface ListDao {

    List<Task> getLists();
    void removeList(ToDoList list);

    void addTaskToList(Task task);
    void removeTaskFromList(Task task);

    void changeStatus(Task task);

}
