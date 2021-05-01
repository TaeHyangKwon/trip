package kr.ac.kumoh.s20180073.project.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import kr.ac.kumoh.s20180073.project.R
import kr.ac.kumoh.s20180073.project.ui.dashboard.DashboardViewModel

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private val mAdapter = FoodAdapter()

    companion object{
        const val IMAGE = "image"
        const val REQUEST_IMAGE = 100
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel = ViewModelProvider(activity as AppCompatActivity).get(NotificationsViewModel::class.java)

        notificationsViewModel.list.observe(viewLifecycleOwner, Observer<ArrayList<NotificationsViewModel.Food>> {
            mAdapter.notifyDataSetChanged()
        })

        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
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

    inner class FoodAdapter: RecyclerView.Adapter<FoodAdapter.ViewHolder>(){
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txText1: TextView = itemView.findViewById<TextView>(R.id.text1)
            val txText2: TextView = itemView.findViewById<TextView>(R.id.text2)

            val niImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.image)

            init {
                niImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodAdapter.ViewHolder {
            val view = layoutInflater.inflate(
                R.layout.item_area,
                parent,
                false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return notificationsViewModel.getSize()
        }

        override fun onBindViewHolder(holder: FoodAdapter.ViewHolder, position: Int) {
            holder.txText1.text = notificationsViewModel.getFood(position).fname
            holder.txText2.text = notificationsViewModel.getFood(position).flocation

            holder.niImage.setImageUrl(
                notificationsViewModel.getImageUrl(position),
                notificationsViewModel.imageLoader
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