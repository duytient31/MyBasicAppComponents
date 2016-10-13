package sample.hawk.com.mybasicappcomponents.oo;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MyJavaClass {
    // private static final String TAG = "[MyJavaClass]";
    private static final String TAG = "[Hawk]";
    static ChildClass StaticRef1;
    static ChildClass StaticRef2;
    ChildClass mObj1;
    ChildClass mObj2;

    public MyJavaClass(int param){
        Log.i(TAG,"MyJavaClass() constructor +++");
        switch(param){
            case 1: // <init>,<cinit> condition
                MyJavaDynamic();
                MyJavaStaticRef();
                break;
            case 2: // NO synchronized, sychronized(this) keyword
                MySyncTestThread t1= new MySyncTestThread("T[1]",false);t1.start();
                MySyncTestThread t2= new MySyncTestThread("T[2]",false);t2.start();
                MySyncTestThread t3= new MySyncTestThread("T[3]",false);t3.start();
                MySyncTestThread t4= new MySyncTestThread("T[4]",false);t4.start();
                MySyncTestThread t5= new MySyncTestThread("T[5]",false);t5.start();
                MySyncTestThread t6= new MySyncTestThread("T[6]",false);t6.start();
                MySyncTestThread t7= new MySyncTestThread("T[7]",false);t7.start();
                break;

            case 3: // synchronized, sychronized(this) keyword
                MySyncTestThread t1sync= new MySyncTestThread("T[1]",true);t1sync.start();
                MySyncTestThread t2sync= new MySyncTestThread("T[2]",true);t2sync.start();
                MySyncTestThread t3sync= new MySyncTestThread("T[3]",true);t3sync.start();
                MySyncTestThread t4sync= new MySyncTestThread("T[4]",true);t4sync.start();
                MySyncTestThread t5sync= new MySyncTestThread("T[5]",true);t5sync.start();
                MySyncTestThread t6sync= new MySyncTestThread("T[6]",true);t6sync.start();
                MySyncTestThread t7sync= new MySyncTestThread("T[7]",true);t7sync.start();
                break;

            default:
                ;

        }
        Log.i(TAG,"MyJavaClass() constructor ---");
    }

    public void MyJavaDynamic(){
        mObj1 = new ChildClass("DynamicObject1");
        mObj2 = new ChildClass("DynamicObject2");
    }

    public void MyJavaStaticRef(){
        StaticRef1 = mObj1;
        StaticRef2 = mObj2;
    }

    public void method_1(){
        Log.i(TAG,"call MyJavaClass::method_1()  API");
    }


    public void cc_instanceof_keyword(ChildClass obj){
        if( obj instanceof ParentClass ){
            Log.i(TAG,"OBJ is instanceof ParentClass");
        }
        if( obj instanceof MyInterface ){
            Log.i(TAG,"OBJ is instanceof MyInterface"); // ChildClass implements this interface
        }
        if( obj instanceof ChildClass ){
            Log.i(TAG,"OBJ is instanceof ChildClass");
        }
    }

    public void pc_instanceof_keyword(ParentClass obj){
        if( obj instanceof ParentClass ){
            Log.i(TAG,"OBJ is instanceof ParentClass");
        }
        if( obj instanceof MyInterface ){
            Log.i(TAG,"OBJ is instanceof MyInterface"); // ChildClass implements this interface
        }
        if( obj instanceof ChildClass ){
            Log.i(TAG,"OBJ is instanceof ChildClass");
        }
    }



}