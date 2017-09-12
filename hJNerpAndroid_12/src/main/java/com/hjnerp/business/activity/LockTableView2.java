package com.hjnerp.business.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.rmondjone.locktableview.CustomHorizontalScrollView;
import com.rmondjone.locktableview.CustomHorizontalScrollView.onScrollChangeListener;
import com.rmondjone.locktableview.DisplayUtil;
import com.rmondjone.locktableview.R.color;
import com.rmondjone.locktableview.R.id;
import com.rmondjone.locktableview.R.layout;

import java.util.ArrayList;
import java.util.List;


public class LockTableView2 {
    private Context mContext;
    private ViewGroup mContentView;
    private ArrayList<ArrayList<String>> mTableDatas = new ArrayList();
    private View mTableView;
    private boolean isLockFristRow = true;
    private boolean isLockFristColumn = true;
    private int maxColumnWidth;
    private int minColumnWidth;
    private int maxRowHeight;
    private int minRowHeight;
    private int mFristRowBackGroudColor;
    private String mNullableString;
    private int mTextViewSize;
    private int mHeadTextViewSize;
    private int mTableHeadTextColor;
    private int mTableContentTextColor;
    private LockTableView.OnTableViewListener mTableViewListener;
    private ArrayList<String> mTableFristData = new ArrayList();
    private ArrayList<String> mTableColumnDatas = new ArrayList();
    private String mColumnTitle;
    private ArrayList<ArrayList<String>> mTableRowDatas = new ArrayList();
    private ArrayList<Integer> mColumnMaxWidths = new ArrayList();
    private ArrayList<Integer> mRowMaxHeights = new ArrayList();
    private ArrayList<HorizontalScrollView> mScrollViews = new ArrayList();
    private TextView mColumnTitleView;
    private LinearLayout mLockHeadView;
    private LinearLayout mUnLockHeadView;
    private CustomHorizontalScrollView mLockScrollView;
    private CustomHorizontalScrollView mUnLockScrollView;
    private ScrollView mTableScrollView;
    private int margin_num = 10;

    public LockTableView2(Context mContext, ViewGroup mContentView, ArrayList<ArrayList<String>> mTableDatas) {
        this.mContext = mContext;
        this.mContentView = mContentView;
        this.mTableDatas = mTableDatas;
        this.initAttrs();
    }

    private void initAttrs() {
        this.mTableView = LayoutInflater.from(this.mContext).inflate(layout.locktableview, (ViewGroup) null);
        this.maxColumnWidth = 100;
        this.minColumnWidth = 70;
        this.minRowHeight = 20;
        this.maxRowHeight = 60;
        this.mNullableString = "N/A";
        this.mTableHeadTextColor = color.beijin;
        this.mTableContentTextColor = color.border_color;
        this.mFristRowBackGroudColor = color.table_head;
        this.mTextViewSize = 16;
    }

    public void show() {
        this.initData();
        this.initView();
        this.mContentView.removeAllViews();
        this.mContentView.addView(this.mTableView);
    }

