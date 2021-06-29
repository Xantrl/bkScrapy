package nn.pager

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import nn.pager.events.EventDatabase
import nn.pager.events.EventRepository

class App : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy {
        EventDatabase.getDatabase(this, applicationScope)
    }
    val repository by lazy {
        EventRepository(database)
    }
}