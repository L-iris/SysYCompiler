package ir.constval;

import ir.Value;
import ir.types.ArrayType;
import ir.types.Type;

import java.util.ArrayList;
import java.util.List;

public class ConstArray extends Value {
    public List<Value> containedValue;

    public ConstArray(List<Value> containedValue) {
        super(Type.arrayType(containedValue.get(0).getType(), containedValue.size()));
        this.containedValue = containedValue;
    }

    public ConstArray(Type type) {
        super(Type.arrayType(type, 0));
        this.containedValue = new ArrayList<>();
    }

    public static ConstArray create(Type valueType, Value... values) {
        if(values.length ==0 ){
            return new ConstArray(valueType);
        } else {
            assert valueType.equals(values[0].getType());
            for(int i=0; i < values.length - 1 ;i ++)
                assert values[i].getType().equals(values[i+1].getType());
            return new ConstArray(List.of(values));
        }
    }

    public static ConstArray create(Value... values) {
        assert values.length != 0;
        return create(values[0].getType(), values);
    }

    public Value get(int i) {
        return containedValue.get(i);
    }

    public Value set(int i, Value v) {
        assert v.getType().equals(((ArrayType) this.type).getContainedType());
        return containedValue.set(i, v);
    }
}
