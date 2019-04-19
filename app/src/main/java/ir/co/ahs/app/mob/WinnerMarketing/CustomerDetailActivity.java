package ir.co.ahs.app.mob.WinnerMarketing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by imac on 2/25/18.
 */

public class CustomerDetailActivity extends Activity {
    private Adapter_Recycler adapter_recycler;
    private TextView name;
    private TextView connector;
    private TextView cellphone;
    private TextView tel;
    private TextView fax;
    private TextView email;
    private TextView website;
    private TextView city;
    private TextView address;
    private TextView status;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private Dialog dialog_verify;
    private Dialog dialog_phone;
    ArrayList<Struct> arrayList = new ArrayList<>();
    private Dialog dialog_credit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        //Toolbar
        ImageView btnNavigation = (ImageView) findViewById(R.id.btnNavigation);
        TextView toolbar_main_tv_header = (TextView) findViewById(R.id.toolbar_main_tv_header);
        toolbar_main_tv_header.setText("مشخصات مشتری");
        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (G.isNetworkAvailable()) {

            getInformation();
        } else {
            Toast.makeText(G.CONTEXT, "اتصال با اینترنت قطع شده است", Toast.LENGTH_SHORT).show();
        }

        name = (TextView) findViewById(R.id.name);
        connector = (TextView) findViewById(R.id.connector);
        cellphone = (TextView) findViewById(R.id.cellphone);
        tel = (TextView) findViewById(R.id.tel);
        fax = (TextView) findViewById(R.id.fax);
        email = (TextView) findViewById(R.id.email);
        website = (TextView) findViewById(R.id.website);
        city = (TextView) findViewById(R.id.city);
        address = (TextView) findViewById(R.id.address);
        status = (TextView) findViewById(R.id.status);

    }


    public void getInformation() {
        Intent intent_data = getIntent();
        String ID = intent_data.getStringExtra("id");
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
        String customerID = G.CUSTOMER_ID.getString("CUSTOMER_ID", "nothing");
        new getCustomerDetail().execute(Urls.BASE_URL + Urls.GET_BUYER_DETAIL, sessionId, userToken, customerID, ID);
    }

    private class getCustomerDetail extends Webservice.getCustomerDetail {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {


            try {

                JSONObject mainObject = new JSONObject(result);
                String result_name = mainObject.getString("Name");
                String result_ConnectorPerson = mainObject.getString("ConnectorPerson");
                String result_Cellphone = mainObject.getString("Cellphone");
                String result_Tel = mainObject.getString("Tel");
                String result_Fax = mainObject.getString("Fax");
                String result_Email = mainObject.getString("Email");
                String result_Website = mainObject.getString("Website");
                String result_Address = mainObject.getString("Address");
                int result_Status = mainObject.getInt("Status");

                name.setText(result_name);
                connector.setText(result_ConnectorPerson);
                cellphone.setText(result_Cellphone);
                tel.setText(result_Tel);
                fax.setText(result_Fax);
                email.setText(result_Email);
                website.setText(result_Website);
                address.setText(result_Address);

                if (result_Status==1){
                   status.setText("تایید شده");
                }else if(result_Status==0){
                    status.setText("در حال بررسی");

                }else {
                    status.setText("تایید نشده");

                }

            } catch (JSONException e) {


            }


        }
    }
}
