package com.example.miniproyecto2.model;

public abstract class AbstractNodeMatrix implements INodeMatrix {
    NodeMatrix left;
    NodeMatrix right;
    int value;
    Boolean original = false;

    public AbstractNodeMatrix() {

    }
    Boolean getOriginal(int x, int y){
        if (x > 0) {
            if (left == null) {
                return false;
            }


            return left.getOriginal(x - 1, y);

        }
        if (y > 0) {
            if (right == null) {
                return false;
            }
            return right.getOriginal(x, y - 1);

        }
        if (x == 0 && y == 0) {
            return this.original ;
        }
        return false;
    }

    void setOriginal(int x, int y, Boolean valueSave){
        if (x > 0) {
            if (left == null) {
                left = new NodeMatrix();
            }


            left.setOriginal(x - 1, y, valueSave);
            return;
        }
        if (y > 0) {
            if (right == null) {
                right = new NodeMatrix();
            }
            right.setOriginal(x, y - 1, valueSave);
            return;
        }
        if (x == 0 && y == 0) {
            this.original = valueSave;
        }

    }
    @Override
    public void addInfo(int x, int y, int valueSave) {
        if (x > 0) {
            if (left == null) {
                left = new NodeMatrix();
            }


            left.addInfo(x - 1, y, valueSave);
            return;
        }
        if (y > 0) {
            if (right == null) {
                right = new NodeMatrix();
            }
            right.addInfo(x, y - 1, valueSave);
            return;
        }
        if (x == 0 && y == 0) {
            this.value = valueSave;
        }

    }
    @Override
    public int getValue(int x, int y ) {
        if (x > 0) {
            if (left == null) {
                return 0;
            }


            return left.getValue(x - 1, y);

        }
        if (y > 0) {
            if (right == null) {
                return 0;
            }
            return right.getValue(x, y - 1);

        }
        if (x == 0 && y == 0) {
            return this.value ;
        }
    return 0;
    }

}