package com.propertyfile;

import java.util.ArrayList;
import java.util.List;

public class PlaceHolderDto {
String key;
List <String> value = new ArrayList<String>();

public String getKey() {
	return key;
}
public void setKey(String key) {
	this.key = key;
}
public List<String> getValue() {
	return value;
}
public void setValue(List<String> value) {
	this.value = value;
}

	
}
