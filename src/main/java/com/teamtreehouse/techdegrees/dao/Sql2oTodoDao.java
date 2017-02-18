package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exc.DaoException;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oTodoDao implements TodoDao{
    private final Sql2o sql2o;

    public Sql2oTodoDao(Sql2o sql2o){
        this.sql2o = sql2o;
    }

    @Override
    public List<Todo> findAll(){
        String sql = "SELECT * FROM todos";
        try (Connection conn = sql2o.open()){
            return conn.createQuery(sql)
                    .executeAndFetch(Todo.class);
        }
    }

    @Override
    public void add(Todo todo) throws DaoException{
        String sql = "INSERT INTO todos (name, completed) VALUES (:name, :completed)";
        try (Connection conn = sql2o.open()){
            int id = (Integer) conn.createQuery(sql)
                    .bind(todo)
//                    .addParameter("name", todo.getName())
//                    .addParameter("completed", todo.isCompleted())
                    .executeUpdate()
                    .getKey();
            todo.setId(id);
        }catch(Sql2oException ex){
            throw new DaoException(ex, "Problem adding todo");
        }
    }

    @Override
    public void update(Todo todo) throws DaoException {
        String sql = "UPDATE todos SET name=:name, completed=:completed WHERE id=:id";
        try (Connection conn = sql2o.open()){
            conn.createQuery(sql)
                    .addParameter("name", todo.getName())
                    .addParameter("completed", todo.isCompleted())
                    .addParameter("id", todo.getId())
                    .executeUpdate();
        }catch(Sql2oException ex){
            throw new DaoException(ex, "Problem updating todo");
        }
    }

    @Override
    public void delete(Todo todo) throws DaoException{
        String sql = "DELETE FROM todos WHERE id=:id";
        try (Connection conn = sql2o.open()){
            conn.createQuery(sql)
                    .addParameter("id", todo.getId())
                    .executeUpdate();
        }catch(Sql2oException ex){
            throw new DaoException(ex, "Problem deleting todo");
        }
    }

    @Override
    public Todo findById(int todo) throws DaoException{
        String sql = "SELECT * FROM todos WHERE id=:id";
        try (Connection conn = sql2o.open()){
            return conn.createQuery(sql)
                    .addParameter("id", todo)
                    .executeAndFetchFirst(Todo.class);
        }catch(Sql2oException ex){
            throw new DaoException(ex, "Problem finding todo");
        }

    }
}
