package nn.pager.events
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.flow.Flow
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import kotlin.coroutines.coroutineContext

class EventRepository(private val db : EventDatabase) {


    fun insert(event : EventEntity) {
        db.eventDao().insert(event)
    }
    fun getSaved() : Flow<List<EventEntity>> = db.eventDao().getSaved()

    suspend fun deleteAll() = db.eventDao().deleteAll()

    fun crawl(user : String, pass : String): List<EventEntity> {
        val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0"
        val USERNAME = user
        val PASSWORD = pass

        val BASE_URL = "https://sso.hcmut.edu.vn/cas/login?service=http%3A%2F%2Fe-learning.hcmut.edu.vn%2Flogin%2Findex.php%3FauthCAS%3DCAS"

        val initdoc : Document
        val login : Connection.Response
        val ltVal  : String
        val hiddenSub : String = "Đăng+nhập"
        val executionVal : String
        val cookies : Map<String, String>

        val events : MutableList<EventEntity> = mutableListOf()

        // get "lt" and "execution" value
        val initialResponse : Connection.Response = Jsoup.connect(BASE_URL)
            .method(Connection.Method.GET)
            .userAgent(USER_AGENT)
            .execute()

        initdoc = initialResponse.parse()

        // get lt
        val lt : Element = initdoc.select("input[name=lt]").first()
        ltVal = lt.attr("value")

        // get execution
        val execution : Element = initdoc.select("input[name=execution]").first()
        executionVal = execution.attr("value")

        // get cookies
        cookies = initialResponse.cookies()

        // now do the login
        login = Jsoup.connect(BASE_URL)
            .data("username",USERNAME)
            .data("password",PASSWORD)
            .data("lt", ltVal)
            .data("execution", executionVal)
            .data("_eventId", "submit")
            .data("submit",hiddenSub)
            .cookies(cookies)
            .userAgent(USER_AGENT)
            .method(Connection.Method.POST)
            .execute()

        val tester = Jsoup.connect(BASE_URL)
            .userAgent(USER_AGENT)
            .cookies(login.cookies())
            .ignoreHttpErrors(true)
            .followRedirects(false)
            .method(Connection.Method.POST)
            .execute()
        Log.d("status", tester.statusCode().toString())
        if (tester.statusCode() == 302) {
            //fetch
            val fetch_event: Document =
                Jsoup.connect("http://e-learning.hcmut.edu.vn/calendar/view.php?view=upcoming")
                    .userAgent(USER_AGENT)
                    .cookies(login.cookies())
                    .get()

            val event_HTML = fetch_event.getElementsByClass("card rounded")

            event_HTML.forEach { item ->
                val title = item.select("h3.name").text()
                val time = item.select("div.col-11:contains(,)").text()
                val desc = item.select("div.description-content").text()
                val course = item.select("div.col-11:contains(_)").text()

                if (!title.isNullOrEmpty() || !time.isNullOrEmpty() ||
                    !course.isNullOrEmpty()
                ) {
                    val event = EventEntity(
                        title,
                        time,
                        desc,
                        course
                    )
                    events.add(event)
                    insert(event)

                }
            }
        }
        return events
    }
}