package fr.yaniv.visitar.ui.information

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fr.yaniv.visitar.ARactivity
import fr.yaniv.visitar.R
import fr.yaniv.visitar.databinding.FragmentCameraBinding
import fr.yaniv.visitar.databinding.FragmentInformationBinding
import fr.yaniv.visitar.ui.information.InformationViewModel

class InformationFragment : Fragment() {

    private var _binding: FragmentInformationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val informationViewModel =
            ViewModelProvider(this).get(InformationViewModel::class.java)

        _binding = FragmentInformationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var arActivity = activity as ARactivity;
        _binding!!.textInformationsHoraire.text = arActivity.waypoint.horaires;
        _binding!!.textInformationsAffluence.text = arActivity.waypoint.affluence;
        _binding!!.textInformationsTarif.text = arActivity.waypoint.tarif;

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}