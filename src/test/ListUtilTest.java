package test;

import java.util.ArrayList;
import java.util.List;
import util.ListUtil;

/**
 * Test class for {@link model.util.ListUtil}.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ListUtilTest {

    public boolean makeDeepCopyTest() {
        // prepare data
        List<SimpleData> list = new ArrayList<>();
        SimpleData p1 = new SimpleData(1, "NAME1");
        SimpleData p2 = new SimpleData(2, "NAME2");
        list.add(p1);
        list.add(p2);

        // execute tests
        ListUtil<SimpleData> lu = new ListUtil<>();
        List<SimpleData> listCopy = lu.makeDeepCopy(list);

        // check
        boolean success = true;
        success &= listCopy.size() == list.size();
        if (success) {
            for (int i = 0; i < list.size(); i++) {
                success &= list.get(i).equals(listCopy.get(i));
                success &= !(list.get(i) == listCopy.get(i));
            }
        }

        // return result
        return success;
    }

    public boolean addAllNoDuplicatesTest() {
        // prepare data
        SimpleData p1 = new SimpleData(1, "NAME1");
        SimpleData p2 = new SimpleData(2, "NAME2");
        SimpleData p3 = new SimpleData(3, "NAME3");
        SimpleData p4 = new SimpleData(4, "NAME4");

        List<SimpleData> list1 = new ArrayList<>();
        list1.add(p1);
        list1.add(p3);
        list1.add(p2);

        List<SimpleData> list2 = new ArrayList<>();
        list2.add(p2);
        list2.add(p4);
        list2.add(p3);

        // execute tests
        ListUtil<SimpleData> lu = new ListUtil<>();
        lu.addAllNoDuplicates(list1, list2);

        // check
        List<SimpleData> listExpectedResult = new ArrayList<>();
        listExpectedResult.add(p1);
        listExpectedResult.add(p3);
        listExpectedResult.add(p2);
        listExpectedResult.add(p4);

        boolean success = true;
        success &= listExpectedResult.size() == list1.size();
        if (success) {
            for (int i = 0; i < listExpectedResult.size(); i++) {
                success &= list1.get(i).equals(listExpectedResult.get(i));
                success &= list1.get(i) == listExpectedResult.get(i);
            }
        }

        // return result
        return success;
    }

    public boolean addNoDuplicatesTest() {
        // prepare data
        SimpleData p1 = new SimpleData(1, "NAME1");
        SimpleData p2 = new SimpleData(2, "NAME2");
        SimpleData p3 = new SimpleData(3, "NAME3");
        SimpleData p4 = new SimpleData(4, "NAME4");

        List<SimpleData> list1 = new ArrayList<>();
        list1.add(p1);
        list1.add(p3);
        list1.add(p2);

        // execute tests
        ListUtil<SimpleData> lu = new ListUtil<>();
        lu.addNoDuplicates(list1, p2);
        lu.addNoDuplicates(list1, p1);
        lu.addNoDuplicates(list1, p4);
        lu.addNoDuplicates(list1, p3);
        lu.addNoDuplicates(list1, p3);
        lu.addNoDuplicates(list1, p2);

        // check
        List<SimpleData> listExpectedResult = new ArrayList<>();
        listExpectedResult.add(p1);
        listExpectedResult.add(p3);
        listExpectedResult.add(p2);
        listExpectedResult.add(p4);

        boolean success = true;
        success &= listExpectedResult.size() == list1.size();
        if (success) {
            for (int i = 0; i < listExpectedResult.size(); i++) {
                success &= list1.get(i).equals(listExpectedResult.get(i));
                success &= list1.get(i) == listExpectedResult.get(i);
            }
        }

        // return result
        return success;
    }

    public boolean returnOnlyDuplicatesTest1() {
        // prepare data
        SimpleData p1 = new SimpleData(1, "NAME1");
        SimpleData p2 = new SimpleData(2, "NAME2");
        SimpleData p3 = new SimpleData(3, "NAME3");
        SimpleData p4 = new SimpleData(4, "NAME4");

        List<SimpleData> list1 = new ArrayList<>();
        list1.add(p1);
        list1.add(p3);
        list1.add(p2);

        List<SimpleData> list2 = new ArrayList<>();
        list2.add(p1);
        list2.add(p4);
        list2.add(p3);

        // execute tests
        ListUtil<SimpleData> lu = new ListUtil<>();
        List<SimpleData> retList = lu.returnOnlyDuplicates(list1, list2);

        // check
        List<SimpleData> listExpectedResult = new ArrayList<>();
        listExpectedResult.add(p1);
        listExpectedResult.add(p3);

        boolean success = true;
        success &= listExpectedResult.size() == retList.size();
        if (success) {
            for (int i = 0; i < listExpectedResult.size(); i++) {
                success &= retList.get(i).equals(listExpectedResult.get(i));
                success &= retList.get(i) == listExpectedResult.get(i);
            }
        }

        // return result
        return success;
    }

    public boolean returnOnlyDuplicatesTest2() {
        // prepare data
        SimpleData p1 = new SimpleData(1, "NAME1");
        SimpleData p2 = new SimpleData(2, "NAME2");
        SimpleData p3 = new SimpleData(3, "NAME3");
        SimpleData p4 = new SimpleData(4, "NAME4");

        List<SimpleData> list1 = new ArrayList<>();
        list1.add(p1);
        list1.add(p3);
        list1.add(p2);
        list1.add(p3);
        list1.add(p2);
        list1.add(p4);
        list1.add(p2);

        // execute tests
        ListUtil<SimpleData> lu = new ListUtil<>();
        List<SimpleData> retList = lu.returnOnlyDuplicates(list1);

        // check
        List<SimpleData> listExpectedResult = new ArrayList<>();
        listExpectedResult.add(p3);
        listExpectedResult.add(p2);

        boolean success = true;
        success &= listExpectedResult.size() == retList.size();
        if (success) {
            for (int i = 0; i < listExpectedResult.size(); i++) {
                success &= retList.get(i).equals(listExpectedResult.get(i));
                success &= retList.get(i) == listExpectedResult.get(i);
            }
        }

        // return result
        return success;
    }
}
