package operations;

import java.util.*;
import viewModel.MstTest;

public class DispSeqNoComparator implements Comparator<MstTest> {
    @Override
    public int compare(MstTest a, MstTest b) {
        return a.getDispSeqNo() - b.getDispSeqNo();
    }
}
