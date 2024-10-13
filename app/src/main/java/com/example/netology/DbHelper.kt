package com.example.netology

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "mouse_app_db"
        const val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS games (
                id TEXT PRIMARY KEY,
                success_count INTEGER NOT NULL,
                total_count INTEGER NOT NULL,
                created_at TEXT NOT NULL
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}