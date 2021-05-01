package kr.ac.kumoh.s20180073.project.ui.home

import android.app.Application
import android.content.Context
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

class HomeViewModel (application: Application) : AndroidViewModel(application) {

    companion object{
        const val QUEUE_TAG="VolleyRequest"

        //우리집
        const val SERVER_URL = "http://192.168.0.7:8080"
        //이정연집
        //const val SERVER_URL = "http://192.168.0.9:8080"
    }

    private var mQueue: RequestQueue

    data class Area(var id: Int, var name: String, var image: String, var detailimage: String)

    val list = MutableLiveData<ArrayList<Area>>()
    private val area = ArrayList<Area>()

    val imageLoader: ImageLoader
    init {
        list.value = area
        //mQueue = Volley.newRequestQueue(application)

        mQueue = MySingleton.getInstance(application).requestQueue
        imageLoader = MySingleton.getInstance(application).imageLoader

        requestArea()
    }

    fun getImageUrl(i: Int): String = "$SERVER_URL/image/"+URLEncoder.encode(area[i].image, "utf-8")

    fun getDetailImageUrl(i: Int): String = "$SERVER_URL/image/"+URLEncoder.encode(area[i].detailimage, "utf-8")

    fun requestArea(){
        val request = JsonArrayRequest(
            Request.Method.GET,
            SERVER_URL,
            null,
            {
                area.clear()
                parseJson(it)
                list.value=area
            },
            {
                Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
            }
        )

        request.tag = QUEUE_TAG
        //mQueue.add(request)
        MySingleton.getInstance(getApplication()).addToRequestQueue(request)
    }

    fun getArea(i: Int) = area[i]

    fun getSize() = area.size

    override fun onCleared() {
        super.onCleared()
        mQueue.cancelAll(QUEUE_TAG)
    }

    private fun parseJson(items: JSONArray){
        for(i in 0 until items.length()){
            val item: JSONObject = items[i] as JSONObject
            val id = item.getInt("id")
            val name = item.getString("name")
            val image= item.getString("image")
            val detailimage = item.getString("detailimage")

            if(!area.contains(Area(id, name, image, detailimage)))
                area.add(Area(id, name, image, detailimage))
        }
    }


    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}