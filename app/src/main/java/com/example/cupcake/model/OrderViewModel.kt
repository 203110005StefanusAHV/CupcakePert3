package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/** Price for a single cupcake */
private const val PRICE_PER_CUPCAKE = 2.00

/** Additional cost for same day pickup of an order */
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

/**
 * [OrderViewModel] menyimpan informasi tentang pesanan cupcake dalam hal kuantitas, rasa, dan
 * tanggal pengambilan. Ia juga tahu bagaimana menghitung harga total berdasarkan rincian pesanan ini.
 */
class OrderViewModel : ViewModel() {

    // Jumlah kue mangkuk dalam urutan ini
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    // Rasa cupcake untuk pesanan ini
    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor

    // Kemungkinan opsi tanggal
    val dateOptions: List<String> = getPickupOptions()

    // Tanggal pengambilan
    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    // Harga pesanan sejauh ini
    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        // Format the price into the local currency and return this as LiveData<String>
        NumberFormat.getCurrencyInstance().format(it)
    }

    init {
        // Tetapkan nilai awal untuk pesanan
        resetOrder()
    }

    /**
     * Tetapkan jumlah cupcakes untuk pesanan ini.
     *
     * @param numberCupcakes to order
     */
    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        updatePrice()
    }

    /**
     * Atur rasa cupcakes untuk pesanan ini. Hanya 1 rasa yang dapat dipilih untuk seluruh pesanan.
     *
     * @param desiredFlavor is the cupcake flavor as a string
     */
    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

    /**
     * Tetapkan tanggal pengambilan untuk pesanan ini.
     *
     * @param pickupDate is the date for pickup as a string
     */
    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    /**
     * Mengembalikan nilai true jika rasa belum dipilih untuk pesanan. Mengembalikan false sebaliknya.
     */
    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    /**
     * Atur ulang pesanan dengan menggunakan nilai default awal untuk kuantitas, rasa, tanggal, dan harga.
     */
    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

    /**
     * Memperbarui harga berdasarkan detail pesanan.
     */
    private fun updatePrice() {
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        // If the user selected the first option (today) for pickup, add the surcharge
        if (dateOptions[0] == _date.value) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        _price.value = calculatedPrice
    }

    /**
     * Mengembalikan daftar opsi tanggal yang dimulai dengan tanggal saat ini dan 3 tanggal berikut.
     */
    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }
}