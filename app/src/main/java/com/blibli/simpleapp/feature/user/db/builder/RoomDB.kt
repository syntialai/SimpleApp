package com.blibli.simpleapp.feature.user.db.builder

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.blibli.simpleapp.feature.user.db.dao.UserDAO
import com.blibli.simpleapp.feature.user.db.model.User

@Database(entities = [User::class], version = 2)
abstract class RoomDB : RoomDatabase() {

    abstract fun userDao(): UserDAO

    companion object {
        val MIGRATION_1_TO_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create new table
                database.execSQL("CREATE TABLE user_table_new (id INTEGER, username TEXT, avatar_url TEXT, public_repos INTEGER, followers INTEGER, following INTEGER, followers_url TEXT, PRIMARY KEY(id))")

                // Copy data
                database.execSQL("INSERT INTO user_table_new (id, username, avatar_url, public_repos, followers, following, followers_url) SELECT id, username, avatar_url, public_repos, followers, following, followers_url FROM user_table")

                // Remove old table
                database.execSQL("DROP TABLE user_table")

                // Change new table name
                database.execSQL("ALTER TABLE user_table_new RENAME TO user_table")
            }
        }

        @Volatile
        private var INSTANCE: RoomDB? = null

        fun getDatabase(context: Context): RoomDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDB::class.java,
                    "user_database"
                )
//                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_1_TO_2)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}