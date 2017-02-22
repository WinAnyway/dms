package pl.com.bottega.dms.model;

import java.io.Serializable;

public class DocumentNumber implements Serializable{


    private String number;

    public DocumentNumber(){}

    public DocumentNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocumentNumber that = (DocumentNumber) o;

        return number.equals(that.number);

    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    public String getNumber() {
        return number;
    }
}
