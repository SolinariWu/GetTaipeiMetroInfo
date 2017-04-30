package com.example.solinari.GetTaipeiMetroInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Solinari on 2016/12/31.
 */

public class FragmentList_One extends Fragment{
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private RecyclerView.Adapter recyclerAdapter;
    private String[] MetroInfo;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentlist_one, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        getMetroInfo();//取得台北捷運列車到站資訊
        return view;
    }
    public void setRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapter = new RecyclerViewAdapter(MetroInfo);//設置RecyclerView.Adapter
        recyclerView.setAdapter(recyclerAdapter);
        progressBar.setVisibility(View.GONE);//Loading圖隱藏
        recyclerView.setVisibility(View.VISIBLE);//資料呈現
    }
    public void getMetroInfo(){
        RequestQueue queue = Volley.newRequestQueue(getActivity());//創建一個RequestQueue
        //台北捷運到站資料網址，按終點站排序
        String url = "http://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire&rid=55ec6d6e-dc5c-4268-a725-d04cc262172b&sort=Destination";
        //對URL發出Request取得JsonObject的Response
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Response的JSONObject中，我們要的資訊是results(JSONArray)
                            //{"result":{"offset":0,"limit":10000,"count":25,"sort":"Destination","results":[{"_id":"1","Station":"士林站","Destination":"大安站","UpdateTime":"2017-04-10T20:59:27.577"},...}]}}
                            JSONArray info = response.getJSONObject("result").getJSONArray("results");
                            MetroInfo = new String[info.length()];
                            for(int i = 0; i < info.length();i++){
                                JSONObject jsonObject = info.getJSONObject(i);
                                //{"_id":"1","Station":"士林站","Destination":"大安站","UpdateTime":"2017-04-10T20:59:27.577"}
                                String time = jsonObject.getString("UpdateTime").substring(11,19);
                                MetroInfo[i] = jsonObject.getString("Station") + "_" + jsonObject.getString("Destination") + "_" + time;
                            }
                            setRecyclerView();//設置RecyclerView
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //當發生錯誤時，提示使用者發生錯誤
                MetroInfo = new String[1];
                MetroInfo[0] = "Error_Error_Error";
                setRecyclerView();
                Toast.makeText(getActivity(),"Get data error !",Toast.LENGTH_LONG).show();
            }
        }
        );
        //將Request加入RequestQueue
        queue.add(jsonObjectRequest);
    }
}
