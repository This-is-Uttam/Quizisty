package com.reward.quizisty.Fragments;

import static com.reward.quizisty.Utils.Constants.AUTHORISATION;
import static com.reward.quizisty.Utils.Constants.BEARER;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE;
import static com.reward.quizisty.Utils.Constants.CONTENT_TYPE_VALUE;
import static com.reward.quizisty.Utils.Constants.LOGOUT_API_URL;
import static com.reward.quizisty.Utils.Constants.PRIVACY_POLICY;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reward.quizisty.Activities.LoginActivity;
import com.reward.quizisty.Activities.MyInfoActivity;
import com.reward.quizisty.Activities.RedeemHistoryActivity;
import com.reward.quizisty.Activities.VoucherHistoryActivity;
import com.reward.quizisty.R;
import com.reward.quizisty.Utils.Constants;
import com.reward.quizisty.Utils.ControlRoom;
import com.reward.quizisty.databinding.FragmentMoreBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.playtimeads.PlaytimeAds;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MoreFragment extends Fragment {

    private static final int CAMERA_PERMISSION_CODE = 0;
    ActivityResultLauncher<Intent> galleryLauncher, cameraLauncher;
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int GALLERY_REQUEST_CODE = 10;
    public static final String WEB_CLIENT_ID = "724526109677-7jhs2433o51gq6hh56eo1rro8ubmnqnb.apps.googleusercontent.com";
    GoogleSignInClient gsc;
    Context context;
    FragmentMoreBinding binding;
    AlertDialog dialog;
    View progressDialog;
    String fullName, userName, email, phone, profilePic;

    public MoreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(inflater, container, false);


//      myInfo
        binding.constraintLayoutMyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), MyInfoActivity.class));
            }
        });
//        Product bidding
        binding.constraintLayoutProdBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getContext(), BiddingHistoryActivity.class));
            }
        });
//        voucher bidding
        binding.constraintLayoutVouBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), VoucherHistoryActivity.class));
            }
        });
//         Redeem History
        binding.constrLayoutRedeemHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), RedeemHistoryActivity.class));
            }
        });
//        Payment History
        binding.constraintLayoutPayHistory.setVisibility(View.GONE);
        binding.constraintLayoutPayHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getContext(), PaymentHistoryActivity.class));
            }
        });


        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(new CustomTabColorSchemeParams.Builder()
                        .setToolbarColor(
                                ResourcesCompat.getColor(getResources(),
                                        R.color.md_theme_primary, requireContext().getTheme()))
                        .build())
                .build();

//        Privacy Policy
        binding.privacyPolicyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customTabsIntent.launchUrl(context, Uri.parse(PRIVACY_POLICY));
            }
        });
//        Term & Condition
        binding.tnCLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customTabsIntent.launchUrl(requireActivity(), Uri.parse(Constants.TERMS_N_CONDITION));
            }
        });
//         Shopping & Refund Policy
        binding.shopNrefundPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customTabsIntent.launchUrl(requireActivity(), Uri.parse(Constants.SHOPPING_REFUND));
            }
        });
//         Return & Replacement Policy
        binding.returnNreplacementLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customTabsIntent.launchUrl(requireActivity(), Uri.parse(Constants.REFUND_CANCELLATION));
            }
        });
//         About Us
        binding.aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customTabsIntent.launchUrl(requireActivity(), Uri.parse(Constants.ABOUT_US));
            }
        });
//         Contact Us
        binding.contactUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customTabsIntent.launchUrl(requireActivity(), Uri.parse(Constants.CONTACT_US));
            }
        });


        //        editProfileImg
/*
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Uri uri = result.getData().getData();
                    if (uri == null) {
                        Toast.makeText(getContext(), "Uri null", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getContext(), ImageCropperActivity.class);
                        intent.putExtra("GALLERY", uri.toString());
                        startActivityForResult(intent, GALLERY_REQUEST_CODE);

//                        binding.profileImageView.setImageURI(uri);
//
//                        binding.userFirstLetter.setVisibility(View.INVISIBLE);
                    }


                }

            }
        });
*/

