import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Main {
    public static final String CONFIG = "src/config.properties";
    public static final String CONTENT_FILE = "src/content.txt";

    public static void main(String[] args){
        FileInputStream fileInputStream;
        Properties props = new Properties();

        try {

            fileInputStream = new FileInputStream(CONFIG);
            props.load(fileInputStream);
            String content = new String(Files.readAllBytes(Paths.get(CONTENT_FILE)));

            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(props.getProperty("userName"), props.getProperty("password"));
                        }
                    });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(props.getProperty("from_email")));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(props.getProperty("to_email")));
            message.setSubject("Тестовое задание");
            message.setText(String.format(content, props.getProperty("to_full_name"), props.getProperty("number")));

            Transport.send(message);

            System.out.println("Отправлено!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }  catch (IOException e) {
            System.out.println("Ошибка в программе: файла " + CONFIG + " или " + CONTENT_FILE + " не обнаружено!");
            e.printStackTrace();
        }
    }
}