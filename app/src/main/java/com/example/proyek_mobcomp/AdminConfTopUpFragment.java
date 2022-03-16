package com.example.proyek_mobcomp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cTopup;
import com.example.proyek_mobcomp.classFolder.cUser;
import com.example.proyek_mobcomp.databinding.FragmentAdminConfTopUpBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterAdminConfTopUp;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterCustomerHistoryTopUp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminConfTopUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminConfTopUpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminConfTopUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfTopUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminConfTopUpFragment newInstance(String param1, String param2) {
        AdminConfTopUpFragment fragment = new AdminConfTopUpFragment();
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







    protected FragmentAdminConfTopUpBinding binding;

    ArrayList<cTopup> arrTopUp = new ArrayList<>();

    RecyclerAdapterAdminConfTopUp recyclerAdapterAdminConfTopUp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_admin_conf_top_up, container, false);
        binding = FragmentAdminConfTopUpBinding.inflate(inflater, container, false);

        getTopUpData();

        return binding.getRoot();
    }

    public void getTopUpData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/admin/gettopupdata",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray historyarray = jsonObject.getJSONArray("datatopup");
                            arrTopUp = new ArrayList<>();
                            for (int i = 0; i < historyarray.length();i++){
                                int id = historyarray.getJSONObject(i).getInt("id");
                                String fk_username = historyarray.getJSONObject(i).getString("fk_username");
                                int jumlah_topup = historyarray.getJSONObject(i).getInt("jumlah_topup");
                                String bukti_topup = historyarray.getJSONObject(i).getString("bukti_topup");
                                int status_topup = historyarray.getJSONObject(i).getInt("status_topup");
                                String created_at = historyarray.getJSONObject(i).getString("created_at");
                                String updated_at = historyarray.getJSONObject(i).getString("updated_at");

                                arrTopUp.add(new cTopup(id, fk_username, jumlah_topup, bukti_topup, status_topup, created_at, updated_at));
                            }

                            JSONArray userarray = jsonObject.getJSONArray("datauser");
                            ArrayList<cUser> arrUser = new ArrayList<>();
                            for (int i = 0; i < userarray.length();i++){
                                String username = userarray.getJSONObject(i).getString("username");
                                String password = userarray.getJSONObject(i).getString("password");
                                String email = userarray.getJSONObject(i).getString("email");
                                String nama = userarray.getJSONObject(i).getString("nama");
                                String rekening = userarray.getJSONObject(i).getString("rekening");
                                String saldo = userarray.getJSONObject(i).getString("saldo");
                                String toko = userarray.getJSONObject(i).getString("toko");
                                String role = userarray.getJSONObject(i).getString("role");
                                String gambar = userarray.getJSONObject(i).getString("gambar");
                                int status = userarray.getJSONObject(i).getInt("status");
                                int is_verified = userarray.getJSONObject(i).getInt("is_verified");

                                arrUser.add(new cUser(username, password, email, nama, rekening, saldo, toko, role, gambar, status, is_verified));
                            }

                            setRv(arrUser);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error get top up data in conf fragment " + error);
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function", "gettopupdata");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    protected void setRv(ArrayList<cUser> arrUser) {
        binding.recyclerViewMasterConfTopUp.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewMasterConfTopUp.setHasFixedSize(true);

        recyclerAdapterAdminConfTopUp = new RecyclerAdapterAdminConfTopUp(
                arrTopUp, arrUser
        );
        binding.recyclerViewMasterConfTopUp.setAdapter(recyclerAdapterAdminConfTopUp);
        binding.progressBar.setVisibility(View.INVISIBLE);
    }
}