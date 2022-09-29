/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cupcake

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentSummaryBinding
import com.example.cupcake.model.OrderViewModel

/**
 * [SummaryFragment] contains a summary of the order details with a button to share the order
 * via another app.
 */
class SummaryFragment : Fragment() {

    // Binding objek instance yang sesuai dengan layout fragment_summary.xml
    // Properti ini bukan nol antara callback lifecycle onCreateView() dan onDestroyView(),
    // ketika hierarchy tampilan dilampirkan ke fragmen.
    private var binding: FragmentSummaryBinding? = null

    // Gunakan delegasi properti Kotlin 'by activityViewModels()' dari artefak fragmen-ktx
    private val sharedViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentSummaryBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            summaryFragment = this@SummaryFragment
        }
    }

    /**
     * Kirim pesanan dengan membagikan detail pesanan ke aplikasi lain melalui maksud implisit.
     */
    fun sendOrder() {
        // Buat teks ringkasan pesanan dengan informasi dari model tampilan
        val numberOfCupcakes = sharedViewModel.quantity.value ?: 0
        val orderSummary = getString(
            R.string.order_details,
            resources.getQuantityString(R.plurals.cupcakes, numberOfCupcakes, numberOfCupcakes),
            sharedViewModel.flavor.value.toString(),
            sharedViewModel.date.value.toString(),
            sharedViewModel.price.value.toString()
        )

        // Buat maksud implisit ACTION_SEND dengan detail pesanan di ekstra maksud
        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.new_cupcake_order))
            .putExtra(Intent.EXTRA_TEXT, orderSummary)

        // Periksa apakah ada aplikasi yang dapat menangani maksud ini sebelum meluncurkannya
        if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
            // // Memulai aktivitas baru dengan maksud tertentu (ini dapat membuka dialog berbagi di
            // perangkat jika beberapa aplikasi dapat menangani maksud ini)
            startActivity(intent)
        }
    }

    /**
     * Batalkan pesanan dan mulai dari awal.
     */
    fun cancelOrder() {
        // Atur ulang urutan dalam model tampilan
        sharedViewModel.resetOrder()

        // Arahkan kembali ke [StartFragment] untuk memulai kembali
        findNavController().navigate(R.id.action_summaryFragment_to_startFragment)
    }

    /**
     * Metode siklus hidup fragmen ini dipanggil saat hierarki tampilan terkait dengan fragmen
     * sedang dihapus. Akibatnya, bersihkan objek yang mengikat.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}