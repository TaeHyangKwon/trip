package kr.ac.kumoh.s20180073.project.ui.dashboard

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
import kr.ac.kumoh.s20180073.project.R

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private val mAdapter = PlaceAdapter()

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
        dashboardViewModel = ViewModelProvider(activity as AppCompatActivity).get(DashboardViewModel::class.java)

        dashboardViewModel.list.observe(viewLifecycleOwner, Observer<ArrayList<DashboardViewModel.Place>> {
            mAdapter.notifyDataSetChanged()
        })

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
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

    inner class PlaceAdapter: RecyclerView.Adapter<PlaceAdapter.ViewHolder>(){
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txText1: TextView = itemView.findViewById<TextView>(R.id.text1)
            val txText2: TextView = itemView.findViewById<TextView>(R.id.text2)

            val niImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.image)

            init {
                niImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceAdapter.ViewHolder {
            val view = layoutInflater.inflate(
                R.layout.item_area,
                parent,
                false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return dashboardViewModel.getSize()
        }

        override fun onBindViewHolder(holder: PlaceAdapter.ViewHolder, position: Int) {
            holder.txText1.text = dashboardViewModel.getPlace(position).pname
            holder.txText2.text = dashboardViewModel.getPlace(position).location

            holder.niImage.setImageUrl(
                dashboardViewModel.getImageUrl(position),
                dashboardViewModel.imageLoader
            )

//            holder.itemView.setOnClickListener {
//                var dimage = Intent(context, Detail::class.java)
//                dimage.putExtra(IMAGE, dashboardViewModel.getDetailImageUrl(position))
//                startActivityForResult(dimage, REQUEST_IMAGE)
//                //Toast.makeText(context,dashboardViewModel.getDetailImageUrl(position),Toast.LENGTH_LONG).show()
//            }
        }
    }
}