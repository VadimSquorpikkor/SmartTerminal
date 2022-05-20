package com.atomtex.smartterminal;

public class TerminalUtils {

    public static String convertStringToLooksLikeCommand(String s) {
        if (s.length()<3) return s;
        String out = "";
        for (int i = 0; i < s.length(); i++) {
            if (i%2==0&&i!=0) out+=" "+s.charAt(i);
            else out+=s.charAt(i);
        }
        return out;
    }

}
