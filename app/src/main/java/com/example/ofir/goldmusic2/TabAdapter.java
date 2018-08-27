package com.example.ofir.goldmusic2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ofir on 25-Jun-18.
 */

public class TabAdapter extends PagerAdapter
{
    private CustomViewPager pager;
    private ArrayList<FragmentInfo> pages = new ArrayList<>();
    private LayoutInflater li;
    private List<View> views = new ArrayList<>();
    private Context context;


    public TabAdapter(Context context, CustomViewPager pager)
    {
        this.li = LayoutInflater.from(context);
        this.pager = pager;
        this.context = context;
    }


    public void add(RecyclerView.Adapter adapter, int col)
    {
        pages.add(new FragmentInfo(adapter, col));
        notifyDataSetChanged();
        pager.setCurrentItem(pager.getCurrentItem() + 1, true);
    }

    public void remove(int position)
    {
        pager.setCurrentItem(pager.getCurrentItem() - 1, true);
        pages.remove(position);
        views.remove(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position)
    {
        View view = li.inflate(R.layout.list, container, false);
        FragmentInfo info = pages.get(position);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(info.adapter);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, info.col);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        view.setTag(info);
        // Add the newly created View to the ViewPager
        container.addView(view);
        views.add(position, view);
        // Return the View
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object)
    {
        container.removeView((View) object);
    }

    @Override
    public int getCount()
    {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
    {
        return object == view;
    }

    @Override
    public int getItemPosition(@NonNull Object object)
    {
        if (views.contains((View) object))
            return views.indexOf((View) object);
        else
            return POSITION_NONE;
    }

    private class FragmentInfo
    {
        RecyclerView.Adapter adapter;
        int col;

        FragmentInfo(RecyclerView.Adapter adapter, int col)
        {
            this.adapter = adapter;
            this.col = col;
        }
    }
}
