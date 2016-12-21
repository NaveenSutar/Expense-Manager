package DBFragment;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.shrikantbadwaik.casestudy.ExpanseActivity;
import com.shrikantbadwaik.casestudy.R;

public class AboutUsFragment extends Fragment implements RatingBar.OnRatingBarChangeListener {

    private MenuItem activityItem;
    private Menu activityMenu;
    private RatingBar ratingBar;

    public AboutUsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ratingBar=(RatingBar)view.findViewById(R.id.ratingBar);
        setHasOptionsMenu(true);

        ratingBar.setOnRatingBarChangeListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        activityMenu = menu;

        for (int index=0;index<=3;index++){
            activityItem = activityMenu.getItem(index);
            activityItem.setVisible(false);
        }
        menu.add("Share");
        menu.add("Rate Us");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Share")) {
            String shareBody = "https://play.google.com/store/apps/details?id=com.shrikantbadwaik.casestudy";

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Expense Manager (Open it in Google Play Store to Download the Application)");

            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } else if (item.getTitle().equals("Rate Us")) {
            ratingBar.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(goToMarket);
    }

    @Override
    public void onResume() {
        super.onResume();
        ratingBar.setVisibility(View.GONE);
    }
}
