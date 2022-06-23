package fr.yaniv.visitar.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.yaniv.visitar.*

class ParcoursAdapter(
    private val context: MainActivity,
    private val parcoursList: List<ParcoursModel>,
    private val layoutId: Int,
    private val path: String
) : RecyclerView.Adapter<ParcoursAdapter.ViewHolder>() {
    val mapActivity = context

    // boite pour ranger tous les composants à controler
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val parcoursImage = view.findViewById<ImageView>(R.id.image_item)
        val parcoursName:TextView? = view.findViewById(R.id.name_item)
        val parcoursDescripton:TextView? =  view.findViewById(R.id.description_item)
        val parcoursTemps:TextView? =  view.findViewById(R.id.temps_item)
        val parcoursDistance:TextView? =  view.findViewById(R.id.distance_item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // recuperer les infos du parcours courant
        val currentParcours = parcoursList[position]

        //glide pour recuperer l'image à partir de son lien -> composant
        Glide.with(context).load(Uri.parse(currentParcours.imageUrl)).into(holder.parcoursImage)

        // MAJ nom de la plante
        holder.parcoursName?.text = currentParcours.name
        holder.parcoursDescripton?.text = currentParcours.descrption
        holder.parcoursDistance?.text = currentParcours.distance
        holder.parcoursTemps?.text = currentParcours.duree


        holder.parcoursImage.setOnClickListener {
            val switchActivityIntent = Intent(context, MapActivity::class.java)
            switchActivityIntent.putExtra("path",path)
            context.startActivity(switchActivityIntent)
        }
    }

    override fun getItemCount(): Int {
        return parcoursList.size
    }
}