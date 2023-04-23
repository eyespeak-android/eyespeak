package com.example.eyespeak

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.first


suspend fun getIntValueByKey(dataStore: DataStore<Preferences>, key:String): Int
{
    val intKey = intPreferencesKey(key)
    val preferences = dataStore.data.first()
    println("Final Key: ${preferences[intKey]}")
    return preferences[intKey] ?:0
}

suspend fun getFloatValueByKey(dataStore: DataStore<Preferences>, key:String): Float
{
    val floatKey = floatPreferencesKey(key)
    val preferences = dataStore.data.first()
    println("Final Key: ${preferences[floatKey]}")
    return preferences[floatKey] ?:0f
}

suspend fun getStringValueByKey(dataStore:DataStore<Preferences>,key:String):String
{
    val stringKey = stringPreferencesKey(key)
    val preferences = dataStore.data.first()
    return preferences[stringKey] ?:""
}

suspend fun setStringValueByKey(dataStore:DataStore<Preferences>,key:String,value:String)
{
    val stringKey = stringPreferencesKey(key)
    dataStore.edit{preferences->
        preferences[stringKey] = value
    }
}
suspend fun setIntValueByKey(dataStore: DataStore<Preferences>, key:String, value:Int)
{
    val intKey = intPreferencesKey(key)
    dataStore.edit{preferences->
        preferences[intKey] = value
    }
}

suspend fun setFloatValueByKey(dataStore: DataStore<Preferences>, key:String, value:Float)
{
    val floatKey = floatPreferencesKey(key)
    dataStore.edit{preferences->
        preferences[floatKey] = value
    }
}