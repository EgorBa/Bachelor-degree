package ru.bazhenov.sd.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.bazhenov.sd.mvc.dao.ListDao;
import ru.bazhenov.sd.mvc.logic.Grouper;
import ru.bazhenov.sd.mvc.model.Task;
import ru.bazhenov.sd.mvc.model.ToDoList;

import java.util.List;

@Controller
public class ListController {
    private final ListDao listDao;

    public ListController(ListDao listDao) {
        this.listDao = listDao;
    }

    @RequestMapping(value = "/get-lists", method = RequestMethod.GET)
    public String getLists(ModelMap map) {
        prepareModelMap(map, Grouper.prepare(listDao.getLists()));
        return "index";
    }

    @RequestMapping(value = "/add-task", method = RequestMethod.POST)
    public String addTask(@ModelAttribute("task") Task task) {
        listDao.addTaskToList(task);
        return "redirect:/get-lists";
    }

    @RequestMapping(value = "/remove-task", method = RequestMethod.POST)
    public String removeTask(@ModelAttribute("task") Task task) {
        listDao.removeTaskFromList(task);
        return "redirect:/get-lists";
    }

    @RequestMapping(value = "/change-status", method = RequestMethod.POST)
    public String changeStatus(@ModelAttribute("task") Task task) {
        listDao.changeStatus(task);
        return "redirect:/get-lists";
    }

    @RequestMapping(value = "/remove-list", method = RequestMethod.POST)
    public String removeList(@ModelAttribute("list") ToDoList list) {
        listDao.removeList(list);
        return "redirect:/get-lists";
    }

    private void prepareModelMap(ModelMap map, List<ToDoList> list) {
        map.addAttribute("lists", list);
        map.addAttribute("list", new ToDoList());
        map.addAttribute("task", new Task());
    }
}
