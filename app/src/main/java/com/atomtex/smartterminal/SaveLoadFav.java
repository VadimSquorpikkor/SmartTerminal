package com.atomtex.smartterminal;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.atomtex.smartterminal.App.TAG;

public class SaveLoadFav {

    public static ArrayList<FavCommand> parseFile(String path) {

        Log.e(TAG, "...........parseFile: " + path);

        List<Charset> charsets = new ArrayList<>();

        //charsets to parse data
        charsets.add(Charset.forName("windows-1251"));
        charsets.add(StandardCharsets.UTF_8);
        charsets.add(StandardCharsets.UTF_16LE);

        Uri uri = Uri.parse(path);
        Log.e(TAG, "...............Uri.parse(path): " + uri);
        String scheme = uri.getScheme();

        InputStream inputStream;
        ArrayList<FavCommand> favCommand = null;

        for (Charset charset : charsets) {
            try {
                if (scheme != null && scheme.equals("content")) {
                    inputStream = App.getContext().getContentResolver().openInputStream(uri);
                } else {
                    File file = new File(path);
                    inputStream = new FileInputStream(file);
                }
            } catch (FileNotFoundException e) {
                Log.e(TAG, "parseFile: ERROR FNF");
                return null;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream, charset))) {
                favCommand = parseCommand(reader);
                if (favCommand.size() == 0) continue;
                break;
            } catch (IOException | NumberFormatException e) {
                Log.e(TAG, "parseFile: ERROR");
            }
        }

        return favCommand;
    }

    private static ArrayList<FavCommand> parseCommand(BufferedReader reader)
            throws IOException, NumberFormatException {
        ArrayList<FavCommand> list = new ArrayList<>();
        String line;
        String[] arr;
        while ((line = reader.readLine()) != null) {
            arr = line.split(":");
            if (arr.length!=2) continue;
            list.add(new FavCommand(arr[1], arr[0]));
        }
        return list;
    }

}
