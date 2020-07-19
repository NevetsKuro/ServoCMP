/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewModel;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author NIT_steven
 */
public class uGraphData {

    String testName, minVal, maxVal, minRangeVal, maxRangeVal;
    ArrayList<Map<String,String>> testData;
    
    public uGraphData() {
        
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public ArrayList<Map<String,String>> getTestData() {
        return testData;
    }

    public void setTestData(ArrayList<Map<String,String>> testData) {
        this.testData = testData;
    }

    public String getMinVal() {
        return minVal;
    }

    public void setMinVal(String minVal) {
        this.minVal = minVal;
    }

    public String getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(String maxVal) {
        this.maxVal = maxVal;
    }

    public String getMinRangeVal() {
        return minRangeVal;
    }

    public void setMinRangeVal(String minRangeVal) {
        this.minRangeVal = minRangeVal;
    }

    public String getMaxRangeVal() {
        return maxRangeVal;
    }

    public void setMaxRangeVal(String maxRangeVal) {
        this.maxRangeVal = maxRangeVal;
    }
    
}
