package com.amplify.stofor;

public class StatsDMetric {

    public final String metricName;
    public final int value;
    public final String metricType;

    public StatsDMetric(String metricName, int value, Type metricType) {
        this.metricName = metricName;
        this.value = value;
        this.metricType = metricType.getValue();
    }

    public enum Type {
        INCREMENT("increment"),
        TIME("time"),
        GAUGE("gauge");

        private String value;

        private Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
