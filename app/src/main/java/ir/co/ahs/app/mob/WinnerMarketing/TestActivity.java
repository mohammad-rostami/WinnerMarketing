package ir.co.ahs.app.mob.WinnerMarketing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class TestActivity extends Activity {


    private Dialog dialog_verify;
    private Dialog dialog_phone;
    ArrayList<Struct> arrayList = new ArrayList<>();
    private Dialog dialog_credit;
    private Adapter_Recycler adapter_recycler;
    private EditText editText;
    private LinearLayout mainLayout;
    private Dialog dialogPoll;
    private LinearLayout dialogPoll_MainLayout;
    private TextView dialogPoll_Title;
    private String headerID;
    private Typeface typeFace_Light;
    private Typeface typeFace_Medium;
    private Typeface typeFace_Bold;
    private TextView dialogPoll_Confirm;
    private TextView dialogPoll_Cancel;
    private int size;
    private String itemsJSON;
    private Boolean isSelected;
    private String itemID = null;
    private ArrayList<String> itemIDs = new ArrayList<>();
    private JSONArray A;
    private TextView state;
    private View.OnClickListener onclicklistener;
    private String headerTitle;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        typeFace_Light = Typeface.createFromAsset(G.CONTEXT.getAssets(), "iran_light.ttf");
        typeFace_Medium = Typeface.createFromAsset(G.CONTEXT.getAssets(), "iran_light.ttf");
        typeFace_Bold = Typeface.createFromAsset(G.CONTEXT.getAssets(), "iran_bold.ttf");

        //Toolbar
        ImageView btnNavigation = (ImageView) findViewById(R.id.btnNavigation);
        TextView toolbar_main_tv_header = (TextView) findViewById(R.id.toolbar_main_tv_header);
        toolbar_main_tv_header.setText("تست روانشناسی");
        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        state = (TextView) findViewById(R.id.state);
        TextView btnSubmit = (TextView) findViewById(R.id.btnSubmit);
        btnSubmit.setVisibility(View.GONE);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this, "نظر شما با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
            }
        });

        // Dialog Add To Cart

        dialogPoll = new Dialog(TestActivity.this);
        dialogPoll.setContentView(R.layout.dialog_poll);
        dialogPoll.setCancelable(false);
        dialogPoll_Title = (TextView) dialogPoll.findViewById(R.id.title);
        dialogPoll_MainLayout = (LinearLayout) dialogPoll.findViewById(R.id.main_layout);
        dialogPoll_Confirm = (TextView) dialogPoll.findViewById(R.id.confirm);
        dialogPoll_Cancel = (TextView) dialogPoll.findViewById(R.id.cancel);
        dialogPoll_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                A = new JSONArray();
                if (isSelected) {
                    if (itemIDs.size() > 0) {


                        JSONArray items = new JSONArray();
                        for (int i = 0; i < itemIDs.size(); i++) {
                            JSONObject itemsObject = new JSONObject();
                            try {
                                itemsObject.put("itemId", itemIDs.get(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            items.put(itemsObject);
                        }


                        JSONObject O = new JSONObject();
                        try {
                            O.put("headerId", headerID);
                            O.put("itemIdList", items);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        A.put(O);
                        Log.d("sending items", String.valueOf(A));

                        dialogPoll.dismiss();
                        dialogPoll_MainLayout.removeAllViews();
                    } else {
                        Toast.makeText(TestActivity.this, "لطفا یک مورد را انتخاب کنید", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    if (itemID != null) {
                        JSONArray items = new JSONArray();
                        JSONObject itemsObject = new JSONObject();
                        try {
                            itemsObject.put("itemId", itemID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        items.put(itemsObject);


                        JSONObject O = new JSONObject();
                        try {
                            O.put("headerId", headerID);
                            O.put("itemIdList", items);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        A.put(O);
                        Log.d("sending items", String.valueOf(A));

                        dialogPoll.dismiss();
                        dialogPoll_MainLayout.removeAllViews();
                    } else {
                        Toast.makeText(TestActivity.this, "لطفا یک مورد را انتخاب کنید", Toast.LENGTH_SHORT).show();

                    }
                }
                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
                String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                new AsyncTests().execute(Urls.BASE_URL + Urls.GET_TEST_ALL, sessionId, userToken, customerId);
            }
        });
        dialogPoll_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPoll.dismiss();
                dialogPoll_MainLayout.removeAllViews();
            }
        });

        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
//        getPoll();
        getTests();

        onclicklistener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Toast.makeText(ActivityPoll.this, String.valueOf(v.getId()), Toast.LENGTH_SHORT).show();

                JSONArray items = null;
                try {
                    if (isSelected) {
                        items = new JSONArray(itemsJSON);
                        JSONObject item = items.getJSONObject(v.getId());
                        itemID = item.getString("ItemID");
                        if (!itemIDs.contains(itemID)) {
                            itemIDs.add(itemID);
                        } else {
                            itemIDs.remove(itemID);
                        }
//                        Toast.makeText(TestActivity.this, item.getString("ItemID"), Toast.LENGTH_SHORT).show();
                    } else {
                        items = new JSONArray(itemsJSON);
                        JSONObject item = items.getJSONObject(v.getId());
                        itemID = item.getString("ItemID");
//                        Toast.makeText(TestActivity.this, item.getString("ItemID"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                }
            }
        };

        final RecyclerView list = (RecyclerView) findViewById(R.id.pollList);
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
//                dialogPoll.show();
                headerID = arrayList.get(position).strId;
                headerTitle = arrayList.get(position).strText;

                getTestItem(headerID);

//                size = arrayList.get(position).intQuantity;
//                itemsJSON = arrayList.get(position).json;
//                isSelected = arrayList.get(position).isSelected;
//
//                try {
//                    JSONArray items = new JSONArray(arrayList.get(position).json);
//                    RadioGroup radioGroup = new RadioGroup(G.CONTEXT);
//                    radioGroup.setLayoutParams(new LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.MATCH_PARENT,
//                            LinearLayout.LayoutParams.WRAP_CONTENT));
//                    dialogPoll_MainLayout.addView(radioGroup);
//                    for (int j = 0; j < items.length(); j++) {
//                        JSONObject item = items.getJSONObject(j);
//                        if (arrayList.get(position).isSelected) {
//                            CheckBox checkBox = new CheckBox(G.CONTEXT);
//                            checkBox.setId(j);
//                            checkBox.setOnClickListener(onclicklistener);
//                            checkBox.setLayoutParams(new LinearLayout.LayoutParams(
//                                    LinearLayout.LayoutParams.MATCH_PARENT,
//                                    LinearLayout.LayoutParams.WRAP_CONTENT));
//                            dialogPoll_MainLayout.addView(checkBox);
//                            checkBox.setText(item.getString("itemTitle"));
////                            struct.strItemId = item.getString("ItemID");
////                            struct.isDeletedItem = item.getBoolean("isDeletedItem");
//                            checkBox.setTypeface(typeFace_Light);
//
//
//                        } else {
//                            RadioButton radioButton = new RadioButton(G.CONTEXT);
//                            radioButton.setId(j);
//                            radioButton.setOnClickListener(onclicklistener);
//                            radioButton.setLayoutParams(new LinearLayout.LayoutParams(
//                                    LinearLayout.LayoutParams.MATCH_PARENT,
//                                    LinearLayout.LayoutParams.WRAP_CONTENT));
//                            radioGroup.addView(radioButton);
//                            radioButton.setText(item.getString("itemTitle"));
//                            radioButton.setPadding(0, 10, 0, 10);
////                            struct.strItemId = item.getString("ItemID");
////                            struct.isDeletedItem = item.getBoolean("isDeletedItem");
//                            radioButton.setTypeface(typeFace_Light);
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onEdit(int position) {

            }
        }, 7, false);
        list.setAdapter(adapter_recycler);
        list.setLayoutManager(manager);


    }

    private void checkState() {
        if (arrayList.size() > 0) {
            state.setVisibility(View.GONE);
        } else {
            state.setVisibility(View.VISIBLE);
        }
    }

    private void getTestItem(String id) {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
        String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
        new Async().execute(Urls.BASE_URL + Urls.GET_TEST_ITEMS, sessionId, userToken, customerId, id);

    }

    private class Async extends Webservice.getTestItem {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            String x = result;

            Intent intent = new Intent(TestActivity.this,TestItemActivity.class);
            intent.putExtra("json",result);
            intent.putExtra("headerID",headerID);
            intent.putExtra("headerTitle",headerTitle);
            TestActivity.this.startActivity(intent);

            //                size = arrayList.get(position).intQuantity;
//                itemsJSON = arrayList.get(position).json;
//                isSelected = arrayList.get(position).isSelected;
//
//                try {
//                    JSONArray items = new JSONArray(arrayList.get(position).json);
//                    RadioGroup radioGroup = new RadioGroup(G.CONTEXT);
//                    radioGroup.setLayoutParams(new LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.MATCH_PARENT,
//                            LinearLayout.LayoutParams.WRAP_CONTENT));
//                    dialogPoll_MainLayout.addView(radioGroup);
//                    for (int j = 0; j < items.length(); j++) {
//                        JSONObject item = items.getJSONObject(j);
//                        if (arrayList.get(position).isSelected) {
//                            CheckBox checkBox = new CheckBox(G.CONTEXT);
//                            checkBox.setId(j);
//                            checkBox.setOnClickListener(onclicklistener);
//                            checkBox.setLayoutParams(new LinearLayout.LayoutParams(
//                                    LinearLayout.LayoutParams.MATCH_PARENT,
//                                    LinearLayout.LayoutParams.WRAP_CONTENT));
//                            dialogPoll_MainLayout.addView(checkBox);
//                            checkBox.setText(item.getString("itemTitle"));
////                            struct.strItemId = item.getString("ItemID");
////                            struct.isDeletedItem = item.getBoolean("isDeletedItem");
//                            checkBox.setTypeface(typeFace_Light);
//
//
//                        } else {
//                            RadioButton radioButton = new RadioButton(G.CONTEXT);
//                            radioButton.setId(j);
//                            radioButton.setOnClickListener(onclicklistener);
//                            radioButton.setLayoutParams(new LinearLayout.LayoutParams(
//                                    LinearLayout.LayoutParams.MATCH_PARENT,
//                                    LinearLayout.LayoutParams.WRAP_CONTENT));
//                            radioGroup.addView(radioButton);
//                            radioButton.setText(item.getString("itemTitle"));
//                            radioButton.setPadding(0, 10, 0, 10);
////                            struct.strItemId = item.getString("ItemID");
////                            struct.isDeletedItem = item.getBoolean("isDeletedItem");
//                            radioButton.setTypeface(typeFace_Light);
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


//            try {
//                JSONArray main = new JSONArray(result);
//                for (int i = 0; i < main.length(); i++) {
//                    JSONObject object = main.getJSONObject(i);
//                    Struct struct = new Struct();
//                    struct.strId = object.getString("HeaderID");
//                    struct.strText = object.getString("pollTitle");
//                    struct.isSelected = object.getBoolean("isMultiSelect");
//
//                    JSONArray items = object.getJSONArray("pollItemList");
//                    struct.intQuantity = items.length();
//                    struct.json = String.valueOf(items);
//                    arrayList.add(struct);
//////                    struct.strId = object.getString("HeaderID");
//////                    struct.strText = object.getString("pollTitle");
//////                    struct.strId = object.getString("pollOrder");
//////                    struct.isSelected = object.getBoolean("isMultiSelect");
//////                    struct.isDeleted = object.getBoolean("isDeletedHeader");
////                    JSONArray items = object.getJSONArray("pollItemList");
//////                    struct.intQuantity = items.length();
////                    for (int j = 0; j < items.length(); j++) {
////                        Struct struct = new Struct();
////                        if (j == 0) {
////                            struct.strId = object.getString("HeaderID");
////                            struct.strText = object.getString("pollTitle");
////                        } else {
////                            struct.strId = "";
////                            struct.strText = "";
////                        }
////                        struct.isSelected = object.getBoolean("isMultiSelect");
////                        JSONObject item = items.getJSONObject(j);
////                        struct.strItemId = item.getString("ItemID");
////                        struct.strItemTitle = item.getString("itemTitle");
////                        struct.isDeletedItem = item.getBoolean("isDeletedItem");
//////                        struct.strItemId = item.getString("isAnswer");
////
////                        if (j == items.length() - 1) {
////                            struct.space = "true";
////                        } else {
////                            struct.space = "false";
////                        }
////                        arrayList.add(struct);
////                    }
//                }
//                adapter_recycler.notifyDataSetChanged();
//                checkState();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        }
    }

    public void getTests() {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
        String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
        new AsyncTests().execute(Urls.BASE_URL + Urls.GET_TEST_ALL, sessionId, userToken, customerId);
    }

    private class AsyncTests extends Webservice.getPoll {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            arrayList.clear();


            JSONArray array = null;
            try {
                array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Struct struct = new Struct();
                    struct.strId = object.getString("HeaderID");
                    struct.strText = object.getString("HeaderTitle");

                    arrayList.add(struct);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter_recycler.notifyDataSetChanged();
//            getPoll();

        }
    }
}