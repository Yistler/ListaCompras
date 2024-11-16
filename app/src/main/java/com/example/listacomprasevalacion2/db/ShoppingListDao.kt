package com.example.listacomprasevalacion2.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ShoppingListDao {
    //Obtener todos los productos
    @Query("SELECT * FROM shoppinglistitem ORDER BY purchased ASC")
    fun getAll(): List<ShoppingLIstItem>

    @Query("SELECT COUNT(*) FROM shoppinglistitem")
    fun getCount(): Int

    @Query("SELECT * FROM ShoppingLIstItem WHERE id = :id")
    fun findById(id:Int):ShoppingLIstItem

    //Insertar un nuevo producto a la lista
    @Insert
    fun insertItem(item: ShoppingLIstItem):Long

    //marcar producto como comprado
    @Update
    fun updateItem(item: ShoppingLIstItem)

    //Eliminar
    @Delete
    fun deleteItem(item: ShoppingLIstItem)






}