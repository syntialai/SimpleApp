package com.blibli.simpleapp.feature.db.builder

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.blibli.simpleapp.feature.user.db.builder.RoomDB
import com.blibli.simpleapp.feature.user.db.model.User
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    private val TEST_DB = "user_database_test"

    private val ALL_MIGRATIONS = arrayOf(RoomDB.MIGRATION_1_TO_2)

    private val USER = User(1, "syntialai")

    private var dbVersion1: SupportSQLiteDatabase? = null
    private var dbVersion2: SupportSQLiteDatabase? = null

    @Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        RoomDB::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Before
    fun setUp() {
        dbVersion1 = createDummyDB(1)
    }

    @After
    fun tearDown() {
        dbVersion1 = null
    }

    @Test
    @Throws(IOException::class)
    fun migrateAll() {
        dbVersion2 = createDB(2)

        val userDb = getMigratedDB()

        USER.login?.let {
            userDb.userDao().getUserByUsername(it)?.apply {
                assertEquals(id, USER.id)
                assertEquals(login, USER.id)
            }
        }
    }

    private fun getMigratedDB(): RoomDB {
        return Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            RoomDB::class.java,
            TEST_DB
        )
            .addMigrations(*ALL_MIGRATIONS)
            .build()
            .apply {
                openHelper.writableDatabase
                close()
            }
    }

    private fun createDummyDB(version: Int): SupportSQLiteDatabase {
        return helper.createDatabase(TEST_DB, version)
    }

    private fun createDB(version: Int): SupportSQLiteDatabase {
        return helper.createDatabase(TEST_DB, version).apply {
            insertUserDummy(USER, this)
            close()
        }
    }

    private fun insertUserDummy(user: User, db: SupportSQLiteDatabase) {
        val values = ContentValues().apply {
            put("id", user.id)
            put("username", user.login)
        }

        db.insert("user_table", SQLiteDatabase.CONFLICT_REPLACE, values)
    }
}