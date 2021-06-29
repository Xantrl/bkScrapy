package nn.pager.events

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM events")
    fun getSaved(): Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(events: EventEntity)

    @Query("DELETE FROM events")
    suspend fun deleteAll()

}