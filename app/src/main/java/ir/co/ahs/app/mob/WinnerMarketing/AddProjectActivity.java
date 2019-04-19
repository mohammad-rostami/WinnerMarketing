package ir.co.ahs.app.mob.WinnerMarketing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import ir.co.ahs.app.mob.WinnerMarketing.Helper.DataBaseWriter_Conversation;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by imac on 2/25/18.
 */

public class AddProjectActivity extends Activity {

    private Dialog dialog_verify;
    private Dialog dialog_phone;
    static ArrayList<Struct> arrayList = new ArrayList<>();
    static ArrayList<Struct> customerArrayList = new ArrayList<>();
    private Dialog dialog_credit;
    private static Adapter_Recycler_Select adapter_recycler;
    private EditText editText;
    private static TextView state;
    private Dialog dialog_guid;
    private TextView dialog_guid_title;
    private TextView dialog_guid_exit;
    private TextView nameSpinner;
    private TextView dialog_guid_detail;
    private TextView dialog_guid_price;
    private Dialog dialog_customer;
    private RecyclerView dialog_customer_list;
    private TextView dialog_customer_exit;
    private Adapter_Recycler customer_adapter_recycler;
    private String selectedID;
    ArrayList<String> itemIDs = new ArrayList();
    ArrayList<Integer> itemQuantities = new ArrayList();
    private ArrayList<String> identityArray = new ArrayList<>();
    private ArrayList<Boolean> items = new ArrayList<>();
    private boolean anyItemRemoved = false;
    boolean customerSelected = false;
    private TextView register;
    private JSONArray jsonArray;
    public static ArrayList<String> selected = new ArrayList<>();
    private TextView dialog_guid_price1;
    private TextView dialog_guid_price2;
    private TextView list_state;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getConversations();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        register = (TextView) findViewById(R.id.btn_register);
        list_state = (TextView) findViewById(R.id.list_state);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!customerSelected && identityArray.size() == 0) {
                    Toast.makeText(AddProjectActivity.this, "مشتری و محصول را انتخاب کنید", Toast.LENGTH_SHORT).show();
                } else if (identityArray.size() == 0) {
                    Toast.makeText(AddProjectActivity.this, " محصول را انتخاب کنید", Toast.LENGTH_SHORT).show();
                } else if (!customerSelected) {
                    Toast.makeText(AddProjectActivity.this, "مشتری را انتخاب کنید", Toast.LENGTH_SHORT).show();
                } else {
                    jsonArray = new JSONArray();
                    for (int i = 0; i < identityArray.size(); i++) {
                        String currentString = String.valueOf(identityArray.get(i));
                        String[] separated = currentString.split(":");
                        String id = separated[0];
                        String quantity = separated[1];
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("ProductId", id);
                            jsonObject.put("Quantity", quantity);
                            jsonArray.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                    if (G.isNetworkAvailable()) {
                        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
                        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
                        String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                        new AsyncProject().execute(Urls.BASE_URL + Urls.ADD_PROJECT, sessionId, userToken, customerId, selectedID, String.valueOf(jsonArray));
                    } else {
                        Toast.makeText(G.CONTEXT, "اتصال با اینترنت قطع شده است", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // Dialog
        dialog_guid = new Dialog(AddProjectActivity.this);
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


        // Dialog
        dialog_customer = new Dialog(AddProjectActivity.this);
        dialog_customer.setContentView(R.layout.dialog_customer);
        dialog_customer_list = (RecyclerView) dialog_customer.findViewById(R.id.customerList);
        dialog_customer_exit = (TextView) dialog_customer.findViewById(R.id.exit);
//        dialog_customer_detail = (TextView) dialog_customer.findViewById(R.id.detail);
//        dialog_customer_price = (TextView) dialog_customer.findViewById(R.id.price);
        dialog_customer_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_guid.dismiss();
            }
        });


//        ArrayList<String> names = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            names.add("مشتری" + i);
//        }


        nameSpinner = (TextView) findViewById(R.id.name_spinner);
        nameSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_customer.show();
            }
        });

        //Toolbar
        ImageView btnNavigation = (ImageView) findViewById(R.id.btnNavigation);
        TextView toolbar_main_tv_header = (TextView) findViewById(R.id.toolbar_main_tv_header);
        toolbar_main_tv_header.setText("ثبت پروژه");
        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        state = (TextView) findViewById(R.id.state);
        editText = (EditText) findViewById(R.id.new_message);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
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

        LinearLayoutManager customerManager = new LinearLayoutManager(getApplicationContext());
        customer_adapter_recycler = new Adapter_Recycler(getApplicationContext(), customerArrayList, new OnItemListener() {
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
                dialog_customer.dismiss();
                nameSpinner.setText(customerArrayList.get(position).strName);
                selectedID = customerArrayList.get(position).strId;
                customerSelected = true;

            }

            @Override
            public void onEdit(int position) {

            }
        }, 24, false);
        dialog_customer_list.setAdapter(customer_adapter_recycler);
        dialog_customer_list.setLayoutManager(customerManager);


        if (G.isNetworkAvailable()) {
            getCustomers();
        } else {
            Toast.makeText(G.CONTEXT, "اتصال با اینترنت قطع شده است", Toast.LENGTH_SHORT).show();
        }

        RecyclerView list = (RecyclerView) findViewById(R.id.messageList);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        adapter_recycler = new Adapter_Recycler_Select(getApplicationContext(), arrayList, new OnItemSelector() {

            @Override
            public void onItemSelect(int position, String string) {
                String ID = arrayList.get(position).strItemId;
                String Quantity = string;
                String identity = ID + ":" + Quantity;
                for (int i = 0; i < identityArray.size(); i++) {
                    if (identityArray.get(i).contains(ID)) {
                        identityArray.remove(i);
                        anyItemRemoved = true;
                    }
                }

                if (!anyItemRemoved) {
                    identityArray.add(identity);
                }

                Log.d("array size", String.valueOf(identityArray.size()));
                for (int i = 0; i < identityArray.size(); i++) {
                    Log.d("array item", String.valueOf(identityArray.get(i)));
                }
                anyItemRemoved = false;

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
            public void onEdit(int position, String no) {
                String ID = arrayList.get(position).strItemId;
                String Quantity = no;
                String identity = ID + ":" + Quantity;
                for (int i = 0; i < identityArray.size(); i++) {
                    if (identityArray.get(i).contains(ID)) {
                        identityArray.remove(i);
                    }
                }

                identityArray.add(identity);

                Log.d("array size", String.valueOf(identityArray.size()));
                for (int i = 0; i < identityArray.size(); i++) {
                    Log.d("array item", String.valueOf(identityArray.get(i)));
                }

            }
        }, 22, false);
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

    private class Async extends Webservice.getConversationList {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            clearDateBase("CONVERSATION_TABLE");
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject newObject = jsonArray.getJSONObject(i);
                    String conversationId = newObject.getString("conversationId");
                    String title = newObject.getString("title");
                    int unread = newObject.getInt("unReadMessageCount");
                    String senderName = newObject.getString("senderName");
                    String receiverName = newObject.getString("receiverName");
                    String message = newObject.getString("lastMessage");
                    String sendTime = newObject.getString("theTime");
                    String sendDate = newObject.getString("theDate");

                    addToDatabase("CONVERSATION_TABLE", conversationId, title, String.valueOf(unread), senderName, receiverName, message, sendDate, sendTime);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            recyclerInitializer("CONVERSATION_TABLE");

        }
    }

    public static void cartStateCheck() {
        System.out.println(String.valueOf(arrayList.size()));
        if (arrayList.size() == 0) {
            state.setVisibility(View.VISIBLE);

        } else {
            state.setVisibility(View.GONE);


        }
    }

    private static void addToDatabase(String TableName, String conversationId, String title, String unReadMessageCount, String senderName, String receiverName, String lastMessage, String theDate, String theTime) {
        DataBaseWriter_Conversation helper = new DataBaseWriter_Conversation(G.CONTEXT, "CONVERSATION_DATABASE", TableName, G.ConversationTableFieldNames, G.ConversationTableFieldTypes);
        try {
            helper.sampleMethod(new String[]{
                    conversationId,
                    title,
                    unReadMessageCount,
                    senderName,
                    receiverName,
                    lastMessage,
                    theDate,
                    theTime
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void recyclerInitializer(String TableName) {
        arrayList.clear();
        try {
            adapter_recycler.notifyDataSetChanged();
        } catch (Exception e) {
        }
        DataBaseWriter_Conversation helper = new DataBaseWriter_Conversation(G.CONTEXT, "CONVERSATION_DATABASE", TableName, G.ConversationTableFieldNames, G.ConversationTableFieldTypes);
        try {
            helper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = helper.getWritableDatabase();

        Cursor cursor = sqld.rawQuery("select * from " + TableName, null);
        String conversationId;
        String title;
        int unread;
        String senderName;
        String receiverName;
        String message;
        String sendDate;
        String sendTime;
        if (cursor.moveToFirst()) {
            do {
                conversationId = cursor.getString(cursor.getColumnIndex("conversationId"));
                title = cursor.getString(cursor.getColumnIndex("title"));
                unread = cursor.getInt(cursor.getColumnIndex("unReadMessageCount"));
                senderName = cursor.getString(cursor.getColumnIndex("senderName"));
                receiverName = cursor.getString(cursor.getColumnIndex("receiverName"));
                message = cursor.getString(cursor.getColumnIndex("lastMessage"));
                sendDate = cursor.getString(cursor.getColumnIndex("theDate"));
                sendTime = cursor.getString(cursor.getColumnIndex("theTime"));

                Struct struct = new Struct();
                struct.strId = conversationId;
                struct.strItemTitle = title;
                struct.intQuantity = unread;
                struct.strName = senderName;
                struct.strReceiver = receiverName;
                struct.strText = message;
                struct.strDate = sendDate;
                struct.strTime = sendTime;
                arrayList.add(struct);
            } while (cursor.moveToNext());
            try {
                adapter_recycler.notifyDataSetChanged();
                cartStateCheck();

            } catch (Exception e) {
            }
        }
        sqld.close();
    }

    public void clearDateBase(String TableName) {
        DataBaseWriter_Conversation dataBaseHelper = new DataBaseWriter_Conversation(getBaseContext(), "CONVERSATION_DATABASE", "CONVERSATION_TABLE", G.ConversationTableFieldNames, G.ConversationTableFieldTypes);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = dataBaseHelper.getWritableDatabase();
        sqld.execSQL("delete from " + TableName);


        cartStateCheck();

    }

    public void getConversations() {
        try {
            if (G.isNetworkAvailable()) {

                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
                String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                new Async().execute(Urls.BASE_URL + Urls.GET_CONVERSATION_LIST, sessionId, userToken, customerId);

            } else {
                recyclerInitializer("CONVERSATION_TABLE");
            }
        } catch (Exception e) {

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


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void getCustomers() {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
        String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
        new getCustomer().execute(Urls.BASE_URL + Urls.GET_BUYERS, sessionId, userToken, customerId, "0");
    }

    private class getCustomer extends Webservice.getBuyers {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            String x = result;

            try {
                JSONArray main = new JSONArray(result);
                if (main.length() > 0) {
//                    helper.delete();
                }
                for (int i = 0; i < main.length(); i++) {
                    JSONObject object = main.getJSONObject(i);
                    Struct struct = new Struct();
                    struct.strName = object.getString("Name");
                    struct.strId = object.getString("BuyerId");
                    struct.strStatus = object.getString("Status");
                    struct.strDate = object.getString("RegisterDate");
                    customerArrayList.add(struct);
                }
                customer_adapter_recycler.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private class AsyncProject extends Webservice.sendProject {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            Log.d("poll", result);
            try {
                JSONObject object = new JSONObject(result);
                String message = object.getString("theResult");
                Toast.makeText(AddProjectActivity.this,message , Toast.LENGTH_SHORT).show();
                finish();
                ProjectsActivity.restartActivity();
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            arrayList.clear();
//            adapter_recycler.notifyDataSetChanged();
//            getPoll();

        }
    }

}
