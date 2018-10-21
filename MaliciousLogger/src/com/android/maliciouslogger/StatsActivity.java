package com.android.maliciouslogger;

import java.util.ArrayList;


import com.database.sqlite.DatabaseAdapter;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class StatsActivity extends Activity {

	private String rcv1, rcv2;
	private ArrayList<String> statistics;
	private DatabaseAdapter databaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats_layout);
		
		// initialize some views
		TextView txt1 = (TextView) findViewById(R.id.uidText);
		TextView txt2 = (TextView) findViewById(R.id.interfaceText);
		TextView txt3 = (TextView) findViewById(R.id.timeText);
		TableLayout table = (TableLayout) findViewById(R.id.table);
		
		//get uid and interface from BarActivity
		Intent intent = getIntent();
		rcv1 = intent.getStringExtra("uid");
		rcv2 = intent.getStringExtra("interface");
		
		//initialize databaseAdapter for the connections
		databaseAdapter=new DatabaseAdapter(StatsActivity.this);
		databaseAdapter=databaseAdapter.open();
		
		// Create a list to hold the reports
		statistics = new ArrayList<String>();
		statistics = (ArrayList<String>) databaseAdapter.getAllNodeDeviceReports(rcv1, rcv2);
		
		// if the user hasn't picked a uid or if we don't have an reports, don't show anything
		if(rcv1.equals("") || statistics.size() == 0){
			txt1.setVisibility(View.GONE);
			txt2.setVisibility(View.GONE);
			txt3.setVisibility(View.GONE);
			table.setVisibility(View.GONE);
		}
		else {
			
			
			txt1.setText(rcv1);

			txt2.setText(rcv2);
		
			String[] timer =  statistics.get(0).split("#");  // get the time of the report
		
			txt3.setText(timer[0]);

			// Create the first static Row of the table and set some parameters  
			TableRow tr = new TableRow(this);
			TableLayout.LayoutParams tableRowParams=
					new TableLayout.LayoutParams
					(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
			int leftMargin = 4;
			int topMargin=4;
			int rightMargin=4;
			int bottomMargin=4;

			tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

			tr.setLayoutParams(tableRowParams);
			
	
			TextView up1 = new TextView(this); 
			up1.setText("IP");
			up1.setTextColor(Color.WHITE);
			up1.setTypeface(null, Typeface.BOLD);
			up1.setTextSize(14);
			up1.setGravity(Gravity.CENTER);
			up1.setBackground(getResources().getDrawable(R.drawable.editborder));
			tr.addView(up1);
	
			TextView up2 = new TextView(this); 
			up2.setText("PATTERN");
			up2.setTextColor(Color.WHITE);
			up2.setTypeface(null, Typeface.BOLD);
			up2.setTextSize(14);
			up2.setGravity(Gravity.CENTER);
			up2.setBackground(getResources().getDrawable(R.drawable.editborder));
			tr.addView(up2);
	
			TextView up3 = new TextView(this); 
			up3.setText("FREQUENCY");
			up3.setTextColor(Color.WHITE);
			up3.setTypeface(null, Typeface.BOLD);
			up3.setTextSize(14);
			up3.setGravity(Gravity.CENTER);
			up3.setBackground(getResources().getDrawable(R.drawable.editborder));
			tr.addView(up3);
	
			table.addView(tr,new TableLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			
			
		// insert data to the table 
			for(int i=0;i<statistics.size();i++){
				TableRow tr1 = new TableRow(this);
				TableLayout.LayoutParams tableRowParams1=
						new TableLayout.LayoutParams
						(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
				

				tableRowParams1.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

				tr1.setLayoutParams(tableRowParams1);
				
				String[] temp = statistics.get(i).split("#");
				
				
				TextView t1 = new TextView(this);
				
				t1.setText(temp[1]);
				t1.setTextColor(Color.WHITE);
				t1.setGravity(Gravity.CENTER);
				t1.setTextSize(12);
				
				t1.setBackground(getResources().getDrawable(R.drawable.editborder));
				tr1.addView(t1);
				
				TextView t2 = new TextView(this); 
				t2.setText(temp[2]);
				t2.setTextColor(Color.WHITE);
				t2.setGravity(Gravity.CENTER);
				t2.setTextSize(12);
				
				t2.setBackground(getResources().getDrawable(R.drawable.editborder));
				tr1.addView(t2);
    	
				TextView t3 = new TextView(this); 
				t3.setText(temp[3]);
				t3.setTextColor(Color.WHITE);
				t3.setGravity(Gravity.CENTER);
				t3.setTextSize(12);
				
				t3.setBackground(getResources().getDrawable(R.drawable.editborder));
				tr1.addView(t3);
    	
				table.addView(tr1,new TableLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    	
			}
    	
		}
    	
	}
	
	@Override
	public void onBackPressed() {
		// on back button pressed finish this activity
		statistics.clear();
		StatsActivity.this.finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	    // Close The Database
		databaseAdapter.close();
	}
}
