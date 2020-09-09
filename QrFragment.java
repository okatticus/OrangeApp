package com.example.android.orange;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.android.orange.Database.Db;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class QrFragment extends Fragment {

    private static final String TAG = " QrFragment";
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private DecoratedBarcodeView barcodeView;
    protected long uid=0;
    private BeepManager beepManager;
    View rootview;
    Db db;
    Toolbar toolbar;
    String ltime, x = null, i;
    DrawerLayout mDrawerLayout;
    private String lastText = null, typeS = null;
    public ArrayList<String> resList;
    public ArrayAdapter<String> ss;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private OnFragmentInteractionListener mListener;

    public QrFragment() {

        // Required empty public constructor
    }

    public static QrFragment newInstance() {

        QrFragment qrFragment = new QrFragment();
        Bundle args = new Bundle();
        qrFragment.setArguments(args);
        return qrFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            typeS = (bundle.getString("type"));
            i = bundle.getString("userId");
            uid = Long.parseLong(i);
        } else {
            Log.v(TAG, "Error in bundle");
        }

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootview = inflater.inflate(R.layout.continuous_scan, container, false);
        resList = new ArrayList<String>();
        permissionStatus = getActivity().getSharedPreferences("permissionStatus", MODE_PRIVATE);
        getActivity().setTitle("QR Scanner");
        barcodeView = (DecoratedBarcodeView) rootview.findViewById(R.id.barcode_scanner);
        Collection<BarcodeFormat> formats;

        if (typeS == "qr") {
            barcodeView.setStatusText("Place QR code here");
            formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.DATA_MATRIX);
        } else {
            barcodeView.setStatusText("Place barcode here");
            formats = Arrays.asList(BarcodeFormat.EAN_8, BarcodeFormat.UPC_A, BarcodeFormat.RSS_14, BarcodeFormat.CODE_39, BarcodeFormat.CODE_128, BarcodeFormat.EAN_13, BarcodeFormat.UPC_E);
        }
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.decodeContinuous(callback);
        beepManager = new BeepManager(getActivity());
        if ((ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)) != PackageManager.PERMISSION_GRANTED) {
           /* AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Need Camera permission");
            builder.setMessage("This app needs camera permission.");
            builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();*/
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
           /*     }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();*/
        } else if (permissionStatus.getBoolean(Manifest.permission.CAMERA, false)) {
            //Previously Permission Request was cancelled with 'Dont Ask Again',
            // Redirect to Settings after showing Information about why you need the permission
           /* AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Need Storage Permission");
            builder.setMessage("This app needs storage permission.");
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();*/
                    sentToSettings = true;
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    int REQUEST_PERMISSION_SETTING = 101;
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    Toast.makeText(getContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
              /*  }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();*/
        } else {
            //just request the permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        int i = 0;
        ListView lv;

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void barcodeResult(BarcodeResult result) {
            x = result.getText();
            db = new Db(getContext());
            Date currentDate = new Date();
            String dateStr = "dd/MM/yyyy";
            //SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
            String v= db.checkforScan(uid,x,ltime);
            ltime = DateFormat.getInstance().format(currentDate);
            final String parts[] = ltime.split(" ", 2);
            ltime = parts[0];
            if (x == null || x.equals(lastText)  ) {
                Log.v(TAG, "Scanned");
                // Prevent duplicate scans
                return;
            }
            if( v != null)
            {
                Toast.makeText(getContext(),"Already scanned",Toast.LENGTH_SHORT).show();
                return;
            }
            lastText = x;
            ss = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, resList);
            lv = rootview.findViewById(R.id.Result_bar);
            onPause();
            AlertDialog.Builder builder= new AlertDialog.Builder(getContext());

            AlertDialog ad = builder.create();
            ad.setTitle("Scanned Code");
            ad.setMessage("Save info " + lastText + " ?");
            ad.setButton(DialogInterface.BUTTON_POSITIVE,"SAVE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    onResume();
                    resList.add(0, "Info- " + lastText + "\nTime-" + parts[0]+" "+parts[1]);
                    lv.setAdapter(ss);
                   long id= db.enterScan(uid,lastText,ltime,typeS);
                   if(id<=0)
                       Log.e("QrFragment","Couldn't enter data");
                }
            });
            ad.setButton(DialogInterface.BUTTON_NEGATIVE,
                    "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    lastText = null;
                    onResume();
                }
            });
            ad.show();
            beepManager.playBeepSoundAndVibrate();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    String entry = (String) parent.getItemAtPosition(position);

                    Toast toast = Toast.makeText(getContext(), entry, Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            ad.setCancelable(false);
            ad.setCanceledOnTouchOutside(false);
        }
        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (typeS.equals("bar"))
            getActivity().setTitle("Barcode Scanner");
        else if (typeS.equals("qr"))
            getActivity().setTitle("QR Scanner");
        else
            getActivity().setTitle("Orange");
        toolbar = rootview.findViewById(R.id.toolbar);
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        Toast.makeText(getContext(), "We got the Permission", Toast.LENGTH_LONG).show();
    }

    public void onResume() {
        super.onResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().setTitle("Orange");
    }

}
