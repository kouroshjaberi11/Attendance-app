package com.example.attendanceapp

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DatabaseHelper {
    private val reference: DatabaseReference
    private var user: UserHelper? = null

    /*
    adds a new user that has registered to the database
    */
    fun newUser(uid: String, username: String, admin: Boolean) {
        user = UserHelper(username, "NONE", admin)
        reference.child(uid).setValue(user)
    }

    /*
    set location of of user username to location
    */
    fun setLocation(username: String, location: String): Boolean {
        reference.child(username).child("location").setValue(location)
        return true
    }

    init {
        reference = FirebaseDatabase.getInstance().getReference("users")
    }
}

    /*
    returns a string array of the username and location of all users in the database

    public String[] getUserAndLocs() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) as numrec from " + USER_TABLE_NAME, null);
        cursor.moveToFirst();

        int numRecords = cursor.getInt(cursor.getColumnIndex("numrec"));

        cursor.close();
        String[] strArr = new String[numRecords];
        String tempName;
        String tempLoc;

        Cursor cursor2 = db.rawQuery("select " + USERNAME + ", " + LOCATION + " FROM " + USER_TABLE_NAME, null);

        if (cursor2.moveToFirst()) {
            for (int i=0; i<numRecords; i++) {
                tempName = cursor2.getString(cursor2.getColumnIndex(USERNAME));
                tempLoc = cursor2.getString(cursor2.getColumnIndex(LOCATION));

                strArr[i] = tempName + " - " + tempLoc;
                cursor2.moveToNext();
            }
        }
        cursor2.close();

        return strArr;
    }


    / *private static final String SQL_CREATE_USER_ENTRIES = "CREATE TABLE "+ USER_TABLE_NAME +" (" +
                                                    USERNAME + " TEXT PRIMARY KEY, " +
                                                    PASSWORD + " TEXT, " +
                                                    LOCATION + " TEXT, " +
                                                    ADMIN + " INTEGER)";
    private static final String SQL_DELETE_USER_ENTRIES = "DROP TABLE IF EXISTS " + USER_TABLE_NAME;




    public DatabaseHelper(Context context) {
        super(context, USER_TABLE_NAME, null, 1);
    }

    / *
    adds a new user that has registered to the database
     *
    public boolean newUser(String username, String password, boolean admin) {
        SQLiteDatabase db = this.getWritableDatabase();
        Integer isAdmin=0;
        if (admin) isAdmin=1;

        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME, username);
        contentValues.put(PASSWORD, password);
        contentValues.put(LOCATION, "NONE");
        contentValues.put(ADMIN, isAdmin);
        long result = db.insert(USER_TABLE_NAME, null, contentValues);
        if (result==-1) return false;
        return true;
    }

    public boolean deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(USER_TABLE_NAME, USERNAME + " = ?", new String[] {username});
        if (result==-1) return false;
        return true;
    }

    / *
    if username exists in userDetails.db, then return true, else return false.

    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + USER_TABLE_NAME + " where username = ?", new String[] {username});
        if (cursor.getCount()>0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    / *
    returns true if username and password match a record in db, else returns false
     *
    public boolean checkusernamepassword(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + USER_TABLE_NAME +
                " where username = ? and password = ?", new String[] {username, password});
        if (cursor.getCount()>0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }


    set location of of user username to location
    public boolean setLocation(String username, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(LOCATION, location);
        long result = db.update(USER_TABLE_NAME, contentValues, "username = ?", new String[] {username});

        if (result==-1) return false;
        return true;
    }


    returns location of user username. Returns null if user does not exist or something goes wrong

    public String getLocation(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select " + LOCATION + " FROM " + USER_TABLE_NAME +
                " WHERE " + USERNAME + "=?", new String[] {username});
        if (cursor.getCount()>0 && cursor.moveToFirst()) {
            String location = cursor.getString(cursor.getColumnIndex(LOCATION));
            cursor.close();
            return location;
        }
        cursor.close();
        return null;
    }


    return status of user. 1 for admin, 0 for not admin, and -1 for error

    public int getAdmin(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        int admin = 0;
        Cursor cursor = db.rawQuery("select " + ADMIN + " FROM " + USER_TABLE_NAME +
                " WHERE " + USERNAME + "=?", new String[] {username});

        if (cursor.moveToFirst()) {
            admin = cursor.getInt(cursor.getColumnIndex(ADMIN));
            cursor.close();
            if (admin==1) return 1;
            return 0;
        }
        cursor.close();
        return -1;
    }

    public String[] getUserAndLocs() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) as numrec from " + USER_TABLE_NAME, null);
        cursor.moveToFirst();

        int numRecords = cursor.getInt(cursor.getColumnIndex("numrec"));

        cursor.close();
        String[] strArr = new String[numRecords];
        String tempName;
        String tempLoc;

        Cursor cursor2 = db.rawQuery("select " + USERNAME + ", " + LOCATION + " FROM " + USER_TABLE_NAME, null);

        if (cursor2.moveToFirst()) {
            for (int i=0; i<numRecords; i++) {
                tempName = cursor2.getString(cursor2.getColumnIndex(USERNAME));
                tempLoc = cursor2.getString(cursor2.getColumnIndex(LOCATION));

                strArr[i] = tempName + " - " + tempLoc;
                cursor2.moveToNext();
            }
        }
        cursor2.close();

        return strArr;
    }
    */

