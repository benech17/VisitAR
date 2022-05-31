package fr.yaniv.visitar.ui.histoire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.yaniv.visitar.databinding.FragmentHistoireBinding

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

        val textView: TextView = binding.textHistoire
        histoireViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}