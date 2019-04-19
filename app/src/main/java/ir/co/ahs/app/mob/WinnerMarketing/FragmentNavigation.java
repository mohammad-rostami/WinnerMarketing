package ir.co.ahs.app.mob.WinnerMarketing;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import java.io.IOException;

import ir.co.ahs.app.mob.WinnerMarketing.Helper.DataBaseWrite;


//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;

public class FragmentNavigation extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private String mStr_packages;
    private Dialog dialog;
    private LinearLayout mLinearLayout_Main;
    private static TextView Premium;
    private TextView mTextView_ProShot;
    private String[] OrderTableFieldNames = new String[]{"ID", "NAME", "CURRENT_PRICE", "PREVIOUS_PRICE", "QUANTITY", "COMMENT"};
    private String[] OrderTableFieldTypes = new String[]{"TEXT", "TEXT", "INT", "INT", "INT", "TEXT", "TEXT"};
    private String[] HistoryTableFieldNames = new String[]{"JSON", "DATE", "Empty1", "Empty2", "Empty3", "TIME"};
    private String[] HistoryTableFieldTypes = new String[]{"TEXT", "TEXT", "INT", "INT", "INT", "TEXT"};
    private Dialog dialog_confirm;
    public static TextView S_TEXTVIEW_CREDIT;
    private LinearLayout mLinearLayout_Six;
    private LinearLayout mLinearLayout_Three;
    private LinearLayout mLinearLayout_One;
    private LinearLayout mLinearLayout_Two;
    private LinearLayout mLinearLayout_Four;
    private LinearLayout mLinearLayout_Five;
    private LinearLayout mLinearLayout_Seven;
    private LinearLayout mLinearLayout_Eight;


    public FragmentNavigation() {
        // Required empty public constructor
    }

    public static FragmentNavigation newInstance(String param1, String param2) {
        FragmentNavigation fragment = new FragmentNavigation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        // Dialog confirm
        dialog_confirm = new Dialog(getActivity());
        dialog_confirm.setContentView(R.layout.dialog_confirm);
        TextView dialog_confirm_text = (TextView) dialog_confirm.findViewById(R.id.dialog_txt);
        dialog_confirm_text.setText("آیا از خروج خود اطمینان دارد؟");
        TextView dialog_confirm_cancel = (TextView) dialog_confirm.findViewById(R.id.cancel);
        TextView dialog_confirm_register = (TextView) dialog_confirm.findViewById(R.id.register);

        dialog_confirm_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
                String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                new AsyncLogout().execute(Urls.BASE_URL + "/Logout", sessionId, userToken, customerId);

                G.IS_REGISTERED.edit().putBoolean("IS_REGISTERED", false).apply();
                G.CUSTOMER_NAME.edit().remove("CUSTOMER_NAME").apply();
                G.CUSTOMER_ID.edit().remove("CUSTOMER_ID").apply();

                try {
                    clearDateBase();
                } catch (Exception e) {
                }

                G.FIRST_TIME_REQUEST.edit().putBoolean("FIRST_TIME_CONNECTION", false).apply();
                G.AUTHENTICATIONS_SESSION.edit().remove("SESSION").apply();
                G.AUTHENTICATIONS_TOKEN.edit().remove("TOKEN").apply();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
//                ActivityMain.finisher();
                dialog_confirm.dismiss();
            }
        });
        dialog_confirm_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.dismiss();
            }
        });


        mLinearLayout_One = (LinearLayout) view.findViewById(R.id.fragment_nav_item_one);
        mLinearLayout_Two = (LinearLayout) view.findViewById(R.id.fragment_nav_ll_item_two);
        mLinearLayout_Three = (LinearLayout) view.findViewById(R.id.fragment_nav_item_three);
        mLinearLayout_Four = (LinearLayout) view.findViewById(R.id.fragment_nav_item_four);
        mLinearLayout_Five = (LinearLayout) view.findViewById(R.id.fragment_nav_item_five);
        mLinearLayout_Six = (LinearLayout) view.findViewById(R.id.fragment_nav_item_six);
        mLinearLayout_Seven = (LinearLayout) view.findViewById(R.id.fragment_nav_item_seven);
        mLinearLayout_Eight = (LinearLayout) view.findViewById(R.id.fragment_nav_item_eight);

        mLinearLayout_One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                if (isRegistered) {

                    Intent intent = new Intent(getActivity(), ConversationActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }


            }
        });

        mLinearLayout_Two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                if (isRegistered) {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }


            }
        });
        mLinearLayout_Three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                if (isRegistered) {
                    Intent intent = new Intent(getActivity(), ServicesActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }


            }
        });

        mLinearLayout_Four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                if (isRegistered) {
                    Intent intent = new Intent(getActivity(), CustomerActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }

            }
        });

        mLinearLayout_Five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                if (isRegistered) {
                    Intent intent = new Intent(getActivity(), ProjectsActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }


            }
        });
        mLinearLayout_Six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                if (isRegistered) {
                    Intent intent = new Intent(getActivity(), DashBoardActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }


            }
        });
        mLinearLayout_Seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                if (isRegistered) {
                    Intent intent = new Intent(getActivity(), weekMessageActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }


            }
        });
        mLinearLayout_Eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ContactUsActivity.class);
                getActivity().startActivity(intent);
            }
        });


        final TextView Name = (TextView) view.findViewById(R.id.name);
        final LinearLayout exit = (LinearLayout) view.findViewById(R.id.fragment_nav_item_exit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_confirm.show();
            }
        });
        Name.setText("ورود / ثبت نام");

        Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exit.getVisibility() == View.VISIBLE) {
//                    dialog_confirm.show();
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
//                    ActivityMain.finisher();
                }

            }
        });

        if (G.IS_REGISTERED.getBoolean("IS_REGISTERED", false)) {
            String name = G.CUSTOMER_NAME.getString("CUSTOMER_NAME", "empty");
            if (!name.equals("empty")) {
                Name.setText(name);
                exit.setVisibility(View.VISIBLE);
                refreshCredit();
            } else {
                exit.setVisibility(View.GONE);
                Name.setText("ورود / ثبت نام");
            }
        } else {
            exit.setVisibility(View.GONE);
            Name.setText("ورود / ثبت نام");
        }
        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);

        void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second);

        void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth);
    }

    private void activityFinisher() {

    }


    private class AsyncLogout extends Webservice.logOutSession {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            Toast.makeText(G.CONTEXT, result, Toast.LENGTH_LONG).show();

        }
    }

    //
    public static void refreshCredit() {
        int CREDIT = G.CREDIT.getInt("CREDIT", 0);
        String convertedCredit = String.format("%,d", CREDIT);
//        S_TEXTVIEW_CREDIT.setText(convertedCredit + " ریال");

    }

    public void clearDateBase() {
        DataBaseWrite dataBaseHelper = new DataBaseWrite(getContext(), "ORDER_DATABASE", "ORDER_TABLE", OrderTableFieldNames, OrderTableFieldTypes);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = dataBaseHelper.getWritableDatabase();
        sqld.execSQL("delete from " + "ORDER_TABLE");

        DataBaseWrite db = new DataBaseWrite(getContext(), "HISTORY_DATABASE", "HISTORY_TABLE", HistoryTableFieldNames, HistoryTableFieldTypes);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL("delete from " + "HISTORY_TABLE");

    }


    private class Async extends Webservice.getToken {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (G.ACTIVITY_NAME.equals("DashBoardActivity")) {
            mLinearLayout_Six.setVisibility(View.GONE);
        } else if (G.ACTIVITY_NAME.equals("ServicesActivity")) {
            mLinearLayout_Three.setVisibility(View.GONE);
        } else if (G.ACTIVITY_NAME.equals("ProjectsActivity")) {
            mLinearLayout_Five.setVisibility(View.GONE);
        } else if (G.ACTIVITY_NAME.equals("CustomerActivity")) {
            mLinearLayout_Four.setVisibility(View.GONE);
        } else {
            mLinearLayout_Six.setVisibility(View.VISIBLE);

        }
    }


}
