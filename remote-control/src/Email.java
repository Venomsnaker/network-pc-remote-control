import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Store;


public class Email {

    static final String from  = "g4.22tnt1.hcmus@gmail.com";
    static final String password = "xpfabvasrrgbqmta";

    public static boolean sendEmail(String to, String tieuDe, String noiDung) {
        // Properties : khai báo các thuộc tính
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP HOST
        props.put("mail.smtp.port", "587"); // TLS 587 SSL 465
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // TODO Auto-generated method stub
                return new PasswordAuthentication(from, password);
            }
        };

        Session session = Session.getInstance(props, auth);

        MimeMessage msg = new MimeMessage(session);

        try {
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");

            msg.setFrom(from);

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

            msg.setSubject(tieuDe);

            msg.setSentDate(new Date());

            msg.setContent(noiDung, "text/HTML; charset=UTF-8");

            Transport.send(msg);
            System.out.println("Gửi email thành công");
            return true;
        } catch (Exception e) {
            System.out.println("Gặp lỗi trong quá trình gửi email");
            e.printStackTrace();
            return false;
        }
    }

    private static Properties getServerProperties(String protocol, String host, String port) {
        Properties props = new Properties();
        props.put(String.format("mail.%s.host", protocol), host);
        props.put(String.format("mail.%s.port", protocol), port);

        props.setProperty(
                String.format("mail.%s.socketFactory.class", protocol),
                "javax.net.ssl.SSLSocketFactory");

        props.setProperty(
                String.format("mail.%s.socketFactory.fallback", protocol),
                "false");

        props.setProperty(
                String.format("mail.%s.socketFactory.port", protocol),
                String.valueOf(port));

        return props;
    }

    public static String[] getInfo(String protocol, String host, String port, String userName, String password) {
        String[] ans = {"", ""};

        Properties props = getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(props);

        try {
            Store store = session.getStore(protocol);
            store.connect(userName, password);

            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);

            Message[] messages = folderInbox.getMessages();


            Message msg = messages[messages.length - 1];
            Address[] fromAddress = msg.getFrom();
            String from = fromAddress[0].toString();
            String subject = msg.getSubject();
            String contentType = msg.getContentType();
            String messageContent = "";

            if (contentType.contains("text/html"))
            {
                try {
                    Object content = msg.getContent();
                    if (content != null) {
                        messageContent = content.toString();
                    }
                } catch (Exception ex) {
                    messageContent = "!Error downloading content";
                    ex.printStackTrace();
                }
            }
            ans[0] = subject;
            ans[1] = from;
        }
        catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol " + protocol);
            ex.printStackTrace();
        }
        catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        }

        return ans;
    }


    public static void main(String[] args) {
        String protocol = "imap";
        String host = "imap.gmail.com";
        String port = "993";

        String username = "g4.22tnt1.hcmus@gmail.com";
        String password = "xpfabvasrrgbqmta";

//        Email.sendEmail("vantuankiet.hs@gmail.com", "try", "Day la noi dung thu");

        String[] tmp =  Email.getInfo(protocol, host, port, username, password);
        System.out.println(tmp[0]);
        System.out.println(tmp[1]);

    }

}
