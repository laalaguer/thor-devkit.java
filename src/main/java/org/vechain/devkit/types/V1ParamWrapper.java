package org.vechain.devkit.types;

/** ABI v1 style paramter wrapper. This can be further process into JSON. */
final public class V1ParamWrapper {
    public int index; // The parameter position in the tuple.
    public String name; // The name of the parameter, can be "".
    public String canonicalType; // The solidity type name.
    public Object value; // The real value of the parameter, String/BigInterger, etc.

    public V1ParamWrapper(int index, String name, String canonicalType, Object value) {
        this.index = index;
        this.name = name;
        this.canonicalType = canonicalType;
        this.value = value;
    }
}
