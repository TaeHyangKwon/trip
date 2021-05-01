package kr.ac.kumoh.s20180073.project.ui.dashboard

import android.app.Application
import android.graphics.Bitmap
import android.util.LruCache
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kr.ac.kumoh.s20180073.project.MySingleton
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    companion object{
        const val QUEUE_TAG="VolleyRequest"

        //우리집
        const val SERVER_URL = "http://192.168.0.7:8080"
        //이정연집
        //const val SERVER_URL = "http://192.168.0.9:8080"
    }

    private var mQueue: RequestQueue

    data class Place(var pid: Int, var pname: String, var location: String, var pimage: String, var areanum: Int)

    val list = MutableLiveData<ArrayList<Place>>()
    private val place = ArrayList<Place>()

    val imageLoader: ImageLoader
    init {
        list.value = place

        mQueue = MySingleton.getInstance(application).requestQueue
        imageLoader = MySingleton.getInstance(application).imageLoader

        requestPlace()
    }

    fun getImageUrl(i: Int): String = "$SERVER_URL/image/"+URLEncoder.encode(place[i].pimage, "utf-8")

    //fun getDetailImageUrl(i: Int): String = "$SERVER_URL/image/"+URLEncoder.encode(place[i].detailimage, "utf-8")

    fun requestPlace(){
        val request = JsonArrayRequest(
            Request.Method.GET,
            SERVER_URL,
            null,
            {
                place.clear()
                parseJson(it)
                list.value=place
            },
            {
                Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
            }
        )

        request.tag = QUEUE_TAG
        mQueue.add(request)
    }

    fun getPlace(i: Int) = place[i]

    fun getSize() = place.size

    override fun onCleared() {
        super.onCleared()
        mQueue.cancelAll(QUEUE_TAG)
    }

    private fun parseJson(items: JSONArray){
        for(i in 0 until items.length()){
            val item: JSONObject = items[i] as JSONObject
            val pid = item.getInt("pid")
            val pname = item.getString("pname")
            val location = item.getString("location")
            val pimage= item.getString("pimage")
            val areanum = item.getInt("areanum")

            place.add(Place(pid, pname, location, pimage, areanum))
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}