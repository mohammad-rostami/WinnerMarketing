package ir.co.ahs.app.mob.WinnerMarketing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by imac on 2/25/18.
 */

public class LoginActivity extends Activity {

    private Dialog dialog_verify;
    private Dialog dialog_phone;
    private String oneSignalUserID;
    private String oneSignalRegistrationID;
    private TextView dialog_phone_txt1;
    private TextView dialog_phone_counter;
    private EditText dialog_phone_edt_txt;
    private EditText dialog_phone_edt_txt_code;
    private TextView dialog_phone_resendCode;
    private Dialog dialog_confirm;
    private TextView dialog_confirm_resendCode;
    private TextView dialog_confirm_txt1;
    private TextView dialog_confirm_counter;
    private EditText dialog_confirm_edt_txt;
    private EditText dialog_confirm_edt_txt_code;
    private ImageView dialog_phone_btnNavigation;
    private ImageView dialog_confirm_btnNavigation;
    private TextView dialog_confirm_call_support;
    private CountDownTimer countDownTimer;
    private TextView dialog_confirm_register;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Dialog
        dialog_phone = new Dialog(LoginActivity.this);
        dialog_phone.setContentView(R.layout.dialog_phone);
//        dialog_phone.setTitle("Custom Dialog");
        dialog_phone_resendCode = (TextView) dialog_phone.findViewById(R.id.resendCode);
        TextView dialog_phone_txt = (TextView) dialog_phone.findViewById(R.id.dialog_txt);
        dialog_phone_txt1 = (TextView) dialog_phone.findViewById(R.id.dialog_txt1);
        dialog_phone_counter = (TextView) dialog_phone.findViewById(R.id.dialog_counter);
        dialog_phone_edt_txt = (EditText) dialog_phone.findViewById(R.id.dialog_edt_txt);
        dialog_phone_edt_txt_code = (EditText) dialog_phone.findViewById(R.id.dialog_edt_txt_code);
        dialog_phone_btnNavigation = (ImageView) dialog_phone.findViewById(R.id.btnNavigation);
        TextView dialog_phone_cancel = (TextView) dialog_phone.findViewById(R.id.cancel);
        TextView dialog_phone_register = (TextView) dialog_phone.findViewById(R.id.register);
        dialog_phone_btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_phone.dismiss();
            }
        });


        // Dialog_Confirm
        dialog_confirm = new Dialog(LoginActivity.this);
        dialog_confirm.setContentView(R.layout.dialog_phone_confirm);
        dialog_confirm_resendCode = (TextView) dialog_confirm.findViewById(R.id.resendCode);
        dialog_confirm_call_support = (TextView) dialog_confirm.findViewById(R.id.call_support);
        TextView dialog_confirm_txt = (TextView) dialog_confirm.findViewById(R.id.dialog_txt);
        dialog_confirm_txt1 = (TextView) dialog_confirm.findViewById(R.id.dialog_txt1);
        dialog_confirm_counter = (TextView) dialog_confirm.findViewById(R.id.dialog_counter1);
        dialog_confirm_edt_txt = (EditText) dialog_confirm.findViewById(R.id.dialog_edt_txt);
        dialog_confirm_btnNavigation = (ImageView) dialog_confirm.findViewById(R.id.btnNavigation);
        dialog_confirm_edt_txt_code = (EditText) dialog_confirm.findViewById(R.id.dialog_edt_txt_code);
        TextView dialog_confirm_cancel = (TextView) dialog_confirm.findViewById(R.id.cancel);
        dialog_confirm_register = (TextView) dialog_confirm.findViewById(R.id.register);
        dialog_confirm_btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_confirm.dismiss();
            }
        });
        dialog_confirm_call_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "02177647504", null));
                startActivity(intent);
            }
        });

        dialog_confirm_resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");

                Log.d("session", sessionId);
                Log.d("token", userToken);
                new getActivationCode().execute(Urls.BASE_URL + Urls.SEND_ACTIVATION_CODE, sessionId, userToken, dialog_phone_edt_txt.getText().toString());

                countDownTimer.start();
                dialog_confirm_resendCode.setVisibility(View.GONE);


            }
        });

        countDownTimer = new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                dialog_confirm_counter.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
