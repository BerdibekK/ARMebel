package kz.xyzdev.mebel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Objects;

import kz.xyzdev.mebel.Models.User;
import kz.xyzdev.mebel.ui.profile.ProfileFragment;

public class LoginActivity extends AppCompatActivity {

    private Button login,register;
    private EditText emailEdit, passwordEdit, phoneEdit,addressEdit;
    private String email, password, phone,address;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private static final String USERS = "Users";
    private User user;
    private FirebaseAuth mAuth;;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEdit = (EditText) findViewById(R.id.editTextTextEmailAddress);
        passwordEdit= (EditText) findViewById(R.id.editTextTextPassword);
        phoneEdit= (EditText) findViewById(R.id.editTextPhone);
        addressEdit= (EditText) findViewById(R.id.editTextTextPostalAddress);


        login = (Button) findViewById(R.id.signUp);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    email = Objects.requireNonNull(emailEdit.getText().toString());
                    password = Objects.requireNonNull(passwordEdit.getText().toString());
                    phone = phoneEdit.getText().toString();
                    address = addressEdit.getText().toString();
                    user = new User(email,password,phone,address);
                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });
    }

    private void registerUser() {
        mAuth.createUserWithEmailAndPassword("berdibek99@mail.ru", "password")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        String keyid = mDatabase.push().getKey();
        mDatabase.child(keyid).setValue(user); //adding user info to database
        Intent loginIntent = new Intent(this, HomeActivity.class);
        startActivity(loginIntent);
    }
}
