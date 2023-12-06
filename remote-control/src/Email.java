import java.util.*;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import javax.mail.Multipart;

public class Email {

    static final String from  = "g4.22tnt1.hcmus@gmail.com";
    static final String password = "xpfabvasrrgbqmta";
    static final String username = from;
    //    static final String protocol = "imap";
//    static final String host = "imap.gmail.com";
//    static final String get_port = "993";
    static final String protocol = "pop3";
    static final String host = "pop.gmail.com";
    static final String get_port = "995";

    public static boolean sendEmail(String to, String tieuDe, String noiDung, String fileName) {
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

            BodyPart msgText = new MimeBodyPart();
            msgText.setText(noiDung);
            BodyPart msgFile = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(msgText);

            if (!fileName.isEmpty()) {
                DataSource source = new FileDataSource(fileName);
                msgFile.setDataHandler(new DataHandler(source));
                msgFile.setFileName(fileName);
                multipart.addBodyPart(msgFile);
            }

            msg.setContent(multipart);

            Transport.send(msg);
            System.out.println("Gửi email thành công");
            return true;
        } catch (Exception e) {
            System.out.println("Gặp lỗi trong quá trình gửi email");
            e.printStackTrace();
            return false;
        }
    }

    private static Properties getServerProperties() {
        Properties props = new Properties();
        props.put(String.format("mail.%s.host", protocol), host);
        props.put(String.format("mail.%s.port", protocol), get_port);

        props.setProperty(
                String.format("mail.%s.socketFactory.class", protocol),
                "javax.net.ssl.SSLSocketFactory");

        props.setProperty(
                String.format("mail.%s.socketFactory.fallback", protocol),
                "false");

        props.setProperty(
                String.format("mail.%s.socketFactory.port", protocol),
                String.valueOf(get_port));

        return props;
    }

    public static int emailCounter = 0;
    public static Queue<String[]> requests = new LinkedList<>();
    public static void downloadEmails() throws MessagingException {
        Properties props = getServerProperties();
        Session session = Session.getDefaultInstance(props);

        try {
            Store store = session.getStore(protocol);
            store.connect(username, password);

            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);

            Message[] messages = folderInbox.getMessages();

            if (messages.length == emailCounter)
                return;

            for (int i = emailCounter; i < messages.length; i++) {
                Message msg = messages[i];
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

                String[] tmp = {from, subject};
                requests.offer(tmp);
                System.out.println(messageContent);
            }
            emailCounter = messages.length;

        }
        catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol " + protocol);
            ex.printStackTrace();
        }
        catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        }
    }




    public static void main(String[] args) throws MessagingException {

        while (true) {
            Email.downloadEmails();
            if (requests == null)
                continue;
            String[] tmp = requests.poll();
            if (tmp == null)
                continue;
            Email.sendEmail(tmp[0], "Response your request", tmp[1], "");
        }

//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    Email.downloadEmails();
//                    System.out.println(emailCounter);
//                } catch (MessagingException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        };
//
//        timer.scheduleAtFixedRate(task, 0, 2 * 1000);

//        Thread thread = new Thread(() -> {
//            while (true) {
//                if (requests == null)
//                    continue;
//                String[] tmp = requests.poll();
//                if (tmp == null)
//                    continue;
//                System.out.println(tmp[0]);
//                System.out.println(tmp[1]);
//            }
//        });
//
//        thread.start();
    }

}