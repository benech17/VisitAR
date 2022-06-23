package fr.yaniv.visitar.ui.histoire

import android.graphics.Color.red
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.mapbox.maps.extension.style.sources.generated.imageSource
import fr.yaniv.visitar.ARactivity
import fr.yaniv.visitar.data.Photo

import org.imaginativeworld.whynotimagecarousel.R
import fr.yaniv.visitar.databinding.CarouselLayout1Binding
import fr.yaniv.visitar.databinding.FragmentHistoireBinding
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage
import java.io.File
import kotlin.random.Random

class HistoireFragment : Fragment() {

    private var _binding: FragmentHistoireBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val histoireViewModel =
            ViewModelProvider(this).get(HistoireViewModel::class.java)

        _binding = FragmentHistoireBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val carousel: ImageCarousel = binding.carousel
        // Register lifecycle. For activity this will be lifecycle/getLifecycle() and for fragment it will be viewLifecycleOwner/getViewLifecycleOwner().
        carousel.registerLifecycle(lifecycle)


        // Custom view
        carousel.carouselListener = object : CarouselListener {
            override fun onCreateViewHolder(
                layoutInflater: LayoutInflater,
                parent: ViewGroup
            ): ViewBinding {
                return CarouselLayout1Binding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            }

            override fun onBindViewHolder(
                binding: ViewBinding,
                item: CarouselItem,
                position: Int
            ) {
                val currentBinding = binding as CarouselLayout1Binding

                currentBinding.textView.text = item.caption ?: ""

                currentBinding.imageView.apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP

                    setImage(item, R.drawable.test_level_drawable)
                }

                currentBinding.pictureDate.text = item.headers?.get("date") ?: ""
            }
        }

        val list = getPlaceHistoireData()
        carousel.setData(list)


        return root
    }

    fun getPlaceHistoireData(): List<CarouselItem> {
        val list = mutableListOf<CarouselItem>()
        val arActivity = activity as ARactivity
        arActivity.waypoint.photos.forEach{
            /*
            var f = File(arActivity.path + "/" + it.photo + ".jpg")

            var u = Uri.fromFile(f)
            Toast.makeText(arActivity, u.path,Toast.LENGTH_LONG).show()

             */
            list.add(
                CarouselItem(
                    imageUrl = "https://www.worldofsecrets.net/pmr/" + it.photo + ".jpg",
                    caption =  it.description,
                    headers = mapOf("date" to it.date)
                )
            )
        }

        return list;

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}