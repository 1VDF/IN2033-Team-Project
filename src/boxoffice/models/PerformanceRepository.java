package boxoffice.models;

import boxoffice.database.DBConnection;
import boxoffice.database.Performance;
import boxoffice.database.Seat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerformanceRepository {

    public PerformanceRepository(){
    }
    private static List<Performance> readPerformanceColumns(ResultSet rs) throws SQLException {
        List<Performance> performances = new ArrayList<>();
        while(rs.next()){
            performances.add(new Performance(rs.getInt("performance_id"),
                    rs.getString("title"),
                    rs.getString("performance_type"),
                    rs.getString("description"),
                    rs.getDate("date").toLocalDate(),
                    rs.getTime("start_time").toLocalTime(),
                    rs.getInt("duration_minutes")));
        }
        for (Performance performance : performances) {
            System.out.println(performance);
        }
        rs.close();
        return performances;
    }

    public List<Performance> getAllPerformances() throws SQLException{
        try (Connection connection = DriverManager.getConnection(DBConnection.url, DBConnection.user, DBConnection.pass);
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            String Select_Statement = "SELECT * FROM `performance`";
            ResultSet rs = statement.executeQuery(Select_Statement);
            return readPerformanceColumns(rs);
        }
    }
}
