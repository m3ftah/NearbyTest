package com.example.activity;

import android.util.Log;

import org.robolectric.annotation.Implements;

/**
 * Created by guema on 2/19/2018.
 */
@Implements(Log.class)
public class LogShadow extends org.robolectric.shadows.ShadowLog{
    public static void i(String tag, String msg) {
        print("i/ " + "[" + tag + "] " + msg);
    }
    public static void e(String tag, String msg) {
        print("e/ " +"[" + tag + "] " + msg);
    }
    public static void d(String tag, String msg) {
        print("d/ " +"[" + tag + "] " + msg);
    }
    public static void w(String tag, String msg) {
        print("w/ " +"[" + tag + "] " + msg);
    }
    public static void print(String str){
        System.out.println(str);
    }
}
