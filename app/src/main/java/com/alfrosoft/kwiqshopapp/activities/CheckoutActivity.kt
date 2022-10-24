package com.alfrosoft.kwiqshopapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.alfrosoft.kwiqshopapp.R
import com.alfrosoft.kwiqshopapp.activities.ui.adapters.CartItemsListAdapter
import com.alfrosoft.kwiqshopapp.databinding.ActivityCheckoutBinding
import com.alfrosoft.kwiqshopapp.firestore.FireStoreClass
import com.alfrosoft.kwiqshopapp.model.Address
import com.alfrosoft.kwiqshopapp.model.CartItem
import com.alfrosoft.kwiqshopapp.model.Order
import com.alfrosoft.kwiqshopapp.model.Product
import com.alfrosoft.kwiqshopapp.utils.Constants


class CheckoutActivity : BaseActivity() {

    private lateinit var binding: ActivityCheckoutBinding

    private lateinit var mProductList: ArrayList<Product>
    private lateinit var mCartItemsList: ArrayList<CartItem>
    private var mSelectedAddressDetails: Address? = null
    private var mSubTotal : Double  = 0.0
    private var mTotalAmount : Double  = 0.0
    private lateinit var mOrderDetails : Order

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpActionBar()

        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)) {
            mSelectedAddressDetails = intent.getParcelableExtra(Constants.EXTRA_SELECTED_ADDRESS)
        }
        if (mSelectedAddressDetails != null) {
            binding.tvCheckoutAddress.text =
                "${mSelectedAddressDetails!!.address}, ${mSelectedAddressDetails!!.zipcode}"
            binding.tvCheckoutAdditionalNote.text = mSelectedAddressDetails!!.additionalNote
            binding.tvCheckoutAddressType.text = mSelectedAddressDetails!!.type
            binding.tvCheckoutFullName.text = mSelectedAddressDetails!!.name
            if (mSelectedAddressDetails!!.otherDetails.isNotEmpty()) {
                binding.tvCheckoutOtherDetails.text = mSelectedAddressDetails!!.otherDetails
            } else {
                binding.tvCheckoutOtherDetails.visibility = View.GONE
            }
            binding.tvMobileNumber.text = mSelectedAddressDetails!!.mobileNumber
        }
        getProductList()

        binding.btnPlaceOrder.setOnClickListener{
            placeAnOrder()
        }
    }

    private fun getProductList() {
        showProgressDialog(getString(R.string.please_wait))
        FireStoreClass().getAllProductList(this@CheckoutActivity)
    }

    fun successProductListFromFirestore(productList: ArrayList<Product>) {
        mProductList = productList
        getCartItemsList()
    }

    private fun getCartItemsList() {
        FireStoreClass().getCartList(this@CheckoutActivity)
    }
    private fun placeAnOrder(){
        showProgressDialog(getString(R.string.please_wait))

        if (mSelectedAddressDetails != null){
            mOrderDetails = Order(
                FireStoreClass().getCurrentUserId(),
                mCartItemsList,
                mSelectedAddressDetails!!,
                "My Order ${System.currentTimeMillis()}",
                mCartItemsList[0].image,
                mSubTotal.toString(),
                "100.00",
                mTotalAmount.toString(),
                System.currentTimeMillis()
            )

            FireStoreClass().placeOrder(this@CheckoutActivity,mOrderDetails)
        }
    }

    @SuppressLint("SetTextI18n")
    fun successCartItemsList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()

        for (product in mProductList) {
            for (cartItem in cartList) {
                if (product.productId == cartItem.productId) {
                    cartItem.stockQuantity = product.stockQuantity
                }

            }
        }
        mCartItemsList = cartList

        binding.rvCartListItems.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        binding.rvCartListItems.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this, mCartItemsList, false)
        binding.rvCartListItems.adapter = cartListAdapter

        for(item in mCartItemsList){
            val availableQuantity = item.stockQuantity.toInt()
            if (availableQuantity > 0){
                val quantity = item.cartQuantity.toInt()
                val price = item.price.toInt()
                //calculating subtotal

                mSubTotal += (price * quantity)
            }
        }
        binding.tvCheckoutSubTotal.text = "$${mSubTotal}"
        //we can use our own logic here in shipping charges
        binding.tvCheckoutShippingCharge.text = "$10.0"

        if (mSubTotal > 0){
            binding.llCheckoutPlaceOrder.visibility = View.VISIBLE

            mTotalAmount = mSubTotal + 10.0

            binding.tvCheckoutTotalAmount.text = "$${mTotalAmount}"
        }else{
            binding.llCheckoutPlaceOrder.visibility = View.GONE
        }


    }

    private fun setUpActionBar() {

        setSupportActionBar(binding.toolbarCheckoutActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_button_white_24)

        binding.toolbarCheckoutActivity.setNavigationOnClickListener {
            onBackPressed()
        }

    }
    fun orderPlaceSuccess(){
        FireStoreClass().updateAllDetails(this,mCartItemsList,mOrderDetails)

    }
    fun allDetailsUpdatedSuccessfully(){
        hideProgressDialog()
        Toast.makeText(this, "Your order was placed successfully.", Toast.LENGTH_SHORT).show()

        val intent = Intent(this@CheckoutActivity,DashboardActivity::class.java)
        //to clear the stack of activities or layer of activities and open the dashboard activity
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()

    }
}