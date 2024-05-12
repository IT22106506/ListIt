package com.example.listit.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "tasks")
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val taskTitle: String,
    val taskDescription: String,
    val taskPriority: String,
    val taskDeadline: String
): Parcelable
