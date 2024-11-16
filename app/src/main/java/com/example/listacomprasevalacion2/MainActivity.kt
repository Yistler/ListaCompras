package com.example.listacomprasevalacion2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import com.example.listacomprasevalacion2.db.AppDatabase
import com.example.listacomprasevalacion2.db.ShoppingLIstItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*lifecycleScope.launch(Dispatchers.IO){
            val shopingDao = AppDatabase.getInstance(this@MainActivity).shoppingListDao()
            val shopingCount = shopingDao.getCount()
            if(shopingCount < 1){
                shopingDao.insertItem(ShoppingLIstItem(0, false, "tomates"))
            }
        }*/
        setContent{
            ShoppingListUI()
        }
    }
}


/*@Preview(showBackground = true)*/
@Composable
fun ShoppingListUI() {
    val contexto = LocalContext.current
    val (shoppingLIst, setShoppingLIst) = remember { mutableStateOf(emptyList<ShoppingLIstItem>())}

    //para cargar la lista desde la base de datos
    LaunchedEffect(shoppingLIst) {
        withContext(Dispatchers.IO){
            val dao = AppDatabase.getInstance(contexto).shoppingListDao()
            setShoppingLIst(dao.getAll())
        }
    }
    Box(modifier = Modifier.fillMaxSize()){
        if(shoppingLIst.isEmpty()){
            Text(
                text = stringResource(id = R.string.main_text_msg),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
            Button(
                onClick = {
                    val intent = Intent(contexto, AgregarActivity::class.java)
                    contexto.startActivity(intent)
                },
                modifier = Modifier.align(Alignment.BottomEnd)//Alinia el botón en la parte inferior derecha
                    .padding(16.dp)//Espaciado alrededor del botón
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Agregar item"
                )
            }
        }else{
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(shoppingLIst){shoppingLIstItem ->
                    ShoppingItemUI(shoppingLIstItem){
                        setShoppingLIst(emptyList<ShoppingLIstItem>() )
                    }
                }
            }
            Button(
                onClick = {
                    val intent = Intent(contexto, AgregarActivity::class.java)
                    contexto.startActivity(intent)
                },
                modifier = Modifier.align(Alignment.BottomEnd)//Alinia el botón en la parte inferior derecha
                    .padding(16.dp)//Espaciado alrededor del botón
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Agregar item"
                )
            }
        }
    }
}

@Composable
fun ShoppingItemUI(shoppingLIstItem: ShoppingLIstItem, onSave:() -> Unit = {}){
    //Crea un CoroutineScope vinculado al ciclo de vida de ShoppingItemUI
    val scope = rememberCoroutineScope()
    val contexto = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp, horizontal = 20.dp)
    ){
        if(shoppingLIstItem.purchased){
            Icon(
                Icons.Filled.Check,
                contentDescription = "Tarea realizada",
                modifier = Modifier.clickable {
                    scope.launch(Dispatchers.IO){
                        val dao = AppDatabase.getInstance(contexto).shoppingListDao()
                        shoppingLIstItem.purchased = !shoppingLIstItem.purchased //Alterna el estado
                        dao.updateItem(shoppingLIstItem)
                        withContext(Dispatchers.Main){//Regresa al hilo principal para actualizar la UI
                            onSave()
                        }
                    }
                }
            )
        }else{
            Icon(
                Icons.Filled.ShoppingCart ,
                contentDescription = "Tarea por hacer",
                modifier = Modifier.clickable {
                    scope.launch(Dispatchers.IO) {
                        val dao = AppDatabase.getInstance(contexto).shoppingListDao()
                        shoppingLIstItem.purchased = true
                        dao.updateItem(shoppingLIstItem)
                        onSave()
                    }
                }
            )
        }

        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = shoppingLIstItem.description,
            modifier = Modifier.weight(2f)
        )
        Icon(
            Icons.Filled.Delete ,
            contentDescription = "Eliminar",
            modifier = Modifier.clickable {
                scope.launch(Dispatchers.IO) {
                    val dao = AppDatabase.getInstance(contexto).shoppingListDao()
                    dao.deleteItem(shoppingLIstItem)
                    onSave()
                }
            }
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ShoppingItemUIPreview(){
    //val shoppingLIstItem = ShoppingLIstItem(1, false, "Comprar ropa")
    //ShoppingItemUI(shoppingLIstItem)
//}