package org.activeacademy.portal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.messaging.RemoteMessage;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.activeacademy.portal.db.DataListener;
import org.activeacademy.portal.db.LocalDatabase;
import org.activeacademy.portal.db.NotificationStorage;
import org.activeacademy.portal.db.OnDataSynchronizedListener;
import org.activeacademy.portal.db.OnNotificationReceivedListener;
import org.activeacademy.portal.models.Item;
import org.activeacademy.portal.utils.MessagingService;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements
        OnDataSynchronizedListener, OnNotificationReceivedListener {

    int[] sampleImages = {
            R.drawable.carousel_0,
            R.drawable.carousel_1,
            R.drawable.carousel_2,
            R.drawable.carousel_3,
            R.drawable.carousel_4,
            R.drawable.carousel_5,
            R.drawable.carousel_6,
            R.drawable.carousel_7,
            R.drawable.carousel_8,
            R.drawable.carousel_9
    };

    private ViewPager mPager;
    private View itemChooser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        CarouselView carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        });

        // Display locally saved items
        onDataSynchronized();

        // Register data listener to update items in realtime
        DataListener dataListener = new DataListener(this);
        dataListener.register(this);

        // Start notification service
        MessagingService.setOnNotificationReceivedListener(this);
        startService(new Intent(getApplicationContext(), MessagingService.class));

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                NotificationStorage.getInstance().markAsRead();
                NotificationStorage.getInstance().saveInstance(getSharedPreferences("Khokha", MODE_PRIVATE));
                onNotificationReceived();
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onDataSynchronized() {
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() == R.id.notifications) {
                itemChooser = item.getActionView();
                if (itemChooser != null) {
                    itemChooser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                            mDrawerLayout.openDrawer(Gravity.END);
                        }
                    });

                    onNotificationReceived();
                }
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.feedback:
                startActivity(new Intent(getApplicationContext(), FeedbackActivity.class));
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(0);
        }
    }

    @Override
    public void onNotificationReceived() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    NotificationStorage n = NotificationStorage.getInstance(getSharedPreferences("Khokha", MODE_PRIVATE));
                    TextView counterBadge = itemChooser.findViewById(R.id.nCount);
                    int nCount = n.getNotifications().size();
                    ViewGroup viewGroup = (ViewGroup) findViewById(R.id.notificationContainer);
                    viewGroup.removeAllViews();
                    if (nCount <= 0) {
                        findViewById(R.id.noNotifications).setVisibility(View.VISIBLE);
                        counterBadge.setVisibility(View.INVISIBLE);
                    } else {
                        findViewById(R.id.noNotifications).setVisibility(View.INVISIBLE);
                        counterBadge.setVisibility(View.VISIBLE);
                        counterBadge.setText(String.valueOf(nCount));
                    }

                    for (RemoteMessage.Notification notification : n.getNotifications()) {
                        View.inflate(getApplicationContext(), R.layout.list_notification, viewGroup);
                        View view = viewGroup.getChildAt(viewGroup.getChildCount() - 1);


                        TextView titleView = view.findViewById(R.id.title);
                        titleView.setText(notification.getTitle());

                        TextView bodyView = view.findViewById(R.id.body);
                        bodyView.setText(notification.getBody());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static class ScreenSlidePageFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.inventory_layout, container, false);
            LinearLayout itemsView = rootView.findViewById(R.id.items);

            String title = getArguments() != null ? getArguments().getString("title", null) : "";
            List<Item> inventory = LocalDatabase.getInstance().getInventory().get(title);
            if (inventory != null) {
                inflateItems(itemsView, inventory);
            }

            return rootView;
        }

        private void inflateItems(ViewGroup rootView, List<Item> itemList) {
            rootView.removeAllViews();
            for (Item item : itemList) {
                View.inflate(getContext(), R.layout.list_item, rootView);

                ViewGroup view = (ViewGroup) rootView.getChildAt(rootView.getChildCount() - 1);
                TextView name = view.findViewById(R.id.itemName);
                TextView price = view.findViewById(R.id.itemPrice);

                name.setText(item.getName());
                price.setText(item.getPrice() < 100 ? "Rs.  " + item.getPrice() : "Rs. " + item.getPrice());
            }
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("title", String.valueOf(getPageTitle(position)));

            Fragment fragment = new ScreenSlidePageFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        public int getCount() {
            return LocalDatabase.getInstance().getInventory().size();
        }

        public CharSequence getPageTitle(int position) {
            return LocalDatabase.getInstance().getType(position);
        }
    }
}