//                dialogVerify_txt_counter.setText(String.valueOf("00:" + millisUntilFinished / 1000));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                dialog_confirm_counter.setText("00:00");
//                dialogVerify_txt_counter.setVisibility(View.GONE);
                dialog_confirm_resendCode.setVisibility(View.VISIBLE);
            }

        };


        dialog_phone_resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialog_phone_edt_txt.getText().toString().length() == 11) {

                    if (G.isNetworkAvailable()) {
                        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
                        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");

                        Log.d("session", sessionId);
                        Log.d("token", userToken);
                        new getActivationCode().execute(Urls.BASE_URL + Urls.SEND_ACTIVATION_CODE, sessionId, userToken, dialog_phone_edt_txt.getText().toString());

                        dialog_phone.dismiss();
                        dialog_confirm.show();
                        dialog_confirm_resendCode.setVisibility(View.GONE);
                        countDownTimer.start();
                        Window window = dialog_confirm.getWindow();
                        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    } else {
                        Toast.makeText(G.CONTEXT, "اتصال با اینترنت قطع شده است", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    dialog_phone_edt_txt.setText("");
                    dialog_phone_edt_txt.setHint("شماره تلفن خود را وارد کنید");
                }
            }
        });

        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrate);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dialog_phone_txt1.setTextColor(getResources().getColor(R.color.Red));
                dialog_phone_txt1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        dialog_confirm_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialog_phone_edt_txt.getText().toString().length() == 11 && dialog_confirm_edt_txt_code.getText().toString().length() > 3) {

                    if (G.isNetworkAvailable()) {
                        runSessionService();
                    } else {
                        Toast.makeText(G.CONTEXT, "اتصال با اینترنت قطع شده است", Toast.LENGTH_SHORT).show();
                    }

//                    String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
//                    String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
//                    String phone = dialog_phone_edt_txt.getText().toString();
//                    String memberCode = dialog_phone_edt_txt_code.getText().toString();
//                    new getCustomerAsync().execute("http://mcapi.ahscoltd.ir/mcapi/GetCustomer", sessionId, userToken, phone, memberCode);
//                    dialog_phone_txt1.setText("شماره تلفن وارد شده اشتباه است");
//                    dialog_phone_txt1.setVisibility(View.VISIBLE);
                } else {
                    dialog_confirm_txt1.setText("کد عضویت اشتباه است");
                    dialog_confirm_txt1.startAnimation(animation);

                }

            }
        });
        dialog_phone_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_phone.dismiss();
            }
        });

        TextView login = (TextView) findViewById(R.id.login);
        TextView register = (TextView) findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_phone.show();
                Window window = dialog_phone.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();
            }
        });

