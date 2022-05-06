package com.atomtex.smartterminal;

public class FavCommand {

   private final String command;
   private final String description;

   public FavCommand(String command, String description) {
      this.command = command;
      this.description = description;
   }

   public String getCommand() {
      return command;
   }

   public String getDescription() {
      return description;
   }
}
