package com.edvaldotsi.nodejsandandroid;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edvaldotsi.nodejsandandroid.model.User;
import com.edvaldotsi.nodejsandandroid.retrofit.ServiceGenerator;
import com.edvaldotsi.nodejsandandroid.retrofit.UserService;
import com.edvaldotsi.nodejsandandroid.util.CircleTransform;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AbstractActivity {

    private ImageView mImageViewUserPicture;
    private TextView mTextViewUserName;
    private TextView mTextViewUserEmail;
    private TextView mTextViewUserGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mImageViewUserPicture = (ImageView) findViewById(R.id.image_user_picture);
        mTextViewUserName = (TextView) findViewById(R.id.text_user_name);
        mTextViewUserEmail = (TextView) findViewById(R.id.text_user_email);
        mTextViewUserGroupName = (TextView) findViewById(R.id.text_user_group_name);

        UserService s = ServiceGenerator.createService(UserService.class);
        Call<User> call = s.getMe();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    User u = response.body();

                    Picasso.with(ProfileActivity.this)
                            .load("http://teste")
                            .error(R.drawable.no_profile_picture)
                            .transform(new CircleTransform())
                            .into(mImageViewUserPicture);

                    mTextViewUserName.setText(u.getName());
                    mTextViewUserEmail.setText(u.getEmail());
                    mTextViewUserGroupName.setText(u.getGroup().getName());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Falha na conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
