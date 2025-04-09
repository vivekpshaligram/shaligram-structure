package com.codestracture.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;



public class EpubUtils {
    public static String cacheBookPath = null;

    public static final String SINGLE_PAGE = "SinglePage.html";

    public static String bookRoot = null;
    public static void initFolders(Context ctx){
        File bookCache = new File(ctx.getCacheDir(),"book");
        if(!bookCache.exists()){bookCache.mkdirs();}
        cacheBookPath = bookCache.getAbsolutePath();
        File bookRootFile = new File(Environment.getExternalStorageDirectory(),"Books");
        if(!bookRootFile.exists()){bookRootFile.mkdirs();}
        bookRoot = bookRootFile.getAbsolutePath();
    }

    public static void ExtractBook(final Context ctx, File be, String destinationLocation,final BookLoadCallback callback){
        new AsyncTask<String, Void, Boolean>() {
           // CinematicProgressDialog pdd;
            void delay(int mill){
                try {
                    Thread.sleep(mill);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                pdd = new CinematicProgressDialog(ctx);
//                CinematicProgressDialog.setPromptWin(pdd);
//                pdd.show();
//                CinematicProgressDialog.setPromptWin(pdd);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
              //  pdd.dismiss();
                callback.onResult(aBoolean);
            }

            @Override
            protected Boolean doInBackground(String... strings) {
                try {
                    ZipFile zf = new ZipFile(new File(strings[0]), ZipFile.OPEN_READ);
                    String target  = strings[1];
                    ArrayList<ZipEntry> zes = new ArrayList<>();
                    Enumeration<?> z = zf.entries();
                   // pdd.setMessage(ctx.getString(R.string.load_prepare));
                   // pdd.setProgress(0,1);
                    while (z.hasMoreElements()){
                        ZipEntry ze = (ZipEntry) z.nextElement();
                        if(ze.getName().contains("../")){Log.e("What the fuck zip file!",ze.getName());continue;}
                        zes.add(ze);
                    }
                  //  pdd.setProgress(1,1);
                    delay(200);
                  //  pdd.setMessage(ctx.getString(R.string.load_loading));
                    for (int i = 0; i < zes.size(); i++) {
                       // pdd.setProgress(i+1,zes.size());
                        ZipEntry ze = zes.get(i);
                        File dest = new File(target,ze.getName());
                        if (ze.isDirectory()) {
                            dest.mkdirs();
                        }
                        else{
                            if(dest.exists()){dest.delete();}
                            if(!dest.getParentFile().exists()){dest.getParentFile().mkdirs();}
                            dest.createNewFile();
                            FileOutputStream os = new FileOutputStream(dest);
                            InputStream is = zf.getInputStream(ze);

                            byte[] buffer = new byte[1024];
                            int len=0;
                            while ((len = is.read(buffer))>0){
                                os.write(buffer,0,len);
                            }
                            is.close();
                            os.close();
                        }
                    }
                    File flag = new File(target,SINGLE_PAGE);
                    if(!flag.exists()){flag.createNewFile();}
                    zf.close();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                   // pdd.setMessage(ctx.getString(R.string.load_error)+e.getMessage());
                   // pdd.setProgress(0,1);
                    delay(500);
                    return false;
                }
            }
        }.execute(be.getAbsolutePath(), destinationLocation);
    }

    public interface BookLoadCallback{
        void onResult(boolean b);
    }
}
