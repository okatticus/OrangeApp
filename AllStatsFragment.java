package com.example.android.orange;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.orange.Database.Db;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllStatsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllStatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllStatsFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    public AllStatsFragment() {
        // Required empty public constructor
    }

    public static AllStatsFragment newInstance() {
        AllStatsFragment fragment = new AllStatsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root;
        root = inflater.inflate(R.layout.fragment_all_stats, container, false);
        final BarChart allChart = root.findViewById(R.id.allchart);
        ArrayList<BarEntry> be = new ArrayList<>();
        final ArrayList<String> xVals = new ArrayList<>();
        Db db = new Db(getContext());
        ArrayList<String> arrList = db.getAllElements();
        int t = 0, xt = 0;
        for (int counter = 0; counter < arrList.size(); counter++) {
            String email = arrList.get(counter);
            User u = db.getData(email);
            long id = u.getUserId();
            int b = db.getCountScans(String.valueOf(id), "bar");
            int q = db.getCountScans(String.valueOf(id), "qr");
            if (b + q == 0) {
                t = 0;
            } else {
                t = 1;
            }
            xt = xt + t;
            be.add(new BarEntry(counter, b + q));
            String name = u.getName();
            int l = name.length();
            if (l < 6) {
                String s = new String(new char[6 - l]).replace("\0", " ");
                name = name + s;
            }
            xVals.add(name.substring(0, 6));
        }
        if (xt == 0) {
            android.support.v7.app.AlertDialog ad = new android.support.v7.app.AlertDialog.Builder(
                    getContext()).create();
            ad.setTitle("No Scans");
            ad.setMessage("Nothing scanned yet.");
            ad.setIcon(R.drawable.emptybox);
            ad.show();
        } else {
            BarDataSet dataSet = new BarDataSet(be, "Scanned Items");
            dataSet.setColors(ColorTemplate.PASTEL_COLORS);
            dataSet.setDrawValues(true);
            BarData data = new BarData(dataSet);
            allChart.setData(data);


            // Display labels for bars
            allChart.getXAxis().setLabelCount(arrList.size());
            allChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
            allChart.getAxisLeft().setAxisMaximum(40);
        }
        return root;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Statistics");
        super.onViewCreated(view, savedInstanceState);
    }
}
