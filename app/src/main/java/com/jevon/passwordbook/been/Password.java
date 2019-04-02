package com.jevon.passwordbook.been;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

public class Password extends BaseObservable {

    private String name;
    private String id;
    private String psw;
    private String note;

    public Password() {
    }

    public Password(String name, String id, String psw, String note) {
        this.name = name;
        this.id = id;
        this.psw = psw;
        this.note = note;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
        notifyPropertyChanged(BR.psw);
    }

    @Bindable
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        notifyPropertyChanged(BR.note);
    }

    public void setNull() {
        this.name = "";
        this.id = "";
        this.psw = "";
        this.note = "";
        notifyChange();
    }
}
