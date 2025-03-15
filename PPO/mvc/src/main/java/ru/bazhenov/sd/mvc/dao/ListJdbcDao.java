package ru.bazhenov.sd.mvc.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import ru.bazhenov.sd.mvc.model.Task;
import ru.bazhenov.sd.mvc.model.ToDoList;

import javax.sql.DataSource;
import java.util.List;

public class ListJdbcDao extends JdbcDaoSupport implements ListDao {

    public ListJdbcDao(DataSource dataSource) {
        super();
        setDataSource(dataSource);
    }

    @Override
    public List<Task> getLists() {
        String sql = "SELECT * FROM TASKS;";
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(Task.class));
    }

    @Override
    public void removeList(ToDoList list) {
        String sql = "DELETE FROM TASKS WHERE LIST=?";
        getJdbcTemplate().update(sql, list.getName());
    }

    @Override
    public void addTaskToList(Task task) {
        String sql = "INSERT INTO TASKS (NAME, RESOLVE, LIST) VALUES (?, ?, ?)";
        getJdbcTemplate().update(sql, task.getName(), 0, task.getList());
    }

    @Override
    public void removeTaskFromList(Task task) {
        String sql = "DELETE FROM TASKS WHERE NAME=? AND LIST=?";
        getJdbcTemplate().update(sql, task.getName(), task.getList());
    }

    @Override
    public void changeStatus(Task task) {
        String sql = "UPDATE TASKS SET RESOLVE=? WHERE NAME=? AND LIST=?";
        getJdbcTemplate().update(sql, !task.isResolved() ? 1 : 0, task.getName(), task.getList());
    }
}
