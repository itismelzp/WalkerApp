package com.walker.storage.winkdb.safe;

import androidx.sqlite.db.SupportSQLiteStatement;

import net.sqlcipher.database.SQLiteStatement;

/**
 * SupportSQLiteStatement实现类
 * <p>
 * Created by walkerzpli on 2022/1/26.
 */
class WinkDBStatement extends WinkDBProgram implements SupportSQLiteStatement {
    private final SQLiteStatement safeStatement;

    WinkDBStatement(SQLiteStatement safeStatement) {
        super(safeStatement);
        this.safeStatement = safeStatement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        safeStatement.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int executeUpdateDelete() {
        return safeStatement.executeUpdateDelete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long executeInsert() {
        return safeStatement.executeInsert();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long simpleQueryForLong() {
        return safeStatement.simpleQueryForLong();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String simpleQueryForString() {
        return safeStatement.simpleQueryForString();
    }
}
