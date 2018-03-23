Compiled from "FreezeAlien.java"
final class xfiles.T extends java.lang.Enum<xfiles.T> {
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
       0: iconst_0
       1: anewarray     #4                  // class xfiles/T
       4: putstatic     #1                  // Field $VALUES:[Lxfiles/T;
       7: return