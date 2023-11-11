import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.Month;
import java.util.Calendar;
import java.util.Vector;

public class ExpenseTracker extends JDialog {
    private JButton btnView_Edit;
    private JButton btnAdd;
    private JPanel MainPanel;
    private JTable TableCurrentMonth;
    private JLabel lbCurrentTotal;
    private JLabel labelMainPage;
    private JTextArea textAreaTotal;
    Calendar cal = Calendar.getInstance();
    int mon = cal.get(Calendar.MONTH)+1;

    Month currentM = Month.of(cal.get(Calendar.MONTH)+1);
    ExpenseData objdbconn = new ExpenseData();
    Statement statement;

    public ExpenseTracker(Frame owner) {
        super(owner);
        setTitle("Expense Tracker Application");
        setContentPane(MainPanel);
        setMinimumSize(new Dimension(500, 500));
        setSize(560,500);
        setLocationRelativeTo(owner);
        setVisible(true);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        labelMainPage.setText("Expense list for " + currentM + " month ");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewForm obj = new addNewForm(null);
            }
        });

        btnView_Edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("click");
                ViewForm obj = new ViewForm(null);
            }
        });

        try {
            Connection var = objdbconn.expensedb_Connection();
            String sqltableVals = "Select * From " + objdbconn.dbname + " where(EXTRACT('MONTH' FROM date) = " + mon + " ) Order BY rowid ASC;";
            statement = var.createStatement();
            ResultSet all_table = statement.executeQuery(sqltableVals);
            ResultSetMetaData resultSetMetaData = all_table.getMetaData();
            DefaultTableModel defaultTableModel = (DefaultTableModel) TableCurrentMonth.getModel();

            int num_col = resultSetMetaData.getColumnCount();
            Vector<String> Colm = new Vector<String>(num_col);
            for (int i =0; i< num_col; i++)
            {
                Colm.insertElementAt(resultSetMetaData.getColumnName(i+1), i);
                defaultTableModel.setColumnIdentifiers(Colm);
            }

            String id, date, description, amount, category;
            while (all_table.next()){
                id = all_table.getString(1);
                date = all_table.getString(2);
                description = all_table.getString(3);
                amount = all_table.getString(4);
                category = all_table.getString(5);
                Vector<String> row = new Vector<String>();
                row.add(id);
                row.add(date);
                row.add(description);
                row.add(amount);
                row.add(category);
                defaultTableModel.addRow(row);
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            Connection var = objdbconn.expensedb_Connection();
            String sum = "SELECT SUM(amount) AS total_price From " + objdbconn.dbname + " where(EXTRACT('MONTH' FROM date) = " + mon + " ); ";
            statement = var.createStatement();
            PreparedStatement ps = var.prepareStatement(sum);
            ResultSet sum_amount = statement.executeQuery(sum);
            DefaultTableModel defaultTableModel = (DefaultTableModel) TableCurrentMonth.getModel();

           while(sum_amount.next())
            {
                textAreaTotal.setText(sum_amount.getString(1));
            }

            statement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
