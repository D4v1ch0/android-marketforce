package rp3.auna.util.helper;

import android.os.Parcel;
import android.os.Parcelable;

import rp3.auna.models.ventanueva.AlarmJvs;

/**
 * Created by Jesus Villa on 26/12/2017.
 */

public class Parcelables {
    public static byte[] toByteArray(Parcelable parcelable) {
        Parcel parcel = Parcel.obtain();

        parcelable.writeToParcel(parcel, 0);

        byte[] result = parcel.marshall();

        parcel.recycle();

        return (result);
    }

    public static AlarmJvs toParcelableAlarm(byte[] bytes) {
        AlarmJvs alarm = null;

        try {
            alarm = toParcelable(bytes, AlarmJvs.CREATOR);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return alarm;
    }

    private static <T> T toParcelable(byte[] bytes,
                                      Parcelable.Creator<T> creator) throws Exception {
        final Parcel parcel = Parcel.obtain();

        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);

        final T result = creator.createFromParcel(parcel);

        parcel.recycle();

        return (result);
    }
}
