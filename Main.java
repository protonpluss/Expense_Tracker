import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main extends JFrame {
    public static void main(String[] args) {

        ExpenseData objdbconn = new ExpenseData();
        try (Connection var = objdbconn.expensedb_Connection()) {
            objdbconn.expensedb_Connection();
            objdbconn.Create_ExpenseDB(var);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ExpenseTracker expenseTracker = new ExpenseTracker(null);

    }
}