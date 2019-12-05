package com.mashkovska.authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;

    private ImageView profilePicture;
    private TextInputLayout newUsernameLayout;
    private TextInputEditText newUsernameInputEditText;
    private TextInputLayout newEmailLayout;
    private TextInputEditText newEmailInputEditText;

    private Button usernameSaveBtn;
    private Button emailSaveBtn;
    private Button pictureBtn;
    private Button signOutBtn;

    private TextView usernameTextView;
    private TextView emailTextView;
    private StorageReference Folder;
    private StorageReference ImageName;

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View profileFragment = inflater.inflate(R.layout.fragment_profile, container, false);

        initProfileFragmentViews(profileFragment);
        auth = getApplicationEx().getAuth();
        user = auth.getCurrentUser();

        return profileFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (user != null) {
            setUserData();
            buttonClickListenersInit();
        }
        else {
            makeText(getActivity(),getString(R.string.error), LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK){
            Uri ImageData = Objects.requireNonNull(data).getData();
            ImageName.putFile(Objects.requireNonNull(ImageData))
                    .addOnSuccessListener(taskSnapshot -> makeText(getActivity(),
                            R.string.load_succes, LENGTH_SHORT).show());
            getUserAvatar();
        }
    }

    private void setUserData() {
        usernameTextView.setText(user.getDisplayName());
        emailTextView.setText(user.getEmail());
        ImageName = Folder.child(user.getUid() + ".jpg");
        getUserAvatar();
    }

    private void buttonClickListenersInit() {

        usernameSaveBtn.setOnClickListener(view -> {
            String newUsername = Objects.requireNonNull(newUsernameInputEditText.getText())
                    .toString().trim();
            if (isNewUsernameValid(newUsername)){
                editUsername(user, newUsername);
            }
        });

        emailSaveBtn.setOnClickListener(view -> {
            String newEmail = Objects.requireNonNull(newEmailInputEditText.getText())
                    .toString().trim();
            if (isNewEmailValid(newEmail)){
                editEmail(user, newEmail);
            }
        });

        signOutBtn.setOnClickListener(view -> {
            auth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(0, 0);
        });

        pictureBtn.setOnClickListener(view -> loadUserAvatar());
    }

    private void loadUserAvatar() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    private void getUserAvatar(){
        ImageName.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri.toString()).into(profilePicture);
            makeText(getActivity(), uri.toString(), LENGTH_LONG).show();
        });
    }

    private void editEmail(final FirebaseUser user, final String newEmail) {

        user.updateEmail(newEmail).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                emailTextView.setText(user.getEmail());
                makeText(getActivity(), R.string.update_success, LENGTH_SHORT).show();
            }
            else {
                makeText(getActivity(), R.string.update_fail, LENGTH_SHORT).show();
            }
        });

    }

    private boolean isNewEmailValid(String newEmail) {
        if (newEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            newEmailLayout.setError(getString(R.string.enter_email));
            newEmailLayout.requestFocus();
            return false;
        } else {
            newEmailLayout.setError(null);
            return true;
        }
    }

    private boolean isNewUsernameValid(final String username) {
        if (username.isEmpty()) {
            newUsernameLayout.setError(getString(R.string.enter_name));
            newUsernameLayout.requestFocus();
            return false;
        } else {
            newUsernameLayout.setError(null);
            return true;
        }
    }

    private void editUsername(final FirebaseUser user, final String newName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        usernameTextView.setText(user.getDisplayName());
                        makeText(getActivity(), R.string.update_success, LENGTH_SHORT).show();
                    } else {
                        makeText(getActivity(), R.string.update_fail, LENGTH_SHORT).show();
                    }
                });
    }

    private void initProfileFragmentViews(View profileFragment){
        profilePicture = profileFragment.findViewById(R.id.fragment_profile_user_avatar_image_view);
        newUsernameLayout = profileFragment.findViewById(R.id.fragment_profile_edit_username_layout);
        newUsernameInputEditText = profileFragment.findViewById(R.id.fragment_profile_edit_username_text_view);
        newEmailLayout = profileFragment.findViewById(R.id.fragment_profile_edit_email_layout);
        newEmailInputEditText = profileFragment.findViewById(R.id.fragment_profile_edit_email_text_view);
        usernameSaveBtn = profileFragment.findViewById(R.id.fragment_profile_edit_username_btn);
        emailSaveBtn = profileFragment.findViewById(R.id.fragment_profile_edit_email_btn);
        pictureBtn = profileFragment.findViewById(R.id.fragment_profile_load_user_avatar);
        signOutBtn = profileFragment.findViewById(R.id.fragment_profile_sign_out_btn);
        usernameTextView = profileFragment.findViewById(R.id.fragment_profile_username_view);
        emailTextView = profileFragment.findViewById(R.id.fragment_profile_email_view);
        Folder = FirebaseStorage.getInstance().getReference().child("ImageFolder");
    }

    private ApplicationEx getApplicationEx(){
        return ((ApplicationEx) Objects.requireNonNull(getActivity()).getApplication());
    }
}