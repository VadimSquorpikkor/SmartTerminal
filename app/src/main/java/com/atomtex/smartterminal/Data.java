package com.atomtex.smartterminal;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class Data {

   private final MutableLiveData<ArrayList<FavCommand>> savedList;

   public Data(MutableLiveData<ArrayList<FavCommand>> savedList) {
      this.savedList = savedList;
   }

   public void addCommand(FavCommand favCommand) {
      if (favCommand==null) return;
      if (savedList.getValue()==null) return;
      ArrayList<FavCommand> list = savedList.getValue();
      list.add(favCommand);
      save(list);
      savedList.setValue(list);
   }

   public void loadAllCommands() {
      savedList.setValue(load());
   }

//--------------------------------------------------------------------------------------------------

   public static final String FAV_COMMAND_DATA = "favCommandData";

   private void save(ArrayList<FavCommand> list) {
      ArrayList<String> strList = new ArrayList<>();
      for (FavCommand c:list) strList.add(c.getCommand()+"&&"+c.getDescription());
      SaveLoad.saveArray(FAV_COMMAND_DATA, strList);
   }

   private ArrayList<FavCommand> load() {
      ArrayList<FavCommand> list = new ArrayList<>();
      for (String s:SaveLoad.loadStringArray(FAV_COMMAND_DATA)) {
         list.add(new FavCommand(s.split("&&")[0], s.split("&&")[1]));
      }
      return list;
   }
}
