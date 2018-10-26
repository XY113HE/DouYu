package com.ibagou.dou.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import com.ibagou.dou.R;
import com.ibagou.dou.customview.CollapsibleTextView;
import com.ibagou.dou.databinding.ItemMainTextFootViewBinding;
import com.ibagou.dou.databinding.ItemMainTextViewBinding;
import com.ibagou.dou.interf.MainInterf;
import com.ibagou.dou.model.BaseBean;
import com.ibagou.dou.model.ContentDataBean;
import com.ibagou.dou.network.NetWorkManager;
import com.ibagou.dou.util.DatabaseHelper;
import com.ibagou.dou.util.ImageBindingUtils;
import com.ibagou.dou.util.RxUtils;
import com.ibagou.dou.util.ShareDao;
import com.ibagou.dou.util.ToastUtils;

/**
 * Created by liumingyu on 2018/8/21.
 */

public class MainTextAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private MainInterf.MainView mMainView;
    private List<ContentDataBean.ItemsBean> data;
    private List<Animation> animations;
    private int[] flag = {0, 0, 0};

    private final int normalType = 0;//第一种ViewType，正常的item
    private final int footType = 1;//第二种ViewType，底部的提示View

    private boolean hasMore = true;//变量，是否有更多数据
    private boolean fadeTips = false;//变量，是否隐藏了底部的提示

    private Handler mHandler = new Handler(Looper.getMainLooper()); //获取主线程的Handler

    public MainTextAdapter(MainInterf.MainView mMainView){
        this.mContext = mMainView.getContext();
        this.mMainView = mMainView;
        animations = new ArrayList<>();
        animations.add(AnimationUtils.loadAnimation(mContext, R.anim.like_addone_anim));
        animations.add(AnimationUtils.loadAnimation(mContext, R.anim.like_addone_anim));
        animations.add(AnimationUtils.loadAnimation(mContext, R.anim.like_addone_anim));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case normalType:
                ItemMainTextViewBinding mItemNormalBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_main_text_view, parent, false);
                return new NormalHolder(mItemNormalBinding);
            case footType:
                ItemMainTextFootViewBinding mItemFootBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_main_text_foot_view, parent, false);
                return new FootHolder(mItemFootBinding);
            default:
                return null;
        }

    }

    public void setData(List<ContentDataBean.ItemsBean> data){
        this.data = data;
        notifyDataSetChanged();
    }

    // 自定义方法，获取列表中数据源的最后一个位置，比getItemCount少1，因为不计上footView
    public int getRealLastPosition() {
        return data.size();
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case normalType:
                ((NormalHolder)holder).bindItem(position);
                break;
            case footType:
                // 之所以要设置可见，是因为我在没有更多数据时会隐藏了这个footView
                ((FootHolder)holder).itemBinding.getRoot().setVisibility(View.GONE);
                // 只有获取数据为空时，hasMore为false，所以当我们拉到底部时基本都会首先显示“正在加载更多...”
                if(hasMore){
                    if (data.size() > 0) {
                        ((FootHolder)holder).itemBinding.getRoot().setVisibility(View.VISIBLE);
                        // 如果查询数据发现增加之后，就显示正在加载更多
                    }
                }else{
                    //TODO 需要判断数据是否为0 ？
//                    if(data.size() > 0){
//                        ((FootHolder)holder).itemView.setVisibility(View.VISIBLE);
                        // 如果查询数据发现并没有增加时，就显示没有更多数据了
//                        ((FootHolder) holder).setHint("没有更多数据了");

                    new ToastUtils(mContext).setType(ToastUtils.MY_TOAST_BOTTOM).show();
                    ((FootHolder)holder).itemBinding.getRoot().setVisibility(View.GONE);

                    // 然后通过延时加载模拟网络请求的时间，在500ms后执行
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // 隐藏提示条
//                                ((FootHolder) holder).itemView.setVisibility(View.GONE);
                                // 将fadeTips设置true
                                fadeTips = true;
                                // hasMore设为true是为了让再次拉到底时，会先显示正在加载更多
                                hasMore = false;
                            }
                        }, 500);
