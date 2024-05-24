package co.edu.poli.ces3.momentovalorativo.database.repositories;

import co.edu.poli.ces3.momentovalorativo.database.ConexionMySql;
import co.edu.poli.ces3.momentovalorativo.database.dao.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskRepository {

    private ConexionMySql conexionMySql;

    public TaskRepository() {
        conexionMySql = new ConexionMySql("localhost");
    }

    public List<Task> getAllTasks() throws SQLException {
        Connection con = conexionMySql.conexion();
        Statement sts = con.createStatement();
        ResultSet rs = sts.executeQuery("SELECT * FROM tasks");
        List<Task> list = new ArrayList<>();

        while (rs.next()){
            list.add(new Task(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getInt("priority"),
                    rs.getInt("id_project"),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at"),
                    rs.getDate("deleted_at")
            ));
        }

        rs.close();
        sts.close();
        con.close();

        return list;
    }

    public List<Task> getTasksByProject(int projectId) throws SQLException {
        Connection con = conexionMySql.conexion();
        PreparedStatement sts = con.prepareStatement("SELECT * FROM tasks WHERE id_project = ?");
        sts.setInt(1, projectId);
        ResultSet rs = sts.executeQuery();
        List<Task> list = new ArrayList<>();

        while (rs.next()){
            list.add(new Task(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getInt("priority"),
                    rs.getInt("id_project"),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at"),
                    rs.getDate("deleted_at")
            ));
        }

        rs.close();
        sts.close();
        con.close();

        return list;
    }

    public Map<Integer, Integer> getQuantityTasksByProject() throws SQLException {
        Connection con = conexionMySql.conexion();
        Statement sts = con.createStatement();
        ResultSet rs = sts.executeQuery("SELECT id_project, COUNT(*) as quantity FROM tasks GROUP BY id_project");
        Map<Integer, Integer> quantityMap = new HashMap<>();

        while (rs.next()){
            int projectId = rs.getInt("id_project");
            int quantity = rs.getInt("quantity");
            quantityMap.put(projectId, quantity);
        }

        rs.close();
        sts.close();
        con.close();

        return quantityMap;
    }
}
