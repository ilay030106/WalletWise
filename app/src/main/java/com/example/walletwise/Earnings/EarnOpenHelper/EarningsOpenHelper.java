package com.example.walletwise.Earnings.EarnOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.walletwise.Spendings.SpendOpenHelper.Spending;
import com.example.walletwise.Spendings.SpendOpenHelper.SpendingsOpenHelper;

import java.util.ArrayList;

public class EarningsOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASENAME = "DBearn";//שם מסד נתונים
    public static final String TABLE_EARNING = "tblEarn";//שם הטבלה
    public static final int DATABASEVERSION = 1;

    public static final String COLUMN_ID = "ID";//מפתח ראשי - מספור אוטומטי
    public static final String COLUMN_START_DATE = "START_DATE";
    public static final String COLUMN_END_DATE = "END_DATE";
    public static final String COLUMN_START_TIME = "START_TIME";
    public static final String COLUMN_END_TIME = "END_TIME";
    public static final String COLUMN_MONEY = "MONEY";

    public static final String COLUMN_TIP = "TIP";


    private static final String CREATE_TABLE_ALL_EARNINGS = "CREATE TABLE IF NOT EXISTS " +
            TABLE_EARNING + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_START_DATE + " VARCHAR, "
            + COLUMN_START_TIME + " VARCHAR, "
            + COLUMN_END_DATE + " VARCHAR,"
            + COLUMN_END_TIME + " VARCHAR,"
            + COLUMN_MONEY + " REAL,"
            + COLUMN_TIP + " REAL " + ");";

    String[] allColumns = {COLUMN_ID, COLUMN_START_DATE, COLUMN_START_TIME, COLUMN_END_DATE, COLUMN_END_TIME, COLUMN_MONEY, COLUMN_TIP};

    SQLiteDatabase database;

    public EarningsOpenHelper(Context context) {//פעולה בונה
        super(context, DATABASENAME, null, DATABASEVERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//יצירת מסד נתונים
        db.execSQL(CREATE_TABLE_ALL_EARNINGS);
        Log.i("data", "Table Spendings created");

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EARNING);
        onCreate(db);
    }

    public void open() {//פתיחת מסד נתונים
        database = this.getWritableDatabase();
        Log.i("data", "Database connection open");
    }

    public Earning createEarning(Earning earning) {// DB
        ContentValues values = new ContentValues();
        values.put(COLUMN_START_DATE, earning.getStartDate());
        values.put(COLUMN_START_TIME, earning.getStartTime());
        values.put(COLUMN_END_DATE, earning.getEndDate());
        values.put(COLUMN_END_TIME, earning.getEndTime());
        values.put(COLUMN_MONEY, earning.getMoney());
        values.put(COLUMN_TIP, earning.getTip());


        long insertId = database.insert(EarningsOpenHelper.TABLE_EARNING, null, values);
        Log.i("data", "UserEarn " + insertId + "insert to database");
        earning.setId(insertId);
        return earning;
    }

    public ArrayList<Earning> getAllEarnings() {
        ArrayList<Earning> Earnings = new ArrayList<Earning>();
        //שאילתת בחירה
        Cursor cursor = database.query(EarningsOpenHelper.TABLE_EARNING, allColumns, null, null, null, null, null);
        //אם מספר שורות גדול מ0
        if (cursor.getCount() > 0) {
            //כל עוד שניתן להתקדם בטבלה
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String startDate = cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE));
                String startTime = cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME));
                String endDate = cursor.getString(cursor.getColumnIndex(COLUMN_END_DATE));
                String endTime = cursor.getString(cursor.getColumnIndex(COLUMN_END_DATE));
                double money = cursor.getDouble(cursor.getColumnIndex(COLUMN_MONEY));
                double tip = cursor.getDouble(cursor.getColumnIndex(COLUMN_TIP));

                //יצירת מכונית
                Earning earning = new Earning(startDate, startTime, endDate, endTime, money, tip);
                //עדכון מפתח ראשי
                earning.setId(id);
                //הוספת מכונית לרשימת מכוניות
                Earnings.add(earning);
            }
        }
        Log.i("data", "כל ההכנסות");
        return Earnings;
    }

    public Earning getEarningByID(long id) {
        ArrayList<Earning> l = new ArrayList<Earning>();
        Cursor cursor = database.query(EarningsOpenHelper.TABLE_EARNING, allColumns, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id1 = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String startDate = cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE));
                String startTime = cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME));
                String endDate = cursor.getString(cursor.getColumnIndex(COLUMN_END_DATE));
                String endTime = cursor.getString(cursor.getColumnIndex(COLUMN_END_DATE));
                double money = cursor.getDouble(cursor.getColumnIndex(COLUMN_MONEY));
                double tip = cursor.getDouble(cursor.getColumnIndex(COLUMN_TIP));

                if (id == id1) {
                    Earning earning = new Earning(startDate, startTime, endDate, endTime, money, tip);
                    earning.setId(id);
                    return earning;
                }
            }
            Log.i("data", "כל  ההכנסות ");
        }
        return null;

    }

    public long UpdateEarning(Earning earning) {// DB
        ContentValues values = new ContentValues();
        values.put(COLUMN_START_DATE, earning.getStartDate());
        values.put(COLUMN_START_TIME, earning.getStartTime());
        values.put(COLUMN_END_DATE, earning.getEndDate());
        values.put(COLUMN_END_TIME, earning.getEndTime());
        values.put(COLUMN_MONEY, earning.getMoney());
        values.put(COLUMN_TIP, earning.getTip());
        long updateId = database.update(EarningsOpenHelper.TABLE_EARNING, values, COLUMN_ID + "=" + earning.getId(), null);
        Log.i("data", "Earning " + updateId + "insert to database");
        return updateId;
    }

    public long deleteById(long id) {
        long deleteid = database.delete(EarningsOpenHelper.TABLE_EARNING, COLUMN_ID + "=" + id, null);
        Log.i("data", "Earning " + deleteid + "insert to database");
        return deleteid;
    }

    public double getSumEarningByDate(String month, String year) {
        double sumEarn = 0;
        double earnMoney;
        String selectQuery = "SELECT * FROM " + TABLE_EARNING + " WHERE ";
        selectQuery += COLUMN_START_DATE + " LIKE '%/" + month + "/" + year + "' AND ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery.substring(0, selectQuery.length() - " AND ".length()), null);
        if (cursor.moveToFirst()) {
            do {
                earnMoney = cursor.getDouble(cursor.getColumnIndex(COLUMN_MONEY));
                earnMoney += cursor.getDouble(cursor.getColumnIndex(COLUMN_TIP));
                sumEarn += earnMoney;

            } while (cursor.moveToNext());
        }
        return sumEarn;
    }

    public ArrayList<Earning> searchEarnings(String startDateMonth, String startDateYear, String endDateMonth, String endDateYear, double max, double min, double maxTips, double minTips) {

        ArrayList<Earning> earningList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EARNING + " WHERE ";
        if (!startDateMonth.equals("") && !startDateYear.equals("")) {
            selectQuery += COLUMN_START_DATE + " LIKE '%/" + startDateMonth + "/" + startDateYear + "' AND ";
        }
        if (!endDateMonth.equals("") && !endDateYear.equals("")) {
            selectQuery += COLUMN_END_DATE + " LIKE '%/" + startDateMonth + "/" + startDateYear + "' AND ";
        }

        if (min > -1) {
            selectQuery += COLUMN_MONEY + " >= " + min + " AND ";

        }
        if (max > -1) {
            selectQuery += COLUMN_MONEY + " <= " + max + " AND ";

        }
        if (minTips > -1) {
            selectQuery += COLUMN_TIP + " >= " + minTips + " AND ";

        }
        if (maxTips > -1) {
            selectQuery += COLUMN_TIP + " <= " + maxTips + " AND ";

        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery.substring(0, selectQuery.length() - " AND ".length()), null);
        if (cursor.moveToFirst()) {
            do {
                Earning earning = new Earning(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getDouble(5),
                        cursor.getDouble(6)

                );
                earning.setId(Integer.parseInt(cursor.getString(0)));
                earningList.add(earning);
            } while (cursor.moveToNext());

        }
        return earningList;
    }

    public double getSumTipsByDate(String month, String year) {
        double sumTips = getSumEarningByDate(month, year);
        double earnSalary;
        String selectQuery = "SELECT * FROM " + TABLE_EARNING + " WHERE ";
        selectQuery += COLUMN_START_DATE + " LIKE '%/" + month + "/" + year + "' AND ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery.substring(0, selectQuery.length() - " AND ".length()), null);
        if (cursor.moveToFirst()) {
            do {
                earnSalary = cursor.getDouble(cursor.getColumnIndex(COLUMN_MONEY));
                sumTips -= earnSalary;
            } while (cursor.moveToNext());
        }
        return sumTips;
    }

    public double getSumPaymentByDate(String month, String year) {
        double sumPayment = getSumEarningByDate(month, year);
        double earnTip;
        String selectQuery = "SELECT * FROM " + TABLE_EARNING + " WHERE ";
        selectQuery += COLUMN_START_DATE + " LIKE '%/" + month + "/" + year + "' AND ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery.substring(0, selectQuery.length() - " AND ".length()), null);
        if (cursor.moveToFirst()) {
            do {
                earnTip = cursor.getDouble(cursor.getColumnIndex(COLUMN_TIP));
                sumPayment -= earnTip;
            } while (cursor.moveToNext());
        }
        return sumPayment;
    }

    public int calculateHoursbyDate(String month, String year) {
        int sumMinutes = 0;
        String selectQuery = "SELECT * FROM " + TABLE_EARNING + " WHERE ";
        selectQuery += COLUMN_START_DATE + " LIKE '%/" + month + "/" + year + "' AND ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery.substring(0, selectQuery.length() - " AND ".length()), null);
        if (cursor.moveToFirst()) {
            do {
                String startDate = cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE));
                String startTime = cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME));
                String endDate = cursor.getString(cursor.getColumnIndex(COLUMN_END_DATE));
                String endTime = cursor.getString(cursor.getColumnIndex(COLUMN_END_TIME));
                double money = cursor.getDouble(cursor.getColumnIndex(COLUMN_MONEY));
                double tip = cursor.getDouble(cursor.getColumnIndex(COLUMN_TIP));
                Earning earning = new Earning(startDate, startTime, endDate, endTime, money, tip);
                sumMinutes += (int) earning.calculateMinutes();
            } while (cursor.moveToNext());

        }
        return sumMinutes / 60;
    }

    public int calculateMinutesbyDate(String month, String year) {
        int sumMinutes = 0;
        String selectQuery = "SELECT * FROM " + TABLE_EARNING + " WHERE ";
        selectQuery += COLUMN_START_DATE + " LIKE '%/" + month + "/" + year + "' AND ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery.substring(0, selectQuery.length() - " AND ".length()), null);
        if (cursor.moveToFirst()) {
            do {
                String startDate = cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE));
                String startTime = cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME));
                String endDate = cursor.getString(cursor.getColumnIndex(COLUMN_END_DATE));
                String endTime = cursor.getString(cursor.getColumnIndex(COLUMN_END_TIME));
                double money = cursor.getDouble(cursor.getColumnIndex(COLUMN_MONEY));
                double tip = cursor.getDouble(cursor.getColumnIndex(COLUMN_TIP));
                Earning earning = new Earning(startDate, startTime, endDate, endTime, money, tip);
                sumMinutes += (int) earning.calculateMinutes();
            } while (cursor.moveToNext());

        }
        return sumMinutes % 60;
    }


}
