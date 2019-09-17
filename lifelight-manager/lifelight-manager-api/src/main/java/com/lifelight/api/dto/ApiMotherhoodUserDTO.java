package com.lifelight.api.dto;

import java.io.Serializable;

import com.lifelight.api.entity.ApiMotherhoodUser;

/**
 * Created by kwinxu on 2017/12/18.
 */
public class ApiMotherhoodUserDTO extends ApiMotherhoodUser implements Serializable{

    private String doctorId;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

}
