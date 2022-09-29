package com.example.cupcake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentStartBinding
import com.example.cupcake.model.OrderViewModel

/**
 * Ini adalah layar pertama dari aplikasi Cupcake. Pengguna dapat memilih berapa banyak cupcakes yang akan dipesan.
 */
class StartFragment : Fragment() {

    // Binding objek instance yang sesuai dengan layout fragment_start.xml
    // Properti ini bukan nol antara callback lifecycle onCreateView() dan onDestroyView(),
    // ketika hierarchy tampilan dilampirkan ke fragmen.
    private var binding: FragmentStartBinding? = null

    // Gunakan delegasi properti Kotlin 'by activityViewModels()' dari artefak fragmen-ktx
    private val sharedViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentStartBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.startFragment = this
    }

    /**
     * Mulai pesanan dengan jumlah kue mangkuk yang diinginkan dan navigasikan ke layar berikutnya.
     */
    fun orderCupcake(quantity: Int) {
        // Perbarui model tampilan dengan kuantitas
        sharedViewModel.setQuantity(quantity)

        // Jika belum ada rasa yang diatur dalam model tampilan, pilih vanilla sebagai rasa default
        if (sharedViewModel.hasNoFlavorSet()) {
            sharedViewModel.setFlavor(getString(R.string.vanilla))
        }

        // Navigasikan ke tujuan berikutnya untuk memilih rasa cupcakes
        findNavController().navigate(R.id.action_startFragment_to_flavorFragment)
    }

    /**
     * Metode siklus hidup fragmen ini dipanggil saat hierarki tampilan terkait dengan fragment
     * sedang dihapus. Akibatnya, bersihkan objek yang mengikat.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}