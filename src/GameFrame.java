import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.io.*;
import javax.swing.*;
import java.sql.*;

public class GameFrame extends JFrame{
    private JButton a5050Button;
    private JButton HelpButton;
    private JButton CallButton;
    private JButton MoneyButton;
    private JList lstLevel;
    private JTextArea QuestionText;
    private JButton BButton;
    private JButton CButton;
    private JButton DButton;
    private JButton AButton;
    private JPanel mainPanel;
    ArrayList<Question> questions = new ArrayList<Question>();
    private Random  rnd = new Random();
    int Level =0;
    Question currentQuestion;
    public GameFrame(){
        setContentPane(mainPanel);
        setTitle("Welcome");
        setSize(1000, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        ReadFile();
        File2Db();
        startGame();
        a5050Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton[] btns = new JButton[]{AButton, BButton,
                        CButton, DButton};

                int count = 0;
                while (count<2)
                {
                    int n = rnd.nextInt(4);
                    String ac = btns[n].getActionCommand();

                    if (!ac.equals(currentQuestion.RightAnswer)
                            && btns[n].isEnabled())
                    {
                        btns[n].setEnabled(false);
                        count++;
                    }
                }

            }
        });
        AButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (currentQuestion.RightAnswer.equals(e.getActionCommand()))
                    NextStep();
                else
                {
                    JOptionPane.showMessageDialog(getRootPane(), "Неверный ответ!");
                    startGame();
                }
            }
        });
        BButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentQuestion.RightAnswer.equals(e.getActionCommand()))
                    NextStep();
                else
                {
                    JOptionPane.showMessageDialog(getRootPane(), "Неверный ответ!");
                    startGame();
                }
            }
        });
        CButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentQuestion.RightAnswer.equals(e.getActionCommand()))
                    NextStep();
                else
                {
                    JOptionPane.showMessageDialog(getRootPane(), "Неверный ответ!");
                    startGame();
                }
            }
        });
        DButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentQuestion.RightAnswer.equals(e.getActionCommand()))
                    NextStep();
                else
                {
                    JOptionPane.showMessageDialog(getRootPane(), "Неверный ответ!");
                    startGame();
                }
            }
        });
    }
    private void ReadFile()
    {
        try{
            FileInputStream fstream = new FileInputStream("C:\\Users\\ponki\\IdeaProjects\\untitled\\questions.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;

            while ((strLine = br.readLine()) != null) {
                String[] s = strLine.split("\t");
                questions.add(new Question(s));
            }
        } catch (IOException e) {
            System.out.println("Ошибка");
        }
    }
    private void File2Db(){
        Connection conn = null;
        try{
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:C:\\Users\\ponki\\sqlite\\millioner.db";
            conn = DriverManager.getConnection(url);

            FileInputStream fstream = new FileInputStream("C:\\Users\\ponki\\IdeaProjects\\untitled\\questions.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;

            while ((strLine = br.readLine()) != null) {
                Statement statement = conn.createStatement();
                statement.executeUpdate("INSERT INTO questions " + "VALUES ('" + strLine +"')");
            }

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    private void ReadDatabase(){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\ponki\\sqlite\\milloner.db");

            Statement statmt = conn.createStatement();
            String query = "select question from millioner";

            ResultSet resSet = statmt.executeQuery(query);


            while (resSet.next()) {
                String strLine = resSet.getString(0);
                String[] s = strLine.split("\t");
                questions.add(new Question(s));
            }

            resSet.close();
            conn.close();

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }
    private void ShowQuestion(Question q)
    {
        QuestionText.setText(q.Text);
        AButton.setText(q.Answers[0]);
        BButton.setText(q.Answers[1]);
        CButton.setText(q.Answers[2]);
        DButton.setText(q.Answers[3]);
    }
    private Question GetQuestion(int level)
    {
        List<Question> list =
                questions.stream().filter(q->q.Level==level).collect(Collectors.toList());
        return list.get(rnd.nextInt(list.size()));
    }
    private void NextStep()
    {
        JButton[] btns = new JButton[]{AButton, BButton,
                CButton, DButton};

        for(JButton btn: btns)
            btn.setEnabled(true);

        Level++;
        currentQuestion = GetQuestion(Level);
        ShowQuestion(currentQuestion);
        lstLevel.setSelectedIndex(lstLevel.getModel().getSize()-Level);
    }
    private void startGame()
    {
        Level = 0;
        NextStep();
    }
}