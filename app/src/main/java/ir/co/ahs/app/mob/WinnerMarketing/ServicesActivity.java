package ir.co.ahs.app.mob.WinnerMarketing;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ServicesActivity extends AppCompatActivity implements FragmentNavigation.OnFragmentInteractionListener, CanceledFragment.OnFragmentInteractionListener, InProgressFragment.OnFragmentInteractionListener,
        ShutedDownFragment.OnFragmentInteractionListener {

    private String[] mStr_TabNames;
    private TextView events;
    private TextView events_header;
    String monthName;
    private FloatingActionButton add_event_fab;
    private Dialog dialog_event;
    private TextView dialog_edt_header;
    private EditText dialog_edt_title;
    private TextView dialog_tv_cancel;
    private TextView dialog_tv_register;
    private LinearLayoutManager localEventManager;
    private Adapter_Recycler localEventAdapter;
    private ArrayList<Struct> localEventList = new ArrayList<>();
    private ArrayList<Struct> arrayList = new ArrayList<>();
    private ArrayList<String> Dates = new ArrayList<>();
    private LinearLayoutManager officialEventManager;
    private Adapter_Recycler adapter_recycler;
    private DrawerLayout drawer;
    private static TextView toolbar_main_tv_header;
    private static ImageView btnAdd;
    private TextView list_state;
    private Dialog dialog_guid;
    private TextView dialog_guid_title;
    private TextView dialog_guid_exit;
    private TextView dialog_guid_detail;
    private TextView dialog_guid_price;
    private TextView dialog_guid_price1;
    private TextView dialog_guid_price2;

    @Override
    protected void onResume() {
        super.onResume();
        G.ACTIVITY_NAME =this.getClass().getSimpleName();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);


        // Dialog
        dialog_guid = new Dialog(ServicesActivity.this);
        dialog_guid.setContentView(R.layout.dialog_guid);
        dialog_guid_title = (TextView) dialog_guid.findViewById(R.id.dialog_title);
        dialog_guid_exit = (TextView) dialog_guid.findViewById(R.id.exit);
        dialog_guid_detail = (TextView) dialog_guid.findViewById(R.id.detail);
        dialog_guid_price = (TextView) dialog_guid.findViewById(R.id.price);
        dialog_guid_price1 = (TextView) dialog_guid.findViewById(R.id.price1);
        dialog_guid_price2 = (TextView) dialog_guid.findViewById(R.id.price2);
        dialog_guid_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_guid.dismiss();
            }
        });

        // Navigation Drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Fragment squadFragment = new FragmentNavigation();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_view, squadFragment, null);
        fragmentTransaction.commit();


        //Toolbar
        ImageView btnNavigation = (ImageView) findViewById(R.id.btnNavigation);
        toolbar_main_tv_header = (TextView) findViewById(R.id.toolbar_main_tv_header);
        list_state = (TextView) findViewById(R.id.list_state);
        btnAdd = (ImageView) findViewById(R.id.btnAdd);



        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        adapter_recycler = new Adapter_Recycler(getApplicationContext(), arrayList, new OnItemListener() {
            @Override
            public void onItemSelect(int position) {

            }

            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onItemDelete(int position) {

            }

            @Override
            public void onCardClick(int position) {

                String title = arrayList.get(position).strItemTitle;
                dialog_guid_title.setText(title);
                dialog_guid_detail.setText(arrayList.get(position).strText);
                dialog_guid_price.setText(arrayList.get(position).strItemPrice + " " + arrayList.get(position).strCurrencyName);
                dialog_guid_price1.setText(arrayList.get(position).strDiscountPercent+ " % ");
                dialog_guid_price2.setText(arrayList.get(position).strDiscountValue );
                dialog_guid.show();
            }

            @Override
            public void onEdit(int position) {

            }
        }, 25, false);
        list.setAdapter(adapter_recycler);
        list.setLayoutManager(manager);


        if (G.isNetworkAvailable()) {
            String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
            String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
            String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
            new newAsync().execute(Urls.BASE_URL + Urls.GET_PRODUCT_LIST, sessionId, userToken, customerId);

        } else {
            Toast.makeText(G.CONTEXT, "اتصال با اینترنت قطع شده است", Toast.LENGTH_SHORT).show();
            list_state.setVisibility(View.VISIBLE);
            list_state.setText("اتصال با اینترنت قطع شده است");
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        //Returns a new instance of this fragment for the given section number.

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
    }

    public static void headerTitle(String text) {
        toolbar_main_tv_header.setText(text);
    }

    public static void addButton(Boolean isAvailable) {
        if (isAvailable) {
            btnAdd.setVisibility(View.VISIBLE);
        } else {
            btnAdd.setVisibility(View.GONE);
        }
    }
    private class newAsync extends Webservice.getConversationList {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {

            try {
                String x = result;
                JSONArray products = new JSONArray(result);
                for (int i = 0; i < products.length(); i++) {
                    JSONObject product = products.getJSONObject(i);
                    Struct struct = new Struct();
                    struct.strItemId = product.getString("productId");
                    struct.strItemCode = product.getString("productCode");
                    struct.strItemTitle = product.getString("productName");
                    struct.strText = product.getString("productDetail");
                    struct.strCurrencyName = product.getString("currencyName");
                    struct.strItemPrice = product.getString("price");
                    struct.strDiscountPercent = product.getString("comissionPercent");
                    struct.strDiscountValue = product.getString("comissionValue");
                    arrayList.add(struct);
                }
                adapter_recycler.notifyDataSetChanged();

                if (arrayList.size()<1){
                    list_state.setVisibility(View.VISIBLE);
                }else {
                    list_state.setVisibility(View.GONE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
