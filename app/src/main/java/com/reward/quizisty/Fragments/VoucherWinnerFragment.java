package com.reward.quizisty.Fragments;


import static com.reward.quizisty.Utils.Constants.*;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reward.quizisty.Adapters.VoucherWinnersAdapter;
import com.reward.quizisty.Modals.VoucherWinModal;
import com.reward.quizisty.Utils.ControlRoom;
import com.reward.quizisty.databinding.FragmentVoucherWinnerBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VoucherWinnerFragment extends Fragment {
    FragmentVoucherWinnerBinding binding;
    static boolean isLoading = false;
    ArrayList<VoucherWinModal> voucherWinList;
    private int pageCount = 1;
    private int pageLimit;
    VoucherWinnersAdapter voucherWinnersAdapter;
    private String TAG = "VoucherWinnerFragment";

    public VoucherWinnerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVoucherWinnerBinding.inflate(inflater, container, false);

        voucherWinList = new ArrayList<>();
        initAdapter();
//        get 50 winners at first.
        getNewVoucherWinList(pageCount);

//        recyclerview reached bottom detection.
        initScrolling();





        return binding.getRoot();
    }

    private void initAdapter() {
        voucherWinnersAdapter = new VoucherWinnersAdapter(voucherWinList, getContext(), true);
        binding.vouWinRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.vouWinRv.setAdapter(voucherWinnersAdapter);

    }

    private void initScrolling() {
        binding.vouWinRv.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == voucherWinList.size() -1) {

                    Log.d(TAG, "onScrolled: isLoading: "+ isLoading);
                    isLoading = true;
//                        load more data
                        if (pageCount <= pageLimit) {
                            pageCount += 1;
                            getNewVoucherWinList(pageCount);
                        }
                    }
                }/*else {
                    Log.d(TAG, "onScrolled: isLoading: "+ false);
                    isLoading = true;
                }*/
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


//                don't fetch data while loading == true .
//                Log.d(TAG, "onScrolled: linearLayoutManager.findLastCompletelyVisibleItemPosition(): "+ linearLayoutManager.findLastCompletelyVisibleItemPosition());

            }
        });
    }

    private void getNewVoucherWinList(int pageCount) {
//        add null value to array list for loading view
        voucherWinList.add(null);

        voucherWinnersAdapter.notifyItemInserted(voucherWinList.size() - 1);



        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", pageCount);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, VOUCHER_WINNERS_NEW_API,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("getVoucherWinnersList", "onResponse: Sucessfull...:" + response);

                        pageLimit = response.getInt("total_page");
                        int totalWinners = response.getInt("total_item");

                        int removedItemIndex = voucherWinList.size()-1;
                        voucherWinList.remove(removedItemIndex);
                        voucherWinnersAdapter.notifyItemRemoved(removedItemIndex);

                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                            VoucherWinModal voucherWinModal = new VoucherWinModal(
                                    jsonObject.getString("userName"),
                                    jsonObject.getString("userImage"),
                                    jsonObject.getString("voucherName"),
                                    jsonObject.getString("voucherImage"),
                                    jsonObject.getString("mrp")

                            );
                            voucherWinModal.setVoucherWinnerCount(totalWinners - voucherWinList.size());

                            // Winning monnth todo: make Sectioned sticky header for month.
                            int monthNum = jsonObject.getInt("winning_month");
                            String month = "";

                            switch (monthNum) {
                                case 1:
                                    month = "January";
                                    break;
                                case 2:
                                    month = "February";
                                    break;
                                case 3:
                                    month = "March";
                                    break;
                                case 4:
                                    month = "April";
                                    break;
                                case 5:
                                    month = "May";
                                    break;
                                case 6:
                                    month = "June";
                                    break;
                                case 7:
                                    month = "July";
                                    break;
                                case 8:
                                    month = "August";
                                    break;
                                case 9:
                                    month = "September";
                                    break;
                                case 10:
                                    month = "October";
                                    break;
                                case 11:
                                    month = "November";
                                    break;
                                case 12:
                                    month = "December";
                                    break;

                            }
                            voucherWinModal.setWinnMonth(month);
                            voucherWinList.add(voucherWinModal);

                        }
                        voucherWinnersAdapter.notifyItemRangeInserted(removedItemIndex, 50);
                        isLoading = false;

                        if (pageCount == 1)
                            startValueAnimation(totalWinners);

                        // Empty List Handle

                        if (voucherWinList.isEmpty()) {
                            binding.vouWinRv.setVisibility(View.GONE);
                            binding.vouMessage2.setVisibility(View.VISIBLE);
                        } else {
//                            reverse list to descending order
//                            Collections.reverse(voucherWinList);



                        }


                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {

                        Log.d("getVoucherWinnersList", "onResponse: Failed..." + response.getString("data"));

                    } else {
                        Log.d("getVoucherWinnersList", "onResponse: something went wrong");

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getVoucherWinnersList", "onResponse: error Response: " + error.getMessage());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(requireActivity()));
                return header;
            }
        };
        Volley.newRequestQueue(binding.getRoot().getContext()).add(jsonObjectRequest);


    }



    private void startValueAnimation(int size) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, size);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                binding.vouWinCount.setText(valueAnimator.getAnimatedValue().toString());
            }
        });
        valueAnimator.start();
    }
}