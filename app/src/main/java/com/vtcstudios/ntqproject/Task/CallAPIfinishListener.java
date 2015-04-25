package com.vtcstudios.ntqproject.Task;

import com.vtcstudios.ntqproject.util.People;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by HIEPKHACH9X on 3/18/2015.
 */
public interface CallAPIfinishListener {

    abstract void onLoadfinish(String Result);

    abstract void onLoadPeoplefinish(int Code, ArrayList<People> Result);

    abstract void onErrorCall(String Result);
}
