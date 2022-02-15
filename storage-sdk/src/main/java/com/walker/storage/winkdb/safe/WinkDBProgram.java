package com.walker.storage.winkdb.safe;

import androidx.sqlite.db.SupportSQLiteProgram;

import net.sqlcipher.database.SQLiteProgram;

/**
 * 包含SQLCipher的SupportSQLiteProgram
 * <p>
 * Created by walkerzpli on 2022/1/26.
 */
class WinkDBProgram implements SupportSQLiteProgram {
    private final SQLiteProgram delegate;

    WinkDBProgram(SQLiteProgram delegate) {
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindNull(int index) {
        delegate.bindNull(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindLong(int index, long value) {
        delegate.bindLong(index, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindDouble(int index, double value) {
        delegate.bindDouble(index, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindString(int index, String value) {
        delegate.bindString(index, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindBlob(int index, byte[] value) {
        delegate.bindBlob(index, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearBindings() {
        delegate.clearBindings();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        delegate.close();
    }
}
