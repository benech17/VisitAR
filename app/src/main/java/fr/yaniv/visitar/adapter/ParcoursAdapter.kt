package fr.yaniv.visitar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import fr.yaniv.visitar.ParcoursModel
import fr.yaniv.visitar.R

class ParcoursAdapter(
    private val parcoursList: List<ParcoursModel>,
    private val layoutId: Int
) : RecyclerView.Adapter<ParcoursAdapter.ViewHolder>() {

    // boite pour ranger tous les composants à controler
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val parcoursImage = view.findViewById<ImageView>(R.id.image_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // recuperer les infos du parcours courant
        val currentParcours = parcoursList[position]

    }

    override fun getItemCount(): Int {
        return parcoursList.size
    }
}