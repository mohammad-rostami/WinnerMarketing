package ir.co.ahs.app.mob.WinnerMarketing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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

public class ProjectActivity extends Activity {

    private Dialog dialog_verify;
    private Dialog dialog_phone;
    ArrayList<Struct> arrayList = new ArrayList<>();
    private Dialog dialog_credit;
    private Adapter_Recycler adapter_recycler;
    private EditText editText;
    private TextView toolbar_main_tv_header;
    private ImageView send;
    public String conversationId = null;
    public String title = null;
    private RecyclerView list;
    private RecyclerView project_list;
    private Adapter_Recycler project_adapter_recycler;
    private ArrayList<Struct> project_arrayList = new ArrayList<>();
    private TextView state;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);


        //Toolbar
        ImageView btnNavigation = (ImageView) findViewById(R.id.btnNavigation);
        toolbar_main_tv_header = (TextView) findViewById(R.id.toolbar_main_tv_header);
        state = (TextView) findViewById(R.id.state);


        Intent intent_data = getIntent();
        String object = intent_data.getStringExtra("object");
        try {
            JSONObject object1 = new JSONObject(object);
            JSONArray jsonArray = object1.getJSONArray("ProductIdList");
            conversationId = object1.getString("ProjectId");
            toolbar_main_tv_header.setText(object1.getString("BuyerName"));


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject f = jsonArray.getJSONObject(i);
                Struct struct = new Struct();
                struct.strText = f.getString("ProductName");
                struct.strNo = String.valueOf(f.getInt("Quantity"));
                project_arrayList.add(struct);
            }


            if (G.isNetworkAvailable()) {

                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
                String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                new AsyncConversation().execute(Urls.BASE_URL + Urls.GET_PROJECT_DETAIL, sessionId, userToken, customerId, conversationId);
            } else {
                Toast.makeText(G.CONTEXT, "اتصال با اینترنت قطع شده است", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrate);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                toolbar_main_tv_header.setHint("عنوان گفتگو را وارد کنید");
                toolbar_main_tv_header.setHintTextColor(getResources().getColor(R.color.Red));
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        send = (ImageView) findViewById(R.id.send);
        send.setEnabled(false);
        send.setColorFilter(getResources().getColor(R.color.lightGray));
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toolbar_main_tv_header.setEnabled(false);
                Toast.makeText(ProjectActivity.this, "worked", Toast.LENGTH_SHORT).show();

                String message = editText.getText().toString();

                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
                String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");

                new Async().execute(Urls.BASE_URL + Urls.SEND_PROJECT_DETAIL, sessionId, userToken, customerId, conversationId, message);
                editText.setText("");
                list.smoothScrollToPosition(arrayList.size() + 1);

            }
        });


        editText = (EditText) findViewById(R.id.new_message);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    send.setColorFilter(getResources().getColor(R.color.colorPrimary));
                    send.setEnabled(true);

                } else {
                    send.setColorFilter(getResources().getColor(R.color.lightGray));
                    send.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Toast.makeText(ProjectActivity.this, "sadsa", Toast.LENGTH_SHORT).show();
                    Struct struct = new Struct();
                    struct.strName = editText.getText().toString();
                    struct.strType = "2";
                    arrayList.add(struct);
                    adapter_recycler.notifyItemInserted(4);

                    editText.setText("");
                    handled = true;
                }
                return handled;
            }
        });


        list = (RecyclerView) findViewById(R.id.messageList);
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

            }

            @Override
            public void onEdit(int position) {

            }
        }, 27, false);
        list.setAdapter(adapter_recycler);
        list.setLayoutManager(manager);


        project_list = (RecyclerView) findViewById(R.id.appList);
        LinearLayoutManager project_manager = new LinearLayoutManager(getApplicationContext());
        project_adapter_recycler = new Adapter_Recycler(getApplicationContext(), project_arrayList, new OnItemListener() {
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
        }, 26, false);
        project_list.setAdapter(project_adapter_recycler);
        project_list.setLayoutManager(project_manager);


    }

    private class Async extends Webservice.sendProjectDetail {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
//            try {
//                JSONArray jsonArray = new JSONArray(result);
//                JSONObject jsonObject = jsonArray.getJSONObject(0);
//                conversationId = jsonObject.getString("conversationId");

                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
                String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                new AsyncConversation().execute(Urls.BASE_URL + Urls.GET_PROJECT_DETAIL, sessionId, userToken, customerId, conversationId);

//            } catch (JSONException e) {
//                e.printStackTrace();
//            }


        }
    }

    private class AsyncConversation extends Webservice.getProjectDetail {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            arrayList.clear();
            try {
                JSONArray jsonArray = new JSONArray(result);
//                JSONArray jsonArray = jsonObject.getJSONArray("messageList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject newObject = jsonArray.getJSONObject(i);
                    String senderName = newObject.getString("SenderName");
//                    String receiverName = newObject.getString("receiverName");
                    String message = newObject.getString("DetailMessage");
//                    String sendTime = newObject.getString("sendTime");
                    String sendDate = newObject.getString("SendDateTime");
                    Boolean amIsender = newObject.getBoolean("amISender");

                    Struct struct = new Struct();
                    struct.strName = senderName;
                    struct.strReceiver = "";
                    struct.strText = message;
                    struct.strDate = sendDate;
                    struct.strTime = "";
                    struct.sender = amIsender;

                    arrayList.add(struct);
                }

                adapter_recycler.notifyDataSetChanged();
                if (arrayList.size()==0){
                    state.setVisibility(View.VISIBLE);
                }else {
                    state.setVisibility(View.GONE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
