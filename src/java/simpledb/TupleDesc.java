package simpledb;

import java.io.Serializable;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {
    private final ArrayList<TDItem> fields;



    /**
     * A help class to facilitate organizing the information of each field
     * */
    public static class TDItem implements Serializable {


        private static final long serialVersionUID = 1L;
        

        /**
         * The type of the field
         * */
        public final Type fieldType;

        /**
         * The name of the field
         * */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }
    }

    /**
     * @return
     *        An iterator which iterates over all the field TDItems
     *        that are included in this TupleDesc
     * */
    public Iterator<TDItem> iterator() {
        return fields.iterator();
    }

    private static final long serialVersionUID = 1L;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     * @param fieldAr
     *            array specifying the names of the fields. Note that names may
     *            be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        fields = new ArrayList<>();

        for (int i = 0; i < typeAr.length; i++) {
            fields.add(new TDItem(typeAr[i], fieldAr == null ? null : fieldAr[i]));
        }
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        fields = new ArrayList<>();
        for (int i = 0; i < typeAr.length; i++) {
            fields.add(new TDItem(typeAr[i], null));
        }
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        return fields.size();
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     * 
     * @param i
     *            index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        if (i < 0 || i >= fields.size()) {
            throw new NoSuchElementException("i is not a valid field reference");
        }
        return fields.get(i).fieldName;
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     * 
     * @param i
     *            The index of the field to get the type of. It must be a valid
     *            index.
     * @return the type of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
        if (i < 0 || i >= fields.size()) {
            throw new NoSuchElementException("i is not a valid field reference");
        }
        return fields.get(i).fieldType;
    }

    /**
     * Find the index of the field with a given name.
     * No match if name is null.
     * 
     * @param name
     *            name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException
     *             if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {

        if (name == null) {
            throw new NoSuchElementException("Field name is null.");
        }
     
        for (int i = 0; i<fields.size(); i++){
            if (Objects.equals(this.getFieldName(i), (name))) {
                return i;
            }
        
        }
        throw new NoSuchElementException("No field name found.");
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     * @see Type#getSizeInBytes
     */
    public int getSizeInBytes() {
        int size = 0;
        for (TDItem field : fields) {
            size += field.fieldType.getSizeInBytes();
        }
        return size;
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     * 
     * @param td1
     *            The TupleDesc with the first fields of the new TupleDesc
     * @param td2
     *            The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        int arrayLength = td1.numFields() + td2.numFields();
        Type[] combinedTypes = new Type[arrayLength];
        String[] combinedFields = new String[arrayLength];

        for (int i = 0; i < td1.numFields(); i++) {
            combinedTypes[i] = (td1.getFieldType(i));
            combinedFields[i] = (td1.getFieldName(i));
        }

        for (int i = 0; i < td2.numFields(); i++) {
            combinedTypes[i + td1.numFields()] = (td2.getFieldType(i));
            combinedFields[i + td1.numFields()] = (td2.getFieldName(i));
            
        }

        return new TupleDesc(combinedTypes, combinedFields);
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they have the same number of items
     * and if the i-th type in this TupleDesc is equal to the i-th type in o
     * for every i. It does not matter if the field names are equal.
     * 
     * @param o
     *            the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */
    public boolean equals(Object o) {
        if (!(o instanceof TupleDesc)){
            return false;
        }
        TupleDesc td = (TupleDesc) o;
        if (fields.size() != td.numFields()) {
            return false;
        }
        for (int i = 0; i < fields.size(); i++) {
            if (!(this.getFieldType(i).equals(td.getFieldType(i)))) {
                return false;
            }

        }
        return true;
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        throw new UnsupportedOperationException("unimplemented");
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldName[0](fieldType[0]), ..., fieldName[M](fieldType[M])", although
     * the exact format does not matter.
     * 
     * @return String describing this descriptor.
     */
    public String toString() {
        String[] str = new String[this.numFields()];
        for (int i = 0; i < this.numFields(); i++) {
            str[i] = this.getFieldName(i) + " " + "(" + this.getFieldType(i) + ")";
        }
        return String.join(",", str);
    }
}
