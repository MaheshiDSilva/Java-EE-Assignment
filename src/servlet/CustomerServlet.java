package servlet;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = {"/pages/customer"})
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("select * from Customer");
            ResultSet rst = pstm.executeQuery();

            resp.addHeader("Content-Type", "application/json");

            JsonArrayBuilder allCustomers = Json.createArrayBuilder();
            while (rst.next()) {
                String id = rst.getString(1);
                String name = rst.getString(2);
                String address = rst.getString(3);
                String salary = rst.getString(4);

                JsonObjectBuilder customerObject = Json.createObjectBuilder();
                customerObject.add("id", id);
                customerObject.add("name", name);
                customerObject.add("address", address);
                customerObject.add("salary", salary);
                allCustomers.add(customerObject.build());
            }

            resp.getWriter().print(allCustomers.build());


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cusID = req.getParameter("cusID");
        String cusName = req.getParameter("cusName");
        String cusAddress = req.getParameter("cusAddress");
        String cusSalary = req.getParameter("cusSalary");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade", "root", "1234");

            PreparedStatement pstm = connection.prepareStatement("insert into Customer values(?,?,?,?)");
            pstm.setObject(1, cusID);
            pstm.setObject(2, cusName);
            pstm.setObject(3, cusAddress);
            pstm.setObject(4, cusSalary);
            resp.addHeader("Content-Type", "application/json");
            if (pstm.executeUpdate() > 0) {
//                        resp.getWriter().println("Customer Added..!");
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("state:", "OK");
                objectBuilder.add("message:", "Successfully Added...");
                objectBuilder.add("data", "");
                resp.getWriter().print(objectBuilder.build());
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("state:", "Error");
            objectBuilder.add("message:", e.getMessage());
            objectBuilder.add("data", "");
            resp.setStatus(400);
            resp.getWriter().print(objectBuilder.build());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade", "root", "1234");

            String cusID = req.getParameter("cusID");
            String cusName = req.getParameter("cusName");
            String cusAddress = req.getParameter("cusAddress");
            String cusSalary = req.getParameter("cusSalary");
            PreparedStatement pstm3 = connection.prepareStatement("update Customer set name=?,address=?,salary=? where id=?");
            pstm3.setObject(4, cusID);
            pstm3.setObject(1, cusName);
            pstm3.setObject(2, cusAddress);
            pstm3.setObject(3, cusSalary);
            resp.addHeader("Content-Type", "application/json");
            if (pstm3.executeUpdate() > 0) {
//                        resp.getWriter().println("Customer Updated..!");
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("state:", "OK");
                objectBuilder.add("message:", "Successfully Updated...");
                objectBuilder.add("data", "");
                resp.getWriter().print(objectBuilder.build());
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade", "root", "1234");

            String cusID = req.getParameter("cusID");
            PreparedStatement pstm2 = connection.prepareStatement("delete from Customer where id=?");
            pstm2.setObject(1, cusID);
            resp.addHeader("Content-Type", "application/json");
            if (pstm2.executeUpdate() > 0) {
//                        resp.getWriter().println("Customer Updated..!");
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("state:", "OK");
                objectBuilder.add("message:", "Successfully Deleted...");
                objectBuilder.add("data", "");
                resp.getWriter().print(objectBuilder.build());
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }
}
