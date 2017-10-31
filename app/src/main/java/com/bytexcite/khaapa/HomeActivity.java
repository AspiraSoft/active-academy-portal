package com.bytexcite.khaapa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bytexcite.khaapa.db.DataListener;
import com.bytexcite.khaapa.db.LocalDatabase;
import com.bytexcite.khaapa.db.OnDataSynchronizedListener;
import com.bytexcite.khaapa.models.Item;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, OnDataSynchronizedListener {

    private ViewGroup foodItems;
    private ViewGroup drinkItems;
    private ViewGroup otherItems;

    private ArrayList<Button> buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        foodItems = (ViewGroup) findViewById(R.id.foodItems);
        foodItems.setVisibility(View.VISIBLE);

        drinkItems = (ViewGroup) findViewById(R.id.drinkItems);
        drinkItems.setVisibility(View.GONE);

        otherItems = (ViewGroup) findViewById(R.id.otherItems);
        otherItems.setVisibility(View.GONE);

        buttons.add((Button) findViewById(R.id.foodButton));
        buttons.add((Button) findViewById(R.id.drinksButton));
        buttons.add((Button) findViewById(R.id.miscButton));
        for (Button b : buttons) {
            b.setSelected(false);
            b.setOnClickListener(this);
        }
        buttons.get(0).setSelected(true);

        findViewById(R.id.feedback).setOnClickListener(this);

        // Display locally saved items
        onDataSynchronized();

        // Register data listener to update items in realtime
        DataListener dataListener = new DataListener(this);
        dataListener.register(this);
    }

    @Override
    public void onDataSynchronized() {
        LocalDatabase db = LocalDatabase.getInstance();

        inflateItems(foodItems, db.getFoods());
        inflateItems(drinkItems, db.getDrinks());
        inflateItems(otherItems, db.getOtherItems());
    }

    private void inflateItems(ViewGroup rootView, List<Item> itemList) {
        rootView.removeAllViews();
        for (Item item : itemList) {
            View.inflate(this, R.layout.list_item, rootView);

            ViewGroup view = (ViewGroup) rootView.getChildAt(rootView.getChildCount() - 1);
            TextView name = view.findViewById(R.id.itemName);
            TextView price = view.findViewById(R.id.itemPrice);

            name.setText(item.getName());
            price.setText(item.getPrice() < 100 ? "Rs.  " + item.getPrice() : "Rs. " + item.getPrice());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback:
                startActivity(new Intent(getApplicationContext(), FeedbackActivity.class));
                break;

            default:
                for (Button b : buttons) {
                    b.setSelected(false);
                    if (v.getId() == b.getId()) b.setSelected(true);

                    foodItems.setVisibility(View.GONE);
                    drinkItems.setVisibility(View.GONE);
                    otherItems.setVisibility(View.GONE);
                    switch (v.getId()) {
                        case R.id.foodButton:
                            foodItems.setVisibility(View.VISIBLE);
                            break;
                        case R.id.drinksButton:
                            drinkItems.setVisibility(View.VISIBLE);
                            break;
                        case R.id.miscButton:
                            otherItems.setVisibility(View.VISIBLE);
                            break;
                    }
                }
        }
    }
}
