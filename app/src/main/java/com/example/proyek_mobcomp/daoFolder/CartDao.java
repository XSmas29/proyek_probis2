package com.example.proyek_mobcomp.daoFolder;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proyek_mobcomp.classFolder.cCart;

import java.util.List;

@Dao
public interface CartDao {
    @Query("select * from cart")
    List<cCart> getCartAll();

    @Query("select * from cart where idCart = :idCart")
    List<cCart> getCartByIdCart(int idCart);

    @Query("select * from cart where username = :username")
    List<cCart> getCartByUsername(String username);

    @Query("select * from cart where idProduct = :idProduct and username = :username")
    List<cCart> getCartByIdProductAndUsername(int idProduct, String username);

    @Query("delete from cart where username = :username")
    void deleteCartByUsername(String username);

    @Insert
    void insertCart(cCart cart);

    @Update
    void updateCart(cCart cart);

    @Delete
    void deleteCart(cCart cart);
}
