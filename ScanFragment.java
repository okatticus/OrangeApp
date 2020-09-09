package com.example.android.orange;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.orange.Database.Db;

import java.util.ArrayList;


public class ScanFragment extends Fragment {

    public ArrayList<String> userList = null,qrList=null;
    public ListView lv1,lv2;
    Db db;
    String uid;
    View rootview;
    private OnFragmentInteractionListener mListener;

    public ScanFragment() {
        // Required empty public constructor
    }

    public static ScanFragment newInstance(long uid) {
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();
        args.putString("uId", String.valueOf(uid));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString("uId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_scan, container, false);
        db = new Db(getContext());
        lv1 = rootview.findViewById(R.id.scanlistview);
        lv2 = rootview.findViewById(R.id.scanqrview);
        TextView tv1 = rootview.findViewById(R.id.noScans);
        TextView tv2=rootview.findViewById(R.id.noQRScans);
        tv1.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);
        userList = db.getAllScans(uid,"bar");
        qrList =db.getAllScans(uid,"qr");
        if (userList.size() <= 0) {
            tv1.setVisibility(View.VISIBLE);
        }
        if (qrList.size() <= 0) {
            tv2.setVisibility(View.VISIBLE);
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, userList);
        lv1.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, qrList);
        lv2.setAdapter(adapter2);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String entry = (String) parent.getItemAtPosition(position);

                Toast toast = Toast.makeText(getContext(), entry, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String entry = (String) parent.getItemAtPosition(position);

                Toast toast = Toast.makeText(getContext(), entry, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        return rootview;

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().setTitle("Orange");
    }
}
