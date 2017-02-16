package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exc.DaoException;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oTodoDao implements TodoDao{
    private Sql2o sql2o;

    public Sql2oTodoDao(Sql2o sql2o){
        this.sql2o = sql2o;
    }

    @Override
    public List<Todo> findAll() throws DaoException{
        String sql = "SELECT * FROM todos";
        try (Connection conn = sql2o.open()){
            return conn.createQuery(sql)
                    .executeAndFetch(Todo.class);
        }catch(Sql2oException ex){
            throw new DaoException(ex, "Problem retrieving todos");
        }
    }

    @Override
    public void save(Todo todo) throws DaoException{
        String sql = "INSERT INTO todos (name, done) VALUES (:name, :done)";
        try (Connection conn = sql2o.open()){
            int id = (Integer) conn.createQuery(sql)
                    .bind(todo)
                    .executeUpdate()
                    .getKey();
            todo.setId(id);
        }catch(Sql2oException ex){
            throw new DaoException(ex, "Problem adding todo");
        }
    }

    @Override
    public void update(Todo todo) throws DaoException {
        String sql = "UPDATE todos SET name=:name, done=:done WHERE id=:id";
        try (Connection conn = sql2o.open()){
            conn.createQuery(sql)
                    .bind(todo)
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
                    .bind(todo)
                    .executeUpdate();
        }catch(Sql2oException ex){
            throw new DaoException(ex, "Problem deleting todo");
        }
    }
}