    private void initData() {
        if (this.mTableDatas != null && this.mTableDatas.size() > 0) {
            int maxLength = 0;

            int i;
            ArrayList rowDatas;
            int rowDatas1;
            for (i = 0; i < this.mTableDatas.size(); ++i) {
                if (((ArrayList) this.mTableDatas.get(i)).size() >= maxLength) {
                    maxLength = ((ArrayList) this.mTableDatas.get(i)).size();
                }

                rowDatas = (ArrayList) this.mTableDatas.get(i);

                for (rowDatas1 = 0; rowDatas1 < rowDatas.size(); ++rowDatas1) {
                    if (rowDatas.get(rowDatas1) == null || ((String) rowDatas.get(rowDatas1)).equals("")) {
                        rowDatas.set(rowDatas1, this.mNullableString);
                    }
                }

                this.mTableDatas.set(i, rowDatas);
            }

            int textView;
            for (i = 0; i < this.mTableDatas.size(); ++i) {
                rowDatas = (ArrayList) this.mTableDatas.get(i);
                if (rowDatas.size() < maxLength) {
                    rowDatas1 = maxLength - rowDatas.size();

                    for (textView = 0; textView < rowDatas1; ++textView) {
                        rowDatas.add(this.mNullableString);
                    }

                    this.mTableDatas.set(i, rowDatas);
                }
            }

            int j;
            int currentHeight;
            StringBuffer var12;
            for (i = 0; i < this.mTableDatas.size(); ++i) {
                rowDatas = (ArrayList) this.mTableDatas.get(i);
                var12 = new StringBuffer();

                for (textView = 0; textView < rowDatas.size(); ++textView) {
                    TextView textViewParams = new TextView(this.mContext);
                    textViewParams.setTextSize(2, (float) this.mTextViewSize);
                    textViewParams.setText((CharSequence) rowDatas.get(textView));
                    textViewParams.setGravity(17);
                    LayoutParams maxHeight = new LayoutParams(-2, -2);
                    maxHeight.setMargins(margin_num, margin_num, margin_num, margin_num);
                    textViewParams.setLayoutParams(maxHeight);
                    if (i == 0) {
                        this.mColumnMaxWidths.add(Integer.valueOf(this.measureTextWidth(textViewParams, (String) rowDatas.get(textView))));
                        var12.append("[" + this.measureTextWidth(textViewParams, (String) rowDatas.get(textView)) + "]");
                    } else {
                        j = ((Integer) this.mColumnMaxWidths.get(textView)).intValue();
                        currentHeight = this.measureTextWidth(textViewParams, (String) rowDatas.get(textView));
                        if (currentHeight > j) {
                            this.mColumnMaxWidths.set(textView, Integer.valueOf(currentHeight));
                        }

                        var12.append("[" + this.measureTextWidth(textViewParams, (String) rowDatas.get(textView)) + "]");
                    }
                }
            }

            for (i = 0; i < this.mTableDatas.size(); ++i) {
                rowDatas = (ArrayList) this.mTableDatas.get(i);
                var12 = new StringBuffer();
                TextView var14 = new TextView(this.mContext);
                var14.setTextSize(2, (float) this.mTextViewSize);
                var14.setGravity(17);
                LayoutParams var15 = new LayoutParams(-2, -2);
                var15.setMargins(margin_num, margin_num, margin_num, margin_num);
                var14.setLayoutParams(var15);
                int var16 = this.measureTextHeight(var14, (String) rowDatas.get(0));
                this.mRowMaxHeights.add(Integer.valueOf(var16));

                for (j = 0; j < rowDatas.size(); ++j) {
                    currentHeight = this.measureTextHeight(var14, (String) rowDatas.get(j));
                    var12.append("[" + currentHeight + "]");
                    if (currentHeight > var16) {
                        this.mRowMaxHeights.set(i, Integer.valueOf(currentHeight));
                    }
                }
            }

            if (this.isLockFristRow) {
                ArrayList var11 = (ArrayList) this.mTableDatas.get(0);
                int var10;
                if (this.isLockFristColumn) {
                    this.mColumnTitle = (String) var11.get(0);
                    var11.remove(0);
                    this.mTableFristData.addAll(var11);

                    for (var10 = 1; var10 < this.mTableDatas.size(); ++var10) {
                        ArrayList var13 = (ArrayList) this.mTableDatas.get(var10);
                        this.mTableColumnDatas.add((String) var13.get(0));
                        var13.remove(0);
                        this.mTableRowDatas.add(var13);
                    }
                } else {
                    this.mTableFristData.addAll(var11);

                    for (var10 = 1; var10 < this.mTableDatas.size(); ++var10) {
                        this.mTableRowDatas.add(this.mTableDatas.get(var10));
                    }
                }
            } else if (this.isLockFristColumn) {
                for (i = 0; i < this.mTableDatas.size(); ++i) {
                    rowDatas = (ArrayList) this.mTableDatas.get(i);
                    this.mTableColumnDatas.add((String) rowDatas.get(0));
                    rowDatas.remove(0);
                    this.mTableRowDatas.add(rowDatas);
                }
            } else {
                for (i = 0; i < this.mTableDatas.size(); ++i) {
                    this.mTableRowDatas.add(this.mTableDatas.get(i));
                }
            }
        } else {
            Toast.makeText(this.mContext, "表格数据为空！", Toast.LENGTH_SHORT).show();
        }

    }

