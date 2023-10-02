import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerWindow extends JFrame {
    private static final int WIDTH = 555;
    private static final int HEIGHT = 507;
    private boolean isServerWorking;
    private final JTextArea textArea = new JTextArea(20, 100);
    JButton btnStart, btnStop;


    ServerWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setTitle("Chat server");
        setResizable(false);
        btnStart = new JButton("Start");
        btnStop = new JButton("Stop");
        textArea.setEnabled(false);


        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                textArea.append("Server stopped \n");
                isServerWorking = false;
                saveStatusServerToLogFile(formatStatusServer());

            }
        });
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnStart.setEnabled(false);
                textArea.append("Server started  \n");
                isServerWorking = true;
                saveStatusServerToLogFile(formatStatusServer());
                btnStop.setEnabled(true);
            }
        });
        JPanel panButton = new JPanel(new GridLayout(1, 2));
        panButton.add(btnStart);
        panButton.add(btnStop);

        add(panButton, BorderLayout.SOUTH);
        add(textArea);

        setVisible(true);
    }

    private void saveStatusServerToLogFile(String string) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedTime = now.format(formatter);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(formattedTime + "__server_log.txt", true))) {
            writer.write(string);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatStatusServer() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.format(formatter);
        return "[" + formattedTime + "]: >" + textArea.getText();
    }

    public boolean isServerWorking() {
        return isServerWorking;
    }
}