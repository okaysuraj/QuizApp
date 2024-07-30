import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class AdminPanel extends JFrame {
    private JTextField option1Field, option2Field, option3Field, option4Field, answerField, remIDfield;

    public AdminPanel() {
        setTitle("Admin Panel");
        setSize(635, 500);
        setLayout(new BorderLayout(0, 0));

        JPanel deletePanel = new JPanel(new GridLayout(0, 2, 0, 0));
        deletePanel.setBackground(new Color(53, 132, 228));
        add(deletePanel, BorderLayout.NORTH);

        JComboBox<String> comboBox = new JComboBox<>(new String[]{"users", "question"});
        deletePanel.add(comboBox);

        remIDfield = new JTextField();
        deletePanel.add(remIDfield);
        remIDfield.setColumns(10);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 0, 10));
        inputPanel.setBackground(new Color(53, 132, 228));
        add(inputPanel, BorderLayout.CENTER);

        inputPanel.add(new JLabel("Question:"));
        JTextArea queTextArea = new JTextArea();
        inputPanel.add(new JScrollPane(queTextArea));

        inputPanel.add(new JLabel("Option 1:"));
        option1Field = new JTextField();
        inputPanel.add(option1Field);

        inputPanel.add(new JLabel("Option 2:"));
        option2Field = new JTextField();
        inputPanel.add(option2Field);

        inputPanel.add(new JLabel("Option 3:"));
        option3Field = new JTextField();
        inputPanel.add(option3Field);

        inputPanel.add(new JLabel("Option 4:"));
        option4Field = new JTextField();
        inputPanel.add(option4Field);

        inputPanel.add(new JLabel("Answer:"));
        answerField = new JTextField();
        inputPanel.add(answerField);

        JPanel buttonsPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        buttonsPanel.setBackground(new Color(53, 132, 228));
        add(buttonsPanel, BorderLayout.EAST);

        JButton btnAddQue = new JButton("Add Question");
        btnAddQue.addActionListener(e -> {
            try {
                String[] options = {option1Field.getText(), option2Field.getText(), option3Field.getText(), option4Field.getText()};
                DataBase.addQuestion(queTextArea.getText(), options, answerField.getText());
                JOptionPane.showMessageDialog(btnAddQue, "Question Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(btnAddQue, "Can't add Question\n" + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        buttonsPanel.add(btnAddQue);

        JButton btnRemove = new JButton("Remove");
        btnRemove.addActionListener(e -> {
            try {
                DataBase.delete(remIDfield.getText(), (String) comboBox.getSelectedItem());
                JOptionPane.showMessageDialog(btnRemove, "Deleted Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(btnRemove, "Delete Error\n" + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });
        buttonsPanel.add(btnRemove);

        JButton btnViewAllQuestions = new JButton("View All Questions");
        btnViewAllQuestions.addActionListener(e -> showAllQuestions());
        buttonsPanel.add(btnViewAllQuestions);

        JButton btnExit = new JButton("Exit");
        btnExit.addActionListener(e -> System.exit(0));
        buttonsPanel.add(btnExit);

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            dispose();
            new AdminLogin();
        });
        buttonsPanel.add(btnLogout);
        setVisible(true);
    }

    protected void showAllQuestions() {
        JFrame frame = new JFrame("Question List");
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        try {
            ArrayList<Question> questions = DataBase.getQuestionAns();
            JTextArea qTextArea = new JTextArea();
            qTextArea.setLineWrap(true);
            qTextArea.setWrapStyleWord(true);
            qTextArea.setEditable(false);
            JScrollPane scroll = new JScrollPane(qTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            frame.add(scroll);

            for (Question question : questions) {
                qTextArea.append("\nQ. " + question.getQuestion() + "\n"
                        + "1. " + question.getOp1() + "\n"
                        + "2. " + question.getOp2() + "\n"
                        + "3. " + question.getOp3() + "\n"
                        + "4. " + question.getOp4() + "\n"
                        + "Ans. " + question.getAns() + "\n"
                        + "---------------------------------------------");
            }
            frame.setSize(300, 300);
            frame.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
