/*
 * Copyright (c) 2016 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.dmsexplorer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.mm2d.android.upnp.cds.CdsObject;
import net.mm2d.android.util.AribUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * CDSのコンテンツリストをRecyclerViewへ表示するためのAdapter。
 *
 * @author <a href="mailto:ryo@mm2d.net">大前良介(OHMAE Ryosuke)</a>
 */
public class CdsListAdapter
        extends RecyclerView.Adapter<CdsListAdapter.ViewHolder> {
    private static final String TAG = "CdsListAdapter";

    public interface OnItemClickListener {
        void onItemClick(View v, int position, CdsObject object);
    }

    private static final int NOT_SELECTED = -1;
    private final int mSelectedZ;
    private final int mAccentRadius;
    private final LayoutInflater mInflater;
    private final List<CdsObject> mList;
    private OnItemClickListener mListener;
    private int mSelection = NOT_SELECTED;

    public CdsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mList = new ArrayList<>();
        final Resources res = context.getResources();
        mSelectedZ = res.getDimensionPixelSize(R.dimen.raise_focus);
        mAccentRadius = res.getDimensionPixelSize(R.dimen.accent_radius);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.cds_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.applyItem(position, mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mListener = l;
    }

    public void clear() {
        mList.clear();
    }

    public void addAll(Collection<? extends CdsObject> objects) {
        mList.addAll(objects);
    }

    public int add(CdsObject obj) {
        mList.add(obj);
        return mList.size() - 1;
    }

    public int remove(CdsObject obj) {
        final int position = mList.indexOf(obj);
        if (position >= 0) {
            mList.remove(position);
        }
        return position;
    }

    public void setSelection(int position) {
        final int previous = mSelection;
        mSelection = position;
        if (previous != position) {
            notifyItemChangedIfPossible(previous);
        }
        notifyItemChangedIfPossible(position);
    }

    private void notifyItemChangedIfPossible(int position) {
        if (position == NOT_SELECTED || position >= getItemCount()) {
            return;
        }
        notifyItemChanged(position);
    }

    public void clearSelection() {
        setSelection(NOT_SELECTED);
    }

    public int getSelection() {
        return mSelection;
    }

    private final View.OnClickListener mItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final ViewHolder holder = (ViewHolder) v.getTag();
            final CdsObject obj = holder.getItem();
            final int position = holder.getListPosition();
            if (mListener != null) {
                mListener.onItemClick(v, position, obj);
            }
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final View mMark;
        private final TextView mAccent;
        private final TextView mText1;
        private final TextView mText2;
        private final ImageView mImage;
        private int mPosition;
        private CdsObject mObject;
        private final GradientDrawable mAccentBackground;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mMark = mView.findViewById(R.id.mark);
            mAccent = (TextView) mView.findViewById(R.id.textAccent);
            mText1 = (TextView) mView.findViewById(R.id.text1);
            mText2 = (TextView) mView.findViewById(R.id.text2);
            mImage = (ImageView) mView.findViewById(R.id.image);
            mView.setOnClickListener(mItemClickListener);
            mView.setTag(this);
            mAccentBackground = new GradientDrawable();
            mAccentBackground.setCornerRadius(mAccentRadius);
            mAccent.setBackground(mAccentBackground);
        }

        void applyItem(int position, CdsObject obj) {
            mPosition = position;
            if (position == mSelection) {
                mMark.setVisibility(View.VISIBLE);
                mView.setSelected(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mView.setTranslationZ(mSelectedZ);
                }
            } else {
                mMark.setVisibility(View.INVISIBLE);
                mView.setSelected(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mView.setTranslationZ(0);
                }
            }
            mObject = obj;
            final String name = obj.getTitle();
            if (!name.isEmpty()) {
                final String c = name.substring(0, 1);
                mAccent.setText(AribUtils.toDisplayableString(c));
            } else {
                mAccent.setText("");
            }
            mAccentBackground.setColor(ThemeUtils.getAccentColor(name));
            mText1.setText(AribUtils.toDisplayableString(name));
            mText2.setText(obj.getUpnpClass());
            mImage.setImageResource(getImageResource(obj.getType()));
        }

        private int getImageResource(int type) {
            switch (type) {
                case CdsObject.TYPE_CONTAINER:
                    return R.drawable.ic_folder;
                case CdsObject.TYPE_AUDIO:
                    return R.drawable.ic_music;
                case CdsObject.TYPE_IMAGE:
                    return R.drawable.ic_image;
                case CdsObject.TYPE_VIDEO:
                    return R.drawable.ic_movie;
                case CdsObject.TYPE_UNKNOWN:
                default:
                    return 0;
            }
        }

        CdsObject getItem() {
            return mObject;
        }

        int getListPosition() {
            return mPosition;
        }
    }
}
