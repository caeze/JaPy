package test;

import java.util.Objects;

/**
 * Simple data class for testing.
 *
 * @author Clemens Strobel
 * @date 19.05.2019
 */
public class SimpleData {

    int a = 0;
    String b = "b";

    public SimpleData(int a, String b) {
        this.a = a;
        this.b = b;
    }

    public SimpleData(SimpleData other) {
        this.a = other.a;
        this.b = other.b;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.a;
        hash = 83 * hash + Objects.hashCode(this.b);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimpleData other = (SimpleData) obj;
        if (this.a != other.a) {
            return false;
        }
        if (!Objects.equals(this.b, other.b)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "a=" + a + ", b=" + b + "}";
    }
}
