package ir.co.ahs.app.mob.WinnerMarketing;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
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

public class DashBoardActivity extends AppCompatActivity implements FragmentNavigation.OnFragmentInteractionListener {

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
    private ArrayList<Struct> officialEventList = new ArrayList<>();
    private ArrayList<String> Dates = new ArrayList<>();
    private LinearLayoutManager officialEventManager;
    private Adapter_Recycler officialEventAdapter;
    private DrawerLayout drawer;
    private static TextView toolbar_main_tv_header;
    private static ImageView btnAdd;
    private TextView numberOfMessages;
    private TextView numberOfCustomers;
    private TextView number_of_finished_projects;
    private TextView number_of_current_projects;
    private TextView number_of_cancelled_projects;
    private TextView month_salary;
    private TextView year_salary;
    private Dialog dialog_guid;
    private TextView dialog_guid_description;
    private TextView dialog_guid_verCode;
    private TextView dialog_guid_verNo;
    private TextView dialog_guid_date;
    private TextView dialog_guid_exit;
    private String downloadUrl;
    private TextView dialog_guid_download;

    @Override
    protected void onResume() {
        super.onResume();
        G.ACTIVITY_NAME = this.getClass().getSimpleName();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        // Dialog
        dialog_guid = new Dialog(DashBoardActivity.this);
        dialog_guid.setContentView(R.layout.dialog_update);
//        dialog_guid_title = (TextView) dialog_guid.findViewById(R.id.description);
        dialog_guid_exit = (TextView) dialog_guid.findViewById(R.id.exit);
        dialog_guid_description = (TextView) dialog_guid.findViewById(R.id.description);
        dialog_guid_verCode = (TextView) dialog_guid.findViewById(R.id.verNo);
        dialog_guid_verNo = (TextView) dialog_guid.findViewById(R.id.verCode);
        dialog_guid_date = (TextView) dialog_guid.findViewById(R.id.date);
        dialog_guid_download = (TextView) dialog_guid.findViewById(R.id.download);
        dialog_guid_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_guid.dismiss();
            }
        });
        dialog_guid_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = downloadUrl;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        if (G.isNetworkAvailable()) {

            getDashboardData();
        } else {
            Toast.makeText(G.CONTEXT, "اتصال با اینترنت قطع شده است", Toast.LENGTH_SHORT).show();
        }

        G.IS_REGISTERED.edit().putBoolean("IS_REGISTERED", true).apply();

        numberOfMessages = (TextView) findViewById(R.id.number_of_messages);
        numberOfCustomers = (TextView) findViewById(R.id.number_of_customers);
        number_of_finished_projects = (TextView) findViewById(R.id.number_of_finished_projects);
        number_of_current_projects = (TextView) findViewById(R.id.number_of_current_projects);
        number_of_cancelled_projects = (TextView) findViewById(R.id.number_of_cancelled_projects);
        month_salary = (TextView) findViewById(R.id.month_salary);
        year_salary = (TextView) findViewById(R.id.year_salary);

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
        btnAdd = (ImageView) findViewById(R.id.btnAdd);


        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });

        RecyclerView officialEventRecycler = (RecyclerView) findViewById(R.id.list);
        officialEventManager = new LinearLayoutManager(getApplicationContext());
        officialEventAdapter = new Adapter_Recycler(getApplicationContext(), officialEventList, new OnItemListener() {
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


            }

            @Override
            public void onEdit(int position) {

            }
        }, 1, false);
        officialEventRecycler.setAdapter(officialEventAdapter);
        officialEventRecycler.setLayoutManager(officialEventManager);
    }

    private void getDashboardData() {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
        String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");

        new getDashboard().execute(Urls.BASE_URL + Urls.GET_DASHBOARD_INFO, sessionId, userToken, customerId);

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

    private class getDashboard extends Webservice.getDashboard {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String result) {
            String x = result;
            try {
                JSONObject data = new JSONObject(result);
                String CustomersCount = data.getString("CustomersCount");
                String NewMessageCount = data.getString("NewMessageCount");
                String ProjectsTotalCount = data.getString("ProjectsTotalCount");
                String ProjectsDoingCount = data.getString("ProjectsDoingCount");
                String ProjectsFinishedCount = data.getString("ProjectsFinishedCount");
                String ProjectsCanceledCount = data.getString("ProjectsCanceledCount");
                String IncomeTotal = data.getString("IncomeTotal");
                String IncomeCurrentMonth = data.getString("IncomeCurrentMonth");
                JSONArray jsonArray = data.getJSONArray("LastProjectsLits");

                numberOfMessages.setText(NewMessageCount);
                numberOfCustomers.setText(CustomersCount);
                number_of_cancelled_projects.setText(ProjectsCanceledCount);
                number_of_finished_projects.setText(ProjectsFinishedCount);
                number_of_current_projects.setText(ProjectsDoingCount);
                month_salary.setText(IncomeCurrentMonth);
                year_salary.setText(IncomeTotal);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Struct struct = new Struct();
                    struct.strItemTitle = object.getString("BuyerName");
                    struct.strItemDate = object.getString("DoingDate");
                    struct.strItemTime = object.getString("DoingTime");
                    officialEventList.add(struct);
                }
                officialEventAdapter.notifyDataSetChanged();



                checkForNewVersion();

            } catch (JSONException e) {


            }

        }

    }
    private void checkForNewVersion (){
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
        Log.d("session",sessionId);
        Log.d("token",userToken);
        String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");

        new getUpdate().execute(Urls.BASE_URL + Urls.HasNewVersionReleased, sessionId, userToken,G.APP_VERSION, String.valueOf(G.APP_VERSION_CODE),"1");
    }
    private class getUpdate extends Webservice.getUpdates {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String result) {
            String x = result;

            try {
                JSONObject data = new JSONObject(result);
                Boolean hasNewVersionReleased = data.getBoolean("hasNewVersionReleased");

                if (hasNewVersionReleased){



                    String versionName = data.getString("versionName");
                    String versionCode = data.getString("versionCode");
                    String releaseDate = data.getString("releaseDate");
                    downloadUrl = data.getString("downloadUrl");
                    String description = data.getString("description");
                    String appFileSize = data.getString("appFileSize");

                    dialog_guid_description.setText(description);
                    dialog_guid_verNo.setText(versionName);
                    dialog_guid_verCode.setText(versionCode);
                    dialog_guid_date.setText(releaseDate);

                    dialog_guid.show();

                }else {

                }


            } catch (JSONException e) {


            }

        }

    }
}
