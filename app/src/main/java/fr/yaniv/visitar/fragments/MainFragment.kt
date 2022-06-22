package fr.yaniv.visitar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.yaniv.visitar.ParcoursModel
import fr.yaniv.visitar.R
import fr.yaniv.visitar.adapter.ParcoursAdapter
import fr.yaniv.visitar.adapter.ParcoursItemDecoration

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(R.layout.fragment_main, container, false)

        //créer une liste qui va stocker ces plantes
        val parcourslist = arrayListOf<ParcoursModel>()
        //enregistrer une premiere plante dans notre liste (pissenlit)
        parcourslist.add(
            ParcoursModel(
                "Pissenlit",
                "jaune soleil",
                "https://pixabay.com/photos/dandelion-heaven-flower-nature-463928/",
                false
            ))
        parcourslist.add(
                    ParcoursModel(
                        "Rose",
                        "ça pique un peu",
                        "https://cdn.pixabay.com/photo/2014/04/10/11/24/rose-320868_1280.jpg",
                        false
                    ))

        parcourslist.add(
                    ParcoursModel(
                        "Cactus",
                        "ça pique beaucoup",
                        "https://cdn.pixabay.com/photo/2016/07/06/20/47/prickly-pear-1501307__480.jpg",
                        false
                    ))

        parcourslist.add(
                    ParcoursModel(
                        "Tulipe",
                        "c'est beau",
                        "https://cdn.pixabay.com/photo/2017/04/23/20/36/tulips-2254970__480.jpg",
                        false
                    ))

            // recuperer le 1er recycler view
            val horizontalRecyclerView = view . findViewById < RecyclerView >(R.id.horizontal_recycler_view)

        horizontalRecyclerView.adapter =
            ParcoursAdapter(parcourslist, R.layout.item_horizontal_parcours)

        // recuperer le second recycler view
        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView.adapter =
            ParcoursAdapter(parcourslist, R.layout.item_vertical_parcours)
        verticalRecyclerView.addItemDecoration(ParcoursItemDecoration())
        return view
    }
}