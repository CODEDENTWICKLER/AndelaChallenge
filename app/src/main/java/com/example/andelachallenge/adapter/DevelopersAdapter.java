package com.example.andelachallenge.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.andelachallenge.R;
import com.example.andelachallenge.activities.DeveloperDetailActivity;
import com.example.andelachallenge.fragments.DeveloperDetailFragment;
import com.example.andelachallenge.activities.DeveloperListActivity;
import com.example.andelachallenge.model.Developer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by codedentwickler on 3/4/17.
 */

public class DevelopersAdapter extends RecyclerView.Adapter<DevelopersAdapter.DeveloperViewHolder> {

    private Context mContext;
    private Comparator<Developer> mComparator;

    private List<Developer> mDevelopers;


    public DevelopersAdapter(Context context, Comparator<Developer> comparator) {
        this.mContext  = context;
        this.mComparator = comparator;
    }

    private final SortedList<Developer> mSortedList =
            new SortedList<>(Developer.class, new SortedList.Callback<Developer>() {
                @Override
                public int compare(Developer developer1, Developer developer2) {
                    return mComparator.compare(developer1,developer2);
                }

                @Override
                public void onChanged(int position, int count) {
                    notifyItemRangeChanged(position, count);
                }

                @Override
                public boolean areContentsTheSame(Developer oldDev, Developer newDev) {
                    return oldDev.equals(newDev);
                }

                @Override
                public boolean areItemsTheSame(Developer developer1, Developer developer2) {
                    return developer1 == developer2;
                }

                @Override
                public void onInserted(int position, int count) {
                    notifyItemRangeInserted(position, count);
                }

                @Override
                public void onRemoved(int position, int count) {

                    notifyItemRangeRemoved(position, count);
                }

                @Override
                public void onMoved(int fromPosition, int toPosition) {
                    notifyItemMoved(fromPosition,toPosition);
                }
            });

    /**
     *  This DeveloperViewHolder initialize the views that belong to the items of our RecyclerView.
     */

    class DeveloperViewHolder extends RecyclerView.ViewHolder {

        TextView devUsernameTextView;
        CircleImageView devImageView;
        Developer mDeveloper;
        View mView;


        DeveloperViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            devUsernameTextView = (TextView) itemView.findViewById(R.id.username_textView);
            devImageView = (CircleImageView) itemView.findViewById(R.id.list_imageView);
        }
    }

    /**
     * @param parent
     * @param viewType
     * @return a new view for each item in our recycler view
     */

    @Override
    public DeveloperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.developer_list_content,parent,false);

        return new DeveloperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DeveloperViewHolder holder, int position) {

        holder.mDeveloper = mSortedList.get(position);
        holder.devUsernameTextView.setText(holder.mDeveloper.getUsername());

        String imageUrl = holder.mDeveloper.getImageUrl();
        if (imageUrl == null || imageUrl.equals("")|| imageUrl.isEmpty())
            holder.devImageView.setImageResource(R.drawable.ic_person_outline_black_32dp);
        else {
            Glide.with(mContext)
                    .load(holder.mDeveloper.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter().dontAnimate()
                    .placeholder(R.drawable.ic_person_outline_black_32dp)
                    .into(holder.devImageView);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DeveloperListActivity.mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(DeveloperDetailFragment.ARG_ITEM, holder.mDeveloper);
                    DeveloperDetailFragment fragment = new DeveloperDetailFragment();
                    fragment.setArguments(arguments);
                    ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DeveloperDetailActivity.class);
                    intent.putExtra(DeveloperDetailFragment.ARG_ITEM, holder.mDeveloper);

                    context.startActivity(intent);
                }
                v.setSelected(true);
            }
        });

    }

    public void add(Developer developer) {
        mSortedList.add(developer);
    }
    public void remove(Developer developer) {
        mSortedList.remove(developer);
    }
    public void add(ArrayList<Developer> developers) {
        mSortedList.addAll(developers);
    }

    public void remove(ArrayList<Developer> developers) {
        mSortedList.beginBatchedUpdates();
        for (Developer developer : developers) {
            mSortedList.remove(developer);
        }
        mSortedList.endBatchedUpdates();
    }
    public void replaceAll(ArrayList<Developer> developers) {
        mSortedList.beginBatchedUpdates();
        for (int i = mSortedList.size() - 1; i >= 0; i--) {
            final Developer developer = mSortedList.get(i);
            if (!developers.contains(developer)) {
                mSortedList.remove(developer);
            }
        }
        mSortedList.addAll(developers);
        mSortedList.endBatchedUpdates();
    }

    /**
     * @return the number of items present in the data.
     */

    @Override
    public int getItemCount() {
            return mSortedList.size();
    }
}

