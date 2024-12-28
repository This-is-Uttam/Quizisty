package com.reward.quizisty.Coins;

import static com.reward.quizisty.Utils.Constants.*;
import static com.reward.quizisty.Utils.Constants.USER_API_URL;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reward.quizisty.Modals.AdNetModal;
import com.reward.quizisty.Modals.BannerModal;
import com.reward.quizisty.Modals.BuyCoinModal;
import com.reward.quizisty.Modals.UserModal;
import com.reward.quizisty.R;
import com.reward.quizisty.Utils.ControlRoom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CoinsRepository {

    MutableLiveData<ArrayList<BannerModal>> banners = new MutableLiveData<>();
    MutableLiveData<ArrayList<BuyCoinModal>> buyCoinList = new MutableLiveData<>();
    MutableLiveData<ArrayList<AdNetModal>> watchVidList = new MutableLiveData<>();
    MutableLiveData<UserModal> user = new MutableLiveData<>();

    public LiveData<ArrayList<BannerModal>> getBannersList() {
        return banners;
    }

    void fetchBannerDataFromApi(Context context) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BANNER_API_URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("getPromotionImage", "onResponse: response Sucessfull: " + response.getString("data"));

                        ArrayList<BannerModal> bannerModals = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("data");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            Log.d("showPromotionsBanner", "showPromotionsBanner: image: " + jsonObject.getString("image"));

                            BannerModal promotionModal = new BannerModal(BANNER_IMG_URL + jsonObject.getString("image"),
                                    jsonObject.getString("banner_url"));
                            bannerModals.add(promotionModal);
                        }

                        Collections.reverse(bannerModals);
                        banners.setValue(bannerModals);


                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
                        Log.d("getPromotionImage", "onResponse: response Failed: " + response.getString("data"));
                    } else {
                        Log.d("getPromotionImage", "onResponse: Something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getPromotionImage", "onResponse: error ResPonse:  " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, "Bearer " + ControlRoom.getInstance().getAccessToken(context));
                return header;
            }
        };
        Volley.newRequestQueue(context).add(jsonObjectRequest);



    }

    LiveData<ArrayList<BuyCoinModal>> getBuyCoinsList() {
        return buyCoinList;
    }
    void fetchBuyCoinListFromApi(Context context) {
        ArrayList<BuyCoinModal> buyCoinModals = new ArrayList<>();

        buyCoinModals.add(new BuyCoinModal(
                9,10,0
        ));
        buyCoinModals.add(new BuyCoinModal(
                99,110,0
        ));
        buyCoinModals.add(new BuyCoinModal(
                999,1110,0
        ));
        buyCoinModals.add(new BuyCoinModal(
                9999,11110,0
        ));

        buyCoinList.setValue(buyCoinModals);
    }

    LiveData<ArrayList<AdNetModal>> getWatchVidList() {
        return watchVidList;
    }
    void fetchWatchVideoListFromApi(Context context) {
        ArrayList<AdNetModal> watchVidModals = new ArrayList<>();

        watchVidModals.add(new AdNetModal(
                R.drawable.video_svg, "Watch & Earn I",0
        ));
        watchVidModals.add(new AdNetModal(
                R.drawable.video_svg, "Watch & Earn II",1
        ));
        watchVidModals.add(new AdNetModal(
                R.drawable.video_svg, "Watch & Earn III",3
        ));
        watchVidList.setValue(watchVidModals);
    }

    public LiveData<UserModal> getUserData() {return user;}

    public void fetchUserDataFromApi(Context context) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, USER_API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        Log.d("updateCoins", "onResponse: Sucessfull...:" + response.getString("data"));
                        UserModal userData = new UserModal();
                        JSONObject userObj = response.getJSONObject("data");
                        userData.setCoins(userObj.getString("points"));
                        userData.setDiamonds(userObj.getString("daimond"));

                        ControlRoom.getInstance().setUserData(userObj, context);

                        user.setValue(userData);

                    } else
                        Log.d("updateCoins", "onResponse: Failed..." + response.getString("data"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("updateCoins", "onResponse: error Response: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                header.put(AUTHORISATION, BEARER + ControlRoom.getInstance().getAccessToken(context));
                return header;
            }
        };
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }
}
