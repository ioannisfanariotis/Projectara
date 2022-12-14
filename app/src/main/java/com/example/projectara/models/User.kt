package com.example.projectara.models

import android.os.Parcel
import android.os.Parcelable

data class User (
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val image: String = "",
    val number: Long = 0,
    val token: String = "",
        ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(username)
        writeString(email)
        writeString(image)
        writeLong(number)
        writeString(token)
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}