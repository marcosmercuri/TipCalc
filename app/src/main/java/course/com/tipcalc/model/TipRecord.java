package course.com.tipcalc.model;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TipRecord {
    private double bill;
    private int tipPercentage;
    private Date createdDate;

    public double getBill() {
        return bill;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }

    public int getTipPercentage() {
        return tipPercentage;
    }

    public void setTipPercentage(int tipPercentage) {
        this.tipPercentage = tipPercentage;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public double getTip() {
        return  bill * (tipPercentage / 100d);
    }

    public String getFormattedCreatedDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return format.format(createdDate);
    }
}
