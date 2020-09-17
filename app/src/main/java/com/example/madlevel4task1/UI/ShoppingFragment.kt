package com.example.madlevel4task1.UI

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel4task1.Model.Product
import com.example.madlevel4task1.R
import com.example.madlevel4task1.Repository.ProductRepository
import kotlinx.android.synthetic.main.fragment_shopping.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ShoppingFragment : Fragment() {

    private lateinit var productRepository: ProductRepository
    private val productsList = arrayListOf<Product>()
    private val productAdapter = ProductAdapter(productsList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productRepository = ProductRepository(requireContext())
        getProductsFromDatabase()

        initViews()

        fb_add_product.setOnClickListener {
            showAddProductdialog();
        }

        fb_remove_all.setOnClickListener {
            removeAllProducts()
        }
    }

    private fun initViews() {
        rv_shopping_list.layoutManager = LinearLayoutManager(activity)
        rv_shopping_list.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        createItemTouchHelper().attachToRecyclerView(rv_shopping_list)

        rv_shopping_list.apply {
            setHasFixedSize(true)
            layoutManager = rv_shopping_list.layoutManager
            adapter = productAdapter
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {
        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val productToDelete = productsList[position]
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        productRepository.deleteProduct(productToDelete)
                    }
                    getProductsFromDatabase()
                }
            }
        }
        return ItemTouchHelper(callback)
    }

    private fun getProductsFromDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            val shoppingList = withContext(Dispatchers.IO) {
                productRepository.getAllProducts()
            }
            this@ShoppingFragment.productsList.clear()
            this@ShoppingFragment.productsList.addAll(shoppingList)
            this@ShoppingFragment.productAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("InflateParams")
    private fun showAddProductdialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.add_product_dialog_title))
        val dialogLayout = layoutInflater.inflate(R.layout.add_product_dialog, null)
        val productName = dialogLayout.findViewById<EditText>(R.id.et_product_name)
        val amount = dialogLayout.findViewById<EditText>(R.id.et_amount)

        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.dialog_ok_btn) { _: DialogInterface, _: Int ->
            addProduct(productName, amount)
        }
        builder.show()
    }

    private fun addProduct(txtProductName: EditText, txtAmount: EditText) {
        if (validateFields(txtProductName, txtAmount)) {
            CoroutineScope(Dispatchers.Main).launch {
                val product = Product(
                    productName = txtProductName.text.toString(),
                    amount = txtAmount.text.toString().toInt()
                )

                withContext(Dispatchers.IO) {
                    productRepository.insertProduct(product)
                }

                getProductsFromDatabase()
            }
        }
    }

    private fun validateFields(
        txtProductName: EditText, txtAmount: EditText
    ): Boolean {
        return if (txtProductName.text.toString().isNotBlank()
            && txtAmount.text.toString().isNotBlank()
        ) {
            true
        } else {
            Toast.makeText(activity, "Please fill in the fields", Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun removeAllProducts() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                productRepository.deleteAllProducts()
            }
            getProductsFromDatabase()
        }
    }
}
