package com.example.naukari_group_05.Model

import android.os.Parcel
import android.os.Parcelable

data class Cadidate(
    val id: String? = "",
    val name: String? = "",
    val photo_url: String? = "",
    val jobtitle: String? = "",
    val about_us: String? = "",
    val college_degree: String? = "",
    var conneted: Boolean? = false,

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readBoolean()


    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(photo_url)
        parcel.writeString(jobtitle)
        parcel.writeString(about_us)
        parcel.writeString(college_degree)
        parcel.writeBoolean(conneted!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cadidate> {
        override fun createFromParcel(parcel: Parcel): Cadidate {
            return Cadidate(parcel)
        }

        override fun newArray(size: Int): Array<Cadidate?> {
            return arrayOfNulls(size)
        }
    }


}