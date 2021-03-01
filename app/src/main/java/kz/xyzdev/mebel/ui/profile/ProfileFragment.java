package kz.xyzdev.mebel.ui.profile;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import kz.xyzdev.mebel.LoginActivity;
import kz.xyzdev.mebel.R;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private Button logBtn,logSave,regBtn;
    private BottomSheetDialog bottomSheetDialog;
    private EditText emailTxt,passwordTxt;
    String email,password;
    FirebaseAuth auth;
    private TextView myName;


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        myName = (TextView) view.findViewById(R.id.myName);

        regBtn = (Button) view.findViewById(R.id.regBtn);


        logBtn = (Button) view.findViewById(R.id.logBtn);
        auth= FirebaseAuth.getInstance();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser()==null){
                    startActivity(new Intent(getContext(),LoginActivity.class));
                }
            }
        });

        if(auth.getCurrentUser()!=null){
            myName.setText((auth.getCurrentUser()).getEmail());
        }else {
            myName.setText("Please Sign in!");
        }


        bottomSheetDialog = new BottomSheetDialog(getActivity());
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser() != null) {
                    // already signed in
                    auth.signOut();
                    logBtn.setText("Log In");


                } else {
                    // not signed in
                    logBtn.setText("Log Out!");
                    bottomSheetDialog.setContentView(R.layout.loginbtmsheet);
                    bottomSheetDialog.show();
                    logSave = (Button) bottomSheetDialog.findViewById(R.id.logSave);
                    emailTxt = (EditText) bottomSheetDialog.findViewById(R.id.logEmail);
                    passwordTxt = (EditText) bottomSheetDialog.findViewById(R.id.logPass);
                    logSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            email = emailTxt.getText().toString();
                            password = passwordTxt.getText().toString();
                            auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information

                                                FirebaseUser user = auth.getCurrentUser();
                                                updateUI(user);
                                            } else {
                                                // If sign in fails, display a message to the user.

                                                Toast.makeText(getContext(), "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                                updateUI(null);
                                            }

                                            // ...
                                        }
                                    });
                            bottomSheetDialog.cancel();
                        }
                    });



                }

            }
        });





        return view;

    }

    private void updateUI(FirebaseUser user) {
        if(auth != null){
            myName.setText(Objects.requireNonNull(auth.getCurrentUser()).getEmail());
            Toast.makeText(getContext(),"U Signed In successfully",Toast.LENGTH_LONG).show();


        }else {
            Toast.makeText(getContext(),"U Didnt signed in",Toast.LENGTH_LONG).show();
            myName.setText("Please Sign in");
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel

    }

}
