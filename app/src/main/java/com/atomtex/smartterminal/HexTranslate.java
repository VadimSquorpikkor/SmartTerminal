package com.atomtex.smartterminal;

public class HexTranslate {

   /* s must be an even-length string. */
   public static byte[] hexStringToByteArray(String s) {
      s = s.replace(" ", "");
      int len = s.length();
      byte[] data = new byte[len / 2];
      for (int i = 0; i < len; i += 2) {
         data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                 + Character.digit(s.charAt(i + 1), 16));
      }
      return data;
   }

   public static String byteArrayToHexString(byte[] bytes) {
      StringBuilder result = new StringBuilder();
      for (byte aByte : bytes) {
         result.append(String.format("%02x", aByte));
         result.append(" ");
         // upper case
         // result.append(String.format("%02X", aByte));
      }
      return result.toString();
   }

}
