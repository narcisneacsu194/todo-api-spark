package todos.dao;

import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.model.Todo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import static org.junit.Assert.*;
public class Sql2oTodoDaoTest {
    private Sql2oTodoDao dao;
    private Connection conn;

    @Before
    public void setUp() throws Exception{
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        dao = new Sql2oTodoDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception{
        conn.close();
    }

    @Test
    public void addingTodoSetsId() throws Exception{
        Todo todo = newTestTodo();
        int originalTodoId = todo.getId();

        dao.add(todo);

        assertNotEquals(originalTodoId, todo.getId());
    }

    @Test
    public void addedTodosAreReturnedFromFindAll() throws Exception{
        Todo todo = newTestTodo();

        dao.add(todo);

        assertEquals(1, dao.findAll().size());
    }

    @Test
    public void noTodosReturnsEmptyList() throws Exception{
        assertEquals(0, dao.findAll().size());
    }

    @Test
    public void deletingOnlyTodoReturnsEmptyList() throws Exception{
        Todo todo = newTestTodo();

        dao.add(todo);

        dao.delete(todo);

        assertEquals(0, dao.findAll().size());
    }

    @Test
    public void updatingTodoNameChangesDaoTodoNameVariable() throws Exception{
        Todo todo = newTestTodo();

        dao.add(todo);

        todo.setName("Sleeping");

        dao.update(todo);
        assertEquals(todo.getName(), dao.findById(todo.getId()).getName());
    }

    @Test
    public void updatingTodoCompletedChangesDaoTodoCompletedVariable() throws Exception{
        Todo todo = newTestTodo();

        dao.add(todo);

        todo.setCompleted(false);

        dao.update(todo);

        assertEquals(todo.isCompleted(), dao.findById(todo.getId()).isCompleted());
    }

    private Todo newTestTodo(){
        return new Todo("Brush teeth", true);
    }
}
