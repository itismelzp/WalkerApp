package com.demo.storage;

import java.util.Objects;

import io.packable.PackCreator;
import io.packable.PackEncoder;
import io.packable.Packable;

/**
 * Created by walkerzpli on 2022/1/19.
 */
//@PackClass
public class PackableObject implements Packable {

    private long id;

    private String name;

    public PackableObject(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PackableObject that = (PackableObject) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public void encode(PackEncoder encoder) {
        encoder.putLong(0, id)
                .putString(1, name);
    }

    public static final PackCreator<PackableObject> CREATOR = decoder -> {
        PackableObject obj = new PackableObject(decoder.getLong(0), decoder.getString(1));
        decoder.recycle();
        return obj;
    };

}