/*
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Bitmap bitmap = null;
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = null;
                    bitmap = (Bitmap) result.getData().getExtras().get("data");

                    String image = MediaStore.Images.Media.insertImage(
                            getContext().getContentResolver(), bitmap, "cropImg", null);


                    if (image != null) {

                        Intent uCropIntent = new Intent(getContext(), ImageCropperActivity.class);
                        assert result.getData() != null;
                        uCropIntent.putExtra("RAW_IMG", image);
                        startActivityForResult(uCropIntent, CAMERA_REQUEST_CODE);
                    } else {

                        Toast.makeText(context, "No Image Selected", Toast.LENGTH_SHORT).show();

                    }


                } else {

                    Toast.makeText(context, "No Image Selected", Toast.LENGTH_SHORT).show();
                }


            }


        });
*/


//        binding.editProfileImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
//
//                View bottomDialogView = getActivity().getLayoutInflater().inflate(R.layout.bottom_dialog, null, false);
//
//                ImageView galleryIcon = bottomDialogView.findViewById(R.id.galleryIcon);
//                ImageView cameraIcon = bottomDialogView.findViewById(R.id.cameraIcon);
//
//                galleryIcon.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                        galleryIntent.setType("image/*");
//                        galleryLauncher.launch(galleryIntent);
//                        bottomSheetDialog.dismiss();
//
//
//                    }
//                });
//
//                cameraIcon.setOnClickListener(new View.OnClickListener() {
//                    @RequiresApi(api = Build.VERSION_CODES.R)
//                    @Override
//                    public void onClick(View view) {
//                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) !=
//                                PackageManager.PERMISSION_GRANTED) {
//
//                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 000);
//
//                        } else {
//                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            cameraLauncher.launch(cameraIntent);
//                            bottomSheetDialog.dismiss();
//                        }
//
//
//                    }
//                });
//
//                bottomSheetDialog.setContentView(bottomDialogView);
//                bottomSheetDialog.setCanceledOnTouchOutside(true);
//                bottomSheetDialog.show();
//            }
//        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();
        context = requireContext();

        gsc = GoogleSignIn.getClient(context, gso);

        checkSignIn();
//         Logout
        binding.logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogLayout = LayoutInflater.from(context).inflate(R.layout.alert_dialog_layout, null);
                AlertDialog.Builder dailogBuilder = new AlertDialog.Builder(context, R.style.CustomDialog);

                TextView dialogTitle = dialogLayout.findViewById(R.id.dialogTitle);
                TextView dialogSubtitle = dialogLayout.findViewById(R.id.dialogSubtitle);
                TextView positiveBtn = dialogLayout.findViewById(R.id.positiveBtn);
                TextView negativeBtn = dialogLayout.findViewById(R.id.negativeBtn);
                progressDialog = dialogLayout.findViewById(R.id.alertProgress);
                progressDialog.setVisibility(View.GONE);

                dialogTitle.setText("Sign Out");
                dialogSubtitle.setText("Do you want to Sign out your account");

                positiveBtn.setText("Signout");
                negativeBtn.setText("Cancel");

                TextView signout = dialogLayout.findViewById(R.id.positiveBtn);
                TextView cancel = dialogLayout.findViewById(R.id.negativeBtn);

                dailogBuilder.setView(dialogLayout);
                dialog = dailogBuilder.create();

                signout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        userSignOut();
                        progressDialog.setVisibility(View.VISIBLE);

                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

