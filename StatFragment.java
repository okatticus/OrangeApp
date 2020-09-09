package com.example.android.orange;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.orange.Database.Db;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatFragment extends Fragment implements OnChartValueSelectedListener {

    private String uid;

    private OnFragmentInteractionListener mListener;

    public StatFragment() {
        // Required empty public constructor
    }

    public static StatFragment newInstance(String uid) {
        StatFragment fragment = new StatFragment();
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
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_stat, container, false);
        final BarChart barChart = root.findViewById(R.id.barchart);
        Db db = new Db(getContext());
        int b = db.getCountScans(uid, "bar");
        int q = db.getCountScans(uid, "qr");
        if (b == 0 && q == 0) {
            android.support.v7.app.AlertDialog ad = new android.support.v7.app.AlertDialog.Builder(
                    getContext()).create();
            ad.setTitle("No Scans");
            ad.setMessage("Nothing scanned yet.");
            ad.setIcon(R.drawable.emptybox);
            ad.show();

        }
        else{
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, b));
        barEntries.add(new BarEntry(1, q));
        BarDataSet dataSet = new BarDataSet(barEntries, "Scanned Items");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setDrawValues(true);
        BarData data = new BarData(dataSet);
        // Format data as percentage
        //data.setValueFormatter(new PercentFormatter());
        barChart.setData(data);
        final ArrayList<String> xVals = new ArrayList<>();
        xVals.add("Bar");
        xVals.add("QR ");
        // Display labels for bars
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
        barChart.getXAxis().setLabelCount(2);
        barChart.getAxisLeft().setAxisMaximum(40);}
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

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.v("Stats Fragment", e.toString());
    }

    @Override
    public void onNothingSelected() {

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
