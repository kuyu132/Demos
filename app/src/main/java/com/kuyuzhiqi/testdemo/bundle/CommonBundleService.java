package com.kuyuzhiqi.testdemo.bundle;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import autobundle.annotation.Box;
import autobundle.annotation.BundleFlag;
import autobundle.annotation.Required;
import java.io.Serializable;
import java.util.ArrayList;

public interface CommonBundleService {

    Bundle getPersonInfo(@Box("name") String name,
            @Box("id") String id,
            @Box("age") int age,
            @Box("sex") boolean sex,
            @Box("address") String address,
            @Box("height") float height,
            @Box("weight") float weight);


    @BundleFlag(0)
    Bundle getLogin(@Required @Box("loginName") String loginName,
            @Required @Box("password") String password);


    @BundleFlag(0)
    Bundle testError(@Box("password") String password);

    @BundleFlag(1)
    Bundle getInt(@Box("int") int value);

    @BundleFlag(2)
    Bundle getString(@Box("string") String value);

    @BundleFlag(3)
    Bundle getIntArray(@Box("intArray") int[] value);

    Bundle getStringArray(@Box("stringArray") String[] value);

    Bundle getParcelable(@Box("parcelable") Parcelable value);

    Bundle getParcelableArray(@Box("parcelableArray") Parcelable[] value);

    Bundle getSparseParcelableArray(@Box("sparseParcelableArray")
            SparseArray<? extends Parcelable> value);

    Bundle getStringArrayList(@Box("stringArrayList") ArrayList<String> value);

    Bundle getParcelableArrayList(@Box("parcelableArrayList") ArrayList<? extends Parcelable> value);


    Bundle getSerializable(@Box("serializable") Serializable value);
}
