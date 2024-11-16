package com.example.listacomprasevalacion2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listacomprasevalacion2.db.AppDatabase
import com.example.listacomprasevalacion2.db.ShoppingLIstItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgregarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddShoppingItem()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddShoppingItem() {
    val (item, setItem) = remember { mutableStateOf("")}

    val scope = rememberCoroutineScope()
    val contexto = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Filled.ShoppingCart,
                contentDescription = "Carro de compras",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            //Campoo de texto donde el usuario ingresa el nombre del producto
            TextField(
                value = item,
                onValueChange ={setItem(it)},
                label = { Text(stringResource(id = R.string.add_item_textField))}
            )
            Spacer(modifier = Modifier.height(20.dp))
            //Botón para agregar el ítem a la bd
            Button(
                onClick = {
                    //Verificar si el campo de texto no está vacío
                    if(item.isNotBlank()){
                        scope.launch(Dispatchers.IO){
                            //Se crea un nuevo ítem con la descripción ingresada
                            val newItem = ShoppingLIstItem(
                                id = 0,
                                purchased = false,
                                description = item
                            )
                            //Inserta el nuevo item en la base de datos
                            val dao = AppDatabase.getInstance(contexto).shoppingListDao()
                            dao.insertItem(newItem)

                            withContext(Dispatchers.Main){
                                setItem("")//Limpia el campo de texto después de agregar el ítem
                            }
                        }
                    }
                }
            ) {
                Text(stringResource(id = R.string.add_item_button))
            }
        }

        Button(
            onClick = {
                val intent = Intent(contexto, MainActivity::class.java)
                contexto.startActivity(intent)
            },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Volver",
            )
        }
    }
}