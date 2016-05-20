package com.edvaldotsi.nodejsandandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.edvaldotsi.nodejsandandroid.model.Auth;
import com.edvaldotsi.nodejsandandroid.model.User;
import com.edvaldotsi.nodejsandandroid.retrofit.AuthService;
import com.edvaldotsi.nodejsandandroid.retrofit.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AbstractActivity {

    private CheckBox mCheckboxRemember;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mCheckboxRemember = (CheckBox) findViewById(R.id.checkbox_remember);
        mCheckboxRemember.setChecked(preferences.getBoolean("login_remember", false));

        mEditTextEmail = (EditText) findViewById(R.id.edit_email);
        mEditTextPassword = (EditText) findViewById(R.id.edit_password);

        mEditTextEmail.setText(preferences.getString("login_email", ""));
        mEditTextPassword.setText(preferences.getString("login_password", ""));

        Button mButtonSignin = (Button) findViewById(R.id.button_signin);
        mButtonSignin.setOnClickListener(new ButtonSigninClickListener());
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("admin", true);
        startActivity(intent);
    }

    private class ButtonSigninClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Carregando...");
            dialog.show();

            String email = mEditTextEmail.getText().toString();
            String password = mEditTextPassword.getText().toString();

            AuthService service = ServiceGenerator.createService(AuthService.class);
            Call<Auth> caller = service.auth(email, password);
            caller.enqueue(new Callback<Auth>() {
                @Override
                public void onResponse(Call<Auth> call, Response<Auth> response) {
                    dialog.dismiss();

                    if (response.code() == 200) {
                        Auth a = response.body();

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("token", a.getToken());

                        /* Store user credentials into the phone */
                        if (mCheckboxRemember.isChecked()) {
                            editor.putString("login_name", a.getUser().getName());
                            editor.putString("login_email", a.getUser().getEmail());
                            editor.putString("login_password", mEditTextPassword.getText().toString());
                            editor.putBoolean("login_remember", mCheckboxRemember.isChecked());
                        }
                        editor.apply();

                        LOGGED_USER = response.body().getUser();

                        finish();
                        startMainActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Auth> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Falha na conexão", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
    }
}
