package np.com.pradipkharbuja.flappybee;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import np.com.pradipkharbuja.flappybee.core.FlappyBee;
import np.com.pradipkharbuja.flappybee.core.misc.Constants;
import np.com.pradipkharbuja.flappybee.core.service.DialogInterface;
import np.com.pradipkharbuja.flappybee.core.states.PlayState;


public class AndroidLauncher extends AndroidApplication implements DialogInterface {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        // Create the libgdx View
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        View gameView = initializeForView(new FlappyBee(this), config);

        preferences = preferences = Gdx.app.getPreferences(Constants.PREFS_NAME);
        initDialog();

        // Create and setup the AdMob view
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);

        //My Ad
        adView.setAdUnitId("ca-app-pub-3539844738735929/7725607136");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Create the layout
        RelativeLayout layout = new RelativeLayout(this);

        // Add the libgdx view
        layout.addView(gameView);

        // Add the AdMob view
        //layout.addView(adView);

        setContentView(layout);
    }

    private Dialog waitDialog;
    private Handler dialogHandler;

    private Preferences preferences;

    private EditText etFullName, etEmail, etMobileNumber;

    private void initDialog() {
        dialogHandler = new Handler();
        waitDialog = new Dialog(this);
        waitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        waitDialog.setContentView(R.layout.main_layout);
        waitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        waitDialog.setCancelable(true);

        etFullName = (EditText) waitDialog.findViewById(R.id.etFullName);
        etEmail = (EditText) waitDialog.findViewById(R.id.etEmail);
        etMobileNumber = (EditText) waitDialog.findViewById(R.id.etMobileNumber);

        waitDialog.findViewById(R.id.btnUpdateProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                preferences.putString(Constants.FULL_NAME, etFullName.getText().toString());
                preferences.putString(Constants.EMAIL, etEmail.getText().toString());
                preferences.putString(Constants.MOBILE_NUMBER, etMobileNumber.getText().toString());
                preferences.flush();

                Toast.makeText(AndroidLauncher.this, "Profile Updated!", Toast.LENGTH_SHORT).show();

                hideDialog();
            }
        });
    }

    @Override
    public void showDialog() {
        etFullName.setText(preferences.getString(Constants.FULL_NAME));
        etEmail.setText(preferences.getString(Constants.EMAIL));
        etMobileNumber.setText(preferences.getString(Constants.MOBILE_NUMBER));

        dialogHandler.post(showDialogRunnable);
    }

    final Runnable showDialogRunnable = new Runnable() {
        public void run() {
            // TODO Auto-generated method stub
            if (waitDialog != null && !waitDialog.isShowing())
                waitDialog.show();
        }

    };

    @Override
    public void hideDialog() {
        dialogHandler.post(hideDialogRunnable);
    }

    final Runnable hideDialogRunnable = new Runnable() {
        public void run() {
            if (waitDialog != null && waitDialog.isShowing()) {
                waitDialog.dismiss();
            }
        }
    };

    @Override
    public void postScore() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        postData();
                    }
                });
            }
        });
        thread.start();
    }

    private void postData() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://hamrobhaktapur.com/score/?";

        Map<String, String> params = new HashMap<>();
        params.put("fullName", preferences.getString(Constants.FULL_NAME));
        params.put("email", preferences.getString(Constants.EMAIL));
        params.put("mobileNumber", preferences.getString(Constants.MOBILE_NUMBER));
        params.put("score", "" + PlayState.point);

        for (Map.Entry entry : params.entrySet()) {
            if (entry.getValue().toString().trim().equals("")) {
                Toast.makeText(this, "Please complete the profile before posting data.", Toast.LENGTH_SHORT).show();
                return;
            }

            url += entry.getKey() + "=" + Uri.encode(entry.getValue().toString().trim()) + "&";
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String message = "";
                        try {
                            boolean status = response.getBoolean("success");
                            if (status) {
                                message = "Your score has been posted successfully.";
                            } else {
                                message = "Oops! Error occurred while submitting score.";
                            }
                        } catch (Exception ex) {
                            message = "Error occurred while submitting score.";
                        }

                        Toast.makeText(AndroidLauncher.this, message, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AndroidLauncher.this, "Your score couldn't be posted. Please check the network connection.", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsObjRequest);
    }
}
