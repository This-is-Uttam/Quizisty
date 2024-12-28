package com.reward.quizisty.Activities;

import static com.reward.quizisty.Utils.Constants.*;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reward.quizisty.Adapters.DiamondHistoryAdapter;
import com.reward.quizisty.Modals.HistoryModal;
import com.reward.quizisty.Utils.Constants;
import com.reward.quizisty.Utils.ControlRoom;
import com.reward.quizisty.databinding.ActivityDiamondHistoryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DiamondHistoryActivity extends AppCompatActivity {
    ActivityDiamondHistoryBinding binding;

    ArrayList<HistoryModal> diamondHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiamondHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.diamondHistToolbar.setNavigationOnClickListener(v -> finish());

        getDiamondHistoryList();

    }

    private void getDiamondHistoryList() {
        diamondHistoryList = new ArrayList<>();

        binding.progressBar3.setVisibility(View.VISIBLE);
        binding.errorImg.setVisibility(View.GONE);
        binding.message.setVisibility(View.GONE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.DIAMOND_HISTORY_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {
                                binding.progressBar3.setVisibility(View.GONE);


                                Log.d("getDiamondHistoryList", "onResponse: response Sucessfull: " + response.getString("data"));

                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    int hisDiamonds = jsonObject.getInt("daimonds");
                                    String hisMsg = jsonObject.getString("tran_type");
                                    String type = jsonObject.getString("type");
                                    String hisDate = jsonObject.getString("tran_date");
                                    String hisTime = jsonObject.getString("tran_time");


//                                    date format
                                    Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(hisDate);
                                    String dateMain = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date);

//                                    time format
//                                    Date time = new SimpleDateFormat("hh:mm:ssa", Locale.getDefault()).parse(hisTime);
//                                    String timeMain = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(time);
                                    String timeMain= hisTime;

                                    boolean isCredited;
                                    if (type.equals("DR")) {
                                        isCredited = false;
                                    } else {
                                        isCredited = true;
                                    }

//                                    Date date = new Date(hisTime)
//                                    LocalDate localDate = LocalDate.parse(hisDate);
//
//                                    String sdf = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(localDate);

                                    HistoryModal historyModal = new HistoryModal(
                                            dateMain, timeMain, hisDiamonds + "", isCredited, hisMsg
                                    );
                                    diamondHistoryList.add(historyModal);


                                }

                                if (diamondHistoryList.isEmpty()) {
                                    binding.progressBar3.setVisibility(View.GONE);
                                    binding.errorImg.setVisibility(View.GONE);
                                    binding.message.setVisibility(View.VISIBLE);
                                    binding.message.setText("Coin History is empty.");
                                }
                                else {

                                    binding.diamondHistoryRv.setAdapter(new DiamondHistoryAdapter(diamondHistoryList, DiamondHistoryActivity.this));
                                    binding.diamondHistoryRv.setLayoutManager(new LinearLayoutManager(DiamondHistoryActivity.this));

                                }


//                                ControlRoom.getInstance().setCoins(response.getInt("ava_coins")+"");
                                binding.diamondTv.setText(ControlRoom.getInstance().getDiamonds(DiamondHistoryActivity.this));

                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getDiamondHistoryList", "onResponse: response Failed: " + response.getString("data"));
                                binding.progressBar3.setVisibility(View.GONE);
//                                binding.errorImg.setVisibility(View.VISIBLE);
                                binding.message.setVisibility(View.VISIBLE);
                                String msg = response.getString("data");
                                if (msg.equals("No coin transaction found!.."))
                                    binding.message.setText("No Diamonds history found.");
                                else
                                    binding.message.setText(response.getString(msg));


                            } else {
                                Log.d("getDiamondHistoryList", "onResponse: Something went wrong ");
                                binding.progressBar3.setVisibility(View.GONE);
                                binding.errorImg.setVisibility(View.VISIBLE);
                                binding.message.setVisibility(View.VISIBLE);
                                binding.message.setText("Something went wrong!");
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getDiamondHistoryList", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(DiamondHistoryActivity.this));
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);


    }
}