package com.reward.quizisty.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reward.quizisty.R;
import com.reward.quizisty.Utils.Constants;
import com.reward.quizisty.Utils.ControlRoom;
import com.reward.quizisty.databinding.ActivityMyInfoBinding;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MyInfoActivity extends AppCompatActivity {

    ActivityMyInfoBinding binding;
    String fullName, phone, dob, address, gender, pincode;
    static String initialFullName = "", initialPhone = "", initialDob = "", initialAddress = "", initialGender = "", initialPinCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        back button in title bar
        MaterialToolbar materialToolbar = binding.myInfoToolbar;
        materialToolbar.setNavigationIcon(R.drawable.arrow_left);
        materialToolbar.setNavigationOnClickListener(v -> {
            finish();
        });


//        Show the user details.
        binding.nameInputLayout.getEditText().setText(ControlRoom.getInstance().getFullName(MyInfoActivity.this));
        fetchUserDetails();
//      Name change listener.
        binding.nameInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.submitButton.setEnabled(!initialFullName.equals(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        DOB change listener
        binding.dobTexInpLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (initialDob.equals("null")) {
                    initialDob = "";
                }
//                binding.submitButton.setEnabled(!initialDob.equals(s.toString()));
                if (!initialDob.equals(s.toString())) {
                    binding.submitButton.setEnabled(true);
                } else {
                    binding.submitButton.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //      Address change listener
        binding.addInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (initialAddress.equals("null")) {
                    initialAddress = "";
                }
//                binding.submitButton.setEnabled(!initialDob.equals(s.toString()));
                if (!initialAddress.equals(s.toString())) {
                    binding.submitButton.setEnabled(true);
                } else {
                    binding.submitButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //  Phone no. changed listener
        binding.phoneInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (initialPhone.equals("null")) {
                    initialPhone = "";
                }
//                binding.submitButton.setEnabled(!initialDob.equals(s.toString()));
                if (!initialPhone.equals(s.toString())) {
                    binding.submitButton.setEnabled(true);
                } else {
                    binding.submitButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.pincodeInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (initialPinCode.equals("null")) {
                    initialPinCode = "";
                }
//                binding.submitButton.setEnabled(!initialDob.equals(s.toString()));
                if (!initialPinCode.equals(s.toString())) {
                    Log.d("testtext", "buttonEnable: initialDob: " + initialPinCode);
                    binding.submitButton.setEnabled(true);
                } else {
                    Log.d("testtext", "buttonDisable: " + s);
                    binding.submitButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        Calendar calendar = Calendar.getInstance();
        binding.dobTexInpLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(MyInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);
                        String dob = new SimpleDateFormat("dd/MM/yyy", Locale.getDefault()).format(calendar.getTime());
                        binding.dobTexInpLayout.getEditText().setText(dob);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

                dialog.show();
            }
        });

        binding.dobTexInpLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(MyInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);
                        String dob = new SimpleDateFormat("dd/MM/yyy", Locale.getDefault()).format(calendar.getTime());
                        binding.dobTexInpLayout.getEditText().setText(dob);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

                dialog.show();
            }
        });


        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                clear cursor
                binding.nameInputLayout.getEditText().clearFocus();
                binding.dobTexInpLayout.getEditText().clearFocus();
                binding.phoneInputLayout.getEditText().clearFocus();
                binding.addInputLayout.getEditText().clearFocus();
                binding.pincodeInputLayout.getEditText().clearFocus();

//                check for valid inputs.

                fullName = binding.nameInputLayout.getEditText().getText().toString();
                phone = binding.phoneInputLayout.getEditText().getText().toString();
                dob = binding.dobTexInpLayout.getEditText().getText().toString();
                address = binding.addInputLayout.getEditText().getText().toString();
                pincode = binding.pincodeInputLayout.getEditText().getText().toString();
                if (binding.maleRadio.isChecked())
                    gender = binding.maleRadio.getText().toString();
                else if (binding.femaleRadio.isChecked())
                    gender = binding.femaleRadio.getText().toString();
                else
                    gender = binding.othersRadio.getText().toString();

                checkValidation();
            }
        });


    }

    private void fetchUserDetails() {
        binding.myInfoProgress.setVisibility(View.VISIBLE);
        binding.myInfoLayou.setClickable(false);
        binding.submitButton.setVisibility(View.GONE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.USER_API_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {
                                Log.d("fetchUserDetails", "onResponse: response is Successful " + response.getString("data"));
//                                set user data in local database.
                                JSONObject userData = response.getJSONObject("data");
                                ControlRoom.getInstance().setUserData(userData, MyInfoActivity.this);

                                binding.myInfoProgress.setVisibility(View.GONE);
                                binding.submitButton.setVisibility(View.VISIBLE);
                                binding.myInfoLayou.setClickable(true);
                                JSONObject jsonObject = response.getJSONObject("data");

                                initialFullName = jsonObject.getString("name");

                                binding.nameInputLayout.getEditText().setText(initialFullName);

                                initialPhone = jsonObject.getString("phone");
                                if (initialPhone.equals("null")) {
                                    binding.phoneInputLayout.getEditText().setText("");
                                } else
                                    binding.phoneInputLayout.getEditText().setText(initialPhone);

//                              dob
                                initialDob = jsonObject.getString("d_o_b");
                                if (initialDob.equals("null")) {

                                    binding.dobTexInpLayout.getEditText().setText("");
                                } else {
                                    binding.dobTexInpLayout.getEditText().setText(initialDob);
                                }

//                               gender
                                initialGender = jsonObject.getString("gender");
                                if (initialGender.equals("Male")) {
                                    binding.maleRadio.setChecked(true);
                                } else if (initialGender.equals("Female")) {
                                    binding.femaleRadio.setChecked(true);
                                } else if (initialGender.equals("Others")) {
                                    binding.othersRadio.setChecked(true);
                                }

//                              address
                                initialAddress = jsonObject.getString("address");
                                if (initialAddress.equals("null")) {

                                    binding.addInputLayout.getEditText().setText("");
                                } else {
                                    binding.addInputLayout.getEditText().setText(initialAddress);
                                }

//                                pincode
                                initialPinCode = jsonObject.getString("pincode");
                                if (initialPinCode.equals("null")) {

                                    binding.pincodeInputLayout.getEditText().setText("");
                                } else {
                                    binding.pincodeInputLayout.getEditText().setText(initialPinCode);
                                }



                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("fetchUserDetails", "onResponse: response is Failed " + response.getString("data"));

                            } else {
                                Log.d("fetchUserDetails", "onResponse: something went wrong");
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("fetchUserDetails", "onResponse: error Response " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, Constants.BEARER + ControlRoom.getInstance().getAccessToken(MyInfoActivity.this));
                return header;
            }

        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void checkValidation() {
        if (phone.equals("")) {
            binding.phoneInputLayout.setError("This field is required.");
        } else {
            if (!phone.matches("[6789][0-9]{9}")) {
                binding.phoneInputLayout.setError("Please Enter Valid Phone Number.");
            } else {
                if (fullName.equals("")) {
                    binding.nameInputLayout.setError("This field cannot be empty");
                } else if (fullName.length() >= 30) {
                    binding.nameInputLayout.setError("Name cannot exceeds 30 characters");
                } else {
//                    Toast.makeText(this, "Submitted Data", Toast.LENGTH_SHORT).show();
                    updateUserInfo();

                }
            }
        }


    }

    private void updateUserInfo() {
        binding.submitButton.setVisibility(View.GONE);
        binding.myInfoProgress.setVisibility(View.VISIBLE);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", fullName);
            jsonObject.put("phone", phone);
            jsonObject.put("d_o_b", dob);
            jsonObject.put("gender", gender);
            jsonObject.put("address", address);
            jsonObject.put("pincode", pincode);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.UPDATE_USER_DETAIL_API,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("updateUserInfo", "onResponse: response is Successful " + response.getString("data"));

                        binding.submitButton.setVisibility(View.VISIBLE);
                        binding.submitButton.setEnabled(false);
                        binding.myInfoProgress.setVisibility(View.GONE);

                        Toast.makeText(MyInfoActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        fetchUserDetails();

                        /*Intent intent = new Intent(MyInfoActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        finish();*/
                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                        Log.d("updateUserInfo", "onResponse: response is Failed " + response.getString("data"));
                        binding.myInfoProgress.setVisibility(View.GONE);
                        Toast.makeText(MyInfoActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("updateUserInfo", "onResponse: something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("updateUserInfo", "onResponse: error Response " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE);
                header.put(Constants.AUTHORISATION, Constants.BEARER + ControlRoom.getInstance().getAccessToken(MyInfoActivity.this));
                return header;
            }

        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    public void updateMyInfo(View view) {

        if (fullName.isEmpty()) {
            binding.nameInputLayout.setError("This field can't be empty");


        } else if (phone.isEmpty()) {
            binding.phoneInputLayout.setError("This field can't be empty");
        } else {
            updateAllDetails();

            Toast.makeText(MyInfoActivity.this, "Information Updated", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateAllDetails() {
        JSONObject updateInfo = new JSONObject();
        try {
            updateInfo.put("name", fullName);
            updateInfo.put("phone", phone);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest()
    }
}