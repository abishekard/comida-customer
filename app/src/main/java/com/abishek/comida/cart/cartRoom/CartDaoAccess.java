package com.abishek.comida.cart.cartRoom;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.abishek.comida.home.product.FoodModel;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CartDaoAccess {

    @Insert
    Long insertTask(FoodModel food);

/*

    @Query("SELECT * FROM FoodModel ORDER BY created_at desc")
    LiveData<ArrayList<FoodModel>> fetchAllTasks();
*/


    @Query("SELECT * FROM FoodModel WHERE id =:taskId")
    LiveData<FoodModel> getTask(int taskId);


    @Update
    void updateTask(FoodModel food);


    @Delete
    void deleteTask(FoodModel food);



    @Query("SELECT * FROM FoodModel")
    List<FoodModel> getFoodList();



    @Query("UPDATE FoodModel SET quantity= :qty WHERE productId = :id")
    int setQuantity(int qty,int id);

    @Query("SELECT * FROM FoodModel WHERE productId = :id")
    FoodModel getCartItemByServiceId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FoodModel item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ArrayList<FoodModel> list);

    @Query("DELETE FROM FoodModel WHERE productId = :id")
    void deleteByProductId(int id);


    @Query("DELETE FROM FoodModel")
    void deleteAll();

    @Query("SELECT COUNT(FoodModel.id) FROM FoodModel")
    int getRowCount();
}
