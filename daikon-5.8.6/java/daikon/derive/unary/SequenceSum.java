package daikon.derive.unary;

import daikon.ValueTuple;
import daikon.VarInfo;
import daikon.derive.Derivation;
import daikon.derive.ValueAndModified;
import org.checkerframework.dataflow.qual.Pure;
import org.plumelib.util.Intern;

import java.math.BigInteger;
public final class SequenceSum extends UnaryDerivation {
  // We are Serializable, so we specify a version to allow changes to
  // method signatures without breaking serialization.  If you add or
  // remove fields, you should change this number to the current date.
  static final long serialVersionUID = 20020122L;

  // Variables starting with dkconfig_ should only be set via the
  // daikon.config.Configuration interface.
  /** Boolean. True iff SequenceSum derived variables should be generated. */
  public static boolean dkconfig_enabled = false;

  public SequenceSum(VarInfo vi) {
    super(vi);
   
  }

  @Override
  public ValueAndModified computeValueAndModifiedImpl(ValueTuple vt) {
    int source_mod = base.getModified(vt);
    if (source_mod == ValueTuple.MISSING_NONSENSICAL) {
      return ValueAndModified.MISSING_NONSENSICAL;
    }
    Object val = base.getValue(vt);
    if (val == null) {
      return ValueAndModified.MISSING_NONSENSICAL;
    }
    if (val instanceof long[]) {
      long[] val_array = (long[]) val;
      long result = 0;
      for (int i = 0; i < val_array.length; i++) {
        result += val_array[i];
      }
      return new ValueAndModified(Intern.internedLong(result), source_mod);
    } else if (val instanceof double[]) {
      double[] val_array = (double[]) val;
      double result = 0;
      for (int i = 0; i < val_array.length; i++) {
        result += val_array[i];
      }
      return new ValueAndModified(Intern.internedDouble(result), source_mod);

    }else if (val instanceof BigInteger[]){
      BigInteger[] val_array = (BigInteger[]) val;
      BigInteger sum = BigInteger.ZERO;
      for (int i = 0; i < val_array.length; i++) {
        sum = sum.add(val_array[i]);
      }
      return new ValueAndModified(sum, source_mod);
    }
    else {
      return ValueAndModified.MISSING_NONSENSICAL;
    }
  }

  @Override
  protected VarInfo makeVarInfo() {
    return VarInfo.make_scalar_seq_func("sum", null, base, 0);
  }

  @Pure
  @Override
  public boolean isSameFormula(Derivation other) {
    return (other instanceof SequenceSum);
  }
}