    private void initView() {
        this.mColumnTitleView = (TextView) this.mTableView.findViewById(id.lockHeadView_Text);
        this.mLockHeadView = (LinearLayout) this.mTableView.findViewById(id.lockHeadView);
        this.mUnLockHeadView = (LinearLayout) this.mTableView.findViewById(id.unLockHeadView);
        this.mLockScrollView = (CustomHorizontalScrollView) this.mTableView.findViewById(id.lockHeadView_ScrollView);
        this.mUnLockScrollView = (CustomHorizontalScrollView) this.mTableView.findViewById(id.unlockHeadView_ScrollView);
        this.mTableScrollView = (ScrollView) this.mTableView.findViewById(id.table_scrollView);
        this.mLockHeadView.setBackgroundColor(ContextCompat.getColor(this.mContext, this.mFristRowBackGroudColor));
        this.mUnLockHeadView.setBackgroundColor(ContextCompat.getColor(this.mContext, this.mFristRowBackGroudColor));
        if (this.isLockFristRow) {
            if (this.isLockFristColumn) {
                this.mLockHeadView.setVisibility(View.VISIBLE);
                this.mUnLockHeadView.setVisibility(View.GONE);
            } else {
                this.mLockHeadView.setVisibility(View.GONE);
                this.mUnLockHeadView.setVisibility(View.VISIBLE);
            }

            this.creatHeadView();
        } else {
            this.mLockHeadView.setVisibility(View.GONE);
            this.mUnLockHeadView.setVisibility(View.GONE);
        }

        this.createTableView();
    }

    private void creatHeadView() {
        if (this.isLockFristColumn) {
            this.mColumnTitleView.setTextColor(ContextCompat.getColor(this.mContext, this.mTableHeadTextColor));
            this.mColumnTitleView.setTextSize(2, (float) this.mHeadTextViewSize);
            this.mColumnTitleView.setTypeface(null, Typeface.BOLD);
            this.mColumnTitleView.setText(this.mColumnTitle);
            LayoutParams layoutParams = (LayoutParams) this.mColumnTitleView.getLayoutParams();
            layoutParams.width = DisplayUtil.dip2px(this.mContext, (float) ((Integer) this.mColumnMaxWidths.get(0)).intValue());
            layoutParams.height = DisplayUtil.dip2px(this.mContext, (float) ((Integer) this.mRowMaxHeights.get(0)).intValue());
            layoutParams.setMargins(margin_num, margin_num, margin_num, margin_num);
            this.mColumnTitleView.setLayoutParams(layoutParams);
            this.createScollview(this.mLockScrollView, this.mTableFristData, true);
            this.mScrollViews.add(this.mLockScrollView);
            this.mLockScrollView.setOnScrollChangeListener(new onScrollChangeListener() {
                public void onScrollChanged(HorizontalScrollView scrollView, int x, int y) {
                    LockTableView2.this.changeAllScrollView(x, y);
                }
            });
        } else {
            this.createScollview(this.mUnLockScrollView, this.mTableFristData, true);
            this.mScrollViews.add(this.mUnLockScrollView);
            this.mUnLockScrollView.setOnScrollChangeListener(new onScrollChangeListener() {
                public void onScrollChanged(HorizontalScrollView scrollView, int x, int y) {
                    LockTableView2.this.changeAllScrollView(x, y);
                }
            });
        }

    }

    private void createTableView() {
        if (this.isLockFristColumn) {
            this.createLockColumnView();
        } else {
            this.createUnLockColumnView();
        }

    }

