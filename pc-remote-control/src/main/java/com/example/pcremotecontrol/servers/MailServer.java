package com.example.pcremotecontrol.servers;

import com.example.pcremotecontrol.MainApplication;

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
import java.util.*;

public class MailServer {
    static final String protocol = "imap";
    static final String host = "imap.gmail.com";
    static final String getPort = "993";

    public static boolean sendMail(String[] respondContent) {
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
        String attachment_name = respondContent[4];

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // TODO Auto-generated method stub
                return new PasswordAuthentication(MainApplication.getInstance().getServerInfo().getKey(), MainApplication.getInstance().getServerInfo().getValue());
            }
        };

        Session session = Session.getInstance(props, auth);
        MimeMessage msg = new MimeMessage(session);

        try {
            // Header
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.setFrom(MainApplication.getInstance().getServerInfo().getKey());
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
                msgAttachment.setFileName(attachment_name);
                multipart.addBodyPart(msgAttachment);
            }

            msg.setContent(multipart);
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Properties getServerProperties() {
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

    public static Queue<String[]> downloadEmails() throws MessagingException {
        Properties props = getServerProperties();
        Session session = Session.getDefaultInstance(props);
        Queue<String[]> requests = new LinkedList<>();;

        try {
            Store store = session.getStore(protocol);
            store.connect(MainApplication.getInstance().getServerInfo().getKey(), MainApplication.getInstance().getServerInfo().getValue());

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
                }

                messageContent = messageContent.replace("\n", "").replace("\r", "");
                String[] tmp = {from, subject, messageContent};
                requests.offer(tmp);
            }
            folderInbox.close(false);
            store.close();
        }
        catch (NoSuchProviderException ex) {
            ex.printStackTrace();
        }
        catch (MessagingException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return requests;
    }

    public static String[] processMail(String[] tmp) {
        // Return Variables
        String to = tmp[0];
        String subjectReturn = "";
        String contentReturn = "";
        String attachmentReturn = "";
        String attachmentName = "empty";

        // Input Variables
        String subjectInput = tmp[1];
        String contentInput = tmp[2];

        subjectReturn = "Respond: " + subjectInput;

        System.out.println(to);
        if (!(MainApplication.getInstance().getMailsLibrary().contains(to))) {
            contentReturn = "You gmail don't have the permission to control the machine.";

        }else if (subjectInput.equals("get-apps")) {
            attachmentReturn = MailServerHelpers.getAppsList();

            if (attachmentReturn.isEmpty()) {
                contentReturn = "Fail to get apps list.";
            } else {
                contentReturn = "Successfully get an apps list.";
                attachmentName = "apps";
            }

        }else if (subjectInput.equals("get-services")) {
            attachmentReturn = MailServerHelpers.getServicesList();

            if (attachmentReturn.isEmpty()) {
                contentReturn = "Fail to get services list.";
            } else {
                contentReturn = "Successfully get a services list.";
                attachmentName = "services";
            }

        }else if (subjectInput.equals("start-app")) {
            contentReturn = MailServerHelpers.startApp(contentInput);

        }else if (subjectInput.equals("start-app-by-name")) {
            if (MainApplication.getInstance().getAppsLibrary().containsKey(contentInput)) {
                contentReturn = MailServerHelpers.startApp(MainApplication.getInstance().getAppsLibrary().get(contentInput));
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
                attachmentName = "screenshot";
            }

        }else if (subjectInput.equals("shutdown-server")) {
            contentReturn = MailServerHelpers.shutdownServer(contentInput);

        }else if (subjectInput.equals("restart-server")) {
            contentReturn = MailServerHelpers.restartServer(contentInput);

        }else if (subjectInput.equals("cancel-shutdown")) {
            contentReturn = MailServerHelpers.cancelServerShutdown();

        }else if (subjectInput.equals("logout-app")) {
            MailServerHelpers.logoutServer();

        }else if (subjectInput.equals("start-keylogger")) {
            contentReturn = MailServerHelpers.startKeylogger();

        }else if (subjectInput.equals("stop-keylogger")) {
            attachmentReturn = MailServerHelpers.stopKeylogger();

            if (attachmentReturn.isEmpty()) {
                contentReturn = "Fail to get keylogger result.";
            } else {
                contentReturn = "Successfully get keylogger result.";
                attachmentName = "keylogger";
            }

        }else if (subjectInput.equals("collect-file")) {
            attachmentReturn = MailServerHelpers.collectFile(contentInput);

            if (attachmentReturn.isEmpty()) {
                contentReturn = "Fail to get the file at: " + contentInput;
            } else {
                contentReturn = "Successfully get the file at: " + contentInput;
                attachmentName = "file-result";
            }

        }else if(subjectInput.equals("get-directory")) {
                attachmentReturn = MailServerHelpers.collectDirectory(contentInput);
                if (attachmentReturn.isEmpty()) {
                    contentReturn = "Fail to get the directory path: " + contentInput;
                } else {
                    contentReturn = "Successfully get the directory path: " + contentInput;
                    attachmentName = "directory-result";
                }

        }else {
            contentReturn = "You have input the wrong command!\n" + " Here is the list of command you can try:\n" +
                    "- Nhận danh sách Apps: [get-apps] []\n" +
                    "- Khởi động Apps: [start-app] [địa chỉ file exe]\n" +
                    "- Khởi động Apps lưu sẵn: [start-app-by-name] [tên app]\n" +
                    "- Tắt Apps: [stop-app] [tên app]\n\n" +

                    "- Nhận danh sách Services: [get-services] []\n" +
                    "- Khởi động Service: [start-service] [tên service]\n" +
                    "- Tắt Service: [stop-service] [tên service]\n\n"+

                    "- Lấy Screenshot: [get-screenshot] []\n" +
                    "- Khởi động Keylogger: [start-keylogger] []\n" +
                    "- Tắt Keylogger: [stop-keylogger] []\n\n" +

                    "- Lấy File: [collect-file] [địa chỉ file]\n" +
                    "- In ra thư mục [get-directory] [địa chỉ folder]\n\n" +

                    "- Tắt phần mềm: [logout-server] []\n"+
                    "- Tắt máy tính: [shutdown-server] [thời gian delay theo s]\n" +
                    "- Khởi động lại máy tính: [restart-server] [thời gian delay theo s]\n" +
                    "- Dừng lệnh tắt máy tính: [cancel-shutdown] []\n\n"+

                    "- Xem các chức năng: [help/ nhập bất kì] []\n";
        }

        return new String[]{to, subjectReturn, contentReturn, attachmentReturn, attachmentName};
    }

    public static void main(String[] args) {}
}

