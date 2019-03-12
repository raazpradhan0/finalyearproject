package com.example.restaurantlocator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantlocator.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.example.restaurant2eat.User;

//import android.os.Bundle;
//import android.widget.ImageView;
//import android.widget.TextView;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputName;
    private  EditText inputEmail;
    private  EditText inputPassword;
    private Button SignUpButton;
    private TextView textViewLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    String name, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDatabase = (DatabaseReference) FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        SignUpButton =findViewById(R.id.buttonSignUp);
        inputName = findViewById(R.id.InputName);
        inputEmail = findViewById(R.id.InputEmail);
        inputPassword = findViewById(R.id.InputPassword);
        textViewLogin =  findViewById(R.id.textViewLogin);
        progressDialog = new ProgressDialog(this);


        SignUpButton.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);



    }


    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user+
        writeNewUser(user.getUid(), username, user.getEmail());

        // Go to MainActivity
        startActivity(new Intent(SignUpActivity.this, NavigationDrawer.class));
        finish();
    }




    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void registerUser() {
        name = inputName.getText().toString().trim();
        email = inputEmail.getText().toString().trim();
        password = inputPassword.getText().toString().trim();


        if (name.isEmpty()) {
            inputName.setError("Name is required");
            inputName.requestFocus();
            return;
        }


        if (email.isEmpty()) {
            inputEmail.setError("Email is required");
            inputEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Please enter a valid Email");
            inputEmail.requestFocus();
            return;
        }


        if (password.isEmpty()) {
            inputPassword.setError("Password is required");
            inputPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            inputPassword.setError("Minimum length of password should be 6");
            inputPassword.requestFocus();
            return;
        }

        progressDialog.setMessage("Registering User");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //sendUserData();
                    finish();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        inputEmail.setError("This email is already registered");
                        inputEmail.requestFocus();
                        progressDialog.dismiss();
                        return;

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    Toast.makeText(SignUpActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }


            }
        });
    }
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);
        mDatabase.child("users").child(userId).setValue(user);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null) {
            onAuthSuccess(firebaseAuth.getCurrentUser());
            finish();
            startActivity(new Intent(this, LoginActivity.class));

        }
    }

    @Override
    public void onClick(View view) {

        if(view == SignUpButton )
        {
            registerUser();

        }
        if(view == textViewLogin){
            finish();
            startActivity(new Intent(this,LoginActivity.class));//loginActivity
        }

    }

         /*private void sendUserData(){
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
                User user = new User(name, email);
                myRef.setValue(user);
            }*/
}

