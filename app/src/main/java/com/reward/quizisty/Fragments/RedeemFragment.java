package com.reward.quizisty.Fragments;

import static com.reward.quizisty.Utils.Constants.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reward.quizisty.Activities.VouchersActivity;
import com.reward.quizisty.Adapters.RedeemAdapter;
import com.reward.quizisty.Modals.RedeemModal;
import com.reward.quizisty.Utils.ControlRoom;
import com.reward.quizisty.databinding.FragmentRedeemBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RedeemFragment extends Fragment {
    FragmentRedeemBinding binding;
    ArrayList<RedeemModal> redeemList;
   RecyclerView giftRv;

    public RedeemFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentRedeemBinding.inflate(inflater,container, false);
//        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.transparent_blue, getActivity().getTheme()));

        binding.frameLayout.setVisibility(View.VISIBLE);
        binding.progressBar3.setVisibility(View.VISIBLE);
        binding.errorImg.setVisibility(View.GONE);
        binding.message.setVisibility(View.GONE);
        binding.coins.setText(ControlRoom.getInstance().getCoins(requireContext()));

        binding.vouDesc.setText(HtmlCompat.fromHtml(
                "Book your slot and get a chance to win vouchers of popular brands like Amazon, Flipkart and  more. <b>Kam Coins me Jyada Vouchers.</b>",
                0
        ));


        getAllRedeems();

        binding.redeemSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllRedeems();
                ControlRoom.getInstance().updateCoins(requireContext());
                binding.coins.setText(ControlRoom.getInstance().getCoins(requireContext()));
            }
        });

        binding.voucherCard.setOnClickListener(v -> startActivity(new Intent(requireContext(), VouchersActivity.class)));


        return binding.getRoot();
    }


    private void getAllRedeems() {
        redeemList = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REDEEM_URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200){
                        Log.d("getAllRedeems", "onResponse: response Sucessfull: "+ response.getString("data"));
                        binding.frameLayout.setVisibility(View.GONE);
                        //                      try catch

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int i=0; i<jsonArray.length();i++){
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                String redeemName = jsonObject.getString("name");  // redeem
                                String redeemImg = jsonObject.getString("image");
                                String redeemCoins = jsonObject.getString("coins");
                                String redeemAmt = jsonObject.getString("amount");
                                String redeemHint = jsonObject.getString("hint");
                                String redeemInputType = jsonObject.getString("input_type");
                                String redeemStatus = jsonObject.getString("status");

                                Log.d("getAllRedeems", "onResponse: statussss "+ redeemStatus);

                                RedeemModal redeemModal = new RedeemModal(
                                        redeemName, redeemCoins, redeemAmt, redeemHint, redeemInputType, redeemStatus, redeemImg);
                                redeemModal.setRedeemId(jsonObject.getInt("id"));
                                redeemModal.setVoucherSymbol(jsonObject.getString("symbol"));

                                redeemList.add(redeemModal);
                            }


//                        checking Gifts list empty.
                            if (redeemList.size() == 0) {
                                binding.redeemRv.setVisibility(View.GONE);
                                binding.emptyTxtGifts.setVisibility(View.VISIBLE);
                            } else {
                                binding.redeemRv.setVisibility(View.VISIBLE);
                                binding.emptyTxtGifts.setVisibility(View.GONE);
                                RedeemAdapter redeemAdapter = new RedeemAdapter(redeemList, getContext());
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
                                binding.redeemRv.setAdapter(redeemAdapter);
                                binding.redeemRv.setLayoutManager(gridLayoutManager);
                                binding.redeemRv.setNestedScrollingEnabled(false);
                            }



                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                            binding.redeemSwipeRefresh.setRefreshing(false);

                    }else if (!response.getBoolean("status") && response.getInt("code") == 201){
                        Log.d("getAllRedeems", "onResponse: response Failed: "+ response.getString("data"));
                        String responseMsg = response.getString("data");

                        if (responseMsg.equals("No Data Found")){
                            binding.errorImg.setVisibility(View.GONE);
                            binding.message.setText("No Gifts Available");
                            binding.message.setAlpha(0.6f);
                        }else {
                            binding.errorImg.setVisibility(View.VISIBLE);
                            binding.message.setText(responseMsg);
                        }
                        binding.frameLayout.setVisibility(View.VISIBLE);
                        binding.progressBar3.setVisibility(View.GONE);

                        binding.message.setVisibility(View.VISIBLE);
                        binding.redeemSwipeRefresh.setRefreshing(false);

                    }else {
                        Log.d("getAllRedeems", "onResponse: Something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getAllRedeems", "onResponse: error ResPonse:  " + error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(requireActivity()));
                return header;
            }
        };
        Volley.newRequestQueue(requireActivity()).add(jsonObjectRequest);
    }
}