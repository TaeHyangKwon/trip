package kr.ac.kumoh.s20180073.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.android.volley.toolbox.NetworkImageView
import kr.ac.kumoh.s20180073.project.ui.home.HomeViewModel
import kr.ac.kumoh.s20180073.project.ui.home.HomeFragment

class Detail() : AppCompatActivity() {
    private lateinit var homeViewModel: HomeViewModel

    companion object{
        const val RESULT = "image_result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val dImage: NetworkImageView = findViewById<NetworkImageView>(R.id.detailimage)

        //Toast.makeText(this,intent.getStringExtra(DashboardFragment.IMAGE),Toast.LENGTH_LONG).show()

        dImage.setImageUrl(intent.getStringExtra(HomeFragment.IMAGE), MySingleton.getInstance(application).imageLoader)
    }
}