//                    }
                }
                break;
        }
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public void setFadeTips(boolean fadeTips) {
        this.fadeTips = fadeTips;
    }

    // 暴露接口，改变fadeTips的方法
    public boolean isFadeTips() {
        return fadeTips;
    }

    // 暴露接口，更新数据源，并修改hasMore的值，如果有增加数据，hasMore为true，否则为false
    public void updateList(List<ContentDataBean.ItemsBean> newDatas, boolean hasMore) {
        // 在原有的数据之上增加新数据
        if (newDatas != null) {
            data.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size()+1;
    }

    // 根据条目位置返回ViewType，以供onCreateViewHolder方法内获取不同的Holder
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return footType;
        } else {
            return normalType;
        }
    }

    class NormalHolder extends RecyclerView.ViewHolder{
        private ItemMainTextViewBinding itemBinding;
        public NormalHolder(ItemMainTextViewBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        private void bindItem(final int position){
            itemBinding.itemContent.setText(data.get(position).getBody().replace("\n", ""));
            itemBinding.itemLikeAmount.setText("");
            itemBinding.itemShareAmount.setText("");

            itemBinding.itemLikeAmount.setText(data.get(position).getZan()+"");

            itemBinding.itemContent.setCollapsibleState(data.get(position).isHasSpread() ? 1 : 2);//1显示收起，2显示全文
            itemBinding.itemContent.setChangeStateCallBack(new CollapsibleTextView.changeState() {
                @Override
                public void changeFlag(View v) {
                    data.get(position).setHasSpread(!data.get(position).isHasSpread());
                }
            });

            RxUtils.setOnClick(itemBinding.itemShareLayout, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    ShareDao.getShareDao().showDao(data.get(position).getBody());
                }
            });

            if (DatabaseHelper.getInstance(mContext).getIsLike(data.get(position).getId()+"")) {
                ImageBindingUtils.loadImg(itemBinding.itemLikeBtn, R.drawable.item_like_btn_sel);
            } else {
                ImageBindingUtils.loadImg(itemBinding.itemLikeBtn, R.drawable.item_like_btn);
            }

            //TODO 点击关注变色，这个有数据才可以处理View复用，可根据focus字段判断显示具体颜色
            RxUtils.setOnClick(itemBinding.itemLikeLayout, new Consumer() {
                @Override
                public void accept(Object o) {
                    //一个动画池
                    int i = 0;
                    for(; i < 3; i++){
                        if(flag[i] == 0){
                            break;
                        }
                    }
                    Animation animation = animations.get(i);
                    flag[i] = 1;
                    final int finalI = i;
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            flag[finalI] = 0;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    if (DatabaseHelper.getInstance(mContext).getIsLike(data.get(position).getId()+"")) {
                        NetWorkManager.getInstance()
                                .getDouYuNetApi()
                                .downLikeInfo(data.get(position).getId() + "")
                                .compose(mMainView.getActivity().<BaseBean>bindToLifecycle())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<BaseBean>() {
                                    @Override
                                    public void accept(BaseBean baseBean) throws Exception {
                                        if (baseBean.getCode().equals("0")) {
                                            Toast.makeText(mContext, "取消点赞成功", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                    }
                                });
                        ImageBindingUtils.loadImg(itemBinding.itemLikeBtn, R.drawable.item_like_btn);
                        DatabaseHelper.getInstance(mContext).setIsLike(data.get(position).getId()+"", "0");
                        itemBinding.itemLikeAmount.setText("" + (Integer.parseInt(itemBinding.itemLikeAmount.getText().toString()) - 1));
                        itemBinding.focusAddOneTv.setVisibility(View.VISIBLE);
                        itemBinding.focusAddOneTv.startAnimation(animation);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                itemBinding.focusAddOneTv.setVisibility(View.GONE);
                            }
                        }, 500);
                    } else {
                        NetWorkManager.getInstance()
                                .getDouYuNetApi()
                                .upLikeInfo(data.get(position).getId() + "")
                                .compose(mMainView.getActivity().<BaseBean>bindToLifecycle())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<BaseBean>() {
                                    @Override
                                    public void accept(BaseBean baseBean) throws Exception {
                                        if (baseBean.getCode().equals("0")) {
                                            Toast.makeText(mContext, "点赞成功", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                    }
                                });
                        ImageBindingUtils.loadImg(itemBinding.itemLikeBtn, R.drawable.item_like_btn_sel);
                        DatabaseHelper.getInstance(mContext).setIsLike(data.get(position).getId()+"", "1");
                        itemBinding.focusAddOneTv.setText("+1");
                        itemBinding.itemLikeAmount.setText("" + (Integer.parseInt(itemBinding.itemLikeAmount.getText().toString()) + 1));
                        itemBinding.focusAddOneTv.setVisibility(View.VISIBLE);
                        itemBinding.focusAddOneTv.startAnimation(animation);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                itemBinding.focusAddOneTv.setVisibility(View.GONE);
                            }
                        }, 500);
                    }
                }
            });
        }
    }

    class FootHolder extends RecyclerView.ViewHolder{
        private ItemMainTextFootViewBinding itemBinding;
        public FootHolder(ItemMainTextFootViewBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        private void setHint(String hint){
            itemBinding.itemLoadMoreTv.setText(hint);
        }
    }


}
