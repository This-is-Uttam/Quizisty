package com.reward.quizisty.Activities;

import static com.reward.quizisty.Utils.Constants.AUTHORISATION;
import static com.reward.quizisty.Utils.Constants.BEARER;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE_VALUE;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reward.quizisty.Adapters.CoinHistoryAdapter;
import com.reward.quizisty.Modals.HistoryModal;
import com.reward.quizisty.R;
import com.reward.quizisty.Utils.Constants;
import com.reward.quizisty.Utils.ControlRoom;
import com.reward.quizisty.databinding.ActivityCoinHistoryBinding;

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

public class CoinHistoryActivity extends AppCompatActivity {
    ActivityCoinHistoryBinding binding;
    ArrayList<HistoryModal> historyModals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCoinHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.coinHistToolbar.setNavigationOnClickListener(v -> finish());

        getCoinsHistoryList();

        binding.coinTxt.setText(ControlRoom.getInstance().getCoins(this));

    }

    private void getCoinsHistoryList() {
        historyModals = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.COINS_HISTORY_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200) {
                                binding.progressBar3.setVisibility(View.GONE);


                                Log.d("getCoinsHistoryList", "onResponse: response Sucessfull: " + response.getString("data"));

                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    int hisCoins = jsonObject.getInt("coins");
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
                                    String timeMain = hisTime;


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
                                            dateMain, timeMain, hisCoins + "", isCredited, hisMsg
                                    );
                                    historyModals.add(historyModal);


                                }

                                if (historyModals.isEmpty()) {
                                    binding.progressBar3.setVisibility(View.GONE);
                                    binding.errorImg.setVisibility(View.GONE);
                                    binding.message.setVisibility(View.VISIBLE);
                                    binding.message.setText("Coin History is empty.");
                                }
                                else {

                                    binding.coinHistoryRv.setAdapter(new CoinHistoryAdapter(historyModals, CoinHistoryActivity.this));
                                    binding.coinHistoryRv.setLayoutManager(new LinearLayoutManager(CoinHistoryActivity.this));

                                }


                                ControlRoom.getInstance().setCoins(response.getInt("ava_coins") + "", CoinHistoryActivity.this);

                            } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                                Log.d("getCoinsHistoryList", "onResponse: response Failed: " + response.getString("data"));
                                binding.progressBar3.setVisibility(View.GONE);
                                binding.errorImg.setVisibility(View.VISIBLE);
                                binding.message.setVisibility(View.VISIBLE);
                                String msg = response.getString("data");
                                if (msg.equals("No coin transaction found!.."))
                                    binding.message.setText("No Coins history found.");
                                else
                                    binding.message.setText(response.getString(msg));

                            } else {
                                Log.d("getCoinsHistoryList", "onResponse: Something went wrong ");
                                binding.progressBar3.setVisibility(View.GONE);
                                binding.errorImg.setVisibility(View.VISIBLE);
                                binding.message.setVisibility(View.VISIBLE);
                                binding.message.setText("Something went wrong!");
                            }
                        } catch (JSONException | ParseException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getCoinsHistoryList", "onResponse: error response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(CoinHistoryActivity.this));
                return header;
            }
        };
        Volley.newRequestQueue(CoinHistoryActivity.this).add(jsonObjectRequest);


    }
}