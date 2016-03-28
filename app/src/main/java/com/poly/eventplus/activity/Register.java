package com.poly.eventplus.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.poly.eventplus.R;
import com.poly.eventplus.adapter.imageHelper;

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

public class Register extends Activity {
    private static final int REQUEST_CODE = 10;

    TextView textView;
    EditText edtuser, edtemail, edtpass, edtpassagain;
    TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword, inputLayoutPasswordAG;
    Button btn1, btn2;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Controls();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtemail.setText("");
                edtpass.setText("");
                edtpassagain.setText("");
                edtuser.setText("");
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

        edtuser.addTextChangedListener(new MyTextWatcher(edtuser));
        edtpassagain.addTextChangedListener(new MyTextWatcher(edtpassagain));
        edtpass.addTextChangedListener(new MyTextWatcher(edtpass));
        edtemail.addTextChangedListener(new MyTextWatcher(edtemail));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void Controls() {
        btn1 = (Button) findViewById(R.id.btn_dangkipost);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_emaildk);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_userdk);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_passdk);
        inputLayoutPasswordAG = (TextInputLayout) findViewById(R.id.input_layout_passagaindk);
        img = (ImageView) findViewById(R.id.imgavatar);
        textView = (TextView) findViewById(R.id.tv1);
        edtemail = (EditText) findViewById(R.id.edt_email);
        edtpass = (EditText) findViewById(R.id.edt_pass);
        edtpassagain = (EditText) findViewById(R.id.edt_passagain);
        edtuser = (EditText) findViewById(R.id.edt_username);
        btn2 = (Button) findViewById(R.id.btn_datlai);
    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        if (!validatePasswordag()) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new goiweb().execute("http://trieu.svnteam.net/Api/registerapp.php");
            }
        });
    }

    private boolean validateName() {
        if (edtuser.getText().toString().length() < 6) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(edtuser);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = edtemail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(edtemail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (edtpass.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(edtpass);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }
        if (edtpass.getText().toString().length() < 6) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password6));
            requestFocus(edtpass);
            return false;
        }
        return true;
    }

    private boolean validatePasswordag() {
        if (!edtpass.getText().toString().equals(edtpassagain.getText().toString())) {
            inputLayoutPasswordAG.setError(getString(R.string.err_msg_passwordag));
            requestFocus(edtpassagain);
            return false;
        } else {
            inputLayoutPasswordAG.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
                case R.id.edt_username:
                    validateName();
                    break;
                case R.id.edt_email:
                    validateEmail();
                    break;
                case R.id.edt_pass:
                    validatePassword();
                    break;
                case R.id.edt_passagain:
                    validatePasswordag();
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
            Log.d("Tra ve ", s);
            if (s.equals("OK")) {
                Toast.makeText(getApplicationContext(), "Đăng kí thành công!", Toast.LENGTH_SHORT);
                Intent i = new Intent(getApplicationContext(), Login.class);
                i.putExtra("username", edtuser.getText().toString());
                i.putExtra("pass", edtpass.getText().toString());
                startActivityForResult(i, REQUEST_CODE);
                startActivity(i);
                finish();
            }
            if (s.equals("NO")) {
                Toast.makeText(getApplicationContext(), "Tên đăng nhập đã có người sử dụng", Toast.LENGTH_LONG).show();
            }

        }

    }

    private String makePostRequest(String u) {
        HttpClient httpClient = new DefaultHttpClient();

        // URL của trang web nhận request
        HttpPost httpPost = new HttpPost(u);

        // Các tham số truyền
        List nameValuePair = new ArrayList(5);
        nameValuePair.add(new BasicNameValuePair("action", "insert"));
        nameValuePair.add(new BasicNameValuePair("username", edtuser.getText().toString()));
        nameValuePair.add(new BasicNameValuePair("email", edtemail.getText().toString()));
        nameValuePair.add(new BasicNameValuePair("pass", edtpass.getText().toString()));
        nameValuePair.add(new BasicNameValuePair("passagain", edtpassagain.getText().toString()));

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