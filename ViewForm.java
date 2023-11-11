import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Calendar;
import java.util.Vector;

public class ViewForm extends JDialog{
    private JPanel ViewFormPanel;
    private JComboBox ddmonth;
    private JButton enterButton;
    private JTable ExpenseListTable;
    private JButton deleteButton;
    private JTextField txtAmount;
    private JButton editButton;
    private JPanel editpanel;
    private JTextField txtdate;
    private JTextField txtdesc;
    private JTextField txtCategory;
    int selected_month;
    Calendar cal = Calendar.getInstance();
    int mon = cal.get(Calendar.MONTH)+1;
    String dbString;
    Statement statement;
    ExpenseData objdbconn = new ExpenseData();
    Connection var = objdbconn.expensedb_Connection();
    DefaultTableModel defaultTableModel = (DefaultTableModel) ExpenseListTable.getModel();

    public ViewForm(Frame owner) {
        super(owner);
        setTitle("Edit / View List");
        setContentPane(ViewFormPanel);
        setMinimumSize(new Dimension(700, 600));
        setLocationRelativeTo(owner);
        setModal(true);
        setSize(500,300);
        ddmonth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == ddmonth){
                    defaultTableModel.setRowCount(0);
                    ddmonth.setEditable(true);
                    selected_month = ddmonth.getSelectedIndex();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ExpenseListTable.getSelectedRowCount() == 1){

                    String remove_id;
                    remove_id = defaultTableModel.getValueAt(ExpenseListTable.getSelectedRow(),0).toString();
                    System.out.println("rmid " +remove_id );
                    dbString = " DELETE FROM ExpenseDatabase\n" +
                            "WHERE rowid = " + remove_id + "; ";
                    //Statement statement1;
                    try {
                        statement = var.createStatement();
                        statement.executeUpdate(dbString);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    // dbConn(dbString);
                    defaultTableModel.removeRow(ExpenseListTable.getSelectedRow());
                }
                else {
                    if (defaultTableModel.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "Table is empty!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Please Select Atleast one Row to Delete!");
                    }
                }
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ExpenseListTable.getSelectedRowCount() == 1){
                    String date = txtdate.getText();
                    String desc = txtdesc.getText();
                    String Amt = txtAmount.getText();
                    int amt = Integer.parseInt(Amt);
                    String category = txtCategory.getText();
                    String update_id;
                    update_id = defaultTableModel.getValueAt(ExpenseListTable.getSelectedRow(),0).toString();
                    dbString= " UPDATE ExpenseDatabase\n" +
                            " SET date = '" + Date.valueOf(date) +
                            "' , description = '" + desc+
                            "' ,amount = " + amt +
                            " ,category = '"+ category +
                            "' WHERE rowid = " + update_id + ";";
                    try {
                        statement = var.createStatement();
                        statement.executeUpdate(dbString);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    defaultTableModel.setValueAt(date, ExpenseListTable.getSelectedRow(), 1);
                    defaultTableModel.setValueAt(desc, ExpenseListTable.getSelectedRow(), 2);
                    defaultTableModel.setValueAt(Amt, ExpenseListTable.getSelectedRow(), 3);
                    defaultTableModel.setValueAt(category, ExpenseListTable.getSelectedRow(), 4);

                }
                else {
                    if (defaultTableModel.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "Table is empty!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Please Select Atleast one Row to Update!");
                    }
                }
            }
        });
        ExpenseListTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String tabledate = defaultTableModel.getValueAt(ExpenseListTable.getSelectedRow(),1).toString();
                String tabledesc = defaultTableModel.getValueAt(ExpenseListTable.getSelectedRow(),2).toString();
                String tableAmount = defaultTableModel.getValueAt(ExpenseListTable.getSelectedRow(),3).toString();
                String tableCategory = defaultTableModel.getValueAt(ExpenseListTable.getSelectedRow(),4).toString();

                txtdate.setText(tabledate);
                txtdesc.setText(tabledesc);
                txtAmount.setText(tableAmount);
                txtCategory.setText(tableCategory);
            }
        });
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 System.out.println(selected_month);
                 defaultTableModel.setRowCount(0);
                 if (selected_month == 0) {
                            dbString = " Select * From " + objdbconn.dbname + " Order BY rowid ASC;";
                            dbConn(dbString);
                        } else {
                            dbString = " Select * From " + objdbconn.dbname + " where(EXTRACT('MONTH' FROM date) = "+ selected_month +" ) Order BY rowid ASC;";
                            dbConn(dbString);
                        }
            }
        });
        setVisible(true);
    }
    public void dbConn( String sql){
        try{
            statement = var.createStatement();

            ResultSet all_table = statement.executeQuery(sql);
            ResultSetMetaData resultSetMetaData = all_table.getMetaData();

            int num_col = resultSetMetaData.getColumnCount();
            Vector<String> Colm = new Vector<String>(num_col);
            for (int i = 0; i < num_col; i++) {
                Colm.insertElementAt(resultSetMetaData.getColumnName(i + 1), i);
                defaultTableModel.setColumnIdentifiers(Colm);
            }
            String id, date, description, amount, category;
            while (all_table.next()) {
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
        }
        catch (SQLException exe)
        {
            throw new RuntimeException(exe);
        }
    }
  }



