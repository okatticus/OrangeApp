package com.example.android.orange;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.orange.Database.Db;
import com.example.android.orange.ListAdapter1.customButtonListener;

import java.util.ArrayList;

public class AdminFragment extends Fragment implements customButtonListener {
    public ArrayList<String> userList;
    public ListAdapter1 ss;
    public ListView lv;
    View rootview;
    public Db db;
    String temp, mail,t,is;
    long idu;

    private OnFragmentInteractionListener mListener;

    public AdminFragment() {
        // Required empty public constructor
    }

    public static AdminFragment newInstance(String t,int id) {
        AdminFragment fragment = new AdminFragment();
        Bundle args = new Bundle();
        args.putString("ut",t);
        args.putString("id",String.valueOf(id));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            t= getArguments().getString("ut");
            is=getArguments().getString("id");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = new Db(getContext());
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_admin, container, false);
        lv = rootview.findViewById(R.id.listview);
        userList = db.getAllElements();
        ss = new ListAdapter1(getContext(), userList);
        ss.setCustomButtonListner(this);
        lv.setAdapter(ss);
        return rootview;
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
    public void onButtonClickListner(int position, String value) {

        temp = (String) lv.getItemAtPosition(position);
        User u = db.getData(temp);
        idu = u.getUserId();
        mail = u.getMail();
        final String type=u.getUserType();
        if( type.equals("Admin") && !t.equals("Admin"))
        {
                AlertDialog ad = new AlertDialog.Builder(
                        getContext()).create();
                ad.setTitle("Cannot delete");
                ad.setMessage("You cannot delete admin");
                ad.setIcon(R.drawable.user_error);
                ad.show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Delete");
            final String name = u.getName() + " " + u.getLastName();
            builder.setMessage("Delete data for user : " + name + " with E-mail: " + mail);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {

                    Boolean del1 = db.deleteScan(idu);
                    Boolean del = db.deleteUser(idu);
                    Toast.makeText(getContext(), "Deleted user: " + name, Toast.LENGTH_SHORT).show();
                    if (type.equals("Admin")) {
                        db.adminDel();
                        Intent in = new Intent(getContext(), MainActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);
                    }
                    else if(String.valueOf(idu).equals(is))
                    {
                        Intent in = new Intent(getContext(), MainActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);
                    }
                    onResume();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {

                }
            });
            builder.show();
        }
    }
    public interface OnFragmentInteractionListener {

        public void onAdminFragmentInteraction(String string);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().setTitle("Orange");
    }

    @Override
    public void onResume() {
        userList = db.getAllElements();
        ss = new ListAdapter1(getContext(), userList);
        ss.setCustomButtonListner(this);
        lv.setAdapter(ss);
        super.onResume();
    }
}
