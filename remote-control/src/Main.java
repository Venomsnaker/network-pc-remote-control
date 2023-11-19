import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class Main implements NativeKeyListener {
    String res = "";




//    public void nativeKeyPressed(NativeKeyEvent e) {
//        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
//
//        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
//            try {
//                GlobalScreen.unregisterNativeHook();
//            } catch (NativeHookException nativeHookException) {
//                nativeHookException.printStackTrace();
//            }
//        }
//    }

//    public void nativeKeyReleased(NativeKeyEvent e) {
//        res += NativeKeyEvent.getKeyText(e.getKeyCode()) + " ";
//        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
//        System.out.println("Current res: " + res);
//    }

//    public void nativeKeyTyped(NativeKeyEvent e) {
//        System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
//    }

    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new Main());
    }
}