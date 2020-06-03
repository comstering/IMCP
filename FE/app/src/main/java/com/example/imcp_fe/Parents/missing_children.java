package com.example.imcp_fe.Parents;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.imcp_fe.Network.AppHelper;
import com.example.imcp_fe.Parents.Adapter.rv_missingchild_adapter;
import com.example.imcp_fe.Parents.Data.rv_missingchild_data;
import com.example.imcp_fe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class missing_children extends AppCompatActivity {
    private RecyclerView rv_missingchildren = null;
    private LinearLayoutManager layoutManager = null;
    private rv_missingchild_adapter rvMissingchildrenAdapter = null;
    private ArrayList<rv_missingchild_data> arrayList;
    private rv_missingchild_data rvMissingchildrenData;
    private ImageButton b_back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.missing_children);

        b_back = (ImageButton) findViewById(R.id.btn_missingchilren_back);
        rv_missingchildren = (RecyclerView) findViewById(R.id.rv_missingchildren);


    }



    public void sendRequest() {
        String url = "https://www.google.co.kr";

        //StringRequest를 만듬 (파라미터구분을 쉽게하기위해 엔터를 쳐서 구분하면 좋다)
        //StringRequest는 요청객체중 하나이며 가장 많이 쓰인다고한다.
        //요청객체는 다음고 같이 보내는방식(GET,POST), URL, 응답성공리스너, 응답실패리스너 이렇게 4개의 파라미터를 전달할 수 있다.(리퀘스트큐에 ㅇㅇ)
        //화면에 결과를 표시할때 핸들러를 사용하지 않아도되는 장점이있다.
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {  //응답을 문자열로 받아서 여기다 넣어달란말임(응답을 성공적으로 받았을 떄 이메소드가 자동으로 호출됨
                    @Override
                    public void onResponse(String response) {
                        try {
                            arrayList = new ArrayList<rv_missingchild_data>();
                            JSONArray jarray = new JSONArray(response);
                            int size = jarray.length();
                            for (int i = 0; i < size; i++) {
                                JSONObject row = jarray.getJSONObject(i);
                                rvMissingchildrenData = new rv_missingchild_data();
                               // rvMissingchildrenData.setRv_missingchild_image(row.getString("image"));//이미지 받아서 데이터로 저장
                                rvMissingchildrenData.setRv_missingchild_name(row.getString("name"));// 이름 받아서 데이터로 저장

                            }
                            rvMissingchildrenAdapter = new rv_missingchild_adapter(missing_children.this, arrayList);
                            rv_missingchildren.setAdapter(rvMissingchildrenAdapter);//리사이클러뷰에 어댑터 연결
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() { //에러발생시 호출될 리스너 객체
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            //만약 POST 방식에서 전달할 요청 파라미터가 있다면 getParams 메소드에서 반환하는 HashMap 객체에 넣어줍니다.
            //이렇게 만든 요청 객체는 요청 큐에 넣어주는 것만 해주면 됩니다.
            //POST방식으로 안할거면 없어도 되는거같다.
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
              //  params.put();
                return params;
            }
        };

        //아래 add코드처럼 넣어줄때 Volley라고하는게 내부에서 캐싱을 해준다, 즉, 한번 보내고 받은 응답결과가 있으면
        //그 다음에 보냈을 떄 이전 게 있으면 그냥 이전거를 보여줄수도  있다.
        //따라서 이렇게 하지말고 매번 받은 결과를 그대로 보여주기 위해 다음과같이 setShouldCache를 false로한다.
        //결과적으로 이전 결과가 있어도 새로 요청한 응답을 보여줌
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);

    }
}