package com.alfrosoft.kwiqshopapp.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alfrosoft.kwiqshopapp.R
import com.alfrosoft.kwiqshopapp.databinding.ActivitySoldProductDetailsBinding
import com.alfrosoft.kwiqshopapp.model.SoldProduct
import com.alfrosoft.kwiqshopapp.utils.Constants
import com.alfrosoft.kwiqshopapp.utils.GlideLoader
import java.text.SimpleDateFormat
import java.util.*


class SoldProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySoldProductDetailsBinding
    private  var mSoldProductDetails : SoldProduct? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoldProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpActionBar()

        if (intent.hasExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)){
            mSoldProductDetails = intent.getParcelableExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)
            setUpUI(mSoldProductDetails!!)
        }
    }

    private fun setUpActionBar(){
        setSupportActionBar(binding.toolbarSoldProductDetailsActivity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_button_white_24)

        binding.toolbarSoldProductDetailsActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun setUpUI(productDetails : SoldProduct){

        binding.tvOrderDetailsId.text = productDetails.title

        val dateFormat = "dd MMM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = productDetails.orderDate
        val orderDateTime = formatter.format(calendar.time)

        binding.tvOrderDetailsDate.text = orderDateTime

        GlideLoader(this).loadProductPicture(productDetails.image,binding.ivProductItemImage)
        binding.tvProductItemName.text = productDetails.title
        binding.tvProductItemPrice.text = "$ ${productDetails.price}"
        binding.tvSoldProductQuantity.text = productDetails.soldQuantity


        binding.tvMyOrderDetailsAddressType.text = productDetails.address.type
        binding.tvMyOrderDetailsFullName.text = productDetails.address.name
        binding.tvMyOrderDetailsAddress.text = "${productDetails.address.address} , ${productDetails.address.zipcode}"
        binding.tvMyOrderDetailsAdditionalNote.text = productDetails.address.additionalNote

        if (productDetails.address.otherDetails.isNotEmpty()){
            binding.tvMyOrderDetailsOtherDetails.visibility = View.VISIBLE
            binding.tvMyOrderDetailsOtherDetails.text = productDetails.address.otherDetails
        }else{
            binding.tvMyOrderDetailsOtherDetails.visibility = View.GONE
        }
        binding.tvMyOrderDetailsMobileNumber.text = productDetails.address.mobileNumber

        binding.tvOrderDetailsSubTotal.text = productDetails.subTotalAmount
        binding.tvOrderDetailsShippingCharge.text = productDetails.shippingCharge
        binding.tvOrderDetailsTotalAmount.text = productDetails.totalAmount
    }
}