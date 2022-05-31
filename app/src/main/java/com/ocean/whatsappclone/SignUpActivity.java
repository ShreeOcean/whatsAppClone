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

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
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
import com.ocean.whatsappclone.databinding.ActivitySignUpBinding;
import com.ocean.whatsappclone.model.UserModel;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    ActivitySignUpBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    GoogleSignInClient googleSignInClient;
    GoogleSignInOptions signInOptions;

    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;

    String TAG = "SignUpActivity : --";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //TODO: hide action bar
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("....Creating Account !!!");

        //TODO: onclick events
        binding.btnSignup.setOnClickListener(this);
        binding.btnSignupUsingFacebook.setOnClickListener(this);
        binding.btnSignupUsingGoogle.setOnClickListener(this);
        binding.tvLoginLink.setOnClickListener(this);
        binding.tvSignupWithPhone.setOnClickListener(this);

        //TODO: configure google sign in
        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                 .requestEmail()
                 .build();

         googleSignInClient =  GoogleSignIn.getClient(SignUpActivity.this, signInOptions);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_signup:

                progressDialog.show();
                //TODO: signup giving name, email, password
                firebaseAuth.createUserWithEmailAndPassword(binding.etEmail.getText().toString(), binding.etPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()){

                                    //TODO: using signup constructor from user model
                                    UserModel userModel = new UserModel(binding.etUsername.getText().toString(), binding.etEmail.getText().toString(), binding.etPassword.getText().toString());
                                    String userID = task.getResult().getUser().getUid();

                                    //TODO: setting user value in realtime db with credentials
                                    firebaseDatabase.getReference().child("Users").child(userID).setValue(userModel);

                                    Toast.makeText(SignUpActivity.this, "User created successfully,...", Toast.LENGTH_SHORT).show();
                                    //startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

                                }else {
                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                break;
            case R.id.btn_signup_using_facebook:
                //TODO: signup using facebook
                Toast.makeText(this, "Code to be done for facebook signup...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_signup_using_google:
                //TODO: signup using google account

                signInWithGoogle();

                break;
            case R.id.tv_login_link:
                //TODO: redirect to login activity
/**todo ---> error**/                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                break;
            case R.id.tv_signup_with_phone:
                //TODO: signup using phone number

                startActivity(new Intent(SignUpActivity.this, SignUpWithPhoneNumActivity.class));

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
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            Toast.makeText(SignUpActivity.this, "SignUp successfully using google", Toast.LENGTH_SHORT).show();

                        }else {
                            Log.d(TAG, "onComplete: ", task.getException());
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}