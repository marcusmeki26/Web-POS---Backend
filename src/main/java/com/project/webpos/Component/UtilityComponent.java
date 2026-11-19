package com.project.webpos.Component;

import org.springframework.stereotype.Component;

@Component 
public class UtilityComponent {
  public int getTheIndexOfTheFirstCapitalizedLetter(String word){
    for(int i = 0; i < word.length(); i++){
      if(Character.isUpperCase(word.charAt(i))){
        return i;
      }
    }

    return -1;
  }

  public String humanizeFieldNames(int indexOfCapitalizedLetter, String fieldName){
    if(indexOfCapitalizedLetter == -1)
      return Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1, fieldName.length());
  
    return Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1, indexOfCapitalizedLetter) + ' ' + fieldName.substring(indexOfCapitalizedLetter);
  }
}
