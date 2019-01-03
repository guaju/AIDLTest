package com.example.remote;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.example.remote.IMyAidlInterface.Stub;

import java.io.IOException;
import java.io.InputStream;

public class RemoteService extends Service {

    private MyStub myStub;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //因为Stub继承自Binder类,而Binder类又实现自IBinder接口，所以返回值能接受MyStub类对象
        if (myStub==null){
        myStub = new MyStub();
        }
        return myStub;
    }

    //创建Stub子类，实现远程调用需要的方法，其中默认的basicTypes方法可以删除
    public class MyStub extends Stub{

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString)
                throws RemoteException {

        }

        @Override
        public Bitmap getImage() throws RemoteException {
            Bitmap bitmap=null;
            try {
                InputStream open = getAssets().open("eye.jpeg");
                bitmap= BitmapFactory.decodeStream(open);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }
}
