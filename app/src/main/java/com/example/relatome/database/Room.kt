package com.example.relatome.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface LoginDao {
    @Query("SELECT * FROM loginentity")
    fun getLoginEntity(): LiveData<List<LoginEntity>>

    @Query("SELECT * FROM loginentity")
    fun getDeadLoginEntity(): List<LoginEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLoginEntity(loginEntity: LoginEntity)
}

@Dao
interface RelationshipDao {
    @Query("SELECT * FROM relationshipentity")
    fun getRelationships(): LiveData<List<RelationshipEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRelationships(vararg relationships: RelationshipEntity)

    @Query("DELETE FROM relationshipentity")
    fun clearAll()

}


@Database(entities = [LoginEntity::class, RelationshipEntity::class], version = 2)
abstract class RelatomeDatabase: RoomDatabase() {
    abstract val loginDao : LoginDao
    abstract val relationshipDao : RelationshipDao
}


private lateinit var INSTANCE : RelatomeDatabase

fun getDatabase(context: Context): RelatomeDatabase {
    synchronized(RelatomeDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext, RelatomeDatabase::class.java, "database")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}