//public class DevelopersAdapter extends RecyclerView.Adapter<DevelopersAdapter.DeveloperViewHolder> {
//
//    private List<Developer> mDeveloperList;
//    private Context mContext;
//    private boolean mTwoPane;
//
//    public DevelopersAdapter(Context context,List<Developer> developers, boolean isTwoPane) {
//        this.mContext  = context;
//        this.mDeveloperList = developers;
//        this.mTwoPane = isTwoPane;
//        sortList();
//    }
//
//    public void setList(List<Developer> developers){
//        mDeveloperList.clear();
//        mDeveloperList.addAll(developers);
//        sortList();
//    }
//
//    private void sortList(){
//
//        Collections.sort(mDeveloperList, new Comparator<Developer>() {
//            @Override
//            public int compare(Developer dev1, Developer dev2) {
//                return dev1.getUsername().compareToIgnoreCase(dev2.getUsername());
//            }
//        });
//
//        notifyDataSetChanged();
//    }
//
//    /**
//     *  This DeveloperViewHolder initialize the views that belong to the items of our RecyclerView.
//     */
//
//    public static class DeveloperViewHolder extends RecyclerView.ViewHolder {
//
//        TextView devUsernameTextView;
//        CircleImageView devImageView;
//        Developer mDeveloper;
//        View mView;
//
//
//        public DeveloperViewHolder(View itemView) {
//            super(itemView);
//            mView = itemView;
//            devUsernameTextView = (TextView) itemView.findViewById(R.id.username_textView);
//            devImageView = (CircleImageView) itemView.findViewById(R.id.list_imageView);
//        }
//    }
//
//    /**
//     * @param parent
//     * @param viewType
//     * @return a new view for each item in our recycler view
//     */
//
//    @Override
//    public DeveloperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        View view = LayoutInflater.from(mContext).inflate(R.layout.developer_item,parent,false);
//
//        return new DeveloperViewHolder(view);
//    }
//
//
//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//    }
//
//    @Override
//    public void onBindViewHolder(final DeveloperViewHolder holder, int position) {
//
//        holder.mDeveloper = mDeveloperList.get(position);
//        holder.devUsernameTextView.setText(holder.mDeveloper.getUsername());
//
//        String imageUrl = holder.mDeveloper.getImageUrl();
//        if (imageUrl.equals(null) || imageUrl == "" || imageUrl.isEmpty())
//            holder.devImageView.setImageResource(R.drawable.ic_person_outline_black_52dp);
//        else
//            Glide.with(mContext)
//                    .load(holder.mDeveloper.getImageUrl())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(R.drawable.ic_person_outline_black_52dp)
//                    .into(holder.devImageView);
//
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mTwoPane) {
//                    Bundle arguments = new Bundle();
//                    arguments.putParcelable(DeveloperDetailFragment.ARG_ITEM, holder.mDeveloper);
//                    DeveloperDetailFragment fragment = new DeveloperDetailFragment();
//                    fragment.setArguments(arguments);
//                    ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.item_detail_container, fragment)
//                            .commit();
//                } else {
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, DeveloperDetailActivity.class);
//                    intent.putExtra(DeveloperDetailFragment.ARG_ITEM, holder.mDeveloper);
//
//                    context.startActivity(intent);
//                }
//            }
//        });
//
//    }
//
//    /**
//     * @return the number of items present in the data.
//     */
//
//    @Override
//    public int getItemCount() {
//        return mDeveloperList.size();
//    }
//
//
//}
