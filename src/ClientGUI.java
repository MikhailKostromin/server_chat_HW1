import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientGUI extends JFrame {
    private static final int WIDTH = 555;
    private static final int HEIGHT = 507;

    private final JTextArea chatTextArea = new JTextArea();

    JPanel panNorthButton = new JPanel(new GridLayout(2, 3));
    JPanel panSouthButton = new JPanel(new BorderLayout());


    JButton btnLogin, btnSend, btnDisable;
    JTextField textIp, textPort, textName, messageField;
    JPasswordField textPassword;


    public ClientGUI(ServerWindow serverWindow) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); //?
        setSize(WIDTH, HEIGHT);
        setTitle("Chat Client");
        setResizable(false);
        btnLogin = new JButton("Login");
        btnSend = new JButton("send");
        btnDisable = new JButton("Disable");
        textIp = new JTextField("127.0.0.1");
        textPort = new JTextField("8189");
        textName = new JTextField("Ivan");
        textPassword = new JPasswordField("sdfsds");
        messageField = new JTextField();
        chatTextArea.setEditable(false);
        messageField.setEnabled(false);
        btnSend.setEnabled(false);
        btnDisable.setEnabled(false);



        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // После нажатия кнопки "Login" делаем текстовые поля и пароль неактивными
                checkServer(serverWindow);
                textIp.setEnabled(false);
                textName.setEnabled(false);
                textPort.setEnabled(false);
                textPassword.setEnabled(false);
                chatTextArea.append("Соединяемся с " + textIp.getText() + ":" + textPort.getText() + "\n"
                                    + "Попытка авторизации: " + textName.getText() + "\n");
                btnLogin.setEnabled(false);

                // Делаем текстовое поле для сообщений и кнопку "Send" активными
                messageField.setEnabled(true);
                btnSend.setEnabled(true);
                btnDisable.setEnabled(true);
            }
        });
        btnDisable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // После нажатия кнопки "Disable" делаем текстовые поля и пароль активными
                textIp.setEnabled(true);
                textName.setEnabled(true);
                textPort.setEnabled(true);
                textPassword.setEnabled(true);
                chatTextArea.append("Переход в оффлайн \n");
                btnSend.setEnabled(true);
                btnLogin.setEnabled(true);

                // Делаем текстовое поле для сообщений и кнопку "Send" неактивными
                messageField.setEnabled(false);
                btnSend.setEnabled(false);
                btnDisable.setEnabled(false);

            }
        });


        panNorthButton.add(textIp);
        panNorthButton.add(textPort);
        panNorthButton.add(btnDisable);
        panNorthButton.add(textName);
        panNorthButton.add(textPassword);
        panNorthButton.add(btnLogin);
//        panNorthButton.add(Box.createHorizontalStrut(10)); //Пустая ячейка
        add(panNorthButton, BorderLayout.NORTH);

        panSouthButton.add(messageField, BorderLayout.CENTER);
        panSouthButton.add(btnSend, BorderLayout.EAST);
        add(panSouthButton, BorderLayout.SOUTH);

        chatTextArea.setEditable(false);//?
        JScrollPane scrollLog = new JScrollPane(chatTextArea); //?
        add(scrollLog);
        setVisible(true);


    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            String formattedMessage = formatMessage(message);
            chatTextArea.append(formattedMessage + "\n");
            saveMessageToLogFile(formattedMessage);
            messageField.setText(""); /// установление пустой строки после ввода
        }
    }


    private String formatMessage(String message) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.format(formatter);
        return "Name: " + textName.getText() + " [" + formattedTime + "]: >  " + message;
    }

    private void saveMessageToLogFile(String message) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedTime = now.format(formatter);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(formattedTime + " __chat_log.txt", true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkServer(ServerWindow serverWindow) {
        boolean serverIsWorking = serverWindow.isServerWorking();
        if (serverIsWorking == false) {
            btnLogin.setEnabled(false);
            chatTextArea.append("Сервер выключен. Пожалуйста, включите сервер, прежде чем войти.\n");
        } else {
            btnLogin.setEnabled(true);
            chatTextArea.append("Сервер включен. Теперь вы можете войти в чат.\n");
        }
    }
}


