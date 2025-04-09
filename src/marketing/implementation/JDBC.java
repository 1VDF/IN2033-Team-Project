package marketing.implementation;

import boxoffice.database.TicketSale;
import operations.implementation.OperationsAccessImplementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JDBC {

    private final Connection connection;

    private MarketingAccessImpl marketingData;

    public JDBC() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://sst-stuproj.city.ac.uk/in2033t25";
        String user = "in2033t25_d";
        String pass = "N9CfacRwhIM";

        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection(url,user,pass);
        this.marketingData = new MarketingAccessImpl();
    }

    public List<TicketSale> getTicketSalesBasedOnEvent(String performanceName) throws SQLException {
        return marketingData.getTicketSalesBasedOnEvent(connection,performanceName);
    }

    public int getRevenueBasedOnEvent(String performanceName) {
        return marketingData.getRevenueBasedOnEvent(connection,performanceName);
    }

    public Map<String, Object> getMonthlyRevenueReport(int year, int month) throws SQLException {
        return marketingData.getMonthlyRevenueReport(connection,year,month);
    }

    public void close() throws SQLException{
        if(connection != null){
            connection.close();
        }
    }

    public static void main(String[] args) {
    }
}
