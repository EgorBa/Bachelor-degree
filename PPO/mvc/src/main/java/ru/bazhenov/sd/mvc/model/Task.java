package ru.bazhenov.sd.mvc.model;

public class Task {

    private String name;
    private String list;
    private int resolve;

    public Task() {
    }

    public Task(String name, int resolve, String list) {
        this.name = name;
        this.resolve = resolve;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public void setResolve(int i) {
        resolve = i;
    }

    public int getResolve() {
        return resolve;
    }

    public boolean isResolved() {
        return resolve == 1;
    }

    public String getResolveText() {
        return resolve == 1 ? "resolved" : "not resolved";
    }

    @Override
    public String toString() {
        return "Task: " + name + " " + getResolveText() + " " + list;
    }
}
