package com.tencent.wink.storage.winkdb.safe;

import android.content.Context;
import android.text.Editable;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * SupportSQLiteOpenHelper.Factory实现类, 它支持SQLCipher
 * <p>
 * Created by walkerzpli on 2022/1/26.
 */
public class WinkDBHelperFactory implements SupportSQLiteOpenHelper.Factory {
    
    public static final String POST_KEY_SQL_MIGRATE = "PRAGMA cipher_migrate;";
    public static final String POST_KEY_SQL_V3 = "PRAGMA cipher_compatibility = 3;";

    final private byte[] passphrase;
    final private Options options;

    /**
     * 根据用户输入口令创建SafeHelperFactory
     * <p>
     *
     * @param editor 用户提供口令
     * @return a SafeHelperFactory
     */
    public static WinkDBHelperFactory fromUser(Editable editor) {
        return fromUser(editor, (String) null);
    }

    /**
     * 根据用户输入口令创建SafeHelperFactory，并且执行postKeySql
     * <p>
     *
     * @param editor     用户提供口令
     * @param postKeySql 创建成功后执行的sql，可选
     * @return a SafeHelperFactory
     */
    public static WinkDBHelperFactory fromUser(Editable editor, String postKeySql) {
        return fromUser(editor, Options.builder().setPostKeySql(postKeySql).build());
    }

    /**
     * 根据用户输入editor创建SafeHelperFactory
     * <p>
     *
     * @param editor  用户提供的口令
     * @param options Options
     * @return SafeHelperFactory
     */
    public static WinkDBHelperFactory fromUser(Editable editor, Options options) {
        char[] passphrase = new char[editor.length()];
        WinkDBHelperFactory result;

        editor.getChars(0, editor.length(), passphrase, 0);

        try {
            result = new WinkDBHelperFactory(passphrase, options);
        } finally {
            editor.clear();
        }

        return (result);
    }

    /**
     * 口令重置
     *
     * @param db         SupportSQLiteDatabase
     * @param passphrase 新口令
     */
    public static void reKey(SupportSQLiteDatabase db, char[] passphrase) {
        if (db instanceof WinkDBDatabase) {
            ((WinkDBDatabase) db).reKey(passphrase);
        } else {
            throw new IllegalArgumentException("Database is not from CWAC-SafeRoom");
        }
    }

    /**
     * 口令重置
     *
     * @param db     SupportSQLiteDatabase
     * @param editor 新口令编辑器
     */
    public static void reKey(SupportSQLiteDatabase db, Editable editor) {
        if (db instanceof WinkDBDatabase) {
            ((WinkDBDatabase) db).reKey(editor);
        } else {
            throw new IllegalArgumentException("Database is not from CWAC-SafeRoom");
        }
    }

    public WinkDBHelperFactory(char[] passphrase) {
        this(passphrase, (String) null);
    }

    public WinkDBHelperFactory(char[] passphrase, String postKeySql) {
        this(SQLiteDatabase.getBytes(passphrase), postKeySql);

        if (options.clearPassphrase) {
            clearPassphrase(passphrase);
        }
    }

    public WinkDBHelperFactory(char[] passphrase, Options options) {
        this(SQLiteDatabase.getBytes(passphrase), options);

        if (options.clearPassphrase) {
            clearPassphrase(passphrase);
        }
    }

    public WinkDBHelperFactory(byte[] passphrase) {
        this(passphrase, new Options.Builder().build());
    }

    public WinkDBHelperFactory(byte[] passphrase, String postKeySql) {
        this(passphrase, new Options.Builder().setPostKeySql(postKeySql).build());
    }

    public WinkDBHelperFactory(byte[] passphrase, Options options) {
        this.passphrase = passphrase;
        this.options = options;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SupportSQLiteOpenHelper create(SupportSQLiteOpenHelper.Configuration configuration) {
        return (create(configuration.context, configuration.name,
                configuration.callback));
    }

    public SupportSQLiteOpenHelper create(Context context, String name, SupportSQLiteOpenHelper.Callback callback) {
        return (new WinkOpenHelper(context, name, callback, passphrase, options));
    }

    private void clearPassphrase(char[] passphrase) {
        for (int i = 0; i < passphrase.length; i++) {
            passphrase[i] = (byte) 0;
        }
    }

    public static class Options {
        /**
         * 加密数据库前执行的SQL
         */
        public final String preKeySql;

        /**
         * 加密数据库后执行的SQL
         */
        public final String postKeySql;

        public final boolean clearPassphrase;

        private Options(String preKeySql, String postKeySql, boolean clearPassphrase) {
            this.preKeySql = preKeySql;
            this.postKeySql = postKeySql;
            this.clearPassphrase = clearPassphrase;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String preKeySql;
            private String postKeySql;
            private boolean clearPassphrase = true;

            private Builder() {
            }

            public Builder setPreKeySql(String preKeySql) {
                this.preKeySql = preKeySql;

                return this;
            }

            public Builder setPostKeySql(String postKeySql) {
                this.postKeySql = postKeySql;

                return this;
            }

            public Builder setClearPassphrase(boolean value) {
                this.clearPassphrase = value;

                return this;
            }

            public Options build() {
                return new Options(preKeySql, postKeySql, clearPassphrase);
            }
        }
    }
}
