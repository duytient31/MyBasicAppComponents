package sample.hawk.com.mybasicappcomponents.oo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import sample.hawk.com.mybasicappcomponents.utils.SMLog;

public class ChildClass extends ParentClass implements MyInterface {
    // private static final String TAG = "[ChildClass]";
    private static final String TAG = "[Hawk]";

    static String mStr = init_var(); // keep the value

    static {  // STEP P0, only first-time <cinit>
        SMLog.i(TAG,"instance "+mStr+" class static");
    }

    {  // STEP P1, evey new keyword called <init>
        SMLog.i(TAG,"instance "+mStr+" class");
    }

    public ChildClass(String str){  // STEP P2, evey new keyword called
        mStr = str;
        SMLog.i(TAG,"into "+mStr+" constructor");
    }

    private static String init_var(){
        SMLog.i(TAG,"ChildClass call <cinit> for all static variables");
        return  "ChildClass=====";
    }

    @Override
    public void MyInterfaceAPI(int i) {
        SMLog.i(TAG,"MyInterfaceAPI+++");
    }

}
