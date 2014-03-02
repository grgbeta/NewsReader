package com.example.mynewsreader;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.mynewsreader.data.NRDict;
import com.example.mynewsreader.data.NRNewsItem;
import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NRListAdapter extends BaseAdapter {
	private String NEWSURL = "http://mobilatr.mob.f2.com.au/services/views/9.json" ;
	
	private NRDict newsDict = null ;
	private Context context = null ;
	
	/*
	 * Cache to keep the images so that scrolling will be smoother and avoids downloading
	 * the images again and again.
	 */
	private LruCache<Integer, Bitmap> cache = null ;
	
	public NRListAdapter(Context contextArg) {
		context = contextArg ;
		new JsonLoaderTask().execute(NEWSURL) ;
		
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		
		cache = new LruCache<Integer, Bitmap>(cacheSize) ;
	}
	
	/*
	 * This function is used to refresh the items the adapter is using
	 */
	public void refresh() {
		newsDict = null ;
		notifyDataSetChanged() ;
		new JsonLoaderTask().execute(NEWSURL) ;
		
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		
		cache = new LruCache<Integer, Bitmap>(cacheSize) ;
	}

	@Override
	public int getCount() {
		if (newsDict != null)
			return newsDict.items.size() ;
		else
			return 0 ;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private class ViewHolder {
		TextView title ;
		TextView description ;
		ImageView icon ;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		ViewHolder holder ;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.news_item, parent, false);
		
		holder = new ViewHolder() ;
		holder.icon = (ImageView) view.findViewById(R.id.icon) ;

		holder.title = (TextView) view.findViewById(R.id.title) ;
		holder.description = (TextView) view.findViewById(R.id.description) ;

		if (newsDict != null &&  newsDict.items.size() > position) {
			NRNewsItem item = newsDict.items.get(position) ;
			holder.title.setText(item.headLine) ;
			holder.description.setText(item.slugLine) ;
			
			if (item.thumbnailImageHref != null && (!item.thumbnailImageHref.equals(""))) {
				holder.icon.setVisibility(View.GONE) ;
				new ImageLoaderTask(holder.icon, position).execute(item.thumbnailImageHref) ;
			}

			view.setTag(position) ;
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					/*
					 * Loading the webview if the item is clicked
					 */
					int position = (Integer) v.getTag() ;
					String urlValue = newsDict.items.get(position).tinyUrl ;
					
					Intent intent = new Intent() ;
					intent.setClass(context, NRWebView.class) ;
					
					Bundle bundle = new Bundle() ;
					bundle.putString("URL", urlValue) ;
					intent.putExtra("URL", urlValue) ;
					
					context.startActivity(intent) ;
				}
			}) ;
			return view ;
		} else {
			return null ;
		}
	}
	
	/*
	 * This class is used to load the news feeds
	 * Loads the newsDict global variable once the data is loaded
	 */
	private class JsonLoaderTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			String url = params[0] ;
			
			try {
			    DefaultHttpClient httpClient = new DefaultHttpClient();
			    HttpGet httpGet = new HttpGet(url);

			    HttpResponse httpResponse = httpClient.execute(httpGet);
			    HttpEntity httpEntity = httpResponse.getEntity();
			    String output = EntityUtils.toString(httpEntity);
			    			    			    
			    Gson gson = new Gson();
			    
			    newsDict = gson.fromJson(output, NRDict.class) ;
			    

			    Collections.sort(newsDict.items, new Comparator<NRNewsItem>() {
			    	  public int compare(NRNewsItem o1, NRNewsItem o2) {
			    		  Date d1 = getDate(o1.dateLine) ;
			    		  Date d2 = getDate(o2.dateLine) ;
			    		  
			    	      return d2.compareTo(d1);
			    	  }
			    	});
			    
			} catch(Exception e) {
				e.printStackTrace() ;
			}
			return null;			
		}
		
		private Date getDate(String str) {
			SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");  
			try {  
			    Date date = format.parse(str.substring(0, 19));  
			    
			    return date ;
			} catch (Exception e) {  
			    return null ;  
			}
		} 

		protected void onPostExecute(Void arg) {
		    notifyDataSetChanged() ;
		}
	}

	/*
	 * This class is used to lazy load the images and load the cache
	 */
	private class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {

		private ImageView bmImage;
	    private int position ;
	    
	    public ImageLoaderTask(ImageView bmImage, int position) {
	        this.bmImage = bmImage;
	        this.position = position ;
	    }
	    
	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0]; 
	        Bitmap mIcon11 = cache.get(position) ;
	        
	        if (mIcon11 == null) {
		        try {
		            InputStream in = new java.net.URL(urldisplay).openStream();
		            mIcon11 = BitmapFactory.decodeStream(in);
		            
			        cache.put(position, mIcon11) ;
			        
			        return mIcon11 ;
		        	
		        } catch (Exception e) {
		            e.printStackTrace();
		            return null ;
		        } 
	        } else
	        	return cache.get(position) ;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	        bmImage.setVisibility(View.VISIBLE) ;
	    }
		
	}
}
