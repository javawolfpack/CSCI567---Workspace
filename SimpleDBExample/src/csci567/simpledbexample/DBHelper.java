package csci567.simpledbexample;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	final static String DB_NAME = "example.db";
	final static int DB_VERSION = 1;
	private final String EXAMPLE_TABLE = "configTable";
	Context context;
	
	public DBHelper(Context context){		
		super(context, DB_NAME, null, DB_VERSION);
		this.context=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + EXAMPLE_TABLE + " (text VARCHAR);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean insertText(String text){
		try{
			DBHelper appDB = new DBHelper(context);
			SQLiteDatabase qdb = appDB.getWritableDatabase();
			Log.d("DB Insert: ", "INSERT OR REPLACE INTO " +
	    			EXAMPLE_TABLE + " (text) Values ("+ text + ");");
			qdb.execSQL("INSERT OR REPLACE INTO " +
	    			EXAMPLE_TABLE + " (text) Values (\""+ text + "\");"); 
		}
		catch(SQLiteException se){
			Log.d("DB Insert Error: ",se.toString());
			return false;
		}
		return true;
	}
	public String getText(){
		String toReturn = "";
		try{
			DBHelper appDB = new DBHelper(context);
			SQLiteDatabase qdb = appDB.getWritableDatabase();
			qdb.execSQL("CREATE TABLE IF NOT EXISTS " + EXAMPLE_TABLE + " (text VARCHAR);");
			Cursor c = qdb.rawQuery("SELECT * FROM " +
	    			EXAMPLE_TABLE, null);
			if (c != null ) {
	    		if  (c.moveToFirst()) {
	    			do {
	    				String text = c.getString(c.getColumnIndex("text"));
	    				toReturn += text + "\n";
	    			}
	    			while (c.moveToNext());
	    		}
			}
		}
		catch(SQLiteException se){
			Log.d("DB Select Error: ",se.toString());
			return "";
		}
		return toReturn;
	}

}
