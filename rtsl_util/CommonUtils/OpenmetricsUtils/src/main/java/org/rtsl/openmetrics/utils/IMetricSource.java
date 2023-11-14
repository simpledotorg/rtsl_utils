/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rtsl.openmetrics.utils;


public interface IMetricSource {

    String getAsString() throws Exception;

    void append(StringBuilder sb) throws Exception;

}
