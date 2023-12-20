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
import java.awt.image.MultiPixelPackedSampleModel;
import java.io.IOException;
import java.util.*;
import java.util.stream.StreamSupport;

public class MailServer {
    static final String from = "g4.22tnt1.hcmus@gmail.com";
    static final String password = "xpfabvasrrgbqmta";
    static final String userName = from;
    static final String protocol = "imap";
    static final String host = "imap.gmail.com";
    static final String getPort = "993";

    public static Queue<String[]> requests = new LinkedList<>();

    static private List<String> mailSaved = new ArrayList<String>();
    static private Map<String, String> appsSaved = new HashMap<String, String>();

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

    private static void downloadEmails() throws MessagingException {
        Properties props = getServerProperties();
        Session session = Session.getDefaultInstance(props);

        try {
            Store store = session.getStore(protocol);
            store.connect(userName, password);

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

                Multipart multipart = (Multipart) msg.getContent();
                Part bodyPart = getBodyPart(multipart);

                if (bodyPart.isMimeType("text/plain")) {
                    messageContent = bodyPart.getContent().toString();
                } else if (bodyPart.isMimeType("text/html")) {
                    messageContent = bodyPart.getContent().toString();
                } else {
                    System.out.println("Body Content is not available");
                }

                messageContent = messageContent.replace("\n", "").replace("\r", "");
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

    private static void addMailAddress(String mail) {
        mailSaved.add(mail);
        // Update UI
    }

    private static void removeMailAddress(String mail) {
        mailSaved.remove(mail);
        // Update UI
    }

    private static void addAppAddress(String appName, String appPath) {
        appsSaved.put(appName, appPath);
        // Update UI
    }

    private static void removeAppAddress(String appName) {
        appsSaved.remove(appName);
        // Update UI
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

        subjectReturn = "Respond: " + subjectInput;

        System.out.println(to);
        if (!(mailSaved.contains(to))) {
            contentReturn = "You gmail don't have the permission to control the machine.";

        }else if (subjectInput.equals("get-apps")) {
            attachmentReturn = MailServerHelpers.getAppsList();

            if (attachmentReturn.isEmpty()) {
                contentReturn = "Fail to get apps list.";
            } else {
                contentReturn = "Successfully get an apps list.";
            }

        }else if (subjectInput.equals("get-services")) {
            attachmentReturn = MailServerHelpers.getServicesList();

            if (attachmentReturn.isEmpty()) {
                contentReturn = "Fail to get services list.";
            } else {
                contentReturn = "Successfully get a services list.";
            }

        }else if (subjectInput.equals("start-app")) {
            contentReturn = MailServerHelpers.startApp(contentInput);

        }else if (subjectInput.equals("start-app-by-name")) {
            if (appsSaved.containsKey(contentInput)) {
                contentReturn = MailServerHelpers.startApp(appsSaved.get(contentInput));
            } else {
                contentReturn = "The app you wish to start has not been set up | You have input the wrong name.";
            }

        }else if (subjectInput.equals("stop-app")) {
            contentReturn = MailServerHelpers.stopApp(contentInput);

        }else if (subjectInput.equals("start-service")) {
            contentReturn = MailServerHelpers.startService(contentInput);

        }else if (subjectInput.equals("stop-service")) {
            contentReturn = MailServerHelpers.stopService(contentInput);

        }else if (subjectInput.equals("get-screenshot")) {
            attachmentReturn = MailServerHelpers.getScreenshot();

            if (attachmentReturn.isEmpty()) {
                contentReturn = "Fail to capture a screenshot.";
            } else {
                contentReturn = "Successfully get an screenshot.";
            }

        }else if (subjectInput.equals("shutdown-server")) {
            contentReturn = MailServerHelpers.shutdownServer(contentInput);

        }else if (subjectInput.equals("restart-server")) {
            contentReturn = MailServerHelpers.restartServer(contentInput);

        }else if (subjectInput.equals("cancel-server-shutdown")) {
            contentReturn = MailServerHelpers.cancelServerShutdown();

        }else if (subjectInput.equals("start-keylogger")) {
            contentReturn = MailServerHelpers.startKeylogger();

        }else if (subjectInput.equals("stop-keylogger")) {
            attachmentReturn = MailServerHelpers.stopKeylogger();

            if (attachmentReturn.isEmpty()) {
                contentReturn = "Fail to get keylogger result.";
            } else {
                contentReturn = "Successfully get keylogger result.";
            }

        }else if (subjectInput.equals("collect-file")) {
            attachmentReturn = MailServerHelpers.collectFile(contentInput);

            if (attachmentReturn.isEmpty()) {
                contentReturn = "Fail to get the file at: " + contentInput;
            } else {
                contentReturn = "Successfully get the file at: " + contentInput;
            }

        }else {
            contentReturn = "You have input the wrong command!" + " Here is the list of command you can try:";
        }

        return new String[]{to, subjectReturn, contentReturn, attachmentReturn};
    }


    public static void main(String[] args) throws MessagingException{

        while(true) {
            MailServer.downloadEmails();
            if (requests == null) continue;
            String[] tmp = requests.poll();
            if (tmp != null) {
                String[] respondContent = MailServer.processMail(tmp);
                MailServer.sendMail(respondContent);
            }
        }
    }
}
