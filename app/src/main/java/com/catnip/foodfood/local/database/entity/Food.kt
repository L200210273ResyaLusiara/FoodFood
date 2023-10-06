package com.catnip.foodfood.local.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "foods")
data class Food(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "maps_url") val mapsUrl: String,
    @ColumnInfo(name = "desc") val desc: String
) : Parcelable
