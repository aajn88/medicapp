package com.crossover.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.crossover.common.model.common.Event;
import com.crossover.common.model.common.Invitation;
import com.crossover.common.model.common.User;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * The DBHelper to manage the SQLite DB in Android
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
@Singleton
public class DatabaseHelper extends SQLiteOpenHelper {

    /** Logs Tag **/
    private static final String TAG_LOG = DatabaseHelper.class.getName();

    /** DB name **/
    private static final String DB_NAME = "medicapp.db";

    /** DB Version **/
    private static final int DB_VERSION = 3;

    /** The connection source **/
    protected AndroidConnectionSource connectionSource = new AndroidConnectionSource(this);

    /**
     * Inits the DB Helper
     *
     * @param context
     *         The application Context
     */
    @Inject
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        setUpDatabase(db);
    }

    /**
     * This method sets up the SQLite DB and all its tables
     *
     * @param db
     *         DB Connection
     */
    private void setUpDatabase(SQLiteDatabase db) {
        setUpDatabase(db, null, null);
    }

    /**
     * This method sets up the DB when a new version update is requested. If any of both the {@code
     * oldVersion} or the {@code newVersion} is null, then the updated process will be skipped, the
     * regular build process will be executed
     *
     * @param db
     *         The DB connection
     * @param oldVersion
     *         DB old version
     * @param newVersion
     *         DB new version
     */
    private void setUpDatabase(SQLiteDatabase db, Integer oldVersion, Integer newVersion) {
        DatabaseConnection con = connectionSource.getSpecialConnection();
        boolean cleanSpecial = false;
        if (con == null) {
            con = new AndroidDatabaseConnection(db, true);

            try {
                connectionSource.saveSpecialConnection(con);
                cleanSpecial = true;
            } catch (SQLException e) {
                throw new IllegalStateException("The special connection could not be stored", e);
            }
        }

        try {
            if (oldVersion == null || newVersion == null) {
                onCreate();
            } else {
                onUpgrade(oldVersion, newVersion);
            }
        } finally {
            if (cleanSpecial) {
                connectionSource.clearSpecialConnection(con);
            }
        }
    }

    /**
     * Creates the DB scheme and tables
     */
    private void onCreate() {
        try {
            Log.i(TAG_LOG, "DB onCreate");

            // List of tables to be created
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Event.class);
            TableUtils.createTable(connectionSource, Invitation.class);

            Log.i(TAG_LOG, "DB successfully created");
        } catch (SQLException e) {
            Log.e(TAG_LOG, "An error has occurred while creating the DB", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        setUpDatabase(db, oldVersion, newVersion);
    }

    /**
     * Updates the DB given its {@code oldVersion} and its new {@code newVersion}
     *
     * @param oldVersion
     *         The old version of the DB
     * @param newVersion
     *         The new version of the DB
     */
    private void onUpgrade(int oldVersion, int newVersion) {
        try {
            Log.i(TAG_LOG, "The DB onUpgrade");

            // Just in this case: Drop all tables and create the DB again
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, Event.class, true);
            TableUtils.dropTable(connectionSource, Invitation.class, true);

            onCreate();
        } catch (SQLException e) {
            Log.e(TAG_LOG, "An error has occurred while updating the DB", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This method obtains a DAO given its Class
     * <p/>
     * Source: https://goo.gl/6LIYy2
     *
     * @param clazz
     *         The DAO class
     * @param <D>
     *         DAO super class
     * @param <T>
     *         Requested DAO class
     *
     * @return The DAO instance
     *
     * @throws SQLException
     */
    public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException {
        // lookup the dao, possibly invoking the cached database config
        Dao<T, ?> dao = DaoManager.lookupDao(connectionSource, clazz);
        if (dao == null) {
            // try to use our new reflection magic
            DatabaseTableConfig<T> tableConfig = DatabaseTableConfigUtil
                    .fromClass(connectionSource, clazz);
            if (tableConfig == null) {
                /**
                 * Note: We have to do this to get to see if they are using the deprecated
                 * annotations like
                 * {@link DatabaseFieldSimple}.
                 */
                dao = (Dao<T, ?>) DaoManager.createDao(connectionSource, clazz);
            } else {
                dao = (Dao<T, ?>) DaoManager.createDao(connectionSource, tableConfig);
            }
        }

        @SuppressWarnings("unchecked")
        D castDao = (D) dao;
        return castDao;
    }
}
