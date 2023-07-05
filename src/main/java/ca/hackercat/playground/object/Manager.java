package ca.hackercat.playground.object;

import java.util.ArrayList;
import java.util.Comparator;

public abstract class Manager {
    
    public final ArrayList<Drawable> objs = new ArrayList<>(0);
    
    public void add(Drawable d) {
        objs.add(d);
    }
    public void remove(Drawable d) {
        objs.remove(d);
    }
    public void update() {
        objs.sort(new Comparator<Drawable>() {
            @Override
            public int compare(Drawable o1, Drawable o2) {
                return Float.compare(o1.getOrder(), o2.getOrder());
            }
        });
        for (Drawable d : objs) {
            d.updateSuper();
        }
    }
}
