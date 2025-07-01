package br.com.emersonfiwre.drawteam.features.player.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.emersonfiwre.drawteam.commons.model.PlayerModel

@Database(entities = [PlayerModel::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun playerDao(): PlayerDAO

    companion object {
        private const val databaseName = "draw_team_db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(applicationContext: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, databaseName
            ).allowMainThreadQueries().build()
            INSTANCE = instance
            instance
        }
    }
}
