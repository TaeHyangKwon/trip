package kr.ac.kumoh.s20180073.project.ui.notifications

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonArrayRequest
import kr.ac.kumoh.s20180073.project.MySingleton
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {

    companion object{
        const val QUEUE_TAG="VolleyRequest"

        //우리집
        const val SERVER_URL = "http://192.168.0.7:8080"
        //이정연집
        //const val SERVER_URL = "http://192.168.0.9:8080"
    }

    private var mQueue: RequestQueue

    data class Food(var fid: Int, var fname: String, var flocation: String, var fimage: String, var fareanum: Int)

    val list = MutableLiveData<ArrayList<Food>>()
    private val food = ArrayList<Food>()

    val imageLoader: ImageLoader
    init {
        list.value = food

        mQueue = MySingleton.getInstance(application).requestQueue
        imageLoader = MySingleton.getInstance(application).imageLoader

        requestFood()
    }

    fun getImageUrl(i: Int): String = "$SERVER_URL/image/"+ URLEncoder.encode(food[i].fimage, "utf-8")

    //fun getDetailImageUrl(i: Int): String = "$SERVER_URL/image/"+URLEncoder.encode(place[i].detailimage, "utf-8")

    fun requestFood(){
        val request = JsonArrayRequest(
            Request.Method.GET,
            SERVER_URL,
            null,
            {
                food.clear()
                parseJson(it)
                list.value=food
            },
            {
                Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
            }
        )

        request.tag = QUEUE_TAG
        mQueue.add(request)
    }

    fun getFood(i: Int) = food[i]

    fun getSize() = food.size

    override fun onCleared() {
        super.onCleared()
        mQueue.cancelAll(QUEUE_TAG)
    }

    private fun parseJson(items: JSONArray){
        for(i in 0 until items.length()){
            val item: JSONObject = items[i] as JSONObject
            val fid = item.getInt("fid")
            val fname = item.getString("fname")
            val flocation = item.getString("flocation")
            val fimage= item.getString("fimage")
            val fareanum = item.getInt("fareanum")

            food.add(Food(fid, fname, flocation, fimage, fareanum))
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}