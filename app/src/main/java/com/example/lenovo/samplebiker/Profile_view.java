package com.example.lenovo.samplebiker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;




public class Profile_view extends Activity {



        private TextView mTextDetails;
        private TextView eTextDetails;
        private TextView bTextDetails;
        private CallbackManager mCallbackManager;
        private AccessTokenTracker mTokenTracker;
        private ProfileTracker mProfileTracker;
        AccessToken token;
        public  String bd;

        private ProfilePictureView profilePic;



        private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("VIVZ", "onSuccess");
                token = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                mTextDetails.setText(constructWelcomeMessage(profile));
                profilePic = (ProfilePictureView) findViewById(R.id.myProfilePic);


                GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {

                        if (user != null) {
                            // set the profile picture using their Facebook ID
                            profilePic.setProfileId(user.optString("id"));
                        }
                    }
                }).executeAsync();

                GraphRequest mbrequest = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code

                                try {
                                    String email = object.getString("email");

                                    eTextDetails.setText(email);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                                try {
                                    String birthday = object.getString("birthday");
                                    bd=birthday;
                                    bTextDetails.setText(birthday);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, gender, birthday");
                mbrequest.setParameters(parameters);
                mbrequest.executeAsync();


            }




            @Override
            public void onCancel() {
                Log.d("oncancel", "on cancel");
                profilePic.setProfileId(String.valueOf(R.drawable.com_facebook_profile_picture_blank_portrait));
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("VIVZ", "onError " + e);
            }
        };


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.profile_view);
            FacebookSdk.sdkInitialize(getApplicationContext());
            mCallbackManager = CallbackManager.Factory.create();
            setupTokenTracker();
            setupProfileTracker();
            MyApplication O =new MyApplication();
            mTokenTracker.startTracking();
            mProfileTracker.startTracking();
            mTextDetails = (TextView) findViewById(R.id.text_details);
            eTextDetails = (TextView) findViewById(R.id.text_email);
            bTextDetails = (TextView) findViewById(R.id.text_bday);
            setupLoginButton();
            constructWelcomeMessage(Profile.getCurrentProfile());
            profilePic = (ProfilePictureView) findViewById(R.id.myProfilePic);

        }
        @Override
        public void onResume() {
            super.onResume();
            AppEventsLogger.activateApp(this);
            Profile profile = Profile.getCurrentProfile();
            mTextDetails.setText(constructWelcomeMessage(profile));
            profilePic = (ProfilePictureView) findViewById(R.id.myProfilePic);


            GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject user, GraphResponse response) {

                    if (user != null) {
                        profilePic.setProfileId(user.optString("id"));
                    }
                }
            }).executeAsync();


            bTextDetails = (TextView)findViewById(R.id.text_bday);




        }
        @Override
        protected void onPause() {
            super.onPause();

            // Logs 'app deactivate' App Event.
            AppEventsLogger.deactivateApp(this);
        }

        @Override
        public void onStop() {
            super.onStop();
            mTokenTracker.stopTracking();
            mProfileTracker.stopTracking();
            
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

        private void setupTokenTracker() {
            mTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                    Log.d("VIVZ", "" + currentAccessToken);
                    profilePic.setProfileId(String.valueOf(R.drawable.com_facebook_profile_picture_blank_portrait));
                    bTextDetails.setText(null);
                }
            };
        }

        private void setupProfileTracker() {
            mProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                    Log.d("VIVZ", "" + currentProfile);
                    mTextDetails.setText(constructWelcomeMessage(currentProfile));
                }
            };
        }


        private void setupLoginButton() {
            LoginButton mButtonLogin = (LoginButton) findViewById(R.id.login_button);
            mButtonLogin.setReadPermissions("user_friends");
            mButtonLogin.setReadPermissions("public_profile");
            mButtonLogin.setReadPermissions("email");
            mButtonLogin.setReadPermissions("user_birthday");
            mButtonLogin.registerCallback(mCallbackManager, mFacebookCallback);
        }

        private String constructWelcomeMessage(Profile profile) {
            StringBuffer stringBuffer = new StringBuffer();
            if (profile != null) {
                stringBuffer.append("\n " + profile.getName() );

            }
            return stringBuffer.toString();
        }

}