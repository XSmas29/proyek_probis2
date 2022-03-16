package com.example.proyek_mobcomp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cUser;
import com.example.proyek_mobcomp.databinding.FragmentAdminDashboardBinding;
import com.example.proyek_mobcomp.databinding.FragmentAdminMasterUserBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterAdminMasterUser;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterHeaderPurchase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminMasterUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminMasterUserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminMasterUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminMasterUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminMasterUserFragment newInstance(String param1, String param2) {
        AdminMasterUserFragment fragment = new AdminMasterUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    protected FragmentAdminMasterUserBinding binding;

    ArrayList<cUser> arrUser = new ArrayList<>();

    RecyclerAdapterAdminMasterUser recyclerAdapterAdminMasterUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_admin_master_user, container, false);
        binding = FragmentAdminMasterUserBinding.inflate(inflater, container, false);

        getUsersData();

        return binding.getRoot();
    }

    public void getUsersData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/admin/getusersdata",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int code = jsonObject.getInt("code");


                            if (code == 1){
                                JSONArray userarray = jsonObject.getJSONArray("datausers");
                                arrUser = new ArrayList<>();
                                for (int i = 0; i <  userarray.length(); i++ ){
                                    String username = userarray.getJSONObject(i).getString("username");
                                    String password = userarray.getJSONObject(i).getString("password");
                                    String email = userarray.getJSONObject(i).getString("email");
                                    String nama = userarray.getJSONObject(i).getString("nama");
                                    String rekening = userarray.getJSONObject(i).getString("rekening");
                                    int saldo = userarray.getJSONObject(i).getInt("saldo");
                                    String toko = userarray.getJSONObject(i).getString("toko");
                                    String role = userarray.getJSONObject(i).getString("role");
                                    String gambar = userarray.getJSONObject(i).getString("gambar");
                                    int status = userarray.getJSONObject(i).getInt("status");
                                    int is_verified = userarray.getJSONObject(i).getInt("is_verified");

                                    arrUser.add(new cUser(username,password,email,nama,rekening,saldo+"",toko,role, gambar, status, is_verified));
                                }

                                setRv();
                            }else{
                                String message = jsonObject.getString("message");
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("error get users data in Admin " + error);
                }
            }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function", "getusersdata");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void setRv() {
        binding.recyclerViewAdminMasterUser.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewAdminMasterUser.setHasFixedSize(true);

        recyclerAdapterAdminMasterUser = new RecyclerAdapterAdminMasterUser(
                arrUser
        );
        binding.recyclerViewAdminMasterUser.setAdapter(recyclerAdapterAdminMasterUser);
        binding.progressBar.setVisibility(View.INVISIBLE);
    }
}