package com.reward.quizisty.Adapters;

import static com.reward.quizisty.Utils.Constants.AUTHORISATION;
import static com.reward.quizisty.Utils.Constants.BEARER;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE_VALUE;
import static com.reward.quizisty.Utils.Constants.MILESTONES_CLAIM_REQUEST_API;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reward.quizisty.Activities.MyInfoActivity;
import com.reward.quizisty.Modals.MileStonesModal;
import com.reward.quizisty.R;
import com.reward.quizisty.Utils.ControlRoom;
import com.reward.quizisty.databinding.MilestoneRvItemBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MileStoneAdapter extends RecyclerView.Adapter<MileStoneAdapter.Viewholder> {

    ArrayList<MileStonesModal> mileStonesModalArrayList;
    Context context;
    private String TAG = "MileStoneAdapter";

    public MileStoneAdapter(ArrayList<MileStonesModal> mileStonesModalArrayList, Context context) {
        this.mileStonesModalArrayList = mileStonesModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MileStoneAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.milestone_rv_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MileStoneAdapter.Viewholder holder, int position) {
        MileStonesModal mileStonesModal = mileStonesModalArrayList.get(position);
        if (mileStonesModal.getRewardType() == 0) {
//            reward is Coin
            holder.binding.rewardName.setText(
                    "Get " + mileStonesModal.getPrize() + " Coins for " + mileStonesModal.getRefers() + " Refers.");
//             hide image
            holder.binding.prodCard.setVisibility(View.GONE);

        } else {
//          reward is product
            holder.binding.rewardName.setText(
                    "Get " + mileStonesModal.getPrize() + " for " + mileStonesModal.getRefers() + " Refers.");

//            Image show
            holder.binding.prodCard.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(mileStonesModal.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.binding.productImg);

        }

        if (mileStonesModal.getClaimStatus() == 0) {
            Log.d(TAG, "onBindViewHolder: Status 0");
//            claim button disable
            holder.binding.claimMainBtn.setVisibility(View.VISIBLE);
            holder.binding.claimMainBtn.setEnabled(false);
            holder.binding.claimMainBtn.setClickable(false);
//            remove all other buttons
            holder.binding.claimedBtn.setVisibility(View.GONE);
            holder.binding.pendingBtn.setVisibility(View.GONE);
            holder.binding.rejectedBtn.setVisibility(View.GONE);
//            show rejected info
            holder.binding.infoText.setVisibility(View.GONE);
            //            check junction view
            holder.binding.junctionView.setBackground(ResourcesCompat.getDrawable(
                    context.getResources(), R.drawable.grey3_circle, context.getTheme()));
//            colored junction path
            holder.binding.junctionPath.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(
                    context.getResources(), R.color.grey_3, context.getTheme())));

        } else if (mileStonesModal.getClaimStatus() == 1) {
            Log.d(TAG, "onBindViewHolder: Status 1");
//            claim button enable
            holder.binding.claimMainBtn.setVisibility(View.VISIBLE);
            holder.binding.claimMainBtn.setEnabled(true);
            holder.binding.claimMainBtn.setClickable(true);
//            check junction view
            holder.binding.junctionView.setBackground(ResourcesCompat.getDrawable(
                    context.getResources(), R.drawable.green_circle, context.getTheme()));
//            holder.binding.junctionView.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(
//                    context.getResources(), R.color.green_tick_color, context.getTheme())));
//            colored junction path
            holder.binding.junctionPath.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(
                    context.getResources(), R.color.md_theme_primaryFixedDim_mediumContrast, context.getTheme())));
//            remove all other buttons
            holder.binding.claimedBtn.setVisibility(View.GONE);
            holder.binding.pendingBtn.setVisibility(View.GONE);
            holder.binding.rejectedBtn.setVisibility(View.GONE);
