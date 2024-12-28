package com.reward.quizisty.Activities;

import static com.reward.quizisty.Utils.Constants.AUTHORISATION;
import static com.reward.quizisty.Utils.Constants.BEARER;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE_VALUE;
import static com.reward.quizisty.Utils.Constants.DEVICE_ID;
import static com.reward.quizisty.Utils.Constants.FCM_ID;
import static com.reward.quizisty.Utils.Constants.LOGIN_API_URL;
import static com.reward.quizisty.Utils.Constants.PRIVACY_POLICY;
import static com.reward.quizisty.Utils.Constants.TERMS_N_CONDITION;
import static com.reward.quizisty.Utils.Constants.UPDATE_FCM;
import static com.reward.quizisty.Utils.Constants.USER_API_URL;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.res.ResourcesCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reward.quizisty.R;
import com.reward.quizisty.Utils.ControlRoom;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.reward.quizisty.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static final String WEB_CLIENT_ID = "68217820602-nbu1d5gi12im0mn6sj5vdu9ga4domfh9.apps.googleusercontent.com";
    private static final String TAG = "LoginActivity";
    ActivityLoginBinding binding;
    GoogleSignInClient gsc;
    FirebaseAuth firebaseAuth;
    String fcmToken;
    String email;
    public static String accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseApp.initializeApp(this);

        getDeviceIds();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder()
                .requestIdToken(WEB_CLIENT_ID)
                .requestProfile()
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(getApplicationContext(), gso);
        firebaseAuth = FirebaseAuth.getInstance();


        ActivityResultLauncher<Intent> launcher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            Log.d(TAG, "onCreate: login result:"+ result.getResultCode()+"data: "+ result.getData().getDataString());
                            if (result.getData() != null && result.getResultCode() == Activity.RESULT_OK) {
                                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                                if (task.getResult() != null) {
                                    Log.d("ooo", "onActivityResult: Login Activity token " + task.getResult().getIdToken() + " profile " + task.getResult().getPhotoUrl());

                                    firebaseAuthWithGoogle(task);
                                } else {
                                    Log.d("ooo", "onActivityResult: Login Activity Null account");
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, "No Account Selected", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onCreate: error Google sign in : " + result.getData().getDataString());
                                binding.progressBar2.setVisibility(View.GONE);
                                binding.googleSignIn.setClickable(true);
                                binding.googleSignIn.setEnabled(true);
                            }
                        });


        binding.googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = gsc.getSignInIntent();
                launcher.launch(intent);
                binding.progressBar2.setVisibility(View.VISIBLE);
                binding.googleSignIn.setClickable(false);
                binding.googleSignIn.setEnabled(false);


            }

        });