//        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
//        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
//        String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
//        new AsyncLogout().execute(Urls.BASE_URL + "/Logout", sessionId, userToken, customerId);
//
//        G.IS_REGISTERED.edit().putBoolean("IS_REGISTERED", false).apply();
//        G.CUSTOMER_NAME.edit().remove("CUSTOMER_NAME").apply();
//        G.CUSTOMER_ID.edit().remove("CUSTOMER_ID").apply();
    }

    private class getCustomerAsync extends Webservice.getCustomer {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            try {
//                JSONArray mainArray = new JSONArray(result);
                JSONObject mainObject = new JSONObject(result);
                String customerId = mainObject.getString("customerId");
                String customerName = mainObject.getString("firstName") + " " + mainObject.getString("lastName");
                String customerPhone = mainObject.getString("cellPhone");
                String customerAddress = mainObject.getString("address");
                String customerBirth = mainObject.getString("birthDate");
                String customerCode = mainObject.getString("memberCode");
                int customerCity = mainObject.getInt("cityId");
                int customerGender = mainObject.getInt("gender");
                G.IS_REGISTERED.edit().putBoolean("IS_REGISTERED", true).apply();
                G.CUSTOMER_ID.edit().putString("CUSTOMER_ID", customerId).apply();
                G.CUSTOMER_NAME.edit().putString("CUSTOMER_NAME", customerName).apply();
                G.CUSTOMER_PHONE.edit().putString("CUSTOMER_PHONE", customerPhone).apply();
                G.CUSTOMER_GENDER.edit().putInt("CUSTOMER_GENDER", customerGender).apply();
                G.CUSTOMER_ADDRESS.edit().putString("CUSTOMER_ADDRESS", customerAddress).apply();
                G.CUSTOMER_CITY.edit().putInt("CUSTOMER_CITY", customerCity).apply();
                G.CUSTOMER_BIRTH.edit().putString("CUSTOMER_BIRTH", customerBirth).apply();
                G.CUSTOMER_CODE.edit().putString("CUSTOMER_CODE", customerCode).apply();
//                Log.d("request result", sessionId);
//                Log.d("request result", userToken);
                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
                new CreditAsync().execute(Urls.BASE_URL + Urls.GET_SCORE, sessionId, userToken, customerId);


            } catch (JSONException e) {

                Toast.makeText(LoginActivity.this, "مشخصات وارد شده اشتباه است!", Toast.LENGTH_SHORT).show();
                // TODO : نشان دادن پیغام خطا

            }

        }
    }

    public void runSessionService() {
        boolean firstTimeConnection = G.FIRST_TIME_REQUEST.getBoolean("FIRST_TIME_CONNECTION", false);
        if (!firstTimeConnection) {

            OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {

                @Override
                public void idsAvailable(String userId, String registrationId) {
                    oneSignalUserID = userId;
                    if (registrationId != null)
                        oneSignalRegistrationID = registrationId;

                    Log.d("userId", oneSignalUserID);
                    Log.d("registerId", oneSignalRegistrationID);
                    String uniqueId = G.ANDROID_ID;
                    String androidVersion = G.ANDROID_VERSION;
                    String appVersion = G.APP_VERSION;
                    String deviceName = G.DEVICE_NAME;

                    new AsyncSesssion().execute(Urls.BASE_URL + Urls.REGISTER_SESSION, oneSignalUserID, oneSignalRegistrationID, uniqueId, androidVersion, appVersion, deviceName);

                    G.FIRST_TIME_REQUEST.edit().putBoolean("FIRST_TIME_CONNECTION", true).apply();

                }
            });
        } else {
            String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
            String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
            String phone = dialog_phone_edt_txt.getText().toString();
            String memberCode = dialog_confirm_edt_txt_code.getText().toString();
            new getCustomerAsync().execute(Urls.BASE_URL + Urls.GET_CUSTOMER, sessionId, userToken, phone, memberCode);
        }
    }

    private class AsyncSesssion extends Webservice.sessionConnection {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {
            try {
                JSONObject sessionData = new JSONObject(result);
                String sessionId = sessionData.getString("SessionId");
                String userToken = sessionData.getString("UserToken");

                G.AUTHENTICATIONS_SESSION.edit().putString("SESSION", sessionId).apply();
                G.AUTHENTICATIONS_TOKEN.edit().putString("TOKEN", userToken).apply();

                String phone = dialog_phone_edt_txt.getText().toString();
                String memberCode = dialog_confirm_edt_txt_code.getText().toString();
                new getCustomerAsync().execute(Urls.BASE_URL + Urls.GET_CUSTOMER, sessionId, userToken, phone, memberCode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class CreditAsync extends Webservice.getScore {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {

            try {
                JSONArray main = new JSONArray(result);
                JSONObject mainObject = main.getJSONObject(0);
                JSONArray scoreList = mainObject.getJSONArray("scoreItemsList");
                int Credit = mainObject.getInt("creditValue");
                int Income = mainObject.getInt("incomeValue");
                int Outcome = mainObject.getInt("outcomeValue");
                String convertedCredit = String.format("%,d", Credit);
                String convertedIncome = String.format("%,d", Income);
                String convertedOutcome = String.format("%,d", Outcome);
                G.CREDIT.edit().putInt("CREDIT", Credit).apply();


                Toast.makeText(LoginActivity.this, "ورود با موفقیت انجام شد!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();

            } catch (JSONException e) {


            }


        }
    }


    private class getActivationCode extends Webservice.getCode {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {

            String x = result;
            try {
                JSONObject main = new JSONObject(result);
                String response = main.getString("resendResult");
                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {


            }


        }
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
}