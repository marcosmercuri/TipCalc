package course.com.tipcalc.fragments;


import course.com.tipcalc.model.TipRecord;

public interface TipHistoryListFragmentListener {
    void addToList(TipRecord record);
    void clearList();
}
