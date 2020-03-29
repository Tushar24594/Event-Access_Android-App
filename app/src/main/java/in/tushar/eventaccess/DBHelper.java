package in.tushar.eventaccess;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "data.db";
    public static final String TABLE_NAME = "contacts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_QR = "qr_data";
    public static final String COLUMN_CHECK_IN = "checkIn";
    public static final String COLUMN_CHECK_OUT = "checkOut";
    public static final String COLUMN_DATE = "date";
    private HashMap hp;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyy hh:mm:ss aa");
    String date;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table contacts " +
                        "("+COLUMN_NAME+" text, "+COLUMN_PHONE+" text primary key NOT NULL, "+COLUMN_EMAIL+" text, "+COLUMN_QR+" text, "+COLUMN_CHECK_IN+" text, "+COLUMN_CHECK_OUT+" text, "+COLUMN_DATE+" text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertData(String name, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_PHONE, phone);
        contentValues.put(COLUMN_EMAIL, email);
        date = simpleDateFormat.format(calendar.getTime());
        contentValues.put(COLUMN_DATE, date);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+"where phone=" + phone + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateData(String phone, String qr, String checkIn, String checkOut) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_QR, qr);
        contentValues.put(COLUMN_CHECK_IN, checkIn);
        contentValues.put(COLUMN_CHECK_OUT, checkOut);
        date = simpleDateFormat.format(calendar.getTime());
        contentValues.put(COLUMN_DATE, date);
        db.update(TABLE_NAME, contentValues, COLUMN_PHONE+" = ? ", new String[]{phone});
        return true;
    }

    public Integer deleteData(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                COLUMN_PHONE+" = ? ",
                new String[]{phone});
    }

    public ArrayList<String> getAllData() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}
