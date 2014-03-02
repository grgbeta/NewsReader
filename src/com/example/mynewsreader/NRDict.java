package com.example.mynewsreader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NRDict implements Serializable {
	
	public Integer identifier ;
	public ArrayList<NRNewsItem> items = new ArrayList<NRNewsItem>();
	public String name ;

}
