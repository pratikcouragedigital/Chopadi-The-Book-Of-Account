package com.mobitechs.chopadi.sqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class DataBaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Chopadi";

    private static final String TABLE_CONTACTS = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_FNAME = "fname";
    private static final String KEY_POTO = "poto";

    public static final String TABLE_CUSTOMER = "Customer";
    public static final String CUSTOMER_ID = "customerId"; //primary key
    public static final String CUSTOMER_NAME = "customerName";
    public static final String CUSTOMER_NO = "customerNo";

    public static final String TABLE_PRODUCT = "Product";
    public static final String PRODUCT_ID = "productId"; //primary key
    public static final String PRODUCT_NAME = "productName";
    public static final String PRODUCT_PRICE = "productPrice";

    public static final String TABLE_BILL = "BILL";
    public static final String BILL_ID = "billId"; //primary key
    public static final String BILL_ENTRYDATE = "entryDate";
    public static final String BILL_DATE = "date";
    public static final String PRODUCT_QTY = "qty";
    public static final String TOTAL_PRICE = "totalPrice";
    public static final String DAY = "day";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String ENTRY_DATE = "entryDate";

    public static final String TABLE_USER_BILL = "UserBill";
    public static final String USER_BILL_ID = "UserBillID";
    public static final String FROM_DATE = "fromDate";
    public static final String TO_DATE = "toDate";
    public static final String BILL_AMOUNT = "amount";
    public static final String PAID_AMOUNT = "paid";
    public static final String BALANCE_AMOUNT = "balance";
    public static final String LAST_BILL_RECORD_ID = "lastBillRecordId";

    public HashMap hp;
    Context context;

    //    String CREATE_TABLE_CUSTOMER = "create table " + TABLE_CUSTOMER + "(" + CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CUSTOMER_NAME + " varchar(50))";
    String CREATE_TABLE_CUSTOMER = "create table " + TABLE_CUSTOMER + "(" + CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CUSTOMER_NAME + " varchar(100)," + CUSTOMER_NO + " varchar(500))";
    String CREATE_TABLE_PRODUCT = "create table " + TABLE_PRODUCT + "(" + PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PRODUCT_NAME + " varchar(100)," + PRODUCT_PRICE + " varchar(500))";
    String CREATE_TABLE_BILL = "create table " + TABLE_BILL + "(" + BILL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CUSTOMER_ID + " varchar(100)," + CUSTOMER_NAME + " varchar(500)," + BILL_ENTRYDATE + " varchar(500)," + BILL_DATE + " varchar(500)," + PRODUCT_NAME + " varchar(500)," + PRODUCT_PRICE + " varchar(500)," + PRODUCT_QTY + " varchar(500)," + TOTAL_PRICE + " varchar(500)," + DAY + " INTEGER," + MONTH + " INTEGER," + YEAR + " INTEGER)";
    String CREATE_TABLE_USER_BILL = "create table " + TABLE_USER_BILL + "(" + USER_BILL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CUSTOMER_ID + " varchar(100)," + CUSTOMER_NAME + " varchar(500)," + FROM_DATE + " varchar(500)," + TO_DATE + " varchar(500)," + BILL_AMOUNT + " varchar(500)," + PAID_AMOUNT + " varchar(500)," + BALANCE_AMOUNT + " varchar(500)," + ENTRY_DATE + " varchar(500)," + LAST_BILL_RECORD_ID + " varchar(500))";

    String CREATE_TABLE_CONTACTS = "CREATE TABLE " + TABLE_CONTACTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_FNAME + " TEXT,"
            + KEY_POTO + " BLOB" + ")";

    public DataBaseHelper(Context con) {
        super(con, DATABASE_NAME, null, DATABASE_VERSION);
//       super(context, Environment.getExternalStorageDirectory()+"/data/com.mobitechs.teaDiary/databases/TeaDiary".toString(), null, DATABASE_VERSION);
//        SQLiteDatabase.openOrCreateDatabase( Environment.getExternalStorageDirectory()+"/data/com.mobitechs.teaDiary/databases/TeaDiary", null);
    }

    @Override
    public String getDatabaseName() {
        return super.getDatabaseName();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CUSTOMER);
        db.execSQL(CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_BILL);
        db.execSQL(CREATE_TABLE_USER_BILL);
        db.execSQL(CREATE_TABLE_CONTACTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_BILL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);


        onCreate(db);
    }

    public void addContacts(String f_name, byte[] photo) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_FNAME, f_name);
        values.put(KEY_POTO, photo);


        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    public JSONArray Customer_List() throws JSONException {

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while (res.isAfterLast() == false) {
            JSONObject details = new JSONObject();
            details.put(CUSTOMER_NAME, res.getString(res.getColumnIndex(CUSTOMER_NAME)));
            details.put(CUSTOMER_NO, res.getString(res.getColumnIndex(CUSTOMER_NO)));
            details.put(CUSTOMER_ID, res.getString(res.getColumnIndex(CUSTOMER_ID)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

    public boolean Customer_Add(String CustomerName, String customerNo) {

        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + CUSTOMER_NAME + " = '" + CustomerName + "' AND " + CUSTOMER_NO + " = '" + customerNo + "'";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();

        if (count == 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(CUSTOMER_NAME, CustomerName);
            contentValues.put(CUSTOMER_NO, customerNo);
            db.insert(TABLE_CUSTOMER, null, contentValues);
            db.close();
            return true;
        } else {
            return false;
        }
    }

    public boolean Customer_Edit(String id, String customerName, String customerNo) {

        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + CUSTOMER_ID + " = " + id + " ";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();

        if (count == 1) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(CUSTOMER_NAME, customerName);
            contentValues.put(CUSTOMER_NO, customerNo);
            db.update(TABLE_CUSTOMER, contentValues, CUSTOMER_ID + " = ?", new String[]{id});
            db.close();
            return true;
        } else {
            return false;
        }
    }

    public boolean Customer_Delete(String id) {

        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + CUSTOMER_ID + " = " + id + " ";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();

        if (count == 1) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_CUSTOMER, CUSTOMER_ID + " = ?", new String[]{id});
            db.close();
            return true;
        } else {
            return false;
        }
    }


    public JSONArray Product_List() throws JSONException {

        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while (res.isAfterLast() == false) {
            JSONObject details = new JSONObject();
            details.put(PRODUCT_ID, res.getString(res.getColumnIndex(PRODUCT_ID)));
            details.put(PRODUCT_NAME, res.getString(res.getColumnIndex(PRODUCT_NAME)));
            details.put(PRODUCT_PRICE, res.getString(res.getColumnIndex(PRODUCT_PRICE)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

    public boolean Product_Add(String productName, String productPrice) {

        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT + " WHERE " + PRODUCT_NAME + " = '" + productName + "'";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();

        if (count == 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PRODUCT_NAME, productName);
            contentValues.put(PRODUCT_PRICE, productPrice);
            db.insert(TABLE_PRODUCT, null, contentValues);
            db.close();
            return true;
        } else {
            return false;
        }
    }

    public boolean Product_Edit(String id, String productName, String price) {

        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT + " WHERE " + PRODUCT_ID + " = " + id + " ";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();

        if (count == 1) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PRODUCT_NAME, productName);
            contentValues.put(PRODUCT_PRICE, price);
            db.update(TABLE_PRODUCT, contentValues, PRODUCT_ID + " = ?", new String[]{id});
            db.close();
            return true;
        } else {
            return false;
        }
    }

    public boolean Product_Delete(String id) {

        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT + " WHERE " + PRODUCT_ID + " = " + id + " ";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();

        if (count == 1) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_PRODUCT, PRODUCT_ID + " = ?", new String[]{id});
            db.close();
            return true;
        } else {
            return false;
        }
    }


    public boolean Bill_Generate(String customerId, String customerName, String entryDate, String selectedDate, String productName, String productPrice, String productQty, String pTotal, String aDay, String aMonth, String aYear) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CUSTOMER_ID, customerId);
        contentValues.put(CUSTOMER_NAME, customerName);
        contentValues.put(ENTRY_DATE, entryDate);
        contentValues.put(BILL_DATE, selectedDate);
        contentValues.put(PRODUCT_NAME, productName);
        contentValues.put(PRODUCT_PRICE, productPrice);
        contentValues.put(PRODUCT_QTY, productQty);
        contentValues.put(TOTAL_PRICE, pTotal);
        contentValues.put(DAY, aDay);
        contentValues.put(MONTH, aMonth);
        contentValues.put(YEAR, aYear);
        db.insert(TABLE_BILL, null, contentValues);
        db.close();
        return true;
    }

    public boolean Bill_Edit(String billId, String qty, String pTotal, String date) {

        String selectQuery = "SELECT * FROM " + TABLE_BILL + " WHERE " + BILL_ID + " = " + billId + " ";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();

        if (count == 1) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PRODUCT_QTY, qty);
            contentValues.put(TOTAL_PRICE, pTotal);
            db.update(TABLE_BILL, contentValues, BILL_ID + " = ?", new String[]{billId});
            db.close();
            return true;
        } else {
            return false;
        }
    }

    public JSONArray AllCustomer_BillDetails_DateWise(String date) throws JSONException {

        String selectQuery = "Select b." + CUSTOMER_ID + ",sum(b." + TOTAL_PRICE + ") as " + TOTAL_PRICE + ",b." + BILL_DATE + ",b." + ENTRY_DATE + ",c." + CUSTOMER_NAME + " from " + TABLE_BILL + " b INNER JOIN " + TABLE_CUSTOMER + " c on c." + CUSTOMER_ID + " = b." + CUSTOMER_ID + " WHERE b." + BILL_DATE + " = '" + date + "' GROUP BY b." + CUSTOMER_ID + "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();

        JSONArray detailsArray = new JSONArray();
        int count = res.getCount();

        for(int i = 0; i< count; i++){
            String selectQuery2 = "";
            String custId = res.getString(res.getColumnIndex(CUSTOMER_ID));
            String selectQueryId = "SELECT * FROM " + TABLE_USER_BILL + " WHERE " + CUSTOMER_ID + "='" + custId + "' AND " + TO_DATE + "= '" + date + "'";
            SQLiteDatabase dbId = this.getReadableDatabase();
            Cursor resId = dbId.rawQuery(selectQueryId, null);
            resId.moveToFirst();

            int resIdCount = resId.getCount();
            if(resIdCount != 0){
                String lastBillId = resId.getString(resId.getColumnIndex(LAST_BILL_RECORD_ID));
                selectQuery2 = "Select b." + CUSTOMER_ID + ",sum(b." + TOTAL_PRICE + ") as " + TOTAL_PRICE + ",b." + BILL_DATE + ",b." + ENTRY_DATE + ",c." + CUSTOMER_NAME + " from " + TABLE_BILL + " b INNER JOIN " + TABLE_CUSTOMER + " c on c." + CUSTOMER_ID + " = b." + CUSTOMER_ID + " WHERE b." + BILL_DATE + " = '" + date + "' AND b." + CUSTOMER_ID + " = '" + custId + "' AND  b." + BILL_ID + " > '" + lastBillId + "' GROUP BY b." + CUSTOMER_ID + "";
            }else{
                selectQuery2 = "Select b." + CUSTOMER_ID + ",sum(b." + TOTAL_PRICE + ") as " + TOTAL_PRICE + ",b." + BILL_DATE + ",b." + ENTRY_DATE + ",c." + CUSTOMER_NAME + " from " + TABLE_BILL + " b INNER JOIN " + TABLE_CUSTOMER + " c on c." + CUSTOMER_ID + " = b." + CUSTOMER_ID + " WHERE b." + BILL_DATE + " = '" + date + "' AND  b."+CUSTOMER_ID+" = '"+custId+"' GROUP BY b." + CUSTOMER_ID + "";
            }
            SQLiteDatabase db2 = this.getReadableDatabase();
            Cursor res2 = db2.rawQuery(selectQuery2, null);
            res2.moveToFirst();
            int resCount2 = res2.getCount();

            for(int j = 0; j<resCount2;  j++){
                JSONObject details = new JSONObject();
                details.put(CUSTOMER_ID, res2.getString(res2.getColumnIndex(CUSTOMER_ID)));
                details.put(CUSTOMER_NAME, res2.getString(res2.getColumnIndex(CUSTOMER_NAME)));
                details.put(BILL_DATE, res2.getString(res2.getColumnIndex(BILL_DATE)));
                details.put(TOTAL_PRICE, res2.getString(res2.getColumnIndex(TOTAL_PRICE)));

                detailsArray.put(i, details);
                res2.moveToNext();
            }
            res2.close();

            res.moveToNext();
        }
        res.close();
        return detailsArray;
    }

    public JSONArray Customer_BillDetails_DateWise(String date, String cId) throws JSONException {

//        String selectQuery = "Select b."+DAY+",b."+MONTH+",b."+YEAR+", b."+BILL_ID+",b."+CUSTOMER_ID+",b."+PRODUCT_NAME+",b."+PRODUCT_PRICE+",sum(b."+PRODUCT_QTY+") as "+PRODUCT_QTY+",sum(b."+TOTAL_PRICE+") as "+TOTAL_PRICE+",b."+ENTRY_DATE+",b."+BILL_DATE+",c."+CUSTOMER_NAME+" from "+TABLE_BILL+" b INNER JOIN "+TABLE_CUSTOMER+" c on c."+CUSTOMER_ID+" = b."+CUSTOMER_ID+" WHERE b."+BILL_DATE+" = '"+date+"'  ANd b."+CUSTOMER_ID+" = '"+cId+"' GROUP BY b."+PRODUCT_NAME+"";
        String selectQuery = "Select b." + DAY + ",b." + MONTH + ",b." + YEAR + ", b." + BILL_ID + ",b." + CUSTOMER_ID + ",b." + PRODUCT_NAME + ",b." + PRODUCT_PRICE + ",b." + PRODUCT_QTY + " ,b." + TOTAL_PRICE + ",b." + ENTRY_DATE + ",b." + BILL_DATE + ",c." + CUSTOMER_NAME + " from " + TABLE_BILL + " b INNER JOIN " + TABLE_CUSTOMER + " c on c." + CUSTOMER_ID + " = b." + CUSTOMER_ID + " WHERE b." + BILL_DATE + " = '" + date + "'  ANd b." + CUSTOMER_ID + " = '" + cId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while (res.isAfterLast() == false) {
            JSONObject details = new JSONObject();
            details.put(BILL_ID, res.getString(res.getColumnIndex(BILL_ID)));
            details.put(CUSTOMER_ID, res.getString(res.getColumnIndex(CUSTOMER_ID)));
            details.put(CUSTOMER_NAME, res.getString(res.getColumnIndex(CUSTOMER_NAME)));
            details.put(ENTRY_DATE, res.getString(res.getColumnIndex(ENTRY_DATE)));
            details.put(BILL_DATE, res.getString(res.getColumnIndex(BILL_DATE)));
            details.put(PRODUCT_NAME, res.getString(res.getColumnIndex(PRODUCT_NAME)));
            details.put(PRODUCT_PRICE, res.getString(res.getColumnIndex(PRODUCT_PRICE)));
            details.put(PRODUCT_QTY, res.getString(res.getColumnIndex(PRODUCT_QTY)));
            details.put(TOTAL_PRICE, res.getString(res.getColumnIndex(TOTAL_PRICE)));
            details.put(DAY, res.getString(res.getColumnIndex(DAY)));
            details.put(MONTH, res.getString(res.getColumnIndex(MONTH)));
            details.put(YEAR, res.getString(res.getColumnIndex(YEAR)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

    public JSONArray Customer_BillDetails_MonthWise(String cId, String month, String year) throws JSONException {

//        String selectQuery = "Select b."+DAY+",b."+MONTH+",b."+YEAR+", b."+BILL_ID+",b."+CUSTOMER_ID+",b."+PRODUCT_NAME+",b."+PRODUCT_PRICE+",sum(b."+PRODUCT_QTY+") as "+PRODUCT_QTY+",sum(b."+TOTAL_PRICE+") as "+TOTAL_PRICE+",b."+ENTRY_DATE+",b."+BILL_DATE+",c."+CUSTOMER_NAME+" from "+TABLE_BILL+" b INNER JOIN "+TABLE_CUSTOMER+" c on c."+CUSTOMER_ID+" = b."+CUSTOMER_ID+" WHERE b."+MONTH+" = '"+month+"' ANd b."+YEAR+" = '"+year+"' ANd b."+CUSTOMER_ID+" = '"+cId+"' GROUP BY b."+PRODUCT_NAME+" ,b."+BILL_DATE+" ORDER BY b."+DAY+"";
        String selectQuery = "Select b." + DAY + ",b." + MONTH + ",b." + YEAR + ", b." + BILL_ID + ",b." + CUSTOMER_ID + ",b." + PRODUCT_NAME + ",b." + PRODUCT_PRICE + "," + PRODUCT_QTY + ",b." + TOTAL_PRICE + ",b." + ENTRY_DATE + ",b." + BILL_DATE + ",c." + CUSTOMER_NAME + " from " + TABLE_BILL + " b INNER JOIN " + TABLE_CUSTOMER + " c on c." + CUSTOMER_ID + " = b." + CUSTOMER_ID + " WHERE b." + MONTH + " = '" + month + "' ANd b." + YEAR + " = '" + year + "' ANd b." + CUSTOMER_ID + " = '" + cId + "' ORDER BY b." + DAY + "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while (res.isAfterLast() == false) {
            JSONObject details = new JSONObject();
            details.put(BILL_ID, res.getString(res.getColumnIndex(BILL_ID)));
            details.put(CUSTOMER_ID, res.getString(res.getColumnIndex(CUSTOMER_ID)));
            details.put(CUSTOMER_NAME, res.getString(res.getColumnIndex(CUSTOMER_NAME)));
            details.put(ENTRY_DATE, res.getString(res.getColumnIndex(ENTRY_DATE)));
            details.put(BILL_DATE, res.getString(res.getColumnIndex(BILL_DATE)));
            details.put(PRODUCT_NAME, res.getString(res.getColumnIndex(PRODUCT_NAME)));
            details.put(PRODUCT_PRICE, res.getString(res.getColumnIndex(PRODUCT_PRICE)));
            details.put(PRODUCT_QTY, res.getString(res.getColumnIndex(PRODUCT_QTY)));
            details.put(TOTAL_PRICE, res.getString(res.getColumnIndex(TOTAL_PRICE)));
            details.put(DAY, res.getString(res.getColumnIndex(DAY)));
            details.put(MONTH, res.getString(res.getColumnIndex(MONTH)));
            details.put(YEAR, res.getString(res.getColumnIndex(YEAR)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

    public boolean User_Bill_Add(String customerId, String customerName, String lastBillRecordId, String fromDate, String toDate, String amount, String paidAmount, String balanceAmount, String entryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CUSTOMER_ID, customerId);
        contentValues.put(CUSTOMER_NAME, customerName);
        contentValues.put(FROM_DATE, fromDate);
        contentValues.put(TO_DATE, toDate);
        contentValues.put(BILL_AMOUNT, amount);
        contentValues.put(PAID_AMOUNT, paidAmount);
        contentValues.put(BALANCE_AMOUNT, balanceAmount);
        contentValues.put(LAST_BILL_RECORD_ID, lastBillRecordId);
        contentValues.put(ENTRY_DATE, entryDate);

        db.insert(TABLE_USER_BILL, null, contentValues);
        db.close();
        return true;
    }


    public JSONArray Customer_BillDetails(String cId) throws JSONException {

//        String selectQuery = "SELECT * From "+TABLE_USER_BILL+" WHERE "+CUSTOMER_ID+" = "+cId+" ORDER BY "+USER_BILL_ID+" DESC";
        String selectQuery = "SELECT max(b." + USER_BILL_ID + ") ,b.* FROM " + TABLE_USER_BILL + " b WHERE b." + CUSTOMER_ID + "= '" + cId + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while (res.isAfterLast() == false) {
            JSONObject details = new JSONObject();
            details.put(USER_BILL_ID, res.getString(res.getColumnIndex(USER_BILL_ID)));
            details.put(CUSTOMER_ID, res.getString(res.getColumnIndex(CUSTOMER_ID)));
            details.put(CUSTOMER_NAME, res.getString(res.getColumnIndex(CUSTOMER_NAME)));
            details.put(FROM_DATE, res.getString(res.getColumnIndex(FROM_DATE)));
            details.put(TO_DATE, res.getString(res.getColumnIndex(TO_DATE)));
            details.put(BILL_AMOUNT, res.getString(res.getColumnIndex(BILL_AMOUNT)));
            details.put(PAID_AMOUNT, res.getString(res.getColumnIndex(PAID_AMOUNT)));
            details.put(BALANCE_AMOUNT, res.getString(res.getColumnIndex(BALANCE_AMOUNT)));
            details.put(ENTRY_DATE, res.getString(res.getColumnIndex(ENTRY_DATE)));
            details.put(LAST_BILL_RECORD_ID, res.getString(res.getColumnIndex(LAST_BILL_RECORD_ID)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

    public JSONArray Customer_BillDetails_FromDate(String cId, String toDate, String lastBillRecordId, String currentDate) throws JSONException {
        String selectQuery = "";
        if (toDate.equals("")) {
            selectQuery = "Select b." + DAY + ",b." + MONTH + ",b." + YEAR + ", b." + BILL_ID + ",b." + CUSTOMER_ID + ",b." + PRODUCT_NAME + ",b." + PRODUCT_PRICE + ",b." + PRODUCT_QTY + ",b." + TOTAL_PRICE + ",b." + ENTRY_DATE + ",b." + BILL_DATE + ",c." + CUSTOMER_NAME + " FROM " + TABLE_BILL + " b INNER JOIN " + TABLE_CUSTOMER + " c ON c." + CUSTOMER_ID + " = b." + CUSTOMER_ID + " WHERE  b." + CUSTOMER_ID + " = '" + cId + "' Order BY b." + DAY + ",b." + BILL_ID + "";
        } else {
            selectQuery = "Select b." + DAY + ",b." + MONTH + ",b." + YEAR + ", b." + BILL_ID + ",b." + CUSTOMER_ID + ",b." + PRODUCT_NAME + ",b." + PRODUCT_PRICE + ",b." + PRODUCT_QTY + ",b." + TOTAL_PRICE + ",b." + ENTRY_DATE + ",b." + BILL_DATE + ",c." + CUSTOMER_NAME + " FROM " + TABLE_BILL + " b INNER JOIN " + TABLE_CUSTOMER + " c ON c." + CUSTOMER_ID + " = b." + CUSTOMER_ID + " WHERE  b." + CUSTOMER_ID + " = '" + cId + "' AND b." + BILL_ID + " > '" + lastBillRecordId + "' AND " + BILL_DATE + " BETWEEN '" + toDate + "' AND '" + currentDate + "'  Order BY b." + DAY + ",b." + BILL_ID + "";
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while (res.isAfterLast() == false) {
            JSONObject details = new JSONObject();
            details.put(BILL_ID, res.getString(res.getColumnIndex(BILL_ID)));
            details.put(CUSTOMER_ID, res.getString(res.getColumnIndex(CUSTOMER_ID)));
            details.put(CUSTOMER_NAME, res.getString(res.getColumnIndex(CUSTOMER_NAME)));
            details.put(ENTRY_DATE, res.getString(res.getColumnIndex(ENTRY_DATE)));
            details.put(BILL_DATE, res.getString(res.getColumnIndex(BILL_DATE)));
            details.put(PRODUCT_NAME, res.getString(res.getColumnIndex(PRODUCT_NAME)));
            details.put(PRODUCT_PRICE, res.getString(res.getColumnIndex(PRODUCT_PRICE)));
            details.put(PRODUCT_QTY, res.getString(res.getColumnIndex(PRODUCT_QTY)));
            details.put(TOTAL_PRICE, res.getString(res.getColumnIndex(TOTAL_PRICE)));
            details.put(DAY, res.getString(res.getColumnIndex(DAY)));
            details.put(MONTH, res.getString(res.getColumnIndex(MONTH)));
            details.put(YEAR, res.getString(res.getColumnIndex(YEAR)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }


    //for truncare
    public boolean Bill_Clear() {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL(CREATE_TABLE_USER_BILL);
//        db.execSQL("Drop table "+ TABLE_USER_BILL);
        db.execSQL("DELETE FROM " + TABLE_USER_BILL);
        return true;

    }

    public JSONArray Bill_History(String customerId) throws JSONException {

        String selectQuery = "SELECT * FROM " + TABLE_USER_BILL + "  WHERE " + CUSTOMER_ID + "= '" + customerId + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while (res.isAfterLast() == false) {
            JSONObject details = new JSONObject();
            details.put(USER_BILL_ID, res.getString(res.getColumnIndex(USER_BILL_ID)));
            details.put(CUSTOMER_ID, res.getString(res.getColumnIndex(CUSTOMER_ID)));
            details.put(CUSTOMER_NAME, res.getString(res.getColumnIndex(CUSTOMER_NAME)));
            details.put(FROM_DATE, res.getString(res.getColumnIndex(FROM_DATE)));
            details.put(TO_DATE, res.getString(res.getColumnIndex(TO_DATE)));
            details.put(BILL_AMOUNT, res.getString(res.getColumnIndex(BILL_AMOUNT)));
            details.put(PAID_AMOUNT, res.getString(res.getColumnIndex(PAID_AMOUNT)));
            details.put(BALANCE_AMOUNT, res.getString(res.getColumnIndex(BALANCE_AMOUNT)));
            details.put(ENTRY_DATE, res.getString(res.getColumnIndex(ENTRY_DATE)));
            details.put(LAST_BILL_RECORD_ID, res.getString(res.getColumnIndex(LAST_BILL_RECORD_ID)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

    public JSONArray All_Income_List()  throws JSONException {
        
        String selectQuery = "SELECT sum(b."+TOTAL_PRICE+") as totalAmount,b.* FROM "+TABLE_BILL+" b Group By b."+MONTH+",b."+YEAR+"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while (res.isAfterLast() == false) {
            JSONObject details = new JSONObject();

            details.put("totalAmount", res.getString(res.getColumnIndex("totalAmount")));
            details.put(BILL_DATE, res.getString(res.getColumnIndex(BILL_DATE)));
            details.put(DAY, res.getString(res.getColumnIndex(DAY)));
            details.put(MONTH, res.getString(res.getColumnIndex(MONTH)));
            details.put(YEAR, res.getString(res.getColumnIndex(YEAR)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

    public JSONArray Income_Details(String month, String year)  throws JSONException {

        String selectQuery = "SELECT * FROM "+TABLE_BILL+"  WHERE  "+MONTH+" = '"+month+"' AND  "+YEAR+" = '"+year+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while (res.isAfterLast() == false) {
            JSONObject details = new JSONObject();

            details.put(BILL_ID, res.getString(res.getColumnIndex(BILL_ID)));
            details.put(ENTRY_DATE, res.getString(res.getColumnIndex(ENTRY_DATE)));
            details.put(BILL_DATE, res.getString(res.getColumnIndex(BILL_DATE)));
            details.put(PRODUCT_NAME, res.getString(res.getColumnIndex(PRODUCT_NAME)));
            details.put(PRODUCT_PRICE, res.getString(res.getColumnIndex(PRODUCT_PRICE)));
            details.put(PRODUCT_QTY, res.getString(res.getColumnIndex(PRODUCT_QTY)));
            details.put(TOTAL_PRICE, res.getString(res.getColumnIndex(TOTAL_PRICE)));
            details.put(DAY, res.getString(res.getColumnIndex(DAY)));
            details.put(MONTH, res.getString(res.getColumnIndex(MONTH)));
            details.put(YEAR, res.getString(res.getColumnIndex(YEAR)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }
}