//        for Terms and condition
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(new CustomTabColorSchemeParams.Builder()
                        .setToolbarColor(
                                ResourcesCompat.getColor(getResources(),
                                        R.color.md_theme_primary, getTheme()))
                        .build())
                .build();

        ClickableSpan clickSpanTc = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                customTabsIntent.launchUrl(LoginActivity.this, Uri.parse(TERMS_N_CONDITION));
            }
        };

        ClickableSpan clickSpanPp = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                customTabsIntent.launchUrl(LoginActivity.this, Uri.parse(PRIVACY_POLICY));
            }
        };

        SpannableString spannableString1 =
                new SpannableString("I have read and agree to the Terms and Conditions and Privacy Policy.");
        SpannableString spannableString2 = new SpannableString("");

        spannableString1.setSpan(new UnderlineSpan(), 29, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString1.setSpan(new UnderlineSpan(), 54, 68, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString1.setSpan(clickSpanTc, 29, 34, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString1.setSpan(clickSpanPp, 54, 68, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        binding.checkBox.setText(spannableString1);
        binding.checkBox.setMovementMethod(LinkMovementMethod.getInstance());

//        Initially disable signin button
        signInButtonEnabled(false);


        binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            signInButtonEnabled(isChecked);

        });

        //        onCreate ends
    }

    private void signInButtonEnabled(boolean enable) {
//        alpha, text color, clickable, focusable
        TextView signInButton = binding.googleSignIn;
        if (!enable) {
            signInButton.setAlpha(0.7f);
            signInButton.setTextColor(getResources().getColor(R.color.disableTextColor, getTheme()));
            signInButton.setClickable(false);
            signInButton.setFocusable(false);

        } else {
            signInButton.setAlpha(1f);
            signInButton.setTextColor(getResources().getColor(R.color.black, getTheme()));
            signInButton.setClickable(true);
            signInButton.setFocusable(true);
        }
    }

    private void firebaseAuthWithGoogle(Task<GoogleSignInAccount> task) {
//        SignIn the user's Google credentials with firebase for Authentication.
        GoogleSignInAccount account = task.getResult();
        Log.d("ooo", "firebaseAuthWithGoogle: name: " + account.getDisplayName());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Google login is successful now

                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        String name = firebaseUser.getDisplayName();
                        email = firebaseUser.getEmail();
                        firebaseUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            @Override
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if (task.isSuccessful()) {
                                    // idMainToken (Bearer Token) is generated now
                                    String idMainToken = task.getResult().getToken();

                                    // sending idMainToken to our server
                                    authLogin(idMainToken, firebaseUser.getDisplayName());

                                    Log.d("firebaseAuthWithGoogle", "onComplete: task is successful, Your IdToken: " + idMainToken);
                                } else {
                                    Log.d("firebaseAuthWithGoogle", "onComplete: task is not successful for IdToken");
                                }
                            }
                        });

                    } else {
                        Log.d("ooo", "onComplete: firebaseUser is null");
                    }

                } else
                    Log.d("ooo", "onComplete: task is not successful" + task.getException());
            }
        });
    }

    //  Login to our server
    private void authLogin(String idMainToken, String displayName) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put(DEVICE_ID, getDeviceIds());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_API_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                if (response.getInt("code") == 201 && !response.getBoolean("status")) {

//                                    If Login is not successfull

                                    Log.d("authLogin", "onResponse: response is Failed " + response.getString("data"));
                                    gsc.signOut();
                                    binding.progressBar2.setVisibility(View.GONE);

//                                   Show Error Dialog..........

                                    showErrorDialog(response.getString("data"));

                                } else {
//                                    Login Successfully / got our API Access Token to call every API in the app.

                                    Log.d("authLogin", "onResponse: response is Successfull " + response.getString("data"));
                                    String accessMainToken = response.getString("data");

                                    // Setting AccessToken to Shared Preferences.
                                    ControlRoom.getInstance().setAccessToken(LoginActivity.this, accessMainToken);

//                                   Getting Firebase Messaging Token
                                    getFcmToken();
                                    Toast.makeText(getApplicationContext(), "Welcome " + displayName, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("authLogin", "onResponse: error response " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, "Bearer " + idMainToken); // idMainToken is passed to sever via header
                return header;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public void showErrorDialog(String subtitle) throws JSONException {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this, R.style.CustomDialog);

        View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.alert_dialog_layout, null);
        ImageView dialogImg = view.findViewById(R.id.dialogImg);
        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        TextView dialogSubtitle = view.findViewById(R.id.dialogSubtitle);
        TextView positiveBtn = view.findViewById(R.id.positiveBtn);
        TextView negativeBtn = view.findViewById(R.id.negativeBtn);

        dialogImg.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_error_24, getTheme()));
        dialogTitle.setText("Error!");
        dialogSubtitle.setText(subtitle);
        positiveBtn.setText("Okay");
        negativeBtn.setVisibility(View.GONE);

        dialogBuilder.setView(view);
        dialogBuilder.setCancelable(false);
        AlertDialog dialog = dialogBuilder.create();

        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public String getDeviceIds() {
//        getting Device ID

        String deviceId1 = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "getDeviceIds: Device Id : " + deviceId1);
        return deviceId1;
    }


    public String getFcmToken() {
//      Generate Token for Firebase Messaging (fcm token)
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    fcmToken = task.getResult();
                    Log.d("getFcmToken", "onComplete: fcmToken: " + fcmToken);
                    updateFcmToken(fcmToken);
                } else
                    Log.d("getFcmToken", "onComplete: Failed to get FCM Token...");
            }
        });
        return fcmToken;

    }

    private void updateFcmToken(String fcmToken) {
//        Update and send fcm token to server

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(FCM_ID, fcmToken);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, UPDATE_FCM, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {

                        Log.d("updateFcmToken", "onResponse: ResPonse SucCess: " + response.getString("data"));
                        binding.progressBar2.setVisibility(View.GONE);
                        checkReferral();

                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {

                        Log.d("updateFcmToken", "onResponse: ResPonse FaiLed: " + response.getString("data"));
                    } else
                        Log.d("updateFcmToken", "onResponse: Something went wrong: " + response.getString("data"));

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("updateFcmToken", "onResponse: error ResPonse:  " + error.getMessage());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, "Bearer " + ControlRoom.getInstance().getAccessToken(LoginActivity.this));
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void checkReferral() {
//        Checking if the user have entered referral code before.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, USER_API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("checkReferral", "onResponse: Refer Stutus Activity: " + response.getString("data"));
                        JSONObject userData = response.getJSONObject("data");
                        ControlRoom.getInstance().setUserData(userData, LoginActivity.this);
                        int referStatus = response.getJSONObject("data").getInt("refer_status");

                        if (referStatus == 0) {
//                           show referral activity..
                            Log.d("checkReferral", "onResponse: No Referral Code..refer_status: " + referStatus);
                            startActivity(new Intent(LoginActivity.this, ReferralActivity.class));
                            finish();
                        } else {
                            Log.d("checkReferral", "onResponse: Referral Code Available..refer_status: " + referStatus);

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("checkReferral", "onResponse: Referral Code Something went wrong! : " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(LoginActivity.this));
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

}