package com.webomax.openai.Subscription

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.webomax.openai.R
import games.moisoni.google_iab.BillingConnector
import games.moisoni.google_iab.BillingEventListener
import games.moisoni.google_iab.enums.ErrorType
import games.moisoni.google_iab.models.BillingResponse
import games.moisoni.google_iab.models.PurchaseInfo
import games.moisoni.google_iab.models.SkuInfo

class SubsMainActivity : AppCompatActivity() {
   lateinit var exit_app:ImageView
    private lateinit var billingConnector: BillingConnector
    private lateinit var Preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private  var  consumableIds: MutableList<String> = ArrayList()
    private lateinit var purchaseItemDisplay : ArrayList<String>
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var listView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subs_main)
        exit_app = findViewById(R.id.exit_app)
        exit_app.setOnClickListener(View.OnClickListener { v: View? ->
            onBackPressed()
            Preferences = getSharedPreferences("subs", MODE_PRIVATE)
            editor = Preferences.edit()
            if (!Preferences.getBoolean("isPremium", false)) {
                Toast.makeText(this, "Show ads", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "don't show ads", Toast.LENGTH_SHORT).show()
            }
        })
        listView = findViewById(R.id.list_view)
        arrayAdapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_list_item_1,
            purchaseItemDisplay
        )
        listView.setAdapter(arrayAdapter)
        arrayAdapter.notifyDataSetChanged()
        initializeBillingClient()
        listView.setOnItemClickListener(OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            billingConnector.purchase(
                this@SubsMainActivity,
                consumableIds[position]
            )
        })
    }

    private fun initializeBillingClient() {
        consumableIds.add("weekly")
        consumableIds.add("monthly")
        consumableIds.add("yearly")
        billingConnector = BillingConnector(
            applicationContext,
            R.string.license_key.toString()
        ) //”license_key” – public developer key from Play Console
            .setConsumableIds(consumableIds) //to set consumable ids – call only for consumable products
            .autoAcknowledge() //legacy option – better call this. Alternatively purchases can be acknowledge via public method “acknowledgePurchase(PurchaseInfo purchaseInfo)”
            .autoConsume() //legacy option – better call this. Alternatively purchases can be consumed via public method consumePurchase(PurchaseInfo purchaseInfo)”
            .enableLogging() //to enable logging for debugging throughout the library – this can be skipped
            .connect() //to connect billing client with Play Console
        billingConnector.setBillingEventListener(object : BillingEventListener {
            override fun onProductsFetched(skuDetails: List<SkuInfo>) {
                notifyList(skuDetails)
            }

            override fun onPurchasedProductsFetched(purchases: List<PurchaseInfo>) {}
            override fun onProductsPurchased(purchases: List<PurchaseInfo>) {
                editor.putBoolean("isPremium", true)
                editor.apply()
                var skuName: String
                for (purchaseInfo in purchases) {
                    skuName = purchaseInfo.skuInfo.title
                    Toast.makeText(
                        applicationContext,
                        "Product purchased : $skuName",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onPurchaseAcknowledged(purchase: PurchaseInfo) {
                /*
                 * Grant user entitlement for NON-CONSUMABLE products and SUBSCRIPTIONS here
                 *
                 * Even though onProductsPurchased is triggered when a purchase is successfully made
                 * there might be a problem along the way with the payment and the purchase won’t be acknowledged
                 *
                 * Google will refund users purchases that aren’t acknowledged in 3 days
                 *
                 * To ensure that all valid purchases are acknowledged the library will automatically
                 * check and acknowledge all unacknowledged products at the startup
                 * */
                val acknowledgedSku = purchase.sku
                Log.d("BillingConnector", "Acknowledged: $acknowledgedSku")
                Toast.makeText(
                    applicationContext,
                    "Acknowledged : $acknowledgedSku",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onPurchaseConsumed(purchase: PurchaseInfo) {
                /*
                 * CONSUMABLE products entitlement can be granted either here or in onProductsPurchased
                 * */
                val consumedSkuTitle = purchase.skuInfo.title
                Log.d("BillingConnector", "Consumed: $consumedSkuTitle")
                Toast.makeText(
                    applicationContext,
                    "Consumed : $consumedSkuTitle",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onBillingError(
                billingConnector: BillingConnector,
                response: BillingResponse
            ) {
                when (response.errorType) {
                    ErrorType.CLIENT_NOT_READY -> {}
                    ErrorType.CLIENT_DISCONNECTED -> {}
                    ErrorType.SKU_NOT_EXIST -> {}
                    ErrorType.CONSUME_ERROR -> {}
                    ErrorType.ACKNOWLEDGE_ERROR -> {}
                    ErrorType.ACKNOWLEDGE_WARNING -> {}
                    ErrorType.FETCH_PURCHASED_PRODUCTS_ERROR -> {}
                    ErrorType.BILLING_ERROR -> {}
                    ErrorType.USER_CANCELED -> {}
                    ErrorType.SERVICE_UNAVAILABLE -> {}
                    ErrorType.BILLING_UNAVAILABLE -> {}
                    ErrorType.ITEM_UNAVAILABLE -> {}
                    ErrorType.DEVELOPER_ERROR -> {}
                    ErrorType.ERROR -> {}
                    ErrorType.ITEM_ALREADY_OWNED -> {
                        editor.putBoolean("isPremium", true)
                        editor.apply()
                        Toast.makeText(
                            this@SubsMainActivity,
                            "You Already owned subscription",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    ErrorType.ITEM_NOT_OWNED -> {
                        editor.putBoolean("isPremium", false)
                        editor.apply()
                    }
                }
                Log.d(
                    "BillingConnector",
                    "Error type: " + response.errorType + " Response code: " + response.responseCode + " Message: " + response.debugMessage
                )
                Toast.makeText(
                    applicationContext,
                    "Error: " + response.errorType,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun notifyList(skuDetails: List<SkuInfo>) {
        var sku: String
        var title: String
        var price: String
        purchaseItemDisplay.clear()
        for (skuInfo in skuDetails) {
            sku = skuInfo.sku
            title = skuInfo.title
            price = skuInfo.price
            purchaseItemDisplay.add("$title: $price")
            //            purchaseItemDisplay.add("Donate" + ": " + price);
            arrayAdapter!!.notifyDataSetChanged()
        }
    }


}