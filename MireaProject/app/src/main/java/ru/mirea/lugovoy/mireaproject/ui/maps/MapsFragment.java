package ru.mirea.lugovoy.mireaproject.ui.maps;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.mirea.lugovoy.mireaproject.R;
import ru.mirea.lugovoy.mireaproject.ui.maps.dialog.InfoDialog;
import ru.mirea.lugovoy.mireaproject.ui.maps.dialog.RouteDialog;

public class MapsFragment extends Fragment implements DrivingSession.DrivingRouteListener, Observer<Point>
{
    private static final String NAME_MSK_78 = "РТУ МИРЭА - Главный кампус";
    private static final String NAME_MSK_86 = "РТУ МИРЭА - МИТХТ";
    private static final String NAME_MSK_20 = "РТУ МИРЭА - МГУПИ";
    private static final String NAME_FRZ = "РТУ МИРЭА - Филиал в Фрязино";
    private static final String NAME_STV = "РТУ МИРЭА - Филиал в Ставрополе";

    private static final String ADDRESS_MSK_78 = "Москва, просп. Вернадского, 78, стр. 4";
    private static final String ADDRESS_MSK_86 = "Москва, просп. Вернадского, 86";
    private static final String ADDRESS_MSK_20 = "Москва, ул. Стромынка, 20";
    private static final String ADDRESS_FRZ = "Фрязино, ул. Вокзальная, 2А, корп. 61";
    private static final String ADDRESS_STV = "Ставрополь, просп. Кулакова, 8, лит.А";

    private static final String ESTABLISHMENT_MSK_78 = "28 мая 1947 г.";
    private static final String ESTABLISHMENT_MSK_86 = "1 июля 1900 г.";
    private static final String ESTABLISHMENT_MSK_20 = "16 сентября 1936 г.";
    private static final String ESTABLISHMENT_FRZ = "19 сентярбря 1962 г.";
    private static final String ESTABLISHMENT_STV = "18 декабря 1996 г.";

    private static final Point COORDINATES_MSK_78 = new Point(55.669986, 37.480409);
    private static final Point COORDINATES_MSK_86 = new Point(55.661445, 37.477049);
    private static final Point COORDINATES_MSK_20 = new Point(55.794259, 37.701448);
    private static final Point COORDINATES_FRZ = new Point(55.966887, 38.050533);
    private static final Point COORDINATES_STV = new Point(45.052213, 41.912660);

    private static final Point MOSCOW = new Point(55.755819, 37.617644);
    private static final Point HOME_CITY = new Point(55.383838, 36.722850);

    private static final int color = 0xFF00FF00;

    private MapView mapView;

    private ImageButton zoomInButton;
    private ImageButton zoomOutButton;
    private ImageButton buildRoute;

    private List<Place> places = new ArrayList<>();

    private MapObjectCollection mapObjects;