//            show rejected info
            holder.binding.infoText.setVisibility(View.GONE);

        } else if (mileStonesModal.getClaimStatus() == 2) {
            Log.d(TAG, "onBindViewHolder: Status 2");
//            pending button visible
            holder.binding.pendingBtn.setVisibility(View.VISIBLE);
//            check junction view
            holder.binding.junctionView.setBackground(ResourcesCompat.getDrawable(
                    context.getResources(), R.drawable.green_circle, context.getTheme()));
//            holder.binding.junctionView.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(
//                    context.getResources(), R.color.green_tick_color, context.getTheme())));
//            colored junction path
            holder.binding.junctionPath.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(
                    context.getResources(), R.color.md_theme_primaryFixedDim_mediumContrast, context.getTheme())));

//            remove all other buttons
            holder.binding.claimMainBtn.setVisibility(View.GONE);
            holder.binding.claimMainBtn.setEnabled(false);
            holder.binding.claimMainBtn.setClickable(false);
            holder.binding.claimedBtn.setVisibility(View.GONE);
            holder.binding.rejectedBtn.setVisibility(View.GONE);
//            show rejected info
            holder.binding.infoText.setVisibility(View.GONE);

        } else if (mileStonesModal.getClaimStatus() == 3) {
            Log.d(TAG, "onBindViewHolder: Status 3");
//            claimedd button visible
            holder.binding.claimedBtn.setVisibility(View.VISIBLE);
//            check junction view
            holder.binding.junctionView.setBackground(ResourcesCompat.getDrawable(
                    context.getResources(), R.drawable.tick_circle, context.getTheme()));
//            colored junction path
            holder.binding.junctionPath.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(
                    context.getResources(), R.color.md_theme_primaryFixedDim_mediumContrast, context.getTheme())));

//            remove all other buttons
            holder.binding.claimMainBtn.setVisibility(View.GONE);
            holder.binding.claimMainBtn.setEnabled(false);
            holder.binding.claimMainBtn.setClickable(false);

            holder.binding.pendingBtn.setVisibility(View.GONE);
            holder.binding.rejectedBtn.setVisibility(View.GONE);
//            show rejected info
            holder.binding.infoText.setVisibility(View.GONE);
        } else if (mileStonesModal.getClaimStatus() == 4) {

//            rejected button visible
            holder.binding.rejectedBtn.setVisibility(View.VISIBLE);
//            check junction view
            holder.binding.junctionView.setBackground(ResourcesCompat.getDrawable(
                    context.getResources(), R.drawable.cross_circle, context.getTheme()));
//            colored junction path
            holder.binding.junctionPath.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(
                    context.getResources(), R.color.md_theme_primaryFixedDim_mediumContrast, context.getTheme())));
//            show rejected info
            holder.binding.infoText.setVisibility(View.VISIBLE);
            holder.binding.infoText.setText(mileStonesModal.getRejectReason());

