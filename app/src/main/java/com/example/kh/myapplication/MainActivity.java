package com.example.kh.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kh.myapplication.Module.MyVolley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "vo cong vinh";
    private String url =  "http://192.168.1.10/DuLieu/UploadImage.php";
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.etName)
    EditText etName;
    private final int REQUEST_CHOSE_IMAGE=1;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnChose)
    public void choseImage(){
        chose();

    }

    @OnClick(R.id.btnUpload)
    public void uploadImage(){
        upload();
    }

    public void upload(){
        MyVolley.getInstance(this).startVolley();
        Log.i(TAG, "upload: 1");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i(TAG, "upload: 2 "+response);

                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(MainActivity.this, ""+jsonObject.getString("respond"), Toast.LENGTH_SHORT).show();
                    img.setImageResource(0);
                    img.setVisibility(View.GONE);
                    etName.setText("");
                    etName.setVisibility(View.GONE);
                    bitmap=null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MyVolley.getInstance(MainActivity.this).stopVolley();
                Log.i(TAG, "upload: 3");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("name", etName.getText().toString().trim());
                params.put("image", imagetoString(bitmap));
                return params;
            }
        };

        MyVolley.getInstance(this).addRequestVolley(stringRequest);
    }

    public void chose(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_CHOSE_IMAGE );
    }

    public String imagetoString(Bitmap bitmap){
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imaBytes =  byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imaBytes, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CHOSE_IMAGE && resultCode==RESULT_OK && data !=null){
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                img.setImageBitmap(bitmap);
                img.setVisibility(View.VISIBLE);
                etName.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
