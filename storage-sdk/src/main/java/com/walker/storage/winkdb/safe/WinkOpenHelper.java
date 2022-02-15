package com.walker.storage.winkdb.safe;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import net.sqlcipher.DatabaseErrorHandler;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.Arrays;

/**
 * SupportSQLiteOpenHelper实现类，配合SQLCipher使用
 * <p>
 * Created by walkerzpli on 2022/1/26.
 */
class WinkOpenHelper implements SupportSQLiteOpenHelper {
    private final OpenHelper delegate;
    private final byte[] passphrase;
    private final boolean clearPassphrase;

    WinkOpenHelper(Context context, String name, Callback callback, byte[] passphrase,
                   WinkDBHelperFactory.Options options) {
        SQLiteDatabase.loadLibs(context);
        clearPassphrase = options.clearPassphrase;
        delegate = createDelegate(context, name, callback, options);
        this.passphrase = passphrase;
    }

    private OpenHelper createDelegate(Context context, String name,
                                      final Callback callback, WinkDBHelperFactory.Options options) {
        final WinkDBDatabase[] dbRef = new WinkDBDatabase[1];

        return (new OpenHelper(context, name, dbRef, callback, options));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    synchronized public String getDatabaseName() {
        return delegate.getDatabaseName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    synchronized public void setWriteAheadLoggingEnabled(boolean enabled) {
        delegate.setWriteAheadLoggingEnabled(enabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    synchronized public SupportSQLiteDatabase getWritableDatabase() {
        SupportSQLiteDatabase result;

        try {
            result = delegate.getWritableSupportDatabase(passphrase);
        } catch (SQLiteException e) {
            if (passphrase != null) {
                boolean isCleared = true;

                for (byte b : passphrase) {
                    isCleared = isCleared && (b == (byte) 0);
                }

                if (isCleared) {
                    throw new IllegalStateException("The passphrase appears to be cleared.");
                }
            }

            throw e;
        }

        if (clearPassphrase && passphrase != null) {
            Arrays.fill(passphrase, (byte) 0);
        }

        return (result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SupportSQLiteDatabase getReadableDatabase() {
        return (getWritableDatabase());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    synchronized public void close() {
        delegate.close();
    }

    static class OpenHelper extends SQLiteOpenHelper {
        private final WinkDBDatabase[] dbRef;
        private volatile Callback callback;
        private volatile boolean migrated;

        OpenHelper(Context context, String name, WinkDBDatabase[] dbRef, Callback callback,
                   WinkDBHelperFactory.Options options) {
            super(context, name, null, callback.version, new SQLiteDatabaseHook() {
                @Override
                public void preKey(SQLiteDatabase database) {
                    if (options != null && options.preKeySql != null) {
                        database.rawExecSQL(options.preKeySql);
                    }
                }

                @Override
                public void postKey(SQLiteDatabase database) {
                    if (options != null && options.postKeySql != null) {
                        database.rawExecSQL(options.postKeySql);
                    }
                }
            }, new DatabaseErrorHandler() {
                @Override
                public void onCorruption(SQLiteDatabase dbObj) {
                    WinkDBDatabase db = dbRef[0];

                    if (db != null) {
                        callback.onCorruption(db);
                    }
                }
            });

            this.dbRef = dbRef;
            this.callback = callback;
        }

        synchronized SupportSQLiteDatabase getWritableSupportDatabase(byte[] passphrase) {
            migrated = false;

            SQLiteDatabase db = super.getWritableDatabase(passphrase);

            if (migrated) {
                close();
                return getWritableSupportDatabase(passphrase);
            }

            return getWrappedDb(db);
        }

        synchronized WinkDBDatabase getWrappedDb(SQLiteDatabase db) {
            WinkDBDatabase wrappedDb = dbRef[0];

            if (wrappedDb == null) {
                wrappedDb = new WinkDBDatabase(db);
                dbRef[0] = wrappedDb;
            }

            return (dbRef[0]);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            callback.onCreate(getWrappedDb(sqLiteDatabase));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            migrated = true;
            callback.onUpgrade(getWrappedDb(sqLiteDatabase), oldVersion, newVersion);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onConfigure(SQLiteDatabase db) {
            callback.onConfigure(getWrappedDb(db));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            migrated = true;
            callback.onDowngrade(getWrappedDb(db), oldVersion, newVersion);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onOpen(SQLiteDatabase db) {
            if (!migrated) {
                callback.onOpen(getWrappedDb(db));
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public synchronized void close() {
            super.close();
            dbRef[0] = null;
        }
    }
}
