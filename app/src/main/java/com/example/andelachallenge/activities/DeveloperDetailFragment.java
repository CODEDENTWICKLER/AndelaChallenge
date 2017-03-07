package com.example.andelachallenge.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.andelachallenge.R;
import com.example.andelachallenge.model.Developer;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link DeveloperListActivity}
 * in two-pane mode (on tablets) or a {@link DeveloperDetailActivity}
 * on handsets.
 */
public class DeveloperDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public ImageView mImageView;

    public static final String ARG_ITEM = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Developer mDeveloper;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DeveloperDetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);


        if (getArguments().containsKey(ARG_ITEM)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mDeveloper = getArguments().getParcelable(ARG_ITEM);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout collapsingToolbarLayout =
                    (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

            if (collapsingToolbarLayout != null) {
                collapsingToolbarLayout.setTitle(getActivity().getTitle());

                mImageView = (ImageView) collapsingToolbarLayout.findViewById(R.id.profile_image);

            }
            else {
                mImageView = (ImageView) rootView.findViewById(R.id.profile_image_two_pane);
                mImageView.setVisibility(View.VISIBLE);
            }

            Glide.with(getActivity())
                    .load(mDeveloper.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_person_outline_black_32dp)
                    .into(mImageView);


        }

        if (mDeveloper != null) {
            ((TextView) rootView.findViewById(R.id.username)).setText(mDeveloper.getUsername());
           TextView githubLinkTextView = ((TextView) rootView.findViewById(R.id.github_link));

            githubLinkTextView.setText(mDeveloper.getGithubProfileUrl());
            githubLinkTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(mDeveloper.getGithubProfileUrl()));
                    startActivity(intent);
                }
            });

        }

        return rootView;
    }
}
