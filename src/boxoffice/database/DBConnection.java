package boxoffice.database;
/**
 * This interface represents the credentials used for the database connection.
 * <p>
 *      Fields within an interface are always public, static and final.
 *      Therefore this interface holds constant values which are used to connect to the database.
 * </p>
 *
 * @author Denis Volocaru
 * @version 1.0
 */
//public interface DBConnection{
//    /**
//     * This is the url used to connect to the database(MySQL).
//     */
//    String url = "jdbc:mysql://sst-stuproj.city.ac.uk:3306/in2033t25";
//
//    /**
//     * This is the username used to connect to the database(MySQL).
//     */
//    String user = "in2033t25_a";
//    /**
//     * This is the password used to connect to the database(MySQL).
//     */
//    String pass = "UpCB1Q2vA4I";
//}


public interface DBConnection {
    /**
     * URL for local phpMyAdmin MySQL database.
     */
    String url = "jdbc:mysql://localhost:3306/in2033t25";

    /**
     * Username for local MySQL database (default is 'root').
     */
    String user = "root";

    /**
     * Password for local MySQL database (default is empty).
     */
    String pass = "";
}

