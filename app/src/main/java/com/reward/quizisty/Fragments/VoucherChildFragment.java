package com.reward.quizisty.Fragments;

import static com.reward.quizisty.Utils.Constants.AUTHORISATION;
import static com.reward.quizisty.Utils.Constants.BEARER;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE_VALUE;
import static com.reward.quizisty.Utils.Constants.VOUCHER_MAIN_URL;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reward.quizisty.Adapters.VoucherMainAdapter;
import com.reward.quizisty.Modals.VoucherMainModal;
import com.reward.quizisty.Utils.ControlRoom;
import com.reward.quizisty.databinding.FragmentVoucherChildBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class VoucherChildFragment extends Fragment {
 FragmentVoucherChildBinding binding;
    public VoucherChildFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVoucherChildBinding.inflate( inflater, container, false);


        //      fetch vouchers from server
        getVoucherMainList();
        return  binding.getRoot();
    }

    private void getVoucherMainList() {
        binding.vouProgressBar.setVisibility(View.VISIBLE);
        ArrayList<VoucherMainModal> voucherMainList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, VOUCHER_MAIN_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getBoolean("status") && response.getInt("code") == 200){
                                Log.d("getVoucherMainList", "onResponse: response Sucessfull: "+ response.getString("data"));
                                binding.vouProgressBar.setVisibility(View.GONE);
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String id = jsonObject.getString("id");
                                    String name = jsonObject.getString("name");
                                    String mrp = jsonObject.getString("mrp");
                                    int price_per_spot = jsonObject.getInt("price_per_spot");
                                    String total_spot = jsonObject.getString("total_spot");
                                    String empty_spot = jsonObject.getString("empty_spot");
                                    String winnig_code = jsonObject.getString("winnig_code");
                                    String winnig_daimonds = jsonObject.getString("winnig_daimonds");
                                    String short_desc = jsonObject.getString("short_desc");
                                    String details = jsonObject.getString("details");
                                    String images = jsonObject.getString("images");
                                    String full_status = jsonObject.getString("full_status");
                                    boolean full_status_bool;
                                    if (Integer.parseInt(full_status)==0){
                                        full_status_bool = false;
                                    }else {
                                        full_status_bool= true;
                                    }



                                    VoucherMainModal voucherMainModal = new VoucherMainModal(
                                            "2",empty_spot,total_spot,name,price_per_spot,short_desc,details,id,images,
                                            full_status_bool,winnig_code);

                                    voucherMainModal.setMrp(jsonObject.getString("mrp"));

                                    voucherMainList.add(voucherMainModal);
                                }
                                Log.d("getVoucherMainList", "onResponse: Voucher List Size: "+voucherMainList.size());
//                        checking voucher winners list empty.
                                if (voucherMainList.size() == 0) {
                                    binding.voucherRv.setVisibility(View.GONE);
                                    binding.vouMessage.setVisibility(View.VISIBLE);
                                } else {
                                    binding.voucherRv.setVisibility(View.VISIBLE);
                                    binding.vouMessage.setVisibility(View.GONE);

                                    VoucherMainAdapter voucherDetailAdapter  = new VoucherMainAdapter(getSortedArrayList(voucherMainList),
                                            requireContext());
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
                                    binding.voucherRv.setAdapter(voucherDetailAdapter);
                                    binding.voucherRv.setLayoutManager(layoutManager);
                                    binding.voucherRv.setNestedScrollingEnabled(false);
                                }



                            }else if (!response.getBoolean("status") && response.getInt("code") == 201){
                                Log.d("getVoucherMainList", "onResponse: response Failed: "+ response.getString("data"));
                                binding.vouProgressBar.setVisibility(View.GONE);
                                binding.vouMessage.setVisibility(View.VISIBLE);
                                binding.vouMessage.setText(response.getString("data"));
                            }else {
                                Log.d("getVoucherMainList", "onResponse: Something went wrong");
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getVoucherMainList", "onResponse: error ResPonse:  " + error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(requireContext()));
                return header;
            }
        };
        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);

    }

    public static ArrayList<VoucherMainModal> getSortedArrayList(ArrayList<VoucherMainModal> voucherMainList) {
        ArrayList<VoucherMainModal> voucherNewList = new ArrayList<>();
        ArrayList<VoucherMainModal> voucherEmptyList = new ArrayList<>();

        // Empty spot sorting

        for (int i = 0; i < voucherMainList.size(); i++) {
            VoucherMainModal voucher = voucherMainList.get(i);

            if (voucher.getSpotLeftText().equals("0")) {

                voucherEmptyList.add(voucher);
            } else {
                voucherNewList.add(voucher);

            }
        }

        // Price sorting;
        voucherNewList.sort(new Comparator<VoucherMainModal>() {
            @Override
            public int compare(VoucherMainModal voucherMainModal, VoucherMainModal t1) {
                if (voucherMainModal.getVouPricePerSpot() == t1.getVouPricePerSpot())
                    return 0;
                else if (voucherMainModal.getVouPricePerSpot() > t1.getVouPricePerSpot())
                    return 1;
                else
                    return -1;
            }
        });

        voucherNewList.addAll(voucherEmptyList);
        return voucherNewList;
    }

}