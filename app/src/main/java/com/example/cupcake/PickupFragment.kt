package com.example.cupcake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentPickupBinding
import com.example.cupcake.model.OrderViewModel

/**
 * [PickupFragment] memungkinkan pengguna untuk memilih tanggal pengambilan untuk pesanan cupcake.
 */
class PickupFragment : Fragment() {

    // Binding objek instance yang sesuai dengan layout fragment_pickup.xml
    // Properti ini bukan nol antara callback lifcycle onCreateView() dan onDestroyView(),
    // ketika hierarki tampilan dilampirkan ke fragmen.
    private var binding: FragmentPickupBinding? = null

    // Gunakan delegasi properti Kotlin 'by activityViewModels()' dari artefak fragmen-ktx
    private val sharedViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentPickupBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            pickupFragment = this@PickupFragment
        }
    }

    /**
     * Navigasikan ke layar berikutnya untuk melihat ringkasan pesanan.
     */
    fun goToNextScreen() {
        findNavController().navigate(R.id.action_pickupFragment_to_summaryFragment)
    }

    /**
     * Batalkan pesanan dan mulai dari awal.
     */
    fun cancelOrder() {
        // Atur ulang urutan dalam model tampilan
        sharedViewModel.resetOrder()

        // Arahkan kembali ke [StartFragment] untuk memulai kembali
        findNavController().navigate(R.id.action_pickupFragment_to_startFragment)
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