package com.ocean.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.ocean.whatsappclone.databinding.ActivityLoginBinding;
import com.ocean.whatsappclone.model.UserModel;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity : --";
    ActivityLoginBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    FirebaseDatabase firebaseDatabase;


    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //TODO: hide action bar
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("....Signing in !!!");

        //TODO: onclick events
        binding.tvSignupLinkLogin.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        binding.btnSignupUsingFacebookLogin.setOnClickListener(this);
        binding.btnSignupUsingGoogleLogin.setOnClickListener(this);
        binding.tvSignupWithPhoneLogin.setOnClickListener(this);

        //TODO: logged in session using firebase
        if (firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

        //TODO: configure google sign in
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient =  GoogleSignIn.getClient(LoginActivity.this, signInOptions);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_signup_link_login:
                //TODO: redirect to signup activity
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
            case R.id.btn_login:
                //TODO: sign in code
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(binding.etEmailLogin.getText().toString(), binding.etPasswordLogin.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()){
                                    Log.d(TAG, "onComplete: ");
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                    finish();
                                }else {
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.btn_signup_using_facebook_login:
                break;
            case R.id.btn_signup_using_google_login:
                signInWithGoogle();
                break;
            case R.id.tv_signup_with_phone_login:
                startActivity(new Intent(LoginActivity.this, SignUpWithPhoneNumActivity.class));
                break;
        }
    }

    private void signInWithGoogle() {
        Intent signInGoogle = googleSignInClient.getSignInIntent();
        startActivityForResult(signInGoogle, REQ_ONE_TAP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ONE_TAP){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount signInAccount = task.getResult(ApiException.class);
                Log.d(TAG, "onActivityResult: " + signInAccount.getId());

                firebaseAuthWithGoogle(signInAccount.getIdToken());

            }catch (ApiException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onActivityResult: " , e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();//todo: error firebaseUser is null
                            UserModel userModel = new UserModel();

                            userModel.setUserID(firebaseUser.getUid());
                            userModel.setUserName(firebaseUser.getDisplayName());
                            userModel.setProfilePic(firebaseUser.getPhotoUrl().toString());

                            firebaseDatabase.getReference().child("Users").child(firebaseUser.getUid()).setValue(userModel);

                            Log.d(TAG, "onComplete: signInWithCredential is success");

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            Toast.makeText(LoginActivity.this, "SignUp successfully using google", Toast.LENGTH_SHORT).show();

                        }else {

                            Log.d(TAG, "onComplete: ", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}