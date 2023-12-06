import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientUI {
    private JFrame frame;
    private JButton exitButton;
    private JButton getAppsButton;
    private JButton getServicesButton;
    private JButton startAppButton;
    private JButton stopAppButton;
    private JButton startServiceButton;
    private JButton stopServiceButton;
    private JButton takeScreenshotButton;
    private JButton startKeyloggerButton;
    private JButton stopKeyloggerButton;
    private JButton shutdownButton;
    private JButton restartButton;
    private JButton cancelButton;
    private JButton collectFileButton;

    public ClientUI() {
        frame = new JFrame("Email Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);

        exitButton = new JButton("Exit");
        getAppsButton = new JButton("Get Apps");
        getServicesButton = new JButton("Get Services");
        startAppButton = new JButton("Start App");
        stopAppButton = new JButton("Stop App");
        startServiceButton = new JButton("Start Service");
        stopServiceButton = new JButton("Stop Service");
        takeScreenshotButton = new JButton("Take Screenshot");
        startKeyloggerButton = new JButton("Start Keylogger");
        stopKeyloggerButton = new JButton("Stop and Return Keylogger");
        shutdownButton = new JButton("Shutdown");
        restartButton = new JButton("Restart");
        cancelButton = new JButton("Cancel Shutdown/ Restart");
        collectFileButton = new JButton("Collect File via Location");

        panel.add(exitButton);
        panel.add(getAppsButton);
        panel.add(getServicesButton);
        panel.add(startAppButton);
        panel.add(stopAppButton);
        panel.add(startServiceButton);
        panel.add(stopServiceButton);
        panel.add(takeScreenshotButton);
        panel.add(startKeyloggerButton);
        panel.add(stopKeyloggerButton);
        panel.add(shutdownButton);
        panel.add(restartButton);
        panel.add(cancelButton);
        panel.add(collectFileButton);

        frame.setVisible(true);

        addActionListeners();
    }

    private void addActionListeners() {
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                JOptionPane.showMessageDialog(frame, "Exiting...");
            }
        });

        getAppsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                JOptionPane.showMessageDialog(frame, "Getting apps...");
            }
        });

        getServicesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                JOptionPane.showMessageDialog(frame, "Getting services...");
            }
        });

        startAppButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                JOptionPane.showMessageDialog(frame, "Starting App...");
            }
        });

        stopAppButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                JOptionPane.showMessageDialog(frame, "Stopping App...");
            }
        });

        startServiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                JOptionPane.showMessageDialog(frame, "Starting service...");
            }
        });

        stopServiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                JOptionPane.showMessageDialog(frame, "Stopping service...");
            }
        });

        takeScreenshotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                JOptionPane.showMessageDialog(frame, "Taking screenshot...");
            }
        });

        startKeyloggerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                JOptionPane.showMessageDialog(frame, "Starting Keylogger...");
            }
        });

        stopKeyloggerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                JOptionPane.showMessageDialog(frame, "Returning Keylogger...");
            }
        });

        shutdownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                JOptionPane.showMessageDialog(frame, "Shutdowning...");
            }
        });

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                JOptionPane.showMessageDialog(frame, "Restarting...");
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                JOptionPane.showMessageDialog(frame, "Canceling...");
            }
        });

        collectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                JOptionPane.showMessageDialog(frame, "Collecting File...");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientUI();
            }
        });
    }
}