//            remove all other buttons
            holder.binding.claimMainBtn.setVisibility(View.GONE);
            holder.binding.claimMainBtn.setEnabled(false);
            holder.binding.claimMainBtn.setClickable(false);

            holder.binding.claimedBtn.setVisibility(View.GONE);
            holder.binding.pendingBtn.setVisibility(View.GONE);
        }

        holder.binding.claimMainBtn.setOnClickListener(v -> {
//            Request claim and set the button to pending.
            claimRequest(holder, mileStonesModal.getId(), mileStonesModal);

        });
    }

    private void claimRequest(Viewholder holder, int id, MileStonesModal mileStonesModal) {

//        Check if the reward is product if so, show address bottom sheet.


        if (mileStonesModal.getRewardType() == 0) {
            // Reward is Coin
            sendRequest(id,holder);
            Log.d(TAG, "claimRequest: clicked! id: "+id);

        } else {
            // Reward is product
//            Show Address bottom sheet here...
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            View addressLayoutView = LayoutInflater.from(context).inflate(R.layout.address_layout, null);
            bottomSheetDialog.setContentView(addressLayoutView);
            bottomSheetDialog.show();

            MaterialButton editInfo = addressLayoutView.findViewById(R.id.editMyInfoButton);
            MaterialButton comfirmClaimReq = addressLayoutView.findViewById(R.id.claimConfirmButton);


            setUserInfo(context, addressLayoutView);

//            Edit My info
            editInfo.setOnClickListener(v -> {
                context.startActivity(new Intent(context, MyInfoActivity.class));
                bottomSheetDialog.cancel();
            });

//             send product claim request
            comfirmClaimReq.setOnClickListener(v -> {
                sendRequest(id, holder);
                bottomSheetDialog.cancel();
            });

        }


    }

    public void setUserInfo(Context context, View addressLayoutView) {

//            Show user details in bottom sheet from local database.
        String fullName = ControlRoom.getInstance().getFullName(context);
        String dob = ControlRoom.getInstance().getDob(context);
        if (dob.equals("null") || dob.isEmpty()) {
            dob = "Not provided";
        }
        String gender = ControlRoom.getInstance().getGender(context);
        String phone = ControlRoom.getInstance().getPhone(context);
        if (phone.equals("null") || phone.isEmpty()) {
            phone = "Not provided";
        }
        String address = ControlRoom.getInstance().getAddress(context);
        if (address.equals("null") || address.isEmpty()) {
            address = "Not provided";
        }
        String pinCode = ControlRoom.getInstance().getPinCode(context);
        if (pinCode.equals("null") || pinCode.isEmpty()) {
            pinCode = "Not provided";
        }

//            Set values in address layout.
        TextView nameTv = addressLayoutView.findViewById(R.id.name);
        TextView dobTv = addressLayoutView.findViewById(R.id.dob);
        TextView genderTv = addressLayoutView.findViewById(R.id.gender);
        TextView phoneNoTv = addressLayoutView.findViewById(R.id.phoneNo);
        TextView addressTv = addressLayoutView.findViewById(R.id.address);
        TextView pinCodeTv = addressLayoutView.findViewById(R.id.pincode);

        nameTv.setText(fullName);
        dobTv.setText(dob);
        genderTv.setText(gender);
        phoneNoTv.setText(phone);
        addressTv.setText(address);
        pinCodeTv.setText(pinCode);
    }

    private void sendRequest(int id, Viewholder holder) {

        holder.binding.claimMainBtn.setVisibility(View.GONE);
        holder.binding.mileStoneClaimProgress.setVisibility(View.VISIBLE);

        JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("refer_earn_id", String.valueOf(id));
//                jsonObject.put("address", "");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MILESTONES_CLAIM_REQUEST_API,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
//                        Log.d(TAG, "onResponse: response Sucessfull: " + response.getString("data"));

                        holder.binding.mileStoneClaimProgress.setVisibility(View.GONE);
                        holder.binding.pendingBtn.setVisibility(View.VISIBLE);
//                        notifyDataSetChanged();

                        //       Alert dialog after successful claim request.
                        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(context);
                        alertDialogBuilder.setCancelable(false)
                                .setIcon(R.drawable.tick_circle_white)
                                .setTitle("Claim Request Sent!")
                                .setMessage("Your request have been sent successfully. We will review your request, if it is valid you will receive your reward." +
                                        "\n\nIf the reward is 'Product' type, we will contact you through your provided information.")
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setCancelable(true)
                                .show();


                    } else {
                        Log.d(TAG, "onResponse: Something went wrong");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onResponse: error ResPonse:  " + error.getMessage());
                holder.binding.mileStoneClaimProgress.setVisibility(View.GONE);
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

//    reward = 'coin/product'

    @Override
    public int getItemCount() {
        return mileStonesModalArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        MilestoneRvItemBinding binding;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = MilestoneRvItemBinding.bind(itemView);
        }
    }
}
