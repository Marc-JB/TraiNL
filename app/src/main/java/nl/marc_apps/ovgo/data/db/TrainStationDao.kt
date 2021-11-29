package nl.marc_apps.ovgo.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TrainStationDao {
    @Query("SELECT * FROM trainstation")
    suspend fun getAll(): List<TrainStationEntity>?

    @Query("SELECT * FROM trainstation WHERE uicCode LIKE :uicCode LIMIT 1")
    suspend fun getByUicCode(uicCode: String): TrainStationEntity?

    @Query("SELECT count(*) FROM trainstation")
    suspend fun getSize(): Int

    @Insert
    suspend fun insert(trainStations: Collection<TrainStationEntity>)

    @Delete
    suspend fun delete(trainStation: TrainStationEntity)

    @Query("DELETE FROM trainstation")
    suspend fun deleteAll()
}
