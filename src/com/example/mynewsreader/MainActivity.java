package com.example.mynewsreader;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {
	ListView listView = null ;
	NRListAdapter adapter = null ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*
		 * Setting the list view adapter
		 */
		listView = (ListView) findViewById(R.id.listView) ;
		adapter = new NRListAdapter(this) ;
		listView.setAdapter(adapter) ;
		
		Button refreshButton = (Button) findViewById(R.id.refreshButton) ;
		refreshButton.setOnClickListener(new OnClickListener() {
			
			/*
			 * (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 * Code to handle the click event of refresh button
			 */
			@Override
			public void onClick(View v) {
				adapter.refresh() ;
				
			}
		}) ;
	}
}
