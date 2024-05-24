package co.edu.poli.ces3.momentovalorativo.servlets;

import co.edu.poli.ces3.momentovalorativo.database.repositories.TaskRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "TaskServlet", value = "/tasks")
public class TaskServlet extends HttpServlet {

    private GsonBuilder gsonBuilder;
    private Gson gson;
    private TaskRepository taskRepository;

    public void init(){
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        taskRepository = new TaskRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String servletPath = req.getServletPath();
        switch (servletPath) {
            case "/tasks":
                try {
                    out.print(gson.toJson(taskRepository.getAllTasks()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/tasks-project":
                // Ruta /tasks-project?idProject=?
                String idProjectParam = req.getParameter("idProject");
                if (idProjectParam != null) {
                    int projectId = Integer.parseInt(idProjectParam);
                    try {
                        out.print(gson.toJson(taskRepository.getTasksByProject(projectId)));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.println("Error: el parámetro idProject es obligatorio para la ruta /tasks-project");
                }
                break;
            case "/quantity-tasks-project":
                // Ruta /quantity-tasks-project
                try {
                    out.print(gson.toJson(taskRepository.getQuantityTasksByProject()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                // Ruta no válida
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.println("Error: Ruta no encontrada.");
                break;
        }

        out.flush();
    }
}