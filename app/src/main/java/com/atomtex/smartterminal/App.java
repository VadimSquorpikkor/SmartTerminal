package com.atomtex.smartterminal;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class App extends Application {
   public static final String TAG = "TAG";
   private static Application mApplication;
   private static File sMainDir;
   public static String DIRECTORY = "ATOMTEX/SmartTerminal";

   @Override
   public void onCreate() {
      super.onCreate();
      mApplication = this;
      showLogo();
      sMainDir = new File(Environment.getExternalStorageDirectory(), DIRECTORY);
      sMainDir.mkdirs();
   }

   public static File getMainDir() {
      return sMainDir;
   }

   public static Context getContext(){
      return mApplication.getApplicationContext();
   }

   static void showLogo() {
      Log.e(TAG, "╔═══╗╔════╗╔═══╗╔═══╗╔════╗  ╔════╗╔═══╗╔═══╗╔════╗╔══╗╔═╗ ╔╗╔═══╗╔╗   ");
      Log.e(TAG, "║╔═╗║║╔╗╔╗║║╔═╗║║╔═╗║║╔╗╔╗║  ║╔╗╔╗║║╔══╝║╔═╗║║╔╗╔╗║╚╣║╝║ ╚╗║║║╔═╗║║║   ");
      Log.e(TAG, "║╚══╗║║║║║║║║ ║║║╚═╝║╚╝║║╚╝  ╚╝║║╚╝║╚══╗║╚═╝║║║║║║║ ║║ ║╔╗╚╝║║║ ║║║║   ");
      Log.e(TAG, "╚══╗║║║║║║║║╚═╝║║╔╗╔╝  ║║      ║║  ║╔══╝║╔╗╔╝║║║║║║ ║║ ║║╚╗ ║║╚═╝║║║   ");
      Log.e(TAG, "║╚═╝║║║║║║║║╔═╗║║║║╚╗  ║║      ║║  ║╚══╗║║║╚╗║║║║║║╔╣║╗║║ ║ ║║╔═╗║║╚══╗");
      Log.e(TAG, "╚═══╝╚╝╚╝╚╝╚╝ ╚╝╚╝╚═╝  ╚╝      ╚╝  ╚═══╝╚╝╚═╝╚╝╚╝╚╝╚══╝╚╝ ╚═╝╚╝ ╚╝╚═══╝");
      Log.e(TAG, "♣VERSION_NAME: " + BuildConfig.VERSION_NAME);
   }

}
