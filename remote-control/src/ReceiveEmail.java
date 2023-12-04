import java.util.Properties;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;


public class ReceiveEmail {

    private Properties getServerProperties(String protocol, String host, String port) {
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

    public void downloadEmails(String protocol, String host, String port, String userName, String password) throws MessagingException {
        Properties props = getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(props);

        try {
            Store store = session.getStore(protocol);
            store.connect(userName, password);

            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);

            Message[] messages = folderInbox.getMessages();

            for (int i = 0; i < messages.length; i++) {
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

                System.out.println("Message #" + (i + 1) + ":");
                System.out.println("\t From: " + from);
                System.out.println("\t Subject: " + subject);
                System.out.println("\t Message: " + messageContent);
            }
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

    public String[] getInfo(String protocol, String host, String port, String userName, String password) {
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

    private String parseAddress(Address[] addresses) {
        String listAddress = "";
        if (addresses != null) {
            for (int i = 0; i < addresses.length; i++) {
                listAddress += addresses[i].toString() + ", ";
            }
        }

        if (listAddress.length() > 1) {
            listAddress = listAddress.substring(0, listAddress.length() - 2);
        }

        return listAddress;
    }

    public static void main(String[] args) throws MessagingException {
        String protocol = "imap";
        String host = "imap.gmail.com";
        String port = "993";

        String username = "g4.22tnt1.hcmus@gmail.com";
        String password = "xpfabvasrrgbqmta";

        ReceiveEmail receiver = new ReceiveEmail();
        String[] tmp = receiver.getInfo(protocol, host, port, username, password);
        System.out.println(tmp[0]);
        System.out.println(tmp[1]);
        Email.sendEmail(tmp[1], "try something", "sub ject ne "+tmp[0]);
    }
}
