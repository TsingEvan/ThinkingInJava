Compiled from "FreezeAlien.java"
final class xfiles.T extends java.lang.Enum<xfiles.T> {
  public static final xfiles.T ELEMENT1;

  public static final xfiles.T ELEMENT2;

  public static final xfiles.T ELEMENT3;

  public static xfiles.T[] values();
    Code:
       0: getstatic     #1                  // Field $VALUES:[Lxfiles/T;
       3: invokevirtual #2                  // Method "[Lxfiles/T;".clone:()Ljava/lang/Object;
       6: checkcast     #3                  // class "[Lxfiles/T;"
       9: areturn

  public static xfiles.T valueOf(java.lang.String);
    Code:
       0: ldc           #4                  // class xfiles/T
       2: aload_0
       3: invokestatic  #5                  // Method java/lang/Enum.valueOf:(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;
       6: checkcast     #4                  // class xfiles/T
       9: areturn

  static {};
    Code:
       0: new           #4                  // class xfiles/T
       3: dup
       4: ldc           #7                  // String ELEMENT1
       6: iconst_0
       7: invokespecial #8                  // Method "<init>":(Ljava/lang/String;I)V
      10: putstatic     #9                  // Field ELEMENT1:Lxfiles/T;
      13: new           #4                  // class xfiles/T
      16: dup
      17: ldc           #10                 // String ELEMENT2
      19: iconst_1
      20: invokespecial #8                  // Method "<init>":(Ljava/lang/String;I)V
      23: putstatic     #11                 // Field ELEMENT2:Lxfiles/T;
      26: new           #4                  // class xfiles/T
      29: dup
      30: ldc           #12                 // String ELEMENT3
      32: iconst_2
      33: invokespecial #8                  // Method "<init>":(Ljava/lang/String;I)V
      36: putstatic     #13                 // Field ELEMENT3:Lxfiles/T;
      39: iconst_3
      40: anewarray     #4                  // class xfiles/T
      43: dup
      44: iconst_0
      45: getstatic     #9                  // Field ELEMENT1:Lxfiles/T;
      48: aastore
      49: dup
      50: iconst_1
      51: getstatic     #11                 // Field ELEMENT2:Lxfiles/T;
      54: aastore
      55: dup
      56: iconst_2
      57: getstatic     #13                 // Field ELEMENT3:Lxfiles/T;
      60: aastore
      61: putstatic     #1                  // Field $VALUES:[Lxfiles/T;
      64: return