package vttp.paf.assessmentpractice.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class Search {

    @NotEmpty(message = "Country is required")
    private String country;

    @Min(value = 1, message = "Number of people must be more than or equals to 1")
    @Max(value = 10, message = "Number of people must be less than or equals to 10")
    private Integer pax;

    @Min(value = 1, message = "Price range must be at least 1")
    private Double min;

    @Max(value = 10000, message = "Price range must be at most 10000")
    private Double max;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getPax() {
        return pax;
    }

    public void setPax(Integer pax) {
        this.pax = pax;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Search() {
    }

    public Search(String country, Integer pax, Double min, Double max) {
        this.country = country;
        this.pax = pax;
        this.min = min;
        this.max = max;
    }

    @Override
    public String toString() {
        return "Search [country=" + country + ", pax=" + pax + ", min=" + min + ", max=" + max + "]";
    }
}
