package kr.ac.kumoh.s20180073.project.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import kr.ac.kumoh.s20180073.project.Detail
import kr.ac.kumoh.s20180073.project.R
import kr.ac.kumoh.s20180073.project.ui.home.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private val mAdapter = AreaAdapter()

    companion object{
        const val IMAGE = "image"
        const val REQUEST_IMAGE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 매번 요청하게 하려면 this (Fragment) 사용
        //dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        // 처음에 한 번만 요청하게 하려면 activity 사용
        homeViewModel = ViewModelProvider(activity as AppCompatActivity).get(HomeViewModel::class.java)

        homeViewModel.list.observe(viewLifecycleOwner, Observer<ArrayList<HomeViewModel.Area>> {
            mAdapter.notifyDataSetChanged()
        })

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        //val textView: TextView = root.findViewById(R.id.text_dashboard)
        //dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
        //    textView.text = it
        //})

        val IsResult = root.findViewById<RecyclerView>(R.id.IsResult)
        IsResult.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

        return root
    }

    inner class AreaAdapter: RecyclerView.Adapter<AreaAdapter.ViewHolder>(){
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txText1: TextView = itemView.findViewById<TextView>(R.id.text1)

            val niImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.image)

            init {
                niImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaAdapter.ViewHolder {
            val view = layoutInflater.inflate(
                R.layout.item_area,
                parent,
                false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return homeViewModel.getSize()
        }

        override fun onBindViewHolder(holder: AreaAdapter.ViewHolder, position: Int) {
            holder.txText1.text = homeViewModel.getArea(position).name

            holder.niImage.setImageUrl(
                homeViewModel.getImageUrl(position),
                homeViewModel.imageLoader
            )

            holder.itemView.setOnClickListener {
                var dimage = Intent(context, Detail::class.java)
                dimage.putExtra(IMAGE, homeViewModel.getDetailImageUrl(position))
                startActivityForResult(dimage, REQUEST_IMAGE)
                //Toast.makeText(context,dashboardViewModel.getDetailImageUrl(position),Toast.LENGTH_LONG).show()
            }
        }
    }
}