package com.example.mynewsreader;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NRNewsItem implements Serializable {
	public String dateLine ;
	public String headLine ;
	public Integer identifier ;
	public String slugLine ;
	public String thumbnailImageHref ;
	public String tinyUrl ;
	public String type ;
	public String webHref ;
}
