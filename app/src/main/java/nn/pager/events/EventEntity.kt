package nn.pager.events

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "events")
class EventEntity(
    @PrimaryKey
    @ColumnInfo(name = "title") val title : String = " ",
    @ColumnInfo(name = "time") val time : String? = null,
    @ColumnInfo(name = "desc") val desc : String? = null,
    @ColumnInfo(name = "course") val course : String? = null,
) : Serializable