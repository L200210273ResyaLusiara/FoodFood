package com.catnip.foodfood.presentation.checkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.catnip.foodfood.data.FirebaseAuthDataSourceImpl
import com.catnip.foodfood.databinding.ActivityCheckoutBinding
import com.catnip.foodfood.local.database.entity.Cart
import com.catnip.foodfood.model.Order
import com.catnip.foodfood.model.OrderRequest
import com.catnip.foodfood.model.User
import com.catnip.foodfood.repository.CartRepository
import com.catnip.foodfood.presentation.fragmentcart.adapter.CartAdapter
import com.catnip.foodfood.presentation.fragmentcart.adapter.CartListener
import com.catnip.foodfood.repository.UserRepositoryImpl
import com.catnip.foodfood.utils.GenericViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import java.text.NumberFormat
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {
    private lateinit var bind: ActivityCheckoutBinding
    private var total = 0
    private var listOrder: ArrayList<Order> = arrayListOf()
    private val viewModel: CheckoutViewModel by viewModels {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repo = UserRepositoryImpl(dataSource)
        GenericViewModelFactory.create(CheckoutViewModel(CartRepository(application),repo))
    }
    private val adapter: CartAdapter by lazy {
        CartAdapter(object : CartListener {
            override fun onPlusTotalItemCartClicked(cart: Cart) {
                viewModel.increaseCart(cart)
            }
            override fun onMinusTotalItemCartClicked(cart: Cart) {
                viewModel.decreaseCart(cart)
            }
            override fun onRemoveCartClicked(cart: Cart) {
                viewModel.deleteCart(cart)
            }
            override fun onUserDoneEditingNotes(cart: Cart) {
                viewModel.updateCartNote(cart)
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setupList()
        observeData()
        setClickListener()
    }
    private fun setClickListener() {
        bind.btnCheckout
            .setOnClickListener {
                if(listOrder.isNotEmpty()){
                    val user = viewModel.getCurrentUser()
                    if (user != null) {
                        viewModel.order(OrderRequest(user.username, total, listOrder))
                    }
                }

            }
        bind.ivBack.setOnClickListener{
            finish()
        }
    }
    private fun setupList() {
        bind.rvCart.itemAnimator = null
        bind.rvCart.adapter = adapter
    }
    private fun observeData() {
        viewModel.cartList.observe(this) { result ->
            if(result.isEmpty()){
                finish()
            }
            adapter.submitData(result)
            total = 0
            listOrder = arrayListOf()
            for(data in result){
                total+=data.quantity*data.foodPrice
                listOrder.add(Order(data.foodName!!,data.quantity,data.notes?:"",data.foodPrice))
            }
            bind.tvTotalPrice.text = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(total)
        }
        viewModel.orderResult.observe(this){ result ->
            if(result.status){
                viewModel.deleteAll()
                Toast.makeText(this,result.message,Toast.LENGTH_LONG).show()
            }
        }
    }
}