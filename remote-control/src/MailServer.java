import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.awt.image.MultiPixelPackedSampleModel;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

public class MailServer {
    static final String from = "g4.22tnt1.hcmus@gmail.com";
    static final String password = "xpfabvasrrgbqmta";
    static final String userName = from;
    static final String host = "pop.gmail.com";
    static final String protocol = "pop3";
    static final String getPort = "995";

    public static int emailCounter = 0;
    public static Queue<String[]> requests = new LinkedList<>();

    private static boolean sendMail(String[] respondContent) {
        Properties props = new Properties();
        // SMTP Host - TLS 587 SSL 465
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        String to = respondContent[0];
        String subject = respondContent[1];
        String content = respondContent[2];
        String attachment_path = respondContent[3];

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
            // Header
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.setFrom(from);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject);

            // Body
            BodyPart msgText = new MimeBodyPart();
            msgText.setText(content);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(msgText);

            if (!attachment_path.isEmpty()) {
                BodyPart msgAttachment = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment_path);
                msgAttachment.setDataHandler(new DataHandler(source));
                msgAttachment.setFileName(attachment_path);
                multipart.addBodyPart(msgAttachment);
            }

            msg.setContent(multipart);
            Transport.send(msg);
            System.out.println("Mail sent successfully!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fail to send the mail.");
            return false;
        }
    }

    private static Properties getServerProperties() {
        Properties props = new Properties();
        props.put(String.format("mail.%s.host", protocol), host);
        props.put(String.format("mail.%s.port", protocol), getPort);

        props.setProperty(
                String.format("mail.%s.socketFactory.class", protocol),
                "javax.net.ssl.SSLSocketFactory");

        props.setProperty(
                String.format("mail.%s.socketFactory.fallback", protocol),
                "false");

        props.setProperty(
                String.format("mail.%s.socketFactory.port", protocol),
                String.valueOf(getPort));

        return props;
    }

    private static void downloadEmails() throws MessagingException {
        Properties props = getServerProperties();
        Session session = Session.getDefaultInstance(props);

        try {
            Store store = session.getStore(protocol);
            store.connect(userName, password);

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

                String[] tmp = {from, subject, messageContent};
                requests.offer(tmp);
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

    private static String[] processMail(String[] tmp) {
        // Return Variables
        String to = tmp[0];
        String subjectReturn = "";
        String contentReturn = "";
        String attachmentReturn = "";

        // Input Variables
        String subjectInput = tmp[1];
        String contentInput = tmp[2];

        if (subjectInput.equals("get-apps")) {
            subjectReturn = "Respond: Apps List";
            attachmentReturn = MailServerHelpers.getAppsList();

            if (attachmentReturn.equals("")) {
                contentReturn = "Fail to get apps list.";
            } else {
                contentReturn = "Please check the attachment file.";
            }
        }

        return new String[]{to, subjectReturn, contentReturn, attachmentReturn};
    }


    public static void main(String[] args) throws MessagingException{
        while(true) {
            MailServer.downloadEmails();
            if (requests == null) continue;
            String[] tmp = requests.poll();
            if (tmp == null) continue;

            String[] respondContent = MailServer.processMail(tmp);
            System.out.println(respondContent);
            MailServer.sendMail(respondContent);
        }
    }
}
