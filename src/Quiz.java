import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

public class Quiz extends JFrame {
    ArrayList<Question> questions;
    private int count = 0;
    private int score = 0;

    public Quiz() {
        try {
            questions = DataBase.getQuestionAns();
            initialize();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        setTitle("Welcome to Quiz");
        setSize(600, 500);
        setLayout(new BorderLayout());

        JPanel quePanel = new JPanel();
        quePanel.setLayout(new BoxLayout(quePanel, BoxLayout.X_AXIS));
        getContentPane().add(quePanel, BorderLayout.NORTH);

        JTextArea queTextArea = new JTextArea();
        queTextArea.setFont(new Font("Dialog", Font.BOLD, 20));
        queTextArea.setLineWrap(true);
        queTextArea.setWrapStyleWord(true);
        quePanel.add(queTextArea);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(0, 2, 0, 0));
        getContentPane().add(optionsPanel, BorderLayout.CENTER);

        JRadioButton rdbtnOp1 = new JRadioButton("Option 1");
        rdbtnOp1.setFont(new Font("Dialog", Font.BOLD, 20));
        optionsPanel.add(rdbtnOp1);

        JRadioButton rdbtnOp2 = new JRadioButton("Option 2");
        rdbtnOp2.setFont(new Font("Dialog", Font.BOLD, 20));
        optionsPanel.add(rdbtnOp2);

        JRadioButton rdbtnOp3 = new JRadioButton("Option 3");
        rdbtnOp3.setFont(new Font("Dialog", Font.BOLD, 20));
        optionsPanel.add(rdbtnOp3);

        JRadioButton rdbtnOp4 = new JRadioButton("Option 4");
        rdbtnOp4.setFont(new Font("Dialog", Font.BOLD, 20));
        optionsPanel.add(rdbtnOp4);

        ButtonGroup bg = new ButtonGroup();
        bg.add(rdbtnOp1);
        bg.add(rdbtnOp2);
        bg.add(rdbtnOp3);
        bg.add(rdbtnOp4);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(0, 1, 0, 0));
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

        JButton btnNext = new JButton("Next");
        btnNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (bg.getSelection() == null) {
                    JOptionPane.showMessageDialog(quePanel, "Please select an Answer");
                } else {
                    checkAnswer(count, bg);
                    count++;
                    if (questions.size() > count) {
                        queTextArea.setText(questions.get(count).getQuestion());
                        rdbtnOp1.setText(questions.get(count).getOp1());
                        rdbtnOp2.setText(questions.get(count).getOp2());
                        rdbtnOp3.setText(questions.get(count).getOp3());
                        rdbtnOp4.setText(questions.get(count).getOp4());
                    } else {
                        displayScore();
                    }
                }
            }
        });
        buttonsPanel.add(btnNext);

        setVisible(true);
    }

    private void checkAnswer(int count, ButtonGroup bg) {
        for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected() && button.getText().equals(questions.get(count).getAns())) {
                score++;
            }
        }
    }

    private void displayScore() {
        dispose();
        JOptionPane.showMessageDialog(this, "Thanks for playing the Quiz by ProjectGurukul\nYour Score was: " + score, "Quiz by ProjectGurukul", JOptionPane.PLAIN_MESSAGE);
    }
}
