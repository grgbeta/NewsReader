package com.example.mynewsreader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NRWebView extends Activity {
	private ProgressDialog progDialog ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view);
		
		Bundle b = getIntent().getExtras();
		String url = b.getString("URL");
		
		WebView webView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true) ;
		webView.setWebViewClient(new NewsWebViewClient() {
			
		});
		
		progDialog = ProgressDialog.show( this,"News", "Loading Data ...");
		
		webView.loadUrl(url) ;
	}
	
	private class NewsWebViewClient extends WebViewClient {
		
		public NewsWebViewClient() {
			
		}
		
		@Override
	    public void onPageFinished(WebView view, String url) {
	        super.onPageFinished(view, url);
	        
	        progDialog.dismiss() ;
	    }
	}

}