    private void createLockColumnView() {
        View lockTableViewContent = LayoutInflater.from(this.mContext).inflate(layout.locktablecontentview, (ViewGroup) null);
        LinearLayout lockViewParent = (LinearLayout) lockTableViewContent.findViewById(id.lockView_parent);
        CustomHorizontalScrollView lockScrollViewParent = (CustomHorizontalScrollView) lockTableViewContent.findViewById(id.lockScrollView_parent);

        View splite;
        android.view.ViewGroup.LayoutParams spliteLayoutParam;
        for (int scollViewItemContentView = 0; scollViewItemContentView < this.mTableColumnDatas.size(); ++scollViewItemContentView) {
            LinearLayout scollViewItemContentViewParams = new LinearLayout(this.mContext);
            LayoutParams i = new LayoutParams(-2, -2);
            scollViewItemContentViewParams.setOrientation(LinearLayout.HORIZONTAL);
            scollViewItemContentViewParams.setLayoutParams(i);
            TextView datas = new TextView(this.mContext);
            LayoutParams linearLayout = new LayoutParams(-2, -2);
            linearLayout.setMargins(margin_num, margin_num, margin_num, margin_num);
            datas.setLayoutParams(linearLayout);
            datas.setTextSize(2, (float) this.mTextViewSize);
            datas.setText((CharSequence) this.mTableColumnDatas.get(scollViewItemContentView));
            datas.setGravity(17);
            android.view.ViewGroup.LayoutParams layoutParams = datas.getLayoutParams();
            layoutParams.width = DisplayUtil.dip2px(this.mContext, (float) ((Integer) this.mColumnMaxWidths.get(0)).intValue());
            if (this.isLockFristRow) {
                layoutParams.height = DisplayUtil.dip2px(this.mContext, (float) ((Integer) this.mRowMaxHeights.get(scollViewItemContentView + 1)).intValue());
            } else {
                layoutParams.height = DisplayUtil.dip2px(this.mContext, (float) ((Integer) this.mRowMaxHeights.get(scollViewItemContentView)).intValue());
            }

            datas.setLayoutParams(layoutParams);
            if (!this.isLockFristRow) {
                if (scollViewItemContentView == 0) {
                    scollViewItemContentViewParams.setBackgroundColor(ContextCompat.getColor(this.mContext, this.mFristRowBackGroudColor));
                    datas.setTextColor(ContextCompat.getColor(this.mContext, this.mTableHeadTextColor));
                } else {
                    datas.setTextColor(ContextCompat.getColor(this.mContext, this.mTableContentTextColor));
                }
            } else {
                datas.setTextColor(ContextCompat.getColor(this.mContext, this.mTableContentTextColor));
            }

            scollViewItemContentViewParams.addView(datas);
            splite = new View(this.mContext);
            spliteLayoutParam = new android.view.ViewGroup.LayoutParams(-1, DisplayUtil.dip2px(this.mContext, 1.0F));
            splite.setLayoutParams(spliteLayoutParam);
            splite.setBackgroundColor(ContextCompat.getColor(this.mContext, color.light_gray));
            lockViewParent.addView(scollViewItemContentViewParams);
            lockViewParent.addView(splite);
        }

        LinearLayout var16 = new LinearLayout(this.mContext);
        LayoutParams var17 = new LayoutParams(-1, -2);
        var16.setLayoutParams(var17);
        var16.setOrientation(LinearLayout.VERTICAL);

        for (int var18 = 0; var18 < this.mTableRowDatas.size(); ++var18) {
            ArrayList var19 = (ArrayList) this.mTableRowDatas.get(var18);
            LinearLayout var20 = new LinearLayout(this.mContext);
            LayoutParams var21 = new LayoutParams(-2, -2);
            var20.setLayoutParams(var21);
            var20.setGravity(17);
            var20.setOrientation(LinearLayout.HORIZONTAL);
            if (!this.isLockFristRow && var18 == 0) {
                var20.setBackgroundColor(ContextCompat.getColor(this.mContext, this.mFristRowBackGroudColor));
            }

            for (int var22 = 0; var22 < var19.size(); ++var22) {
                TextView var23 = new TextView(this.mContext);
                if (!this.isLockFristRow) {
                    if (var18 == 0) {
                        var23.setTextColor(ContextCompat.getColor(this.mContext, this.mTableHeadTextColor));
                    } else {
                        var23.setTextColor(ContextCompat.getColor(this.mContext, this.mTableContentTextColor));
                    }
                } else {
                    var23.setTextColor(ContextCompat.getColor(this.mContext, this.mTableContentTextColor));
                }

                var23.setTextSize(2, (float) this.mTextViewSize);
                var23.setGravity(17);
                var23.setText((CharSequence) var19.get(var22));
                LayoutParams textViewParams = new LayoutParams(-2, -2);
                textViewParams.setMargins(margin_num, margin_num, margin_num, margin_num);
                if (this.isLockFristRow) {
                    textViewParams.height = DisplayUtil.dip2px(this.mContext, (float) ((Integer) this.mRowMaxHeights.get(var18 + 1)).intValue());
                } else {
                    textViewParams.height = DisplayUtil.dip2px(this.mContext, (float) ((Integer) this.mRowMaxHeights.get(var18)).intValue());
                }

                var23.setLayoutParams(textViewParams);
                android.view.ViewGroup.LayoutParams textViewParamsCopy = var23.getLayoutParams();
                textViewParamsCopy.width = DisplayUtil.dip2px(this.mContext, (float) ((Integer) this.mColumnMaxWidths.get(var22 + 1)).intValue());
                var20.addView(var23);
                if (var22 != var19.size() - 1) {
                    View splitView = new View(this.mContext);
                    android.view.ViewGroup.LayoutParams splitViewParmas = new android.view.ViewGroup.LayoutParams(DisplayUtil.dip2px(this.mContext, 1.0F), -1);
                    splitView.setLayoutParams(splitViewParmas);
                    if (!this.isLockFristRow) {
                        if (var18 == 0) {
                            splitView.setBackgroundColor(ContextCompat.getColor(this.mContext, color.white));
                        } else {
                            splitView.setBackgroundColor(ContextCompat.getColor(this.mContext, color.light_gray));
                        }
                    } else {
                        splitView.setBackgroundColor(ContextCompat.getColor(this.mContext, color.light_gray));
                    }

                    var20.addView(splitView);
                }
            }

            var16.addView(var20);
            splite = new View(this.mContext);
            spliteLayoutParam = new android.view.ViewGroup.LayoutParams(-1, DisplayUtil.dip2px(this.mContext, 1.0F));
            splite.setLayoutParams(spliteLayoutParam);
            splite.setBackgroundColor(ContextCompat.getColor(this.mContext, color.light_gray));
            var16.addView(splite);
        }

        lockScrollViewParent.addView(var16);
        this.mScrollViews.add(lockScrollViewParent);
        lockScrollViewParent.setOnScrollChangeListener(new onScrollChangeListener() {
            public void onScrollChanged(HorizontalScrollView scrollView, int x, int y) {
                LockTableView2.this.changeAllScrollView(x, y);
            }
        });
        this.mTableScrollView.addView(lockTableViewContent);
    }

