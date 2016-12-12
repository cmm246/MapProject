package com.testapp.mapproject;

import android.support.v4.app.FragmentActivity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.testapp.mapproject.dataload.ParkingDataLoader;
import com.testapp.mapproject.model.Parking;

import org.json.JSONException;

import java.io.InputStream;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, ClusterManager.OnClusterClickListener<Parking>, ClusterManager.OnClusterInfoWindowClickListener<Parking>, ClusterManager.OnClusterItemClickListener<Parking>, ClusterManager.OnClusterItemInfoWindowClickListener<Parking> {

    private GoogleMap mMap;
    private ClusterManager<Parking> mClusterManager;
    //private Realm realm;
    //private Class<M> clazz;

    private class ItemRenderer extends DefaultClusterRenderer<Parking> {
        public ItemRenderer() {
            super(getApplicationContext(), mMap, mClusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(Parking item, MarkerOptions markerOptions) {
            //markerOptions.title("269 Yonge St").snippet("there");
            //markerOptions.snippet("Toronto ON M5B 1N8 Canada");
            markerOptions.title(item.getAddress());
            markerOptions.snippet(item.getCity() + " " + item.getState() + " " + item.getPostalCode());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 4;
        }
    }

    /** Demonstrates customizing the info window and/or its contents. */
    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;

        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            /*render(marker, mWindow);
            return mWindow;*/
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {
            int badge;

            badge = R.drawable.photo_parking;
            ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                //titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(snippet);
                //snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
                //snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpMap();
    }

    @Override
    public boolean onClusterClick(Cluster<Parking> cluster) {
        // Show a toast with some info when the cluster is clicked.
        //String firstName = cluster.getItems().iterator().next().name;
        Toast.makeText(this, "cluster clicked", Toast.LENGTH_SHORT).show();

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Parking> cluster) {
        Toast.makeText(this, "cluster info window clicked", Toast.LENGTH_SHORT).show();
        // Does nothing, but you could go to a list of the users.
    }

    @Override
    public boolean onClusterItemClick(Parking item) {
        // Does nothing, but you could go into the user's profile page, for example.
        Toast.makeText(this, "cluster item clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Parking item) {
        Toast.makeText(this, "cluster item info window clicked", Toast.LENGTH_SHORT).show();
        // Does nothing, but you could go into the user's profile page, for example.
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMap();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (mMap != null) {
            return;
        }

        mMap = map;
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.791116, -122.403816), 10));

        mClusterManager = new ClusterManager<Parking>(this, getMap());
        mClusterManager.setRenderer(new MainActivity.ItemRenderer());

        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);

        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        //mClusterManager.addItems(readItems());
        ParkingDataLoader pk = new ParkingDataLoader();
        mClusterManager.addItems(pk.loadParkingData(this, 500));
    }

    private void setUpMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    private GoogleMap getMap() {
        return mMap;
    }

    /*private List<MyItem> readItems() {
        InputStream inputStream = getResources().openRawResource(R.raw.radar_search);
        List<MyItem> items = null;
        try {
            items = new MyItemReader().read(inputStream);
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }
        return items;
    }*/
}
