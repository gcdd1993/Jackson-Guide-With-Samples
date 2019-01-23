package com.gcdd.jacksonGuide.advancedJacksonMarshalling.HowToSerializeEnumsAsJSONObjectsWithJackson;

/**
 * @author: gaochen
 * Date: 2019/1/23
 */
public enum Distance {
    KILOMETER("km", 1000),
    MILE("miles", 1609.34),
    METER("meters", 1),
    INCH("inches", 0.0254),
    CENTIMETER("cm", 0.01),
    MILLIMETER("mm", 0.001);

    private String unit;
    private final double meters;

    public String getUnit() {
        return unit;
    }

    public double getMeters() {
        return meters;
    }

    Distance(String unit, double meters) {
        this.unit = unit;
        this.meters = meters;
    }
}
