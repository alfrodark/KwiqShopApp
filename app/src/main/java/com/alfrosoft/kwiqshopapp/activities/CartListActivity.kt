package com.alfrosoft.kwiqshopapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.alfrosoft.kwiqshopapp.R
import com.alfrosoft.kwiqshopapp.activities.ui.adapters.CartItemsListAdapter
import com.alfrosoft.kwiqshopapp.databinding.ActivityCartListBinding
import com.alfrosoft.kwiqshopapp.firestore.FireStoreClass
import com.alfrosoft.kwiqshopapp.model.CartItem
import com.alfrosoft.kwiqshopapp.model.Product
import com.alfrosoft.kwiqshopapp.utils.Constants


class CartListActivity : BaseActivity() {

    private lateinit var binding : ActivityCartListBinding
    private lateinit var mProductList : ArrayList<Product>
    private lateinit var mCartListItem : ArrayList<CartItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpActionBar()

        binding.btnCheckout.setOnClickListener{
            val intent = Intent(this@CartListActivity,AddressListActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS,true)
            startActivity(intent)
        }
    }
    private fun setUpActionBar(){

        setSupportActionBar(binding.toolbarCartListActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_button_white_24)

        binding.toolbarCartListActivity.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    override fun onResume() {
        super.onResume()
//        getCartItemList() // when getting product list success then we call get cart items list
        //we need to get the product list before cart items so that we know the quantity that user can buy
        getProductList()
    }

    private fun getCartItemList(){
//        showProgressDialog(getString(R.string.please_wait))
        FireStoreClass().getCartList(this)
    }
    fun itemUpdateSuccess(){
        hideProgressDialog()
        getCartItemList()
    }
    @SuppressLint("SetTextI18n")
    fun successCartItemList(cartList : ArrayList<CartItem>){
        hideProgressDialog()

        for(product in mProductList){
            for (cartItem in cartList){
                //make sure that the product in the cart
                if (product.productId == cartItem.productId){
                    //we just assign the product quantity to the cart item quantity
                    cartItem.stockQuantity = product.stockQuantity
                    if (product.stockQuantity.toInt() == 0){
                     cartItem.cartQuantity = product.stockQuantity
                    }
                }
            }
        }

        mCartListItem = cartList

        if (mCartListItem.size >0){
            binding.rvCartItemsList.visibility = View.VISIBLE
            binding.llCheckout.visibility = View.VISIBLE
            binding.tvNoCartItemFound.visibility = View.GONE

            binding.rvCartItemsList.layoutManager = LinearLayoutManager(this)
            binding.rvCartItemsList.setHasFixedSize(true)
            val adapter = CartItemsListAdapter(this, mCartListItem,true)
            binding.rvCartItemsList.adapter = adapter

            var subTotal  = 0.0
            var price = 0
            for(item in mCartListItem){
                val availableQuantity = item.stockQuantity.toInt()
                if (availableQuantity > 0) {

                    price = when {
                        item.price.contains(",") -> {
                            val index = item.price.indexOf(",")
                            val s1 = item.price.substring(0,index)
                            val s2 = item.price.substring(index+1,item.price.length)

                            (s1 + s2).toInt()
                        }
                        item.price.contains(".") -> {
                            val index = item.price.indexOf(".")
                            val s1 = item.price.substring(0,index)
                            val s2 = item.price.substring(index+1,item.price.length)

                            (s1 + s2).toInt()
                        }
                        else -> {
                            item.price.toInt()
                        }
                    }
                    val quantity = item.cartQuantity.toDouble()
                    subTotal += (price * quantity)
                }
            }
            binding.tvSubTotal.text = "$${subTotal}"
            //Change the logic accordingly
            binding.tvShippingCharge.text = "$${10}"

            if (subTotal >0 ){
                binding.llCheckout.visibility = View.VISIBLE

                val total = subTotal + 10

                binding.tvTotalAmount.text = "$${total}"
            }else{
                binding.llCheckout.visibility = View.GONE
            }
        }else{
            binding.tvNoCartItemFound.visibility = View.VISIBLE
            binding.llCheckout.visibility = View.GONE
            binding.rvCartItemsList.visibility = View.GONE
        }
    }

    fun successProductListFromFireStore(productList : ArrayList<Product>){

        mProductList = productList
        hideProgressDialog()
        //after getting product list
        getCartItemList()

    }
    private fun getProductList(){
        showProgressDialog(getString(R.string.please_wait))
        FireStoreClass().getAllProductList(this)
    }
    fun itemRemoveSuccess(){
        hideProgressDialog()
        Toast.makeText(this, getString(R.string.msg_item_removed_successfully), Toast.LENGTH_SHORT).show()

        getCartItemList()
    }

    override fun onDestroy() {
        dismissProgressDialog()
        super.onDestroy()
    }
}