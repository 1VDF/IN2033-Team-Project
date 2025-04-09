package operations.implementation;

import boxoffice.database.TicketSale;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JDBC {

    private final Connection connection;

    private OperationsAccessImplementation operationsData;

    public JDBC() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://sst-stuproj.city.ac.uk/in2033t25";
        String user = "in2033t25_d";
        String pass = "N9CfacRwhIM";

        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection(url,user,pass);
        this.operationsData = new OperationsAccessImplementation();
    }

    public List<TicketSale> getTicketSalesBasedOnEvent(String performanceName) throws SQLException {
        return operationsData.getTicketSalesBasedOnEvent(connection,performanceName);
    }

    public int getRevenueBasedOnEvent(String performanceName) {
        return operationsData.getRevenueBasedOnEvent(connection,performanceName);
    }

    public Map<String, Object> getMonthlyRevenueReport(int year, int month) throws SQLException {
        return operationsData.getMonthlyRevenueReport(connection,year,month);
    }

    public List<TicketSale> getAllTicketSales() throws SQLException {
        return operationsData.getAllTicketSales(connection);
    }

    public int getTotalRevenue() throws SQLException{
        return operationsData.getTotalRevenue(connection);
    }

    public void close() throws SQLException{
        if(connection != null){
            connection.close();
        }
    }

    public static void main(String[] args) {
    }
}
