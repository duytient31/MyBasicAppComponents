package sample.hawk.com.mybasicappcomponents.designpattern.factory.Normal;

import sample.hawk.com.mybasicappcomponents.designpattern.factory.Material;
import sample.hawk.com.mybasicappcomponents.designpattern.factory.MilkTea;

/**
 * Created by ha271 on 2017/8/3.
 */

public class MilkTeaFactory extends NormalFactory {
    @Override
    public Material createProduct() {
        return new MilkTea();
    }
}
