package database;
import java.sql.*;

public class BD {
  public BD() {}

  /**
   * Łączy program z bazą danych. W przypadku niepowodzenia wyrzuca wyjątek i kończy działanie programu.
   * @return Connection
   */
  public static Connection connect() {
  Connection con = null;

  try {
    con= DriverManager.getConnection("jdbc:postgresql://ella.db.elephantsql.com:5432/zkzcdnsm",
                                    "zkzcdnsm", "9-WoiS7u9vT0Wmzv2u7oqMEMu9SluOaS");
  } catch (SQLException se) {
    System.out.println("Brak polaczenia z baza danych, wydruk logu sledzenia i koniec.");
    se.printStackTrace();
    System.exit(1);
  }
  if (con == null) {
    System.out.println("Brak polaczenia z baza, dalsza czesc aplikacji nie jest wykonywana. ");
  }

    return con;
  }

  /**
   * Zamyka polaczenie i PreparedStatement.
   * @param con
   * @param pst
   */
  public static void close(Connection con, PreparedStatement pst) {
    if (pst != null) {
      try {
        pst.close();
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }
    if (con != null) {
      try {
        con.close();
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }
  }

}