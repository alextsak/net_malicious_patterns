package com.database.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseManager extends SQLiteOpenHelper{
	
	public DatabaseManager(Context context, String name, CursorFactory factory, int version){

		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database){       //if no database exists
		database.execSQL(DatabaseAdapter.DATABASE_CREATE_USER);
		database.execSQL(DatabaseAdapter.DATABASE_CREATE_NODES);
		database.execSQL(DatabaseAdapter.DATABASE_CREATE_PC_HAS_USER);
		database.execSQL(DatabaseAdapter.DATABASE_CREATE_PATTERNS);
		database.execSQL(DatabaseAdapter.DATABASE_CREATE_SR);
		database.execSQL(DatabaseAdapter.DATABASE_CREATE_PC_DELETION);
	}
	
	@Override
	 public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){ //if a new database version exists
	 
	 Log.w("Database","Database Upgrade from "+oldVersion+" to "+newVersion+" all data will destroy");

	 database.execSQL("DROP TABLE IF EXISTS "+DatabaseAdapter.TABLE_NODES); //drop database
	 database.execSQL("DROP TABLE IF EXISTS "+DatabaseAdapter.TABLE_PATTERNS);
	 database.execSQL("DROP TABLE IF EXISTS "+DatabaseAdapter.TABLE_PC_HAS_USER);
	 database.execSQL("DROP TABLE IF EXISTS "+DatabaseAdapter.TABLE_SR);
	 database.execSQL("DROP TABLE IF EXISTS "+DatabaseAdapter.TABLE_USER);
	 onCreate(database); //new database create
	 }
	

	@Override
	 public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion){ //if a new database version exists
	 
	 Log.w("Database","Database Upgrade from "+oldVersion+" to "+newVersion+" all data will destroy");

	 database.execSQL("DROP TABLE IF EXISTS "+DatabaseAdapter.TABLE_NODES); //drop database
	 database.execSQL("DROP TABLE IF EXISTS "+DatabaseAdapter.TABLE_PATTERNS);
	 database.execSQL("DROP TABLE IF EXISTS "+DatabaseAdapter.TABLE_PC_HAS_USER);
	 database.execSQL("DROP TABLE IF EXISTS "+DatabaseAdapter.TABLE_SR);
	 database.execSQL("DROP TABLE IF EXISTS "+DatabaseAdapter.TABLE_USER);
	 onCreate(database); //new database create
	 }
	
}