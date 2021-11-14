package com.edwin.midtermexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.edwin.midtermexam.api.RequestPlaceholder;
import com.edwin.midtermexam.api.RetrofitBuilder;
import com.edwin.midtermexam.pojos.Login;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public EditText username, password;
    public MaterialButton loginBtn;

    public RetrofitBuilder retrofitBuilder;
    public RequestPlaceholder requestPlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginBtn = (MaterialButton) findViewById(R.id.loginBtn);

        retrofitBuilder = new RetrofitBuilder();
        requestPlaceholder = retrofitBuilder.getRetrofit().create(RequestPlaceholder.class);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(username.getText()!=null  && password.getText()!=null){
                    Call<Login> loginCall = requestPlaceholder.login(new Login(null, username.getText().toString(), null, null, password.getText().toString()));

                    loginCall.enqueue(new Callback<Login>() {
                        @Override
                        public void onResponse(Call<Login> call, Response<Login> response) {
                            if(!response.isSuccessful()){
                                if(response.code() == 404){
                                    Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                    Log.e("LOGGING_ERR", response.message());
                                }else{
                                    Toast.makeText(LoginActivity.this, "There is an error upon logging in the API", Toast.LENGTH_SHORT).show();
                                    Log.e("LOGGING_ERR", response.message());
                                }

                            }else{
                                if(response.code() == 200){
                                    Login loginResponse = response.body();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                                    intent.putExtra("USER_ID", loginResponse.getId());
                                    intent.putExtra("USERNAME", loginResponse.getUsername());
                                    intent.putExtra("TOKEN", loginResponse.getToken());

                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Login> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "There is an error upon logging in the API", Toast.LENGTH_SHORT).show();
                            Log.e("LOGGING_ERR", t.getMessage());
                        }
                    });

                }else{
                    Toast.makeText(LoginActivity.this, "Please supply all fields to login!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
