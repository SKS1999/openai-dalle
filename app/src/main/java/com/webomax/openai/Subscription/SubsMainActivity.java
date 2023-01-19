package com.webomax.openai.Subscription;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.webomax.openai.Profile.DashboardActivity;
import com.webomax.openai.R;



import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import games.moisoni.google_iab.BillingConnector;
import games.moisoni.google_iab.BillingEventListener;
import games.moisoni.google_iab.models.BillingResponse;
import games.moisoni.google_iab.models.PurchaseInfo;
import games.moisoni.google_iab.models.SkuInfo;


public class SubsMainActivity extends AppCompatActivity {
    ImageView exit_app,done;
    private BillingConnector billingConnector;
    private SharedPreferences Preferences;
    private SharedPreferences.Editor editor;
    private final List<String> consumableIds = new ArrayList<>();
    private final ArrayList<String> purchaseItemDisplay = new ArrayList<>();
    private ArrayAdapter arrayAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subs_main);
        exit_app = findViewById(R.id.exit_app);
        exit_app.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubsMainActivity.this, DashboardActivity.class);
                startActivity(intent);


            Preferences =

            getSharedPreferences("subs",MODE_PRIVATE);

            editor =Preferences.edit();

            if(!Preferences.getBoolean("isPremium",false))

            {
                Toast.makeText(getApplicationContext(), "Show ads", Toast.LENGTH_SHORT).show();
            }else

            {
                Toast.makeText(getApplicationContext(), "don't show ads", Toast.LENGTH_SHORT).show();
            }
        }

        });

        listView = findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,purchaseItemDisplay);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        initializeBillingClient();
        listView.setOnItemClickListener((parent, view, position, id) -> billingConnector.purchase(SubsMainActivity.this, consumableIds.get(position)));
    }
    private void initializeBillingClient() {
        consumableIds.add("weekly");
        consumableIds.add("monthly");
        consumableIds.add("yearly");


        billingConnector = new BillingConnector(getApplicationContext(), String.valueOf(R.string.license_key)) //”license_key” – public developer key from Play Console
                .setConsumableIds(consumableIds) //to set consumable ids – call only for consumable products
                .autoAcknowledge() //legacy option – better call this. Alternatively purchases can be acknowledge via public method “acknowledgePurchase(PurchaseInfo purchaseInfo)”
                .autoConsume() //legacy option – better call this. Alternatively purchases can be consumed via public method consumePurchase(PurchaseInfo purchaseInfo)”
                .enableLogging() //to enable logging for debugging throughout the library – this can be skipped
                .connect(); //to connect billing client with Play Console

        billingConnector.setBillingEventListener(new BillingEventListener() {
            @Override
            public void onProductsFetched(@NonNull List<SkuInfo> skuDetails) {

                notifyList(skuDetails);
            }

            @Override
            public void onPurchasedProductsFetched(@NonNull List<PurchaseInfo> purchases) {

            }

            @Override
            public void onProductsPurchased(@NonNull List<PurchaseInfo> purchases) {
                editor.putBoolean("isPremium",true);
                editor.apply();
                String skuName;
                for (PurchaseInfo purchaseInfo : purchases) {
                    skuName = purchaseInfo.getSkuInfo().getTitle();
                    Toast.makeText(getApplicationContext(), "Product purchased : "+ skuName, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onPurchaseAcknowledged(@NonNull PurchaseInfo purchase) {
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

                String acknowledgedSku = purchase.getSku();
                Log.d("BillingConnector", "Acknowledged: " + acknowledgedSku);
                Toast.makeText(getApplicationContext(), "Acknowledged : " + acknowledgedSku, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPurchaseConsumed(@NonNull PurchaseInfo purchase) {
                /*
                 * CONSUMABLE products entitlement can be granted either here or in onProductsPurchased
                 * */

                String consumedSkuTitle = purchase.getSkuInfo().getTitle();
                Log.d("BillingConnector", "Consumed: " + consumedSkuTitle);
                Toast.makeText(getApplicationContext(), "Consumed : " + consumedSkuTitle, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBillingError(@NonNull BillingConnector billingConnector, @NonNull BillingResponse response) {
                switch (response.getErrorType()) {
                    case CLIENT_NOT_READY:
//TODO – client is not ready yet
                        break;
                    case CLIENT_DISCONNECTED:
//TODO – client has disconnected
                        break;
                    case SKU_NOT_EXIST:
//TODO – sku does not exist
                        break;
                    case CONSUME_ERROR:
//TODO – error during consumption
                        break;
                    case ACKNOWLEDGE_ERROR:
//TODO – error during acknowledgment
                        break;
                    case ACKNOWLEDGE_WARNING:
                        /*
                         * This will be triggered when a purchase can not be acknowledged because the state is PENDING
                         * A purchase can be acknowledged only when the state is PURCHASED
                         *
                         * PENDING transactions usually occur when users choose cash as their form of payment
                         *
                         * Here users can be informed that it may take a while until the purchase complete
                         * and to come back later to receive their purchase
                         * */
//TODO – warning during acknowledgment
                        break;
                    case FETCH_PURCHASED_PRODUCTS_ERROR:
//TODO – error occurred while querying purchased products
                        break;
                    case BILLING_ERROR:
//TODO – error occurred during initialization / querying sku details
                        break;
                    case USER_CANCELED:
//TODO – user pressed back or canceled a dialog
                        break;
                    case SERVICE_UNAVAILABLE:
//TODO – network connection is down
                        break;
                    case BILLING_UNAVAILABLE:
//TODO – billing API version is not supported for the type requested
                        break;
                    case ITEM_UNAVAILABLE:
//TODO – requested product is not available for purchase
                        break;
                    case DEVELOPER_ERROR:
//TODO – invalid arguments provided to the API
                        break;
                    case ERROR:
//TODO – fatal error during the API action
                        break;
                    case ITEM_ALREADY_OWNED:

                        editor.putBoolean("isPremium",true);
                        editor.apply();
                        Toast.makeText(SubsMainActivity.this, "You Already owned subscription", Toast.LENGTH_SHORT).show();
                        break;
                    case ITEM_NOT_OWNED:
                        editor.putBoolean("isPremium",false);
                        editor.apply();
                        break;
                }

                Log.d("BillingConnector", "Error type: " + response.getErrorType() + " Response code: "+ response.getResponseCode() + " Message: " + response.getDebugMessage());

                Toast.makeText(getApplicationContext(), "Error: "+ response.getErrorType() , Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void notifyList(List<SkuInfo> skuDetails) {
        String sku;
        String title;
        String price;

        purchaseItemDisplay.clear();
        for (SkuInfo skuInfo : skuDetails) {
            sku = skuInfo.getSku();
            title = skuInfo.getTitle();
            price = skuInfo.getPrice();

  purchaseItemDisplay.add(title + ": " + price);
//            purchaseItemDisplay.add("Donate" + ": " + price);
            arrayAdapter.notifyDataSetChanged();

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
