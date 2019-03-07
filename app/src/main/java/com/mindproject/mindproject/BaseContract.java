package com.mindproject.mindproject;

/**
 * Created by Nikita on 15.02.2019.
 */

public interface BaseContract {
    interface BaseView{
        void showMessage(String message);
    }

    interface BasePresenter{
        void onStart();
        void onStop();
    }
}
