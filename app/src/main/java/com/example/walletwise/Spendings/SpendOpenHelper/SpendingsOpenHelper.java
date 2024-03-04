package com.example.walletwise.Spendings.SpendOpenHelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class SpendingsOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASENAME = "DBspend3";//שם מסד נתונים
    public static final String TABLE_SPENDING = "tblSpend3";//שם הטבלה
    public static final int DATABASEVERSION = 3;

    public static final String COLUMN_ID = "ID";//מפתח ראשי - מספור אוטומטי

    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_TIME = "TIME";
    public static final String COLUMN_DESC = "desc1";

    public static final String COLUMN_PRICE = "Price";
    public static final String COLUMN_TYPE = "Type";

    public static final String COLUMN_IMAGE = "image";

    private static final String CREATE_TABLE_ALL_SPENDINGS = "CREATE TABLE IF NOT EXISTS " +
            TABLE_SPENDING + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DESC + " VARCHAR, "
            + COLUMN_PRICE + " REAL, "
            + COLUMN_TYPE + " VARCHAR,"
            + COLUMN_DATE + " VARCHAR,"
            + COLUMN_TIME + " VARCHAR,"
            + COLUMN_IMAGE + " BLOB " + ");";

    String[] allColumns = {COLUMN_ID, COLUMN_DESC, COLUMN_PRICE, COLUMN_TYPE, COLUMN_DATE, COLUMN_TIME, COLUMN_IMAGE, };

    SQLiteDatabase database;


    public SpendingsOpenHelper(Context context) {//פעולה בונה
        super(context, DATABASENAME, null, DATABASEVERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//יצירת מסד נתונים
        db.execSQL(CREATE_TABLE_ALL_SPENDINGS);
        Log.i("data", "Table Spendings created");

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPENDING);
        onCreate(db);
    }

    public void open() {//פתיחת מסד נתונים
        database = this.getWritableDatabase();
        Log.i("data", "Database connection open");
    }

    public Spending createSpending(Spending Spend) {// DB
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESC, Spend.getDesc());
        values.put(COLUMN_PRICE, Spend.getPrice());
        values.put(COLUMN_TYPE, Spend.getType());
        values.put(COLUMN_DATE, Spend.getDate());
        values.put(COLUMN_TIME, Spend.getTime());
        values.put(COLUMN_IMAGE, Spend.getPic());



        long insertId = database.insert(SpendingsOpenHelper.TABLE_SPENDING, null, values);
        Log.i("data", "UserSpend " + insertId + "insert to database");
        Spend.setId(insertId);
        return Spend;
    }

    public ArrayList<Spending> getAllSpendings() {
        ArrayList<Spending> Spendings = new ArrayList<Spending>();
        //שאילתת בחירה
        Cursor cursor = database.query(SpendingsOpenHelper.TABLE_SPENDING, allColumns, null, null, null, null, null);
        //אם מספר שורות גדול מ0
        if (cursor.getCount() > 0) {
            //כל עוד שניתן להתקדם בטבלה
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String desc = cursor.getString(cursor.getColumnIndex(COLUMN_DESC));
                double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));
                String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                byte[] pic = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));

                //יצירת בזבוז
                Spending spending = new Spending(desc, price, type, date, time, pic);
                //עדכון מפתח ראשי
                spending.setId(id);
                //הוספת בזבוז לרשימת בזבוזים
                Spendings.add(spending);
            }
        }
        Log.i("data", "כל הבזבוזים");
        return Spendings;
    }

    public Spending getSpendingbyID(long id) {
        ArrayList<Spending> l = new ArrayList<Spending>();
        Cursor cursor = database.query(SpendingsOpenHelper.TABLE_SPENDING, allColumns, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id1 = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String desc = cursor.getString(cursor.getColumnIndex(COLUMN_DESC));
                double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));
                String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                byte[] pic = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));


                if (id == id1) {
                    Spending spend = new Spending(desc, price, type, date, time, pic);
                    spend.setId(id);
                    return spend;
                }
            }
            Log.i("data", "כל הבזבוזים ");
        }
        return null;

    }

    public long updateSpending(Spending Spend) {// DB
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESC, Spend.getDesc());
        values.put(COLUMN_PRICE, Spend.getPrice());
        values.put(COLUMN_TYPE, Spend.getType());
        values.put(COLUMN_DATE, Spend.getDate());
        values.put(COLUMN_TIME, Spend.getTime());
        values.put(COLUMN_IMAGE, Spend.getPic());
        long updateId = database.update(SpendingsOpenHelper.TABLE_SPENDING, values, COLUMN_ID + "=" + Spend.getId(), null);
        Log.i("data", "UserCar " + updateId + "insert to database");
        return updateId;
    }

    public long deleteById(long id) {
        long deleteid = database.delete(SpendingsOpenHelper.TABLE_SPENDING, COLUMN_ID + "=" + id, null);
        Log.i("data", "Spending " + deleteid + "insert to database");
        return deleteid;
    }

    public long deleteAll() {//יצירת רשומה- מכונית בתוך DB
        return database.delete(SpendingsOpenHelper.TABLE_SPENDING, null, null);
    }

    public ArrayList<Spending> searchSpendings(String month, String year, String type, double min, double max) {

        ArrayList<Spending> spendingList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SPENDING + " WHERE ";
        if (!month.equals("") && !year.equals("")) {
            selectQuery += COLUMN_DATE + " LIKE '%/" + month + "/" + year + "' AND ";
        }

        if (!type.equals("")) {
            selectQuery += COLUMN_TYPE + " = '" + type + "' AND ";

        }
        if (min > -1) {
            selectQuery += COLUMN_PRICE + " >= " + min + " AND ";

        }
        if (max > -1) {
            selectQuery += COLUMN_PRICE + " <= " + max + " AND ";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery.substring(0, selectQuery.length() - " AND ".length()), null);
        if (cursor.moveToFirst()) {
            do {
                Spending spending = new Spending(
                        cursor.getString(cursor.getColumnIndex(COLUMN_DESC)),
                        cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TIME)),
                        cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE)));
                spending.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                spendingList.add(spending);
            } while (cursor.moveToNext());

        }
        return spendingList;
    }


    public double getSumSpendingByDate(String month, String year) {
        double sumSpend = 0;
        double spendPrice = 0;
        String selectQuery = "SELECT * FROM " + TABLE_SPENDING + " WHERE ";
        selectQuery += COLUMN_DATE + " LIKE '%/" + month + "/" + year + "' AND ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery.substring(0, selectQuery.length() - " AND ".length()), null);
        if (cursor.moveToFirst()) {
            do {
                spendPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));
                sumSpend += spendPrice;
                spendPrice = 0;
            } while (cursor.moveToNext());
        }
        return sumSpend;
    }


    public boolean ExistTypeByDate(String month, String year, String type1) {//אם קיים בזבוז מהסוג הנקלט
        String selectQuery = "SELECT * FROM " + TABLE_SPENDING + " WHERE ";
        selectQuery += COLUMN_DATE + " LIKE '%/" + month + "/" + year + "' AND ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery.substring(0, selectQuery.length() - " AND ".length()), null);
        if (cursor.getCount() > 0) {
            //כל עוד שניתן להתקדם בטבלה
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String desc = cursor.getString(cursor.getColumnIndex(COLUMN_DESC));
                double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));
                String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                byte[] pic = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
                Spending spending = new Spending(desc, price, type, date, time, pic);
                spending.setId(id);
                if (type.equals(type1))
                    return true;
            }
        }
        return false;
    }

    public int getCountOfSpendTypeAndDate(String month, String year, String type1) {//לבדוק כמה בזבוזים מהסוג הנקלט יש כל חודש
        int count = 0;

        if (this.ExistTypeByDate(month, year, type1)) {
            String selectQuery = "SELECT * FROM " + TABLE_SPENDING + " WHERE ";
            selectQuery += COLUMN_DATE + " LIKE '%/" + month + "/" + year + "' AND ";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery.substring(0, selectQuery.length() - " AND ".length()), null);
            if (cursor.getCount() > 0) {
                //כל עוד שניתן להתקדם בטבלה
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    String desc = cursor.getString(cursor.getColumnIndex(COLUMN_DESC));
                    double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));
                    String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                    String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                    String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                    byte[] pic = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
                    Spending spending = new Spending(desc, price, type, date, time, pic);
                    spending.setId(id);
                    if (type.equals(type1))
                        count++;
                }
            }
        }
        return count;

    }


    public int countAllSpendingsByDate(String month, String year) {
        int count = 0;
        String selectQuery = "SELECT * FROM " + TABLE_SPENDING + " WHERE ";
        selectQuery += COLUMN_DATE + " LIKE '%/" + month + "/" + year + "' AND ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery.substring(0, selectQuery.length() - " AND ".length()), null);
        if (cursor.getCount() > 0) {
            //כל עוד שניתן להתקדם בטבלה
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String desc = cursor.getString(cursor.getColumnIndex(COLUMN_DESC));
                double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));
                String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                byte[] pic = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
                Spending spending = new Spending(desc, price, type, date, time, pic);
                spending.setId(id);
                count++;

            }
        }
        return count;
    }


}



