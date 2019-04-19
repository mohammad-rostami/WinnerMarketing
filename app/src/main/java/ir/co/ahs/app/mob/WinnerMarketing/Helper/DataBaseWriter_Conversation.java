package ir.co.ahs.app.mob.WinnerMarketing.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseWriter_Conversation extends SQLiteOpenHelper {

    // The Android's default system path of your application database.
    private static String DB_PATH = "";
    public static String TABLE;
    private static String DB_NAME;
    private static String[] FIELD_NAMES;
    private static String[] FIELD_TYPES;

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor Takes and keeps a reference of the passed context in order to
     * access to the application assets and resources.
     *
     * @param context
     */
    public DataBaseWriter_Conversation(Context context, String dataBaseName, String tableName, String[] fieldNames, String[] fieldTypes) {

        super(context, dataBaseName, null, 1);
        DB_NAME = dataBaseName;
        TABLE = tableName;
        FIELD_NAMES = fieldNames;
        FIELD_TYPES = fieldTypes;

        this.myContext = context;

        System.err.println(context.getApplicationInfo().dataDir);

        DB_PATH = context.getApplicationInfo().dataDir + File.separator
                + "databases" + File.separator;

        System.out.println(DB_PATH);
    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            // do nothing - database already exist
            // Toast.makeText(myContext, "already exist",
            // Toast.LENGTH_LONG).show();
        } else {

            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                // throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
            // Toast.makeText(myContext, "already exist",
            // Toast.LENGTH_LONG).show();
        } catch (SQLiteException e) {

            // database does't exist yet.
            // Toast.makeText(myContext, "not already exist",
            // Toast.LENGTH_LONG).show();
        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the Input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        // Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table " + TABLE + " (UserID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FIELD_NAMES[0] + " " + FIELD_TYPES[0] + ", "
                + FIELD_NAMES[1] + " " + FIELD_TYPES[1] + ", "
                + FIELD_NAMES[2] + " " + FIELD_TYPES[2] + ", "
                + FIELD_NAMES[3] + " " + FIELD_TYPES[3] + ", "
                + FIELD_NAMES[4] + " " + FIELD_TYPES[4] + ", "
                + FIELD_NAMES[5] + " " + FIELD_TYPES[5] + ", "
                + FIELD_NAMES[6] + " " + FIELD_TYPES[6] + ", "
                + FIELD_NAMES[7] + " " + FIELD_TYPES[7] + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void sampleMethod(String[] arguments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_NAMES[0], arguments[0]);
        values.put(FIELD_NAMES[1], arguments[1]);
        values.put(FIELD_NAMES[2], Integer.parseInt(arguments[2]));
        values.put(FIELD_NAMES[3], arguments[3]);
        values.put(FIELD_NAMES[4], arguments[4]);
        values.put(FIELD_NAMES[5], arguments[5]);
        values.put(FIELD_NAMES[6], arguments[6]);
        values.put(FIELD_NAMES[7], arguments[7]);

        db.insert(TABLE, null, values);
        db.close();
    }

}