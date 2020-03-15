/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package io.opentracing.contrib.specialagent.rule.thrift.gen;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)", date = "2020-03-13")
public class Address implements org.apache.thrift.TBase<Address, Address._Fields>, java.io.Serializable, Cloneable, Comparable<Address> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Address");

  private static final org.apache.thrift.protocol.TField LINE_FIELD_DESC = new org.apache.thrift.protocol.TField("line", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField CITY_FIELD_DESC = new org.apache.thrift.protocol.TField("city", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField ZIP_CODE_FIELD_DESC = new org.apache.thrift.protocol.TField("zipCode", org.apache.thrift.protocol.TType.STRING, (short)3);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new AddressStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new AddressTupleSchemeFactory();

  public String line; // required
  public String city; // required
  public String zipCode; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    LINE((short)1, "line"),
    CITY((short)2, "city"),
    ZIP_CODE((short)3, "zipCode");

    private static final java.util.Map<String, _Fields> byName = new java.util.HashMap<String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // LINE
          return LINE;
        case 2: // CITY
          return CITY;
        case 3: // ZIP_CODE
          return ZIP_CODE;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.LINE, new org.apache.thrift.meta_data.FieldMetaData("line", org.apache.thrift.TFieldRequirementType.REQUIRED,
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.CITY, new org.apache.thrift.meta_data.FieldMetaData("city", org.apache.thrift.TFieldRequirementType.REQUIRED,
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ZIP_CODE, new org.apache.thrift.meta_data.FieldMetaData("zipCode", org.apache.thrift.TFieldRequirementType.REQUIRED,
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Address.class, metaDataMap);
  }

  public Address() {
  }

  public Address(
    String line,
    String city,
    String zipCode)
  {
    this();
    this.line = line;
    this.city = city;
    this.zipCode = zipCode;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Address(Address other) {
    if (other.isSetLine()) {
      this.line = other.line;
    }
    if (other.isSetCity()) {
      this.city = other.city;
    }
    if (other.isSetZipCode()) {
      this.zipCode = other.zipCode;
    }
  }

  public Address deepCopy() {
    return new Address(this);
  }

  @Override
  public void clear() {
    this.line = null;
    this.city = null;
    this.zipCode = null;
  }

  public String getLine() {
    return this.line;
  }

  public Address setLine(String line) {
    this.line = line;
    return this;
  }

  public void unsetLine() {
    this.line = null;
  }

  /** Returns true if field line is set (has been assigned a value) and false otherwise */
  public boolean isSetLine() {
    return this.line != null;
  }

  public void setLineIsSet(boolean value) {
    if (!value) {
      this.line = null;
    }
  }

  public String getCity() {
    return this.city;
  }

  public Address setCity(String city) {
    this.city = city;
    return this;
  }

  public void unsetCity() {
    this.city = null;
  }

  /** Returns true if field city is set (has been assigned a value) and false otherwise */
  public boolean isSetCity() {
    return this.city != null;
  }

  public void setCityIsSet(boolean value) {
    if (!value) {
      this.city = null;
    }
  }

  public String getZipCode() {
    return this.zipCode;
  }

  public Address setZipCode(String zipCode) {
    this.zipCode = zipCode;
    return this;
  }

  public void unsetZipCode() {
    this.zipCode = null;
  }

  /** Returns true if field zipCode is set (has been assigned a value) and false otherwise */
  public boolean isSetZipCode() {
    return this.zipCode != null;
  }

  public void setZipCodeIsSet(boolean value) {
    if (!value) {
      this.zipCode = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case LINE:
      if (value == null) {
        unsetLine();
      } else {
        setLine((String)value);
      }
      break;

    case CITY:
      if (value == null) {
        unsetCity();
      } else {
        setCity((String)value);
      }
      break;

    case ZIP_CODE:
      if (value == null) {
        unsetZipCode();
      } else {
        setZipCode((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case LINE:
      return getLine();

    case CITY:
      return getCity();

    case ZIP_CODE:
      return getZipCode();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case LINE:
      return isSetLine();
    case CITY:
      return isSetCity();
    case ZIP_CODE:
      return isSetZipCode();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Address)
      return this.equals((Address)that);
    return false;
  }

  public boolean equals(Address that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_line = true && this.isSetLine();
    boolean that_present_line = true && that.isSetLine();
    if (this_present_line || that_present_line) {
      if (!(this_present_line && that_present_line))
        return false;
      if (!this.line.equals(that.line))
        return false;
    }

    boolean this_present_city = true && this.isSetCity();
    boolean that_present_city = true && that.isSetCity();
    if (this_present_city || that_present_city) {
      if (!(this_present_city && that_present_city))
        return false;
      if (!this.city.equals(that.city))
        return false;
    }

    boolean this_present_zipCode = true && this.isSetZipCode();
    boolean that_present_zipCode = true && that.isSetZipCode();
    if (this_present_zipCode || that_present_zipCode) {
      if (!(this_present_zipCode && that_present_zipCode))
        return false;
      if (!this.zipCode.equals(that.zipCode))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetLine()) ? 131071 : 524287);
    if (isSetLine())
      hashCode = hashCode * 8191 + line.hashCode();

    hashCode = hashCode * 8191 + ((isSetCity()) ? 131071 : 524287);
    if (isSetCity())
      hashCode = hashCode * 8191 + city.hashCode();

    hashCode = hashCode * 8191 + ((isSetZipCode()) ? 131071 : 524287);
    if (isSetZipCode())
      hashCode = hashCode * 8191 + zipCode.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(Address other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetLine()).compareTo(other.isSetLine());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLine()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.line, other.line);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCity()).compareTo(other.isSetCity());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCity()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.city, other.city);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetZipCode()).compareTo(other.isSetZipCode());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetZipCode()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.zipCode, other.zipCode);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Address(");
    boolean first = true;

    sb.append("line:");
    if (this.line == null) {
      sb.append("null");
    } else {
      sb.append(this.line);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("city:");
    if (this.city == null) {
      sb.append("null");
    } else {
      sb.append(this.city);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("zipCode:");
    if (this.zipCode == null) {
      sb.append("null");
    } else {
      sb.append(this.zipCode);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (line == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'line' was not present! Struct: " + toString());
    }
    if (city == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'city' was not present! Struct: " + toString());
    }
    if (zipCode == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'zipCode' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class AddressStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public AddressStandardScheme getScheme() {
      return new AddressStandardScheme();
    }
  }

  private static class AddressStandardScheme extends org.apache.thrift.scheme.StandardScheme<Address> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Address struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
          break;
        }
        switch (schemeField.id) {
          case 1: // LINE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.line = iprot.readString();
              struct.setLineIsSet(true);
            } else {
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // CITY
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.city = iprot.readString();
              struct.setCityIsSet(true);
            } else {
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // ZIP_CODE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.zipCode = iprot.readString();
              struct.setZipCodeIsSet(true);
            } else {
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, Address struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.line != null) {
        oprot.writeFieldBegin(LINE_FIELD_DESC);
        oprot.writeString(struct.line);
        oprot.writeFieldEnd();
      }
      if (struct.city != null) {
        oprot.writeFieldBegin(CITY_FIELD_DESC);
        oprot.writeString(struct.city);
        oprot.writeFieldEnd();
      }
      if (struct.zipCode != null) {
        oprot.writeFieldBegin(ZIP_CODE_FIELD_DESC);
        oprot.writeString(struct.zipCode);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class AddressTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public AddressTupleScheme getScheme() {
      return new AddressTupleScheme();
    }
  }

  private static class AddressTupleScheme extends org.apache.thrift.scheme.TupleScheme<Address> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Address struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeString(struct.line);
      oprot.writeString(struct.city);
      oprot.writeString(struct.zipCode);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Address struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.line = iprot.readString();
      struct.setLineIsSet(true);
      struct.city = iprot.readString();
      struct.setCityIsSet(true);
      struct.zipCode = iprot.readString();
      struct.setZipCodeIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

