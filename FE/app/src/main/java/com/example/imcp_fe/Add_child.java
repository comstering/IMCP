package com.example.imcp_fe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.imcp_fe.Network.AppHelper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

/*
 * 아이를 추가하는 기능 클래스
 * http통신으로 이미지 파일과 정보를 서버로 전송
 * 정보들은 직접 입력
 * */
public class Add_child extends AppCompatActivity {

    private final int REQ_CODE_SELECT_IMAGE = 100;//??

    private CircleImageView photo;
    private EditText name;
    private String key;
    private String password;
    private EditText birthday;
    private Button btn_addchild_add;
    private String url = "http://tomcat.comstering.synology.me/IMCP_Server/addChild.jsp";
    private String img_path = new String();
    private String imageName = null;
    private Bitmap image_bitmap = null;
    private Bitmap image_bitmap_copy = null;
    private SharedPreferences login_preference;

    /*
     * 액티비티 생성 시 호출
     * 인터페이스 초기화
     * 버튼 클릭 시 이벤트 설정
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_child);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        password = intent.getStringExtra("password");

        login_preference = getSharedPreferences("Login", MODE_PRIVATE);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());


        photo = (CircleImageView) findViewById(R.id.iv_addchild_photo);
        name = (EditText) findViewById(R.id.etv_addchild_name);
        birthday = (EditText) findViewById(R.id.etv_addchild_birthday);
        btn_addchild_add = (Button) findViewById(R.id.btn_addchild_add);


        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                Log.d("Test", img_path);
            }
        });


        btn_addchild_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.e("check", "click check");
                //edittext에 모두 값이 있는지 확인
                if (name.getText().toString().length() == 0 || birthday.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "공란 없이 채워주세요.", Toast.LENGTH_SHORT).show();
                    Log.e("check", "ddd");
                } else {
                    Log.d("Test", "DoFileUpload 전 : " + img_path);
                    DoFileUpload(url, img_path);
                    Toast.makeText(getApplicationContext(), "이미지 전송 성공", Toast.LENGTH_SHORT).show();
                    Log.e("check", "Success");
                }
            }
        });


    }

    /*
     * 설정한 이미지를 비트맵으로 변환, URI를 얻어 경로값을 반환
     * getImagePathToUri 메소드 이용
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Toast.makeText(getBaseContext(), "resultCode : " + data, Toast.LENGTH_SHORT).show();

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    img_path = getImagePathToUri(data.getData()); //이미지의 URI를 얻어 경로값으로 반환.
                    Toast.makeText(getBaseContext(), "img_path : " + img_path, Toast.LENGTH_SHORT).show();
                    //이미지를 비트맵형식으로 반환
                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //사용자 단말기의 width , height 값 반환
                    int reWidth = (int) (getWindowManager().getDefaultDisplay().getWidth());
                    int reHeight = (int) (getWindowManager().getDefaultDisplay().getHeight());

                    //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                    image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 300, true);
                    ImageView image = (ImageView) findViewById(R.id.iv_addchild_photo);  //이미지를 띄울 위젯 ID값
                    image.setImageBitmap(image_bitmap_copy);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }//end of onActivityResult()

    /*
     * 이미지 경로, 이름 값 설정
     * */
    public String getImagePathToUri(Uri data) {
        //사용자가 선택한 이미지의 정보를 받아옴
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);
        Log.d("test", imgPath);

        //이미지의 이름 값
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
        Toast.makeText(Add_child.this, "이미지 이름 : " + imgName, Toast.LENGTH_SHORT).show();
        this.imageName = imgName;

        return imgPath;
    }//end of getImagePathToUri()

    /*
     * HttpFileUpload 메소드 호출
     * */
    public void DoFileUpload(String apiUrl, String absolutePath) {
        HttpFileUpload(apiUrl, "", absolutePath);
    }

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    /*
     * Http 통신 이미지 파일 전송 및 정보 전송
     *
     */
    public void HttpFileUpload(String urlString, String params, String fileName) {
        try {
            Log.d("Test", urlString);
            File file = new File(fileName);
            Log.d("Test", "name : " + fileName);

            FileInputStream mFileInputStream = new FileInputStream(file);
            Log.e("Test", "1");
            URL connectUrl = new URL(urlString);
            Log.e("Test", "2");

            Log.d("Test", "mFileInputStream  is " + mFileInputStream);

            // HttpURLConnection 통신
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            Log.e("Test", "3");


            // write data
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);


            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"name\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain; UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeUTF(name.getText().toString());
            dos.writeBytes(lineEnd);


            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"key\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(key + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"password\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(password + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"birth\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(birthday.getText().toString() + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"id\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(login_preference.getString("id", "") + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())) + lineEnd);
            dos.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
            dos.writeBytes(lineEnd);


            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;//?1024
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

            Log.d("Test", "image byte is " + bytesRead);

            // read image
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            Log.e("Test", "File is written");
            mFileInputStream.close();

            dos.flush();
            // finish upload...

            // get response
            InputStream is = conn.getInputStream();

            StringBuffer b = new StringBuffer();
            for (int ch = 0; (ch = is.read()) != -1; ) {
                b.append((char) ch);
            }
            is.close();

            Log.e("Test", "responese : " + b.toString());

            switch (b.toString().trim()) {
                case "PtoCError":
                    Toast.makeText(getApplicationContext(), "아이 연결실패", Toast.LENGTH_SHORT).show();
                    break;
                case "AddSuccess":
                    Toast.makeText(getApplicationContext(), "연결 성공", Toast.LENGTH_SHORT).show();
                    Success();
                    break;
                case "NoPrivateKey":
                    Toast.makeText(getApplicationContext(), "등록된 키가 없습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case "DBError":
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    break;

            }


        } catch (Exception e) {
            Log.d("Test", "exception " + e.getMessage());
            // TODO: handle exception
        }
    } // end of HttpFileUpload()

    public void Success() {
        Intent intent = new Intent(getApplicationContext(), Parents_main.class);
        startActivity(intent);
    }
}