    private void createUnLockColumnView() {
        View lockTableViewContent = LayoutInflater.from(this.mContext).inflate(layout.unlocktablecontentview, (ViewGroup) null);
        CustomHorizontalScrollView lockScrollViewParent = (CustomHorizontalScrollView) lockTableViewContent.findViewById(id.unlockScrollView_parent);
        LinearLayout scollViewItemContentView = new LinearLayout(this.mContext);
        LayoutParams scollViewItemContentViewParams = new LayoutParams(-1, -2);
        scollViewItemContentView.setLayoutParams(scollViewItemContentViewParams);
        scollViewItemContentView.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < this.mTableRowDatas.size(); ++i) {
            ArrayList datas = (ArrayList) this.mTableRowDatas.get(i);
            LinearLayout linearLayout = new LinearLayout(this.mContext);
            LayoutParams layoutParams = new LayoutParams(-2, -2);
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setGravity(17);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            if (!this.isLockFristRow && i == 0) {
                linearLayout.setBackgroundColor(ContextCompat.getColor(this.mContext, this.mFristRowBackGroudColor));
            }

            for (int splite = 0; splite < datas.size(); ++splite) {
                TextView spliteLayoutParam = new TextView(this.mContext);
                if (!this.isLockFristRow) {
                    if (i == 0) {
                        spliteLayoutParam.setTextColor(ContextCompat.getColor(this.mContext, this.mTableHeadTextColor));
                    } else {
                        spliteLayoutParam.setTextColor(ContextCompat.getColor(this.mContext, this.mTableContentTextColor));
                    }
                } else {
                    spliteLayoutParam.setTextColor(ContextCompat.getColor(this.mContext, this.mTableContentTextColor));
                }

                spliteLayoutParam.setTextSize(2, (float) this.mTextViewSize);
                spliteLayoutParam.setGravity(17);
                spliteLayoutParam.setText((CharSequence) datas.get(splite));
                LayoutParams textViewParams = new LayoutParams(-2, -2);
                textViewParams.setMargins(margin_num, margin_num, margin_num, margin_num);
                if (this.isLockFristRow) {
                    textViewParams.height = DisplayUtil.dip2px(this.mContext, (float) ((Integer) this.mRowMaxHeights.get(i + 1)).intValue());
                } else {
                    textViewParams.height = DisplayUtil.dip2px(this.mContext, (float) ((Integer) this.mRowMaxHeights.get(i)).intValue());
                }

                spliteLayoutParam.setLayoutParams(textViewParams);
                android.view.ViewGroup.LayoutParams textViewParamsCopy = spliteLayoutParam.getLayoutParams();
                textViewParamsCopy.width = DisplayUtil.dip2px(this.mContext, (float) ((Integer) this.mColumnMaxWidths.get(splite)).intValue());
                linearLayout.addView(spliteLayoutParam);
                if (splite != datas.size() - 1) {
                    View splitView = new View(this.mContext);
                    android.view.ViewGroup.LayoutParams splitViewParmas = new android.view.ViewGroup.LayoutParams(DisplayUtil.dip2px(this.mContext, 1.0F), -1);
                    splitView.setLayoutParams(splitViewParmas);
                    if (!this.isLockFristRow) {
                        if (i == 0) {
                            splitView.setBackgroundColor(ContextCompat.getColor(this.mContext, color.white));
                        } else {
                            splitView.setBackgroundColor(ContextCompat.getColor(this.mContext, color.light_gray));
                        }
                    } else {
                        splitView.setBackgroundColor(ContextCompat.getColor(this.mContext, color.light_gray));
                    }

                    linearLayout.addView(splitView);
                }
            }

            scollViewItemContentView.addView(linearLayout);
            View var15 = new View(this.mContext);
            android.view.ViewGroup.LayoutParams var16 = new android.view.ViewGroup.LayoutParams(-1, DisplayUtil.dip2px(this.mContext, 1.0F));
            var15.setLayoutParams(var16);
            var15.setBackgroundColor(ContextCompat.getColor(this.mContext, color.light_gray));
            scollViewItemContentView.addView(var15);
        }

