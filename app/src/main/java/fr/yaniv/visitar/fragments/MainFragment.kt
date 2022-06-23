package fr.yaniv.visitar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.yaniv.visitar.MainActivity
import fr.yaniv.visitar.ParcoursModel
import fr.yaniv.visitar.R
import fr.yaniv.visitar.adapter.ParcoursAdapter
import fr.yaniv.visitar.adapter.ParcoursItemDecoration

class MainFragment(private val context: MainActivity,private val path: String) : Fragment() {
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
                "Paris en amoureux",
                "Notre Dame - Louvre - Rivoli",
                "1h 50 min",
                "4.8 km",
                "https://cdn.pixabay.com/photo/2018/04/25/09/26/eiffel-tower-3349075_1280.jpg"
            ))
        parcourslist.add(
                    ParcoursModel(
                        "Rome antique et sa culture  ",
                        "Colisée - Fontaine de Trévis",
                        "1h 12 min",
                        "5.3 Km",
                        "https://cdn.pixabay.com/photo/2015/05/09/22/44/the-altar-of-the-fatherland-760337_1280.jpg"
                    ))

        parcourslist.add(
                    ParcoursModel(
                        "Sydney et les kangourous",
                        "Opéra - Kangourou - Bridge",
                        "1h 08 min",
                        "7.2 Km",
                        "https://cdn.pixabay.com/photo/2018/05/07/22/08/opera-house-3381786_1280.jpg"
                    ))

        parcourslist.add(
                    ParcoursModel(
                        "New York",
                        "Manhattan - Times Square ",
                        "2h 27 min",
                        "8.2 Km",
                        "https://cdn.pixabay.com/photo/2020/02/16/20/29/nyc-4854718_1280.jpg"
                    ))

        // recuperer le 1er recycler view
            val horizontalRecyclerView = view . findViewById < RecyclerView >(R.id.horizontal_recycler_view)

        horizontalRecyclerView.adapter =
            ParcoursAdapter(context,parcourslist, R.layout.item_horizontal_parcours,path)

        // recuperer le second recycler view
        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView.adapter =
            ParcoursAdapter(context,parcourslist, R.layout.item_vertical_parcours,path)
        verticalRecyclerView.addItemDecoration(ParcoursItemDecoration())
        return view
    }
}