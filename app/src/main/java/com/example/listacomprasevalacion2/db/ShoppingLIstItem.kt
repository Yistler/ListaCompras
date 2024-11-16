package com.example.listacomprasevalacion2.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.w3c.dom.Text

@Entity
data class ShoppingLIstItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var purchased: Boolean = false,
    var description: String
)