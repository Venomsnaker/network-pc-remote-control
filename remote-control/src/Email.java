import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

public class Email {

    static final String from  = "g4.22tnt1.hcmus@gmail.com";
    static final String password = "xpfabvasrrgbqmta";
    static final String username = from;
    static final String protocol = "imap";
    static final String host = "imap.gmail.com";
    static final String get_port = "993";

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

    private static Part getBodyPart(Multipart multipart) throws MessagingException, IOException {
        for (int i = 0; i < multipart.getCount(); i++) {
            Part part = multipart.getBodyPart(i);
            if (part.isMimeType("text/plain") || part.isMimeType("text/html")) {
                return part;
            } else if (part.isMimeType("multipart/*")) {
                return getBodyPart((Multipart) part.getContent());
            }
        }
        return null;
    }

    public static Queue<String[]> requests = new LinkedList<>();
    public static void downloadEmails() throws MessagingException {
        Properties props = getServerProperties();
        Session session = Session.getDefaultInstance(props);

        try {
            Store store = session.getStore(protocol);
            store.connect(username, password);

            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_WRITE);

            SearchTerm searchTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), false); // Search for UNSEEN messages
            Message[] messages = folderInbox.search(searchTerm);

            for (Message msg : messages) {
                msg.setFlag(Flags.Flag.SEEN, true);

                Address[] fromAddress = msg.getFrom();
                String from = fromAddress[0].toString();
                String subject = msg.getSubject();
                String messageContent = "";

//                String contentType = msg.getContentType();
//
//                if (contentType.contains("text/html")) {
//                    try {
//                        Object content = msg.getContent();
//                        if (content != null) {
//                            messageContent = content.toString();
//                        }
//                    } catch (Exception ex) {
//                        messageContent = "!Error downloading content";
//                        ex.printStackTrace();
//                    }
//                }

                Multipart multipart = (Multipart) msg.getContent();
                Part bodyPart = getBodyPart(multipart);

                if (bodyPart.isMimeType("text/plain")) {
                    messageContent = bodyPart.getContent().toString();
                } else if (bodyPart.isMimeType("text/html")) {
                    messageContent = bodyPart.getContent().toString();
                } else {
                    System.out.println("Body content not available");
                }

                String[] tmp = {from, subject, messageContent};
                requests.offer(tmp);
            }
            folderInbox.close(false);
            store.close();
        }
        catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol " + protocol);
            ex.printStackTrace();
        }
        catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            System.out.println(tmp[0]);
            System.out.println(tmp[1]);
            System.out.println(tmp[2]);
        }

    }

}
