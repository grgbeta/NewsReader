package com.example.mynewsreader;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
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
		
		listView = (ListView) findViewById(R.id.listView) ;
		adapter = new NRListAdapter(this) ;
		listView.setAdapter(adapter) ;
		
		Button refreshButton = (Button) findViewById(R.id.refreshButton) ;
		refreshButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				adapter.refresh() ;
				
			}
		}) ;
	}
}
