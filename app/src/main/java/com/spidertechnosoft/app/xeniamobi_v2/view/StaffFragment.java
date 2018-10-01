package com.spidertechnosoft.app.xeniamobi_v2.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Product;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.User;
import com.spidertechnosoft.app.xeniamobi_v2.model.service.UserService;
import com.spidertechnosoft.app.xeniamobi_v2.session.SessionManager;
import com.spidertechnosoft.app.xeniamobi_v2.view.adpater.StaffGridViewAdapter;
import com.spidertechnosoft.app.xeniamobi_v2.view.helper.OperationFragments;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StaffFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class StaffFragment extends Fragment {

    UserService userService=new UserService();

    private Context mContext;

    private View mRootView;

    User mUser;

    private LayoutInflater mCurrentInflater;

    //private Database mDatabase;
    SessionManager sessionManager;

    List<User> mUsers;

    private OnFragmentInteractionListener mListener;

    GridView gvStaff;

    StaffGridViewAdapter staffGridViewAdapter;

    ProgressDialog dlgProgress;

    final Handler mHandler = new Handler();
    final Runnable mUpdateUserList = new Runnable() {

        @Override
        public void run() {
            updateUserList();
        }

    };

    public StaffFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Save the context
        this.mContext = getActivity();


        // Store the inflater reference
        this.mCurrentInflater = inflater;

        this.mRootView = inflater.inflate(R.layout.fragment_staff, container, false);

        //this.mDatabase= Database.getInstance();
        sessionManager=new SessionManager(mContext);

        this.mUser=sessionManager.getLoggedInUser();

        this.mUsers=new ArrayList<User>();

        mListener.setFooter(OperationFragments.PRODUCT_FRAGMENT);

        configView();

        return mRootView;
    }

    public void configView(){

        gvStaff=(GridView)mRootView.findViewById(R.id.gvStaff);

        gvStaff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get the GridView selected/clicked item text
                User selectedStaff = (User)parent.getItemAtPosition(position);

                ((BillCreationActivity)mContext).getSale().setUserId (selectedStaff.getUid());
                ((BillCreationActivity)mContext).getSale().setUserName (selectedStaff.getName());

            }
        });

        staffGridViewAdapter=new StaffGridViewAdapter(mContext,mUsers);

        gvStaff.setAdapter(staffGridViewAdapter);



    }


    /**
     * Fetch Category List From Database
     */
    protected void fetchUserList() {

        dlgProgress=new ProgressDialog(mContext);

        dlgProgress.setMessage("Fetching users...");
        dlgProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dlgProgress.setIndeterminate(true);
        dlgProgress.show();

        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {

            public void run() {

                try {

                    mUsers.clear();

                    String query=null;

                    String[] params=null;

                    mUsers.addAll(userService.findActiveStaffUsers(mUser));


                } catch(Exception e){
                    Log.e("YOUR_APP_LOG_TAG", "I got an error", e);
                }

                // Post to the mUpdateProductList runnable
                mHandler.post(mUpdateUserList);
            }
        };
        t.start();
    }

    /**
     * Update View With Category Items
     */
    protected  void updateUserList(){

        staffGridViewAdapter.notifyDataSetChanged();

        dlgProgress.dismiss();

        String staffUid=((BillCreationActivity)mContext).getSale().getUserId();

        staffUid=(staffUid==null?sessionManager.getLoggedInUser().getUid():staffUid);

        selectGridViewItemByValue(gvStaff,staffUid);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchUserList();

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
        void setFooterCartTotal();
        Double getProductPrice(Product product);
        void setFooter(OperationFragments uri);
    }

    /**
     * Method to select the sector item by value
     * @param gridView                  : Spinner object
     * @param uid     : The header id
     */
    public void selectGridViewItemByValue(GridView gridView, String uid) {

        if(uid==null){

            // Set the selection
            gridView.setSelection(0);

            return;
        }

        // Create the adapter
        StaffGridViewAdapter adapter = (StaffGridViewAdapter) gridView.getAdapter();

        // Iterate through the values and get the header
        for (int position = 0; position < adapter.getCount(); position++) {

            // GEt the header
            User user=(User) adapter.getItem(position);

            // If the header id and the passed header id are matching, set the
            // item as selected
            if(user.getUid().equals(uid)) {

                // Set the selection
                gridView.setItemChecked(position,true);


                // return control
                return;
            }
        }
    }
}
