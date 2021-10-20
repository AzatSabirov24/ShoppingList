package com.azat_sabirov.shoppinglist.activities

import android.app.Application
import com.azat_sabirov.shoppinglist.db.MainDataBase

class MainApp : Application() {
    val database by lazy { MainDataBase.getDataBase(this) }
}