package ic.doc.sgo;

public class Constraint {
    private int groupSizeLowerBound;
    private int groupSizeUpperBound;

    public void setGroupSizeLowerBound(int groupSizeLowerBound) {
        this.groupSizeLowerBound = groupSizeLowerBound;
    }

    public void setGroupSizeUpperBound(int groupSizeUpperBound) {
        this.groupSizeUpperBound = groupSizeUpperBound;
    }

    public int getGroupSizeLowerBound() {
        return groupSizeLowerBound;
    }

    public int getGroupSizeUpperBound() {
        return groupSizeUpperBound;
    }

}