        lockScrollViewParent.addView(scollViewItemContentView);
        this.mScrollViews.add(lockScrollViewParent);
        lockScrollViewParent.setOnScrollChangeListener(new onScrollChangeListener() {
            public void onScrollChanged(HorizontalScrollView scrollView, int x, int y) {
                LockTableView2.this.changeAllScrollView(x, y);
            }
        });
        this.mTableScrollView.addView(lockTableViewContent);
    }

    private void changeAllScrollView(int x, int y) {
        if (this.mScrollViews.size() > 0) {
            if (this.mTableViewListener != null) {
                this.mTableViewListener.onTableViewScrollChange(x, y);
            }

            for (int i = 0; i < this.mScrollViews.size(); ++i) {
                HorizontalScrollView scrollView = (HorizontalScrollView) this.mScrollViews.get(i);
                scrollView.scrollTo(x, y);
            }
        }

    }

    private int measureTextWidth(TextView textView, String text) {
        if (textView != null) {
            LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
            int width = DisplayUtil.px2dip(this.mContext, (float) layoutParams.leftMargin) + DisplayUtil.px2dip(this.mContext, (float) layoutParams.rightMargin) + this.getTextViewWidth(textView, text);
            return width <= this.minColumnWidth ? this.minColumnWidth : (width > this.minColumnWidth && width <= this.maxColumnWidth ? width : this.maxColumnWidth);
        } else {
            return 0;
        }
    }

    private int measureTextHeight(TextView textView, String text) {
        if (textView != null) {
            LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
            int height = this.getTextViewHeight(textView, text);
            return height < this.minRowHeight ? this.minRowHeight : (height > this.minRowHeight && height < this.maxRowHeight ? height : this.maxRowHeight);
        } else {
            return 0;
        }
    }

    private int getTextViewHeight(TextView textView, String text) {
        if (textView != null) {
            int width = this.measureTextWidth(textView, text);
            TextPaint textPaint = textView.getPaint();
            StaticLayout staticLayout = new StaticLayout(text, textPaint, DisplayUtil.dip2px(this.mContext, (float) width), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
            int height = DisplayUtil.px2dip(this.mContext, (float) staticLayout.getHeight());
            return height;
        } else {
            return 0;
        }
    }

    private int getTextViewWidth(TextView view, String text) {
        if (view != null) {
            TextPaint paint = view.getPaint();
            return DisplayUtil.px2dip(this.mContext, (float) ((int) paint.measureText(text)));
        } else {
            return 0;
        }
    }

    private void createScollview(HorizontalScrollView scrollView, List<String> datas, boolean isFristRow) {
        LinearLayout linearLayout = new LinearLayout(this.mContext);
        LayoutParams layoutParams = new LayoutParams(-2, -1);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setGravity(17);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < datas.size(); ++i) {
            TextView textView = new TextView(this.mContext);
            if (isFristRow) {
                textView.setTextColor(ContextCompat.getColor(this.mContext, this.mTableHeadTextColor));
                textView.setTextSize(2, (float) this.mHeadTextViewSize);
                textView.setTypeface(null, Typeface.BOLD);
            } else {
                textView.setTextColor(ContextCompat.getColor(this.mContext, this.mTableContentTextColor));
                textView.setTextSize(2, (float) this.mTextViewSize);
                textView.setTypeface(null, Typeface.NORMAL);
            }


            textView.setGravity(17);
            textView.setText((CharSequence) datas.get(i));
            LayoutParams textViewParams = new LayoutParams(-2, -2);
            textViewParams.setMargins(margin_num, margin_num, margin_num, margin_num);
            textView.setLayoutParams(textViewParams);
            android.view.ViewGroup.LayoutParams textViewParamsCopy = textView.getLayoutParams();
            if (this.isLockFristColumn) {
                textViewParamsCopy.width = DisplayUtil.dip2px(this.mContext, (float) ((Integer) this.mColumnMaxWidths.get(i + 1)).intValue());
            } else {
                textViewParamsCopy.width = DisplayUtil.dip2px(this.mContext, (float) ((Integer) this.mColumnMaxWidths.get(i)).intValue());
            }

            linearLayout.addView(textView);
            if (i != datas.size() - 1) {
                View splitView = new View(this.mContext);
                android.view.ViewGroup.LayoutParams splitViewParmas = new android.view.ViewGroup.LayoutParams(DisplayUtil.dip2px(this.mContext, 1.0F), -1);
                splitView.setLayoutParams(splitViewParmas);
                if (isFristRow) {
                    splitView.setBackgroundColor(ContextCompat.getColor(this.mContext, color.white));
                } else {
                    splitView.setBackgroundColor(ContextCompat.getColor(this.mContext, color.light_gray));
                }

                linearLayout.addView(splitView);
            }
        }

        scrollView.addView(linearLayout);
    }

    public LockTableView2 setLockFristRow(boolean lockFristRow) {
        this.isLockFristRow = lockFristRow;
        return this;
    }

    public LockTableView2 setLockFristColumn(boolean lockFristColumn) {
        this.isLockFristColumn = lockFristColumn;
        return this;
    }

    public LockTableView2 setMaxColumnWidth(int maxColumnWidth) {
        this.maxColumnWidth = maxColumnWidth;
        return this;
    }

    public LockTableView2 setMinColumnWidth(int minColumnWidth) {
        this.minColumnWidth = minColumnWidth;
        return this;
    }

    public LockTableView2 setFristRowBackGroudColor(int mFristRowBackGroudColor) {
        this.mFristRowBackGroudColor = mFristRowBackGroudColor;
        return this;
    }

    public LockTableView2 setNullableString(String mNullableString) {
        this.mNullableString = mNullableString;
        return this;
    }

    public LockTableView2 setTextViewSize(int mTextViewSize) {
        this.mTextViewSize = mTextViewSize;
        return this;
    }

    public LockTableView2 setTableHeadTextColor(int mTableHeadTextColor) {
        this.mTableHeadTextColor = mTableHeadTextColor;
        return this;
    }

    public LockTableView2 setTableHeadTextSize(int mHeadTextViewSize) {
        this.mHeadTextViewSize = mHeadTextViewSize;
        return this;
    }

    public LockTableView2 setTableContentTextColor(int mTableContentTextColor) {
        this.mTableContentTextColor = mTableContentTextColor;
        return this;
    }

    public LockTableView2 setMaxRowHeight(int maxRowHeight) {
        this.maxRowHeight = maxRowHeight;
        return this;
    }

    public LockTableView2 setMinRowHeight(int minRowHeight) {
        this.minRowHeight = minRowHeight;
        return this;
    }

    public LockTableView2 setTableViewListener(LockTableView.OnTableViewListener mTableViewListener) {
        this.mTableViewListener = mTableViewListener;
        return this;
    }

    public ArrayList<Integer> getColumnMaxWidths() {
        return this.mColumnMaxWidths;
    }

    public ArrayList<Integer> getRowMaxHeights() {
        return this.mRowMaxHeights;
    }

    public LinearLayout getLockHeadView() {
        return this.mLockHeadView;
    }

    public LinearLayout getUnLockHeadView() {
        return this.mUnLockHeadView;
    }

    public ArrayList<HorizontalScrollView> getScrollViews() {
        return this.mScrollViews;
    }

    public interface OnTableViewListener {
        void onTableViewScrollChange(int var1, int var2);
    }
}
