package com.teamtreehouse.techdegrees;


import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.dao.TodoDao;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Sql2o;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        staticFileLocation("/public");
        String datasource = "jdbc:h2:~/todos.db";
        final String API_URI = "/api/v1";

        Sql2o sql2o = new Sql2o(
                String.format("%s;INIT=RUNSCRIPT from 'classpath:db/init.sql'", datasource),
                "", ""
        );
        TodoDao todoDao = new Sql2oTodoDao(sql2o);
        Gson gson = new Gson();

        post(API_URI + "/todos", "application/json", (req, res) ->{
            Todo todo = gson.fromJson(req.body(), Todo.class);
            todoDao.add(todo);
            res.status(201);
            return todo;
        }, gson::toJson);

        get(API_URI + "/todos", "application/json", (req, res) ->
            todoDao.findAll(), gson::toJson);

        put(API_URI + "/todos/:todoId", "application/json", (req, res) ->{
            int todoId = Integer.parseInt(req.params("todoId"));
            Todo todo = gson.fromJson(req.body(), Todo.class);
            todo.setId(todoId);
            todoDao.update(todo);
            res.status(201);
           return todo;
        }, gson::toJson);

        delete(API_URI + "/todos/:todoId", "application/json", (req, res) ->{
            int todoId = Integer.parseInt(req.params("todoId"));
            Todo todo = todoDao.findById(todoId);
            todoDao.delete(todo);
            res.status(201);
            return todo;
        }, gson::toJson);


        after((req, res) ->{
           res.type("application/json");
        });

    }



}
