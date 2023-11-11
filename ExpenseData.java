import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
public class ExpenseData {
    public String date ;
    public String description;
    public int amount;
    public String category;
    final String dbname = "ExpenseDatabase";
    public Connection expensedb_Connection(){
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn =   DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname);
            if(conn != null)
            {
                    System.out.println("COnnection Built");

            }
            else System.out.println("Connection Broke");
        }
        catch (Exception e){
            System.out.println(e);
        }
        return conn;
    }
    public void Create_ExpenseDB(Connection conn){
        try {
            Statement statement;
            String sql = "Create Table " + dbname + " (rowid BIGSERIAL NOT NULL, date date, description varchar(20), amount Integer, category varchar(20), PRIMARY KEY(rowid));";
            statement = conn.createStatement();
            statement.executeUpdate(sql);

        }catch (Exception e){
            System.out.println(e);
        }
    }
}