    private PolylineMapObject route;

    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        PointLiveData.getPoint().observe(this, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        MapKitFactory.initialize(requireContext());
        DirectionsFactory.initialize(requireContext());

        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        this.mapView = view.findViewById(R.id.mapview);
        this.zoomInButton = view.findViewById(R.id.zoom_in_button);
        this.zoomOutButton = view.findViewById(R.id.zoom_out_button);
        this.buildRoute = view.findViewById(R.id.build_route_button);

        this.zoomInButton.setOnClickListener(this::zoomIn);
        this.zoomOutButton.setOnClickListener(this::zoomOut);
        this.buildRoute.setOnClickListener(this::buildRoute);

        this.mapView.getMap().move(
                new CameraPosition(MOSCOW, 9.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0), null);

        this.drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        this.mapObjects = this.mapView.getMap().getMapObjects().addCollection();

        initializeBranches();
        setBranchesOnMap();

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        this.mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        this.mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }

    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> list)
    {
        cleanRoute();

        this.route = this.mapObjects.addPolyline(list.get(0).getGeometry());
        this.route.setStrokeColor(color);
    }

    @Override
    public void onDrivingRoutesError(@NonNull Error error)
    {
        String errorMessage = "ERROR";
        Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChanged(Point point)
    {
        if (point != null)
        {
            if (point.getLatitude() != 0 && point.getLongitude() != 0)
            {
                submitRequest(point);
            }
        }
    }

    private void zoomIn(View view)
    {
        this.mapView.getMap().move(new CameraPosition(this.mapView.getMap().getCameraPosition().getTarget(),
                        this.mapView.getMap().getCameraPosition().getZoom() + 1, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0.5f),
                null);
    }

    private void zoomOut(View view)
    {
        this.mapView.getMap().move(new CameraPosition(this.mapView.getMap().getCameraPosition().getTarget(),
                        this.mapView.getMap().getCameraPosition().getZoom() - 1, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0.5f),
                null);
    }

    private void initializeBranches()
    {
        this.places.add(new Place(NAME_MSK_78, ADDRESS_MSK_78, ESTABLISHMENT_MSK_78, COORDINATES_MSK_78));
        this.places.add(new Place(NAME_MSK_86, ADDRESS_MSK_86, ESTABLISHMENT_MSK_86, COORDINATES_MSK_86));
        this.places.add(new Place(NAME_MSK_20, ADDRESS_MSK_20, ESTABLISHMENT_MSK_20, COORDINATES_MSK_20));
        this.places.add(new Place(NAME_FRZ, ADDRESS_FRZ, ESTABLISHMENT_FRZ, COORDINATES_FRZ));
        this.places.add(new Place(NAME_STV, ADDRESS_STV, ESTABLISHMENT_STV, COORDINATES_STV));
    }

    private void setBranchesOnMap()
    {
        this.mapObjects.clear();

        for (Place place : this.places)
        {
            ImageProvider imageProvider = ImageProvider.fromResource(requireContext(), com.yandex.mapkit.search.R.drawable.search_layer_pin_dust_default);

            PlacemarkMapObject placemarkMapObject = this.mapObjects.addPlacemark(place.getPoint(), imageProvider);
            placemarkMapObject.addTapListener((mapObject, point) ->
            {
                if (mapObject.equals(placemarkMapObject))
                {
                    Point p = place.getPoint();

                    String lat = String.valueOf(p.getLatitude()).replace(',', '.');
                    String lon = String.valueOf(p.getLongitude()).replace(',', '.');

                    String pnt = String.format("%s, %s", lat, lon);

                    Bundle args = new Bundle();
                    args.putString("name", place.getName());
                    args.putString("addr", place.getAddress());
                    args.putString("est", place.getEstablishment());
                    args.putString("pnt", pnt);

                    InfoDialog infoDialog = new InfoDialog();
                    infoDialog.setArguments(args);
                    infoDialog.show(requireActivity().getSupportFragmentManager(), "mirea");

                    return true;
                }

                return false;
            });
        }
    }

    private void buildRoute(View view)
    {
        Bundle args = new Bundle();
        args.putSerializable("places", (Serializable) this.places);

        RouteDialog routeDialog = new RouteDialog();
        routeDialog.setArguments(args);
        routeDialog.show(requireActivity().getSupportFragmentManager(), "mirea");
    }

    private void submitRequest(Point destination)
    {
        DrivingOptions options = new DrivingOptions();

        ArrayList<RequestPoint> requestPoints = new ArrayList<>();

        requestPoints.add(new RequestPoint(HOME_CITY, RequestPointType.WAYPOINT, null));
        requestPoints.add(new RequestPoint(destination, RequestPointType.WAYPOINT, null));

        this.drivingSession = this.drivingRouter.requestRoutes(requestPoints, options, this);
    }

    private void cleanRoute()
    {
        if (this.route != null)
        {
            this.mapObjects.remove(this.route);
        }
    }
}