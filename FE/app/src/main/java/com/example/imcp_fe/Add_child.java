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

    private final int REQ_CODE_SELECT_IMAGE = 100;

    //등록 할 아이 사진, 이름, 생일 변수
    private CircleImageView photo;
    private EditText name;
    private EditText birthday;

    //Add_child_check에서 받아 올 고유키와 비밀번호
    private String key;
    private String password;

    //등록 버튼
    private Button btn_addchild_add;

    //전송할 서버 주소
    private String url = "http://tomcat.comstering.synology.me/IMCP_Server/addChild.jsp";

    //이미지 경로와 비트맵 변수
    private String img_path = new String();
    private String imageName = null;
    private Bitmap image_bitmap = null;
    private Bitmap image_bitmap_copy = null;

    //SharedPreferences 변수
    private SharedPreferences login_preference;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_child);
        Intent intent = getIntent();

        //각 변수의 인스턴스를 받아옴
        photo = (CircleImageView) findViewById(R.id.iv_addchild_photo);
        name = (EditText) findViewById(R.id.etv_addchild_name);
        birthday = (EditText) findViewById(R.id.etv_addchild_birthday);
        btn_addchild_add = (Button) findViewById(R.id.btn_addchild_add);


        //전 액티비티에서 고유키와 비밀번호를 받아옴
        key = intent.getStringExtra("key");
        password = intent.getStringExtra("password");

        //SharedPreferences 인스턴스
        login_preference = getSharedPreferences("Login", MODE_PRIVATE);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());


        //사진 클릭 이벤트, 사진 변경
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });

        //등록 버튼 이벤트
        btn_addchild_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //edittext에 모두 값이 있는지 확인
                if (name.getText().toString().length() == 0 || birthday.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "공란 없이 채워주세요.", Toast.LENGTH_SHORT).show();
                } else { //eidttext에 모두 값이 있을 시
                    DoFileUpload(url, img_path); //서버로 전송
                    Toast.makeText(getApplicationContext(), "이미지 전송 성공", Toast.LENGTH_SHORT).show();
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

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    img_path = getImagePathToUri(data.getData()); //이미지의 URI를 얻어 경로값으로 반환.

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

        //이미지의 이름 값
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
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
     */
    public void HttpFileUpload(String urlString, String params, String fileName) {
        try {
            File file = new File(fileName);

            FileInputStream mFileInputStream = new FileInputStream(file);
            URL connectUrl = new URL(urlString);

            // HttpURLConnection 통신
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);


            // write data
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            //이름 write
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"name\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain; UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeUTF(name.getText().toString());
            dos.writeBytes(lineEnd);

            //이름 write
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


            switch (b.toString()) {//서버에서 받은 값 처리
                case "PtoCError":
                    Toast.makeText(getApplicationContext(), "아이 연결실패", Toast.LENGTH_SHORT).show();
                    break;
                case "AddSuccess":
                    Toast.makeText(getApplicationContext(), "연결 성공", Toast.LENGTH_SHORT).show();
                    Success();//연결 성공 시 엑티비티 전환
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

    public void Success() {//전송 성공 시 엑티비티 변경
        Intent intent = new Intent(getApplicationContext(), Parents_main.class);
        startActivity(intent);
    }
}
