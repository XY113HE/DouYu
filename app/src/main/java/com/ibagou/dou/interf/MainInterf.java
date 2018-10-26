package com.ibagou.dou.interf;

import com.ibagou.dou.view.MainActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by liumingyu on 2018/8/21.
 */

public interface MainInterf  {
    interface MainView extends BaseInterf.MainView{

        void shareAction(SHARE_MEDIA platform, String shareContent);

        MainActivity getActivity();
    }

    interface ViewModel extends BaseInterf.ViewModel{

    }
}
