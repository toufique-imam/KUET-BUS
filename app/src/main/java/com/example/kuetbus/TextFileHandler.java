package com.example.kuetbus;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TextFileHandler {
    public TextFileHandler() {
    }

    public Pair<Boolean,String> READ_TEXT(String fileName) {
        StringBuilder ans = new StringBuilder();
        String tmp;
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                return Pair.create(Boolean.FALSE,null);
            }
            File gpxfile = new File(root, fileName);
            FileReader fileReader = new FileReader(gpxfile);

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((tmp = bufferedReader.readLine()) != null) {
                ans.append(tmp);
                ans.append("\n");
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
            return Pair.create(Boolean.FALSE,null);
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
            return Pair.create(Boolean.FALSE,null);
        }
        Log.e(getClass().getSimpleName(),"DONE READ");
        Log.e("SEE_ME", ans.toString());
        return Pair.create(Boolean.TRUE,ans.toString());
    }

    public void WRITE_TEXT(String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Log.e(getClass().getSimpleName(),"DONE WRITE");
            //Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