//        setting User Profile Image
        Picasso.get()
                .load(ControlRoom.getInstance().getProfilePic(requireContext()))
                .placeholder(R.drawable.placeholder)
                .into(binding.profileImageView);

        fullName = ControlRoom.getInstance().getFullName(requireContext());
        userName = ControlRoom.getInstance().getUserName(requireContext());
        email = ControlRoom.getInstance().getEmail(requireContext());
        phone = ControlRoom.getInstance().getPhone(requireContext());

        setCurrentUserDetails(fullName, userName, email, phone);


        try {
            String appVersionName = requireActivity()
                    .getPackageManager()
                    .getPackageInfo(requireActivity().getPackageName(),0)
                    .versionName;
            binding.versionTxt.setText("App version: "+appVersionName);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        return binding.getRoot();
    }

    private void setCurrentUserDetails(String fullName, String userName, String email, String phone) {

        if (fullName.equals("null"))
            binding.fullName.setText("Not found");
        else
            binding.fullName.setText(fullName);

        if (email.equals("null"))
            binding.emailTxt.setText("Not found");
        else
            binding.emailTxt.setText(email);

        if (userName.equals("null"))
            binding.userName.setText("Not found");
        else
            binding.userName.setText(userName);

        if (phone.equals("null"))
            binding.phoneTxt.setText("Not found");
        else
            binding.phoneTxt.setText(phone);


//        binding.userFirstLetter.setVisibility(View.INVISIBLE);
    }

    private void userSignOut() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, LOGOUT_API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") && response.getInt("code") == 200) {
                        String data = response.getString("data");
                        Log.d("userSignOut", "onResponse: Sucessfull, data: " + data);
                        //Q. Either signOut first in firebase(client) or that in api??
                        //Ans. First signOut from Api then from firebase.

                        PlaytimeAds.getInstance().destroy(); // clears your previous session of Playtime ads.
                        gsc.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "You are Signed out successfully", Toast.LENGTH_SHORT).show();
                                Log.d("userSignOut", "onSuccess: Sign Out from Google Firebase after Sucessfull ResPonse");
                                dialog.dismiss();
                                FirebaseMessaging.getInstance().deleteToken();
                                progressDialog.setVisibility(View.GONE);
                                startActivity(new Intent(getContext(), LoginActivity.class));
                                getActivity().finish();
                            }
                        });


                    } else if (!response.getBoolean("status") && response.getInt("code") == 201) {
//                        Log.d("signout token", "signout token: " + accessToken);

                        Log.d("userSignOut", "onResponse: Failed, data: " + response.getString("data"));
                    } else
                        Log.d("userSignOut", "onResponse: Failed, Something went wrong");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("userSignOut", "onResponse: error response");
                progressDialog.setVisibility(View.GONE);
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
        Volley.newRequestQueue(context).add(jsonObjectRequest);
        // Either signout first in firebase(client) or that in api??
        /*gsc.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "You are Signed out successfully", Toast.LENGTH_SHORT).show();
                Log.d("userSignOut", "onSuccess: Direct Sign Out from Google Firebase ");
                dialog.dismiss();
                progressDialog.setVisibility(View.GONE);
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });*/

    }

    private void checkSignIn() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        if (account == null) {
            binding.logOutButton.setVisibility(View.GONE);
        }
    }

    private void handleResult() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        if (account != null) {

            Log.d("oooo", "handleResult: nAme " + account.getDisplayName() + " pHoto " + account.getPhotoUrl()
                    + " email :" + account.getEmail());

            binding.fullName.setText(account.getDisplayName());
            binding.emailTxt.setText(account.getEmail());
            Picasso.get()
                    .load(account.getPhotoUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(binding.profileImageView);
//            binding.userFirstLetter.setVisibility(View.INVISIBLE);
            binding.logOutButton.setVisibility(View.VISIBLE);
            Log.d("ooo", "handleResult: google Token: " + account.getIdToken());

        } else Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String finalImg;
        Uri finalUri;
        if (resultCode == 2 && requestCode == CAMERA_REQUEST_CODE) {
            if (data != null) {
                finalImg = data.getStringExtra("FINAL_URI");
                finalUri = Uri.parse(finalImg);
                binding.profileImageView.setImageURI(finalUri);
            }

        } else if (resultCode == 2 && requestCode == GALLERY_REQUEST_CODE) {
            if (data != null) {
                finalImg = data.getStringExtra("FINAL_URI");
                finalUri = Uri.parse(finalImg);
                binding.profileImageView.setImageURI(finalUri);
            }
        }
//        binding.userFirstLetter.setVisibility(View.INVISIBLE);
    }
}