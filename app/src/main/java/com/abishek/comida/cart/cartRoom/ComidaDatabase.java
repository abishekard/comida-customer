package com.abishek.comida.cart.cartRoom;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.abishek.comida.R;
import com.abishek.comida.home.product.FoodModel;



@Database(entities = {FoodModel.class}, version = 1, exportSchema = false)
public abstract class ComidaDatabase extends RoomDatabase {


    private static ComidaDatabase INSTANCE;

   public static ComidaDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ComidaDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    ComidaDatabase.class,
                                    "ComidaDatabase.db").build();
                }
            }
        }
        return INSTANCE;
    }



    public abstract CartDaoAccess getDaoAccess();
}
