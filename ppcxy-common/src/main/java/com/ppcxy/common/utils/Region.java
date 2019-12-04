package com.ppcxy.common.utils;

public class Region {
    private int firstRow;
    private int lastRow;
    private int firstColumn;
    private int lastColumn;

    public int getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public int getLastRow() {
        return lastRow;
    }

    public void setLastRow(int lastRow) {
        this.lastRow = lastRow;
    }

    public int getFirstColumn() {
        return firstColumn;
    }

    public void setFirstColumn(int firstColumn) {
        this.firstColumn = firstColumn;
    }

    public int getLastColumn() {
        return lastColumn;
    }

    public void setLastColumn(int lastColumn) {
        this.lastColumn = lastColumn;
    }

    @Override
    public String toString() {
        return "Region{" +
                "firstRow=" + firstRow +
                ", lastRow=" + lastRow +
                ", firstColumn=" + firstColumn +
                ", lastColumn=" + lastColumn +
                '}';
    }
}
