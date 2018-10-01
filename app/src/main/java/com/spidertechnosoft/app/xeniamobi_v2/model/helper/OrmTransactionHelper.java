
package com.orm;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.orm.SugarContext;
import com.orm.SugarTransactionHelper;

/**
 * Created by fayiz-ci on 12/12/16.
 */
public class OrmTransactionHelper {

    public static boolean doInTransaction(Callback callback) {
        SQLiteDatabase database = SugarContext.getSugarContext().getSugarDb().getDB();
        database.beginTransaction();
        boolean result = false;
        try {
            Log.d(SugarTransactionHelper.class.getSimpleName(),
                    "Callback executing within transaction");
            callback.manipulateInTransaction();
            database.setTransactionSuccessful();
            Log.d(SugarTransactionHelper.class.getSimpleName(),
                    "Callback successfully executed within transaction");
            result = true;
        } catch (Throwable e) {
            Log.d(SugarTransactionHelper.class.getSimpleName(),
                    "Could execute callback within transaction", e);
            result = false;
        } finally {
            database.endTransaction();
        }
        return result;
    }

    public static interface Callback {
        void manipulateInTransaction() throws Exception;
    }
}
