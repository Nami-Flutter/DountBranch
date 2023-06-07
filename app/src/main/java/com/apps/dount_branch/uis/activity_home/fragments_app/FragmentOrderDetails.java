package com.apps.dount_branch.uis.activity_home.fragments_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apps.dount_branch.R;
import com.apps.dount_branch.adapter.ProductAdapter;
import com.apps.dount_branch.databinding.FragmentOrderDetailsBinding;
import com.apps.dount_branch.model.OrderModel;
import com.apps.dount_branch.mvvm.FragmentOrderDetailsMvvm;
import com.apps.dount_branch.uis.FragmentMapTouchListener;
import com.apps.dount_branch.uis.activity_base.BaseFragment;
import com.apps.dount_branch.uis.activity_home.HomeActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class FragmentOrderDetails extends BaseFragment implements OnMapReadyCallback {
    private static final String TAG = FragmentOrderDetails.class.getName();
    private HomeActivity activity;
    private FragmentOrderDetailsBinding binding;
    private FragmentOrderDetailsMvvm fragmentOrderDetailsMvvm;
    private OrderModel model;
    private String order_id = "";
    private double lat = 0.0, lng = 0.0;
    private GoogleMap mMap, mMap2, mMap3, mMap4;
    private float zoom = 15.0f;
    private FragmentMapTouchListener fragmentMapFrom, fragmentMapTo, fragmentMapFrom2, fragmentMapTo2;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            order_id = bundle.getString("order_id");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        fragmentOrderDetailsMvvm = ViewModelProviders.of(this).get(FragmentOrderDetailsMvvm.class);
        fragmentOrderDetailsMvvm.onOrderDataSuccess().observe(activity, orderModel -> {

            this.model = orderModel;
            updateData();
        });

        fragmentOrderDetailsMvvm.getIsLoading().observe(activity, isLoading -> {
            if (isLoading) {
                binding.progBar.setVisibility(View.VISIBLE);
            } else {
                binding.progBar.setVisibility(View.GONE);

            }
        });
        fragmentOrderDetailsMvvm.onWay().observe(activity, success -> {
            if (success) {
                binding.setStep("3");
                fragmentOrderDetailsMvvm.getOrderDetails(getUserModel(), order_id);

            }
        });
        fragmentOrderDetailsMvvm.onAccept().observe(activity, success -> {
            if (success) {
                binding.setStep("2");
                fragmentOrderDetailsMvvm.getOrderDetails(getUserModel(), order_id);

            }
        });

        fragmentOrderDetailsMvvm.onRefused().observe(activity, success -> {
            if (success) {
                Navigation.findNavController(binding.getRoot()).popBackStack();

            }
        });

        fragmentOrderDetailsMvvm.onEnded().observe(activity, success -> {
            if (success) {
                Navigation.findNavController(binding.getRoot()).popBackStack();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isEnded", true);
                getParentFragmentManager().setFragmentResult("key1", bundle);

            }
        });

        fragmentOrderDetailsMvvm.getOrderDetails(getUserModel(), order_id);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.color4), PorterDuff.Mode.SRC_IN);

        binding.step1.tvFromShowLocation.setPaintFlags(binding.step1.tvFromShowLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.step1.tvToShowLocation.setPaintFlags(binding.step1.tvToShowLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.step2.tvFromShowLocation.setPaintFlags(binding.step2.tvFromShowLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.step3.tvToShowLocation.setPaintFlags(binding.step3.tvToShowLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        binding.step1.tvFromShowLocation.setOnClickListener(v -> {
            navigateToMap(model.getBranch().getLatitude(), model.getBranch().getLongitude());
        });

        binding.step1.tvToShowLocation.setOnClickListener(v -> {
            navigateToMap(model.getLatitude(), model.getLongitude());
        });

        binding.step2.tvFromShowLocation.setOnClickListener(v -> {

            navigateToMap(model.getBranch().getLatitude(), model.getBranch().getLongitude());
        });

        binding.step3.tvToShowLocation.setOnClickListener(v -> {
            navigateToMap(model.getLatitude(), model.getLongitude());
        });

        binding.btnAccept.setOnClickListener(v -> {
            fragmentOrderDetailsMvvm.acceptOrder(activity, getUserModel(), order_id);
        });

        binding.btnRefuse.setOnClickListener(v -> {
            fragmentOrderDetailsMvvm.refuseOrder(activity, getUserModel(), order_id);

        });

        binding.btnEnd.setOnClickListener(v -> {
            fragmentOrderDetailsMvvm.endOrder(activity, getUserModel(), order_id);

        });

        binding.btnNext.setOnClickListener(v -> {
            fragmentOrderDetailsMvvm.onWayOrder(activity, getUserModel(), order_id);
        });

        binding.imageStep2.setOnClickListener(v -> {
            binding.setStep("2");
        });
        binding.imageStep3.setOnClickListener(v -> {
            binding.setStep("3");

        });

        binding.step1.imageCall.setOnClickListener(v -> {
            String phone = model.getCustomer().getPhone_number();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + phone));
            startActivity(intent);
        });
        binding.step3.imageCall.setOnClickListener(v -> {
            String phone = model.getCustomer().getPhone_number();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + phone));
            startActivity(intent);
        });
    }

    private void navigateToMap(String lat, String longitude) {

        Uri navigationIntentUri = Uri.parse("google.navigation:q=" + lat + "," + longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void updateData() {
        binding.llData.setVisibility(View.VISIBLE);
        updateUI();
        String status = model.getDelivery_status();
        if (status.equals("append")) {
            binding.setStep("1");
            binding.imageStep1.setBackgroundResource(R.drawable.circle_stroke_color4);
            binding.imageStep1.setColorFilter(ContextCompat.getColor(activity, R.color.color4));
        } else if (status.equals("accepted")) {
            binding.setStep("2");

            binding.imageStep2.setBackgroundResource(R.drawable.circle_stroke_color4);
            binding.imageStep2.setColorFilter(ContextCompat.getColor(activity, R.color.color4));
            binding.viewStep2.setBackgroundResource(R.color.color4);


        } else {
            binding.setStep("3");

            binding.imageStep2.setBackgroundResource(R.drawable.circle_stroke_color4);
            binding.imageStep2.setColorFilter(ContextCompat.getColor(activity, R.color.color4));
            binding.viewStep2.setBackgroundResource(R.color.color4);
            binding.imageStep3.setBackgroundResource(R.drawable.circle_stroke_color4);
            binding.imageStep3.setColorFilter(ContextCompat.getColor(activity, R.color.color4));
        }

        binding.step1.setModel(model);
        binding.step2.setModel(model);
        binding.step3.setModel(model);
        binding.step1.recView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        binding.step2.recView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        ProductAdapter adapter = new ProductAdapter(activity);
        adapter.updateList(model.getDetails());
        binding.step1.recView.setAdapter(adapter);
        binding.step2.recView.setAdapter(adapter);

    }

    private void updateUI() {

        fragmentMapFrom = (FragmentMapTouchListener) getChildFragmentManager().findFragmentById(R.id.mapFrom);
        fragmentMapFrom.getMapAsync(this);


        fragmentMapTo = (FragmentMapTouchListener) getChildFragmentManager().findFragmentById(R.id.mapTo);
        fragmentMapTo.getMapAsync(this);

        fragmentMapFrom2 = (FragmentMapTouchListener) getChildFragmentManager().findFragmentById(R.id.mapFrom2);
        fragmentMapFrom2.getMapAsync(this);


        fragmentMapTo2 = (FragmentMapTouchListener) getChildFragmentManager().findFragmentById(R.id.mapTo2);
        fragmentMapTo2.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            if (mMap == null) {
                mMap = googleMap;
                mMap.setTrafficEnabled(false);
                mMap.setBuildingsEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setMapToolbarEnabled(true);
                mMap.setIndoorEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);
                addMarker(mMap, Double.parseDouble(model.getBranch().getLatitude()), Double.parseDouble(model.getBranch().getLongitude()), model.getBranch().getAddress());
            } else if (mMap2 == null) {
                mMap2 = googleMap;
                mMap2.setTrafficEnabled(false);
                mMap2.setBuildingsEnabled(false);
                mMap2.getUiSettings().setMyLocationButtonEnabled(true);
                mMap2.getUiSettings().setMapToolbarEnabled(true);
                mMap2.setIndoorEnabled(true);
                mMap2.getUiSettings().setAllGesturesEnabled(true);
                if (model.getLatitude() != null && !model.getLatitude().isEmpty()) {
                    addMarker(mMap2, Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude()), model.getAddress());
                }

            } else if (mMap3 == null) {
                mMap3 = googleMap;
                mMap3.setTrafficEnabled(false);
                mMap3.setBuildingsEnabled(false);
                mMap3.getUiSettings().setMyLocationButtonEnabled(true);
                mMap3.getUiSettings().setMapToolbarEnabled(true);
                mMap3.setIndoorEnabled(true);
                mMap3.getUiSettings().setAllGesturesEnabled(true);
                if (model.getLatitude() != null && !model.getLatitude().isEmpty()) {

                    addMarker(mMap3, Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude()), model.getAddress());

                }
            } else if (mMap4 == null) {
                mMap4 = googleMap;
                mMap4.setTrafficEnabled(false);
                mMap4.setBuildingsEnabled(false);
                mMap4.getUiSettings().setMyLocationButtonEnabled(true);
                mMap4.getUiSettings().setMapToolbarEnabled(true);
                mMap4.setIndoorEnabled(true);
                mMap4.getUiSettings().setAllGesturesEnabled(true);
                if (model.getLatitude() != null && !model.getLatitude().isEmpty()) {

                    addMarker(mMap4, Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude()), model.getAddress());
                }

            }

            fragmentMapFrom.setListener(() -> binding.step1.scrollView.requestDisallowInterceptTouchEvent(true));
            fragmentMapTo.setListener(() -> binding.step1.scrollView.requestDisallowInterceptTouchEvent(true));

            fragmentMapFrom2.setListener(() -> binding.step2.scrollView.requestDisallowInterceptTouchEvent(true));

            fragmentMapTo2.setListener(() -> binding.step3.scrollView.requestDisallowInterceptTouchEvent(true));


        }
    }


    public void addMarker(GoogleMap mMap, double lat, double lng, String address) {

        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
    }


}