import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
public class AddNewForm extends JDialog{
    private JTextField txtdate;
    private JTextField txtDescription;
    private JTextField txtAmount;
    private JRadioButton RBfood;
    private JRadioButton RBmedicine;
    private JRadioButton RBentertainment;
    private JRadioButton RBshopping;
    private JPanel newElement;
    private JButton btnCancel;
    private JButton btnSubmit;
    private String category;
    public AddNewForm(Frame owner ){
        super(owner);
        setTitle("Add New Expense");
        setContentPane(newElement);
        setMinimumSize(new Dimension(500, 500));
        setLocationRelativeTo(owner);
        setModal(true);
        setSize(600,600);
        RBfood.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(RBfood.isSelected()){
                    category = "Food";
                }
            }
        });
        RBshopping.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (RBshopping.isSelected()) {
                    category= "Shopping";
                }
            }
        });
        RBmedicine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (RBmedicine.isSelected()) {
                    category= "Medicine";
                }
            }
        });
        RBentertainment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (RBentertainment.isSelected()) {
                    category = "Entertainment";
                }
            }
        });
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addElement();
            }
            private void addElement() {
                String date = txtdate.getText();
                String description = txtDescription.getText();
                int amount = Integer.parseInt(txtAmount.getText());
                if (date.isEmpty() || description.isEmpty() || amount <= 0) {
                    JOptionPane.showMessageDialog(null,
                            "Enter All Fields with valid values.",
                            "Try Again",
                            JOptionPane.ERROR_MESSAGE);
                }
                ExpenseDatabase = addNewElementDataBase(date, description, amount, category);
                if (ExpenseDatabase != null) {
                    JOptionPane.showMessageDialog(null,
                            "Expense Added",
                            "Status",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Failed to add/edit Expenses",
                            "Status",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            ExpenseData ExpenseDatabase;
            final ExpenseData objdbconn = new ExpenseData();
            public ExpenseData addNewElementDataBase(String date, String description, int amount, String category) {
                 try {
                   final Connection var = objdbconn.expensedb_Connection();
                     if (var != null) {
                         Statement stmt = objdbconn.expensedb_Connection().createStatement();

                         String sql = "INSERT INTO ExpenseDatabase ( date, description, amount, category) " +
                                " VALUES(?,?,?,?)";
                        PreparedStatement preparedStatement = var.prepareStatement(sql);
                        preparedStatement.setDate(1, Date.valueOf(date));
                        preparedStatement.setString(2, description);
                        preparedStatement.setInt(3, amount);
                        preparedStatement.setString(4,category);

                        int addedRows = preparedStatement.executeUpdate();
                        if (addedRows > 0) {
                            ExpenseDatabase = new ExpenseData();
                            ExpenseDatabase.date = date;
                            ExpenseDatabase.description = description;
                            ExpenseDatabase.amount = amount;
                            ExpenseDatabase.category = category;
                        }
                        stmt.close();
                        var.close();
                    }
                }
                catch( Exception e) {
                    e.printStackTrace();
                }
                return ExpenseDatabase;
            }
          });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }
}
