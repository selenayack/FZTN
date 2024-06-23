package com.app.fztn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public abstract class DbAdapter extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fztn";
    private static final int DATABASE_VERSION =707;

    private final Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DbAdapter(Context ctx) {

        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
        dbHelper = new DatabaseHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }
    public static final String TABLE_ONERILEN_VIDEOLAR = "OnerilenVideolar";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_VIDEO_URL = "videoUrl";







    public long insertRecommendedVideo(String userId,String agrıBolgesi, String recommendedVideoUrl, String textBilgi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("video_url", recommendedVideoUrl);
        values.put("text_bilgi", textBilgi);
        values.put("agri_bolgesi",agrıBolgesi);
        long result = db.insert("ONERILEN_VIDEO", null, values);
        db.close();
        return result;
    }

    public List<VideoItem> getRecommendedVideos(String userId) {
        List<VideoItem> videoItems = new ArrayList<>();
        Cursor cursor = db.query("ONERILEN_VIDEO", null, "user_id=?", new String[]{userId}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String textBilgi = cursor.getString(cursor.getColumnIndex("text_bilgi"));
                    @SuppressLint("Range") String videoUrl = cursor.getString(cursor.getColumnIndex("video_url"));
                    @SuppressLint("Range") String agriBolgesi = cursor.getString(cursor.getColumnIndex("agri_bolgesi"));
                    videoItems.add(new VideoItem(textBilgi, videoUrl, agriBolgesi));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return videoItems;
    }




    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            try {

                db.execSQL("CREATE TABLE IF NOT EXISTS USER(" +
                        "user_id TEXT  ," +
                        "user_dogum_tarih DATE,"+
                        "user_cinsiyet INT," +
                        "user_boy INT," +
                        "user_kilo INT," +
                        "user_yas INT," +
                        "user_olcu  VARCHAR," +
                        "user_email TEXT UNIQUE);");

                db.execSQL("CREATE TABLE IF NOT EXISTS RANDEVU(" +
                        "user_id TEXT  ," +
                        "randevu_tarih TEXT," +
                        "randevu_saat TEXT);");

                db.execSQL("CREATE TABLE IF NOT EXISTS AGRILAR(" +
                        "user_id TEXT  ," +
                        "agri_bolgesi TEXT," +
                        "agri_derece TEXT," +
                        "agri_sekli TEXT," +
                        "agri_suresi TEXT);");
                db.execSQL("CREATE TABLE IF NOT EXISTS ONERILEN_VIDEO(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "user_id TEXT," +
                        "text_bilgi TEXT," +
                        "agri_bolgesi TEXT," +
                        "video_url TEXT);");



            } catch (SQLException e) {
                e.printStackTrace();

            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS USER");
            db.execSQL("DROP TABLE IF EXISTS RANDEVU");
            db.execSQL("DROP TABLE IF EXISTS AGRILAR");
            db.execSQL("DROP TABLE IF EXISTS  ONERILEN_VIDEO");

            onCreate(db);
        }
    }

    @SuppressLint("Range")
    public String getRecommendedVideoUrl(String userId) {
        String url = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT video_url FROM ONERILEN_VIDEO WHERE user_id = ?", new String[]{userId});
        if (cursor != null && cursor.moveToFirst()) {
            url = cursor.getString(cursor.getColumnIndex("video_url"));
            cursor.close();
        }
        return url;
    }
    @SuppressLint("Range")
    public String getRecommendedTextBilgi(String userId) {
        String textBilgi = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT text_bilgi FROM ONERILEN_VIDEO WHERE user_id = ?", new String[]{userId});
        if (cursor != null && cursor.moveToFirst()) {
            textBilgi= cursor.getString(cursor.getColumnIndex("text_bilgi"));
            cursor.close();
        }
        return textBilgi;
    }


    public DbAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public long insertRandevu(String userId, String randevuTarih, String randevuSaat) {
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("randevu_tarih", randevuTarih);
        values.put("randevu_saat", randevuSaat);

        SQLiteDatabase db = null;
        long result = -1;

        try {
            db = this.getWritableDatabase();
            result = db.insert("RANDEVU", null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return result;
    }

    public boolean isHourOccupied(String selectedDate, String selectedTime) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Seçilen tarih ve saatte başka bir randevu var mı kontrol et
            String query = "SELECT * FROM RANDEVU WHERE randevu_tarih = ? AND randevu_saat = ?";
            cursor = db.rawQuery(query, new String[]{selectedDate, selectedTime});
            return cursor.getCount() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    public boolean isSameDateTimeOccupied(String selectedDate, String selectedTime) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM RANDEVU WHERE randevu_tarih = ? AND randevu_saat = ?";
            cursor = db.rawQuery(query, new String[]{selectedDate, selectedTime});

            // Eğer cursor'ın count'u sıfırdan büyükse, bu tarih ve saatte bir randevu var demektir
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    public boolean isDateTimeOccupied(String selectedDate, String selectedTime) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM RANDEVU WHERE randevu_tarih = ? AND randevu_saat = ?";
            cursor = db.rawQuery(query, new String[]{selectedDate, selectedTime});
            return cursor.getCount() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }



    public boolean userExists(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT 1 FROM USER WHERE user_id = ?";
            cursor = db.rawQuery(query, new String[]{userId});
            return cursor != null && cursor.moveToFirst();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    //close database
    /*public void close() {
        dbHelper.close();
    }*/

    public String quoteSmart(String value) {
        boolean isNumeric = false;
        try {
            double myDouble = Double.parseDouble(value);
            isNumeric = true;
        } catch (NumberFormatException nfe) {
            System.out.println("parse edilemedi" + nfe);
        }

        if (isNumeric = false) {

            if (value != null && value.length() > 0) {
                value = value.replace("\\", "\\\\");
                value = value.replace("'", "\\'");
                value = value.replace("\0", "\\0");
                value = value.replace("\n", "\\n");
                value = value.replace("\r", "\\r");
                value = value.replace("\"", "\\\"");
                value = value.replace("\\xla", "\\z");


            }

        }
        value = "'" + value + "'";
        return value;


    }

    public double quoteSmart(double value) {
        return value;
    }

    public int quoteSmart(int value) {
        return value;
    }

    public void addUserEmail(String email, String userID) {
        ContentValues values = new ContentValues();
        values.put("user_email", email);
        values.put("user_id", userID);

        try {
            db.insert("USER", null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addUserRandevu( String randevuTarih) {
        ContentValues values = new ContentValues();
        values.put("user_randevu_tarih", randevuTarih);


        try {
            db.insert("USER", null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void insert(String table, String fields, String values) {


        db.execSQL("INSERT INTO " + table + "(" + fields + ") VALUES (" + values + ")");
        //db.close();

    }

    public Cursor select(String table, String[] columns, String whereColumn, String
            whereValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = whereColumn + " = ?";
        String[] selectionArgs = {whereValue};
        Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null, null);
        return cursor;
    }


    public int count(String table) {
        try {
            Cursor mcount = db.rawQuery("SELECT COUNT(*) FROM " + table, null);
            mcount.moveToFirst();
            int count = mcount.getInt(0);
            mcount.close();
            return count;
        } catch (SQLException e) {
            return -1;
        }
    }


    public Cursor selectPrimaryKey(String table, String primaryKey, long sutunId, String[]
            fields) throws SQLException {

        // Cursor cursor = db.query(table, fields, primaryKey + "=", new String[]{String.valueOf(sutunId)}, null, null, null);
        Cursor cursor = db.query(table, fields, primaryKey + "=" + sutunId, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public boolean update(String table, String primaryKey, long sutunId, String
            fields, String value) throws SQLException {

        value = value.substring(1, value.length() - 1);

        ContentValues args = new ContentValues();
        args.put(fields, value);
        return db.update(table, args, primaryKey + "=" + sutunId, null) > 0;


    }

    public boolean update(String table, String primaryKey, long sutunId, String fields,
                          double value) throws SQLException {


        ContentValues args = new ContentValues();
        args.put(fields, value);
        return db.update(table, args, primaryKey + "=" + sutunId, null) > 0;


    }

    public boolean updateUser(String table, ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();

            // Eğer kullanıcı zaten varsa, kullanıcı bilgilerini güncelle
            if (userExists(values.getAsString("user_id"))) {
                ContentValues updatedValues = new ContentValues();



                if (values.containsKey("user_dogum_tarih")) {
                    String user_dogum_tarih = values.getAsString("user_dogum_tarih");
                    if (values.get("user_dogum_tarih") != null) {
                        updatedValues.put("user_dogum_tarih", values.getAsString("user_dogum_tarih"));
                    }


                    if (values.containsKey("user_cinsiyet")) {
                        String user_cinsiyet = values.getAsString("user_cinsiyet");
                        if (user_cinsiyet != null) {
                            updatedValues.put("user_cinsiyet", user_cinsiyet);
                        }
                    }

                    if (values.containsKey("user_boy")) {
                        Integer user_boy = values.getAsInteger("user_boy");
                        if (user_boy != null) {
                            updatedValues.put("user_boy", user_boy);
                        }
                    }

                    if (values.containsKey("user_kilo")) {
                        Integer user_kilo = values.getAsInteger("user_kilo");
                        if (user_kilo != null) {
                            updatedValues.put("user_kilo", user_kilo);
                        }
                    }

                    if (values.containsKey("user_yas")) {
                        Integer user_yas = values.getAsInteger("user_yas");
                        if (user_yas != null) {
                            updatedValues.put("user_yas", user_yas);
                        }
                    }

                    if (values.containsKey("user_olcu")) {
                        String user_olcu = values.getAsString("user_olcu");
                        if (user_olcu != null) {
                            updatedValues.put("user_olcu", user_olcu);
                        }
                    }

                    if (values.containsKey("user_email")) {
                        String user_email = values.getAsString("user_email");
                        if (user_email != null) {
                            updatedValues.put("user_email", user_email);
                        }
                    }
                }


                int updatedRows = db.update(table, updatedValues, whereClause, whereArgs);
                return updatedRows > 0;
            } else {
                // Eğer kullanıcı yoksa, yeni bir kullanıcı ekleyin
                ContentValues userValues = new ContentValues();
                userValues.put("user_id", values.getAsString("user_id"));
                userValues.put("user_dogum_tarih", values.getAsString("user_dogum_tarih"));
                userValues.put("user_cinsiyet", values.getAsString("user_cinsiyet"));
                userValues.put("user_boy", values.getAsInteger("user_boy"));
                userValues.put("user_kilo", values.getAsInteger("user_kilo"));
                userValues.put("user_yas", values.getAsInteger("user_yas"));
                userValues.put("user_olcu", values.getAsString("user_olcu"));
                userValues.put("user_email", values.getAsString("user_email"));

                long newRowId = db.insert(table, null, userValues);
                return newRowId != -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }


    public boolean update(String table, String primaryKey, long sutunId, String fields,
                          int value) throws SQLException {


        ContentValues args = new ContentValues();
        args.put(fields, value);
        return db.update(table, args, primaryKey + "=" + sutunId, null) > 0;


    }

    // DbAdapter sınıfına eklenen metot
    public long insertAgrilar(String userId, String agriBolgesi, String agriDerece, String agriSekli, String agriSuresi, String suresi) {
        ContentValues values = new ContentValues();
        values.put("user_id", userId); // Kullanıcı ID'sini ekleyin
        values.put("agri_bolgesi", agriBolgesi);
        values.put("agri_derece", agriDerece);
        values.put("agri_sekli", agriSekli);
        values.put("agri_suresi", agriSuresi);

        SQLiteDatabase db = null;
        long result = -1;

        try {
            db = this.getWritableDatabase();
            result = db.insert("AGRILAR", null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return result;
    }



}
