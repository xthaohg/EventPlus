package com.example.hoangthao.eventplus.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoangthao.eventplus.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoangthao on 22/02/16.
 */
public class Login extends Activity {
    TextView textView, logo;
    EditText edtuser, edtpass;
    Button btn1, btn2;
    TextInputLayout txtIL1, txtIL2;
    private static final int REQUEST_CODE = 10;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String USERNAME = "userNameKey";
    public static final String PASS = "passKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        String fontPath = "fonts/FREEBSC_.ttf";
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        logo = (TextView) findViewById(R.id.logo);
        logo.setTypeface(tf);
        textView = (TextView) findViewById(R.id.txtdangnhap);
        TextView txtDangki = (TextView) findViewById(R.id.txtDangki);

        txtDangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });
        txtIL1 = (TextInputLayout) findViewById(R.id.input_layout_name);
        txtIL2 = (TextInputLayout) findViewById(R.id.input_layout_pass);

        edtuser = (EditText) findViewById(R.id.edt_usernamedn);
        edtpass = (EditText) findViewById(R.id.edt_passdn);
        edtuser.addTextChangedListener(new MyTextWatcher(edtuser));
        edtpass.addTextChangedListener(new MyTextWatcher(edtpass));
        btn2 = (Button) findViewById(R.id.btn_dangnhapdn);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        submitForm();
                        Log.d("xxx ", "may vua click");
                    }
                });
            }
        });
    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        new goiweb().execute("http://trieu.svnteam.net/Api/login.php");
    }

    private boolean validateName() {
        if (edtuser.getText().toString().length() < 6) {
            txtIL1.setError(getString(R.string.err_msg_name));
            requestFocus(edtuser);
            return false;
        } else {
            txtIL1.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (edtpass.getText().toString().trim().isEmpty()) {
            txtIL2.setError(getString(R.string.err_msg_password));
            requestFocus(edtpass);
            return false;
        } else {
            txtIL2.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edt_usernamedn:
                    validateName();
                    break;
                case R.id.edt_passdn:
                    validatePassword();
                    break;
            }
        }
    }


    class goiweb extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return makePostRequest(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("Tra ve: ", s);
            if (s.equals("NO")) {
                Toast.makeText(getApplicationContext(), "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_LONG).show();
            }
            if (s.equals("OK")) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("username", edtuser.getText().toString());
                i.putExtra("pass", edtpass.getText().toString());
                saveData(edtuser.getText().toString(), edtpass.getText().toString());
                startActivityForResult(i, REQUEST_CODE);
                finish();
            }
            Log.d("xx ", s);
        }

    }

    private void saveData(String username, String Pass) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASS, Pass);
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Nếu kết quả code trả về là "RESULT_OK" với đoạn REQUEST_CODE tương ứng
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            //Nếu nhận được kết quả trả về có key là "returnKey1"
            if (data.hasExtra("returnKey1")) {
                //Hiển thị thông báo có đính kèm giá trị của key "returnKey1"
                Toast.makeText(this, data.getExtras().getString("returnKey1"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String makePostRequest(String u) {
        HttpClient httpClient = new DefaultHttpClient();

        // URL của trang web nhận request
        HttpPost httpPost = new HttpPost(u);

        // Các tham số truyền
        List nameValuePair = new ArrayList(3);
        nameValuePair.add(new BasicNameValuePair("action", "selectuser"));
        nameValuePair.add(new BasicNameValuePair("username", edtuser.getText().toString()));
        nameValuePair.add(new BasicNameValuePair("pass", edtpass.getText().toString()));

        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String kq = "";
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            kq = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return kq;
    }

}